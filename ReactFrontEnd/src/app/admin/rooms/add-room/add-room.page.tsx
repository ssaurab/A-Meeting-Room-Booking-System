import { Box, FormHelperText, OutlinedInput, Paper } from "@material-ui/core";
import Button from "@material-ui/core/Button";
import Checkbox from "@material-ui/core/Checkbox";
import Collapse from "@material-ui/core/Collapse";
import FormControl from "@material-ui/core/FormControl";
import Grid from "@material-ui/core/Grid";
import IconButton from "@material-ui/core/IconButton";
import InputAdornment from "@material-ui/core/InputAdornment";
import InputLabel from "@material-ui/core/InputLabel";
import ListItemText from "@material-ui/core/ListItemText";
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import CloseIcon from "@material-ui/icons/Close";
import Alert from "@material-ui/lab/Alert";
import React, { useEffect } from "react";
import ImageUploader from "react-images-upload";
import { RouteComponentProps } from "react-router-dom";
import { api } from "../../../app";
import { connectAddRoom } from "./add-room.selecter";

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    buttons: {
      margin: 12,
    },
    formControl: {
      margin: theme.spacing(1),
      width: "100%",
    },
    input: {
      height: 20,
    },
    divinput: {
      margin: 20,
    },
    label: {
      paddingRight: 300,
    },
    divButton: {
      display: "flex",
      justifyContent: "center",
    },
  })
);
const ITEM_HEIGHT = 40;
const ITEM_PADDING_TOP = 0;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 400,
    },
  },
};
export interface AddRoomPageProps extends RouteComponentProps<{ roomId: string }> {}

