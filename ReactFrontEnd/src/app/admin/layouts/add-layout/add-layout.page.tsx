import React, { useEffect } from "react";
import { Paper, FormControl } from "@material-ui/core";
import { makeStyles, createStyles, Theme } from "@material-ui/core/styles";
import Alert from "@material-ui/lab/Alert";
import IconButton from "@material-ui/core/IconButton";
import Collapse from "@material-ui/core/Collapse";
import CloseIcon from "@material-ui/icons/Close";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import ImageUploader from "react-images-upload";
import { RouteComponentProps } from "react-router-dom";
import { connectAddLayout } from "./add-layout.selecter";
import { api } from "../../../app";

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    buttons: {
      margin: 10,
    },
    formControl: {
      margin: theme.spacing(1),
      width: "100%",
    },
    divInput: {
      padding: 20,
    },
    divButton: {
      display: "flex",
      justifyContent: "center",
    },
  })
);

interface AddLayoutPageProps extends RouteComponentProps<{ layoutId?: string }> {}

export const AddLayoutPage = connectAddLayout<AddLayoutPageProps>(({ history, match, ...props }) => {
  const classes = useStyles();
  const [open, setOpen] = React.useState(true);
  const { layoutId } = match.params;

  useEffect(() => {
    props.resetLayouts();
    if (layoutId !== undefined) {
      api
        .get(`/V1/roomlayouts/${layoutId}`)
        .then(({ data }) => {
          props.updateTitle(data.title);
          props.updateImage([], [data.image]);
        })
        .catch(console.error);
    }
    props.fetchExistingLayouts();
  }, []);

  const goBackHandler = () => {
    history.goBack();
    props.resetLayouts();
  };

  const saveLayout = () => {
    if (!props.validate(props, !!layoutId)) return;
    if (layoutId !== undefined) {
      api
        .put(
          `/V1/roomlayouts/${layoutId}`,
          {
            image: props.image,
            title: props.title,
          },
          { headers: { successMsg: "Layout updated successfully!" } }
        )
        .then((res) => {
          props.resetLayouts();
          history.goBack();
        })
        .catch(console.error);
    } else {
      api
        .post(
          "/V1/roomlayouts",
          { image: props.image, title: props.title },
          { headers: { successMsg: "Layout added successfully!" } }
        )
        .then((res) => {
          props.resetLayouts();
          history.goBack();
        })
        .catch(console.error);
    }
  };
  console.log(props.errors);

  return (
    <div>
      <Paper elevation={3}>
        <Collapse in={open}>
          <Alert
            severity="info"
            action={
              <IconButton
                aria-label="close"
                color="inherit"
                size="small"
                onClick={() => {
                  setOpen(false);
                }}
              >
                <CloseIcon fontSize="inherit" />
              </IconButton>
            }
          >
            {!!layoutId?'Edit room layout. After changing fields click save to update room layout':'Add room layout. Enter a title and upload the image for your new room layout.'}
          </Alert>
        </Collapse>
        <Grid container spacing={3} className={classes.divInput}>
          <Grid item xs={12}>
            <FormControl className={classes.formControl}>
              <TextField
                label="Title"
                required
                name="title"
                error={props.errors.has("title")}
                helperText={props.errors.get("title")}
                variant="outlined"
                value={props.title}
                onChange={(evt) => props.updateTitle(evt.target.value)}
                onBlur={(evt) => {
                  console.log(!evt.target.value);
                  !evt.target.value
                    ? props.addError(evt.target.name, "Title is Required", props.errors)
                    : !layoutId && props.existingLayouts.find(({ title }) => title === evt.target.value)
                    ? props.addError(evt.target.name, "Title already exists", props.errors)
                    : props.deleteError(evt.target.name, props.errors);
                }}
              />
            </FormControl>
          </Grid>
          <Grid item xs={12}>
            <ImageUploader
              buttonText="Choose Image"
              singleImage
              // @ts-ignore
              defaultImages={props.image ? [props.image] : []}
              withIcon
              withPreview
              withLabel={false}
              onChange={props.updateImage}
              imgExtension={[".jpg", ".gif", ".png", ".gif"]}
              maxFileSize={5242880}
            />
            {props.errors.has("image") && <Alert severity="error">{props.errors.get("image")}</Alert>}
          </Grid>
        </Grid>
        <div className={classes.divButton}>
          <Button variant="contained" color="primary" className={classes.buttons} onClick={saveLayout}>
            save
          </Button>
          <Button variant="contained" onClick={goBackHandler} color="primary" className={classes.buttons}>
            cancel
          </Button>
        </div>
      </Paper>
    </div>
  );
});