export const AddRoomPage = connectAddRoom<AddRoomPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();
  const [open, setOpen] = React.useState(true);

  const { roomId } = match.params;

  useEffect(() => {
    props.resetFields();
    props.fetchRooms();
    if (roomId !== undefined) {
      api
        .get(`/V1/rooms/${roomId}`)
        .then(({ data }) => {
          props.updateTitle(data.name);
          props.updateImage(data.image);
          props.updateCapacity(data.capacity);
          props.updateDescription(data.description);
          props.updatePrice("day")(data.pricePerDay);
          props.updatePrice("half-day")(data.pricePerHalfDay);
          props.updatePrice("hour")(data.pricePerHour);
          props.fetchLayouts(data.layouts);
          props.updateStatus(data.status);
        })
        .catch(console.error);
    } else {
      props.fetchLayouts();
    }
  }, []);

  const goBackHandler = () => {
    history.goBack();
    props.resetFields();
  };

  const saveRoom = () => {
    if (!props.validate(props, !!roomId)) {
      window.scrollTo({ top: 0, behavior: "smooth" });
      return;
    }
    if (roomId !== undefined) {
      api
        .put(
          `/V1/rooms/${roomId}`,
          {
            capacity: props.capacity,
            description: props.description,
            image: props.image,
            layouts: props.layouts.filter((l) => l.selected).map(({ id, title, image }) => ({ id, title, image })),
            name: props.title,
            pricePerDay: props.pricePerDay,
            pricePerHalfDay: props.pricePerHalfDay,
            pricePerHour: props.pricePerHour,
            status: props.status,
          },
          { headers: { successMsg: "Room updated successfully!" } }
        )
        .then((res) => {
          history.goBack();
          props.resetFields();
        })
        .catch(console.error);
    } else {
      api
        .post(
          "/V1/rooms",
          {
            capacity: props.capacity,
            description: props.description,
            image: props.image,
            layouts: props.layouts.filter((l) => l.selected).map(({ id, title, image }) => ({ id, title, image })),
            name: props.title,
            pricePerDay: props.pricePerDay,
            pricePerHalfDay: props.pricePerHalfDay,
            pricePerHour: props.pricePerHour,
            status: props.status,
          },
          { headers: { successMsg: "Room added successfully!" } }
        )
        .then((res) => {
          history.goBack();
          props.resetFields();
        })
        .catch(console.error);
    }
  };

  return (
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
          {!!roomId
            ? "Edit meeting room and click save to update meeting room"
            : 'Add a new meeting room. Fill in the form below and click "Save" to add a new meeting room.'}
        </Alert>
      </Collapse>
      <Box p={3}>
        <Grid container spacing={3}>
          <Grid item xs={6}>
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <FormControl className={classes.formControl}>
                  <TextField
                    variant="outlined"
                    label="Title"
                    name="title"
                    required
                    error={props.errors.has("title")}
                    helperText={props.errors.get("title")}
                    InputProps={{ classes: { input: classes.input } }}
                    value={props.title}
                    onChange={(evt) => props.updateTitle(evt.target.value)}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(evt.target.name, "Title is Required", props.errors)
                        : !roomId && props.rooms.find(({ title }) => title === evt.target.value)
                        ? props.addError(evt.target.name, "Title already exists", props.errors)
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <FormControl className={classes.formControl}>
                  <TextField
                    label="Capacity"
                    variant="outlined"
                    type="number"
                    name="capacity"
                    required
                    error={props.errors.has("capacity")}
                    helperText={props.errors.get("capacity")}
                    value={props.capacity}
                    onChange={(evt) => props.updateCapacity(parseInt(evt.target.value))}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(evt.target.name, "Capacity is required", props.errors)
                        : parseInt(evt.target.value) <= 0
                        ? props.addError(evt.target.name, "Capacity must be positive", props.errors)
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <FormControl className={classes.formControl}>
                  <TextField
                    label="Description"
                    variant="outlined"
                    name="description"
                    multiline
                    required
                    error={props.errors.has("description")}
                    helperText={props.errors.get("description")}
                    rows={4}
                    value={props.description}
                    onChange={(evt) => props.updateDescription(evt.target.value)}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(evt.target.name, "Description is Required", props.errors)
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
            </Grid>
          </Grid>
          <Grid item xs={6}>
            <ImageUploader
              buttonText="Choose Image"
              singleImage
              withIcon
              withPreview
              name="image"
              withLabel={false}
              // @ts-ignore
              defaultImages={props.image ? [props.image] : []}
              onChange={(_files: File[], [image]: string[]) => props.updateImage(image)}
              imgExtension={[".jpg", ".gif", ".png", ".gif"]}
              maxFileSize={5242880}
            />
            {props.errors.has("image") && <Alert severity="error">{props.errors.get("image")}</Alert>}
          </Grid>
        </Grid>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <FormControl variant="outlined" required className={classes.formControl} error={props.errors.has("bookFor")}>
              <InputLabel>Book For</InputLabel>
              <Select
                multiple
                input={<OutlinedInput label="Book For" />}
                MenuProps={MenuProps}
                value={props.bookFor.filter((b) => b.selected).map((b) => b.title)}
                onChange={props.updateBookFor}
                name="bookfor"
                renderValue={(selected) => (selected as string[]).join(", ")}
              >
                {props.bookFor.map((book) => (
                  <MenuItem key={book.per} value={book.per}>
                    <Checkbox checked={book.selected} />
                    <ListItemText primary={book.title} />
                  </MenuItem>
                ))}
              </Select>
              <FormHelperText>{props.errors.get("bookFor")}</FormHelperText>
            </FormControl>
          </Grid>
          <Grid item xs={12}>
            <Grid container spacing={3}>
              {props.bookFor
                .filter((b) => b.selected)
                .map((book) => (
                  <Grid item xs>
                    <FormControl className={classes.formControl}>
                      <TextField
                        label={"Price per " + book.per}
                        variant="outlined"
                        type="number"
                        value={props[book.prop]}
                        onChange={(evt) => props.updatePrice(book.per)(parseInt(evt.target.value))}
                        InputProps={{
                          startAdornment: <InputAdornment position="start">₹</InputAdornment>,
                        }}
                      />
                    </FormControl>
                  </Grid>
                ))}
            </Grid>
          </Grid>

          <Grid item xs={12}>
            <FormControl variant="outlined" className={classes.formControl} required error={props.errors.has("layouts")}>
              <InputLabel id="layouts-label">Layouts</InputLabel>
              <Select
                multiple
                value={props.layouts.filter((l) => l.selected).map((l) => l.title)}
                onChange={props.toggleLayout}
                input={<OutlinedInput label="Layouts" />}
                renderValue={(selected) => (selected as string[]).join(", ")}
                MenuProps={MenuProps}
              >
                {props.layouts.map((layout) => (
                  <MenuItem key={layout.id} value={layout.id}>
                    <Checkbox checked={layout.selected} />
                    <ListItemText primary={layout.title} />
                  </MenuItem>
                ))}
              </Select>
              <FormHelperText>{props.errors.get("layouts")}</FormHelperText>
            </FormControl>
          </Grid>
          <Grid item xs={12}>
            <FormControl variant="outlined" className={classes.formControl} required error={props.errors.has("status")}>
              <InputLabel id="status">Status</InputLabel>
              <Select
                labelId="status"
                label="Status"
                name="status"
                value={props.status}
                onChange={(evt) => props.updateStatus(evt.target.value as string)}
                onBlur={(evt) => {
                  !evt.target.value
                    ? props.addError(evt.target.name, "Status is Required", props.errors)
                    : props.deleteError(evt.target.name, props.errors);
                }}
              >
                <MenuItem value="active">Active</MenuItem>
                <MenuItem value="inactive">Inactive</MenuItem>
              </Select>
              <FormHelperText>{props.errors.get("status")}</FormHelperText>
            </FormControl>
          </Grid>
        </Grid>
      </Box>
      <div className={classes.divButton}>
        <Button variant="contained" color="primary" className={classes.buttons} onClick={saveRoom}>
          save
        </Button>
        <Button variant="contained" onClick={goBackHandler} color="primary" className={classes.buttons}>
          cancel
        </Button>
      </div>
    </Paper>
  );
});
