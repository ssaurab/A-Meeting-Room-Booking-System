import { TextField, Grid, Paper, Collapse, IconButton, Box } from "@material-ui/core";
import React, { useEffect } from "react";
import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";
import FormControl from "@material-ui/core/FormControl";
import Button from "@material-ui/core/Button";
import InputAdornment from "@material-ui/core/InputAdornment";
import CloseIcon from "@material-ui/icons/Close";
import Alert from "@material-ui/lab/Alert";
import { RouteComponentProps } from "react-router-dom";
import { connectAddFoodDrinks } from "./add-food-drinks.selector";
import { api } from "../../../app";

export interface AddFoodDrinksPageProps extends RouteComponentProps<{ snackId: string }> {}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    container: {
      display: "flex",
      flexWrap: "wrap",
    },
    textField: {
      marginLeft: theme.spacing(1),
      marginRight: theme.spacing(1),
      width: 200,
    },
    buttons: {
      margin: 12,
    },
    button: {
      display: "in-line",
      marginTop: theme.spacing(2),
    },
    formControl: {
      margin: theme.spacing(1),
      width: "100%",
    },
    root: {
      display: "flex",
      flexWrap: "wrap",
    },
    divButton: {
      width: "calc(100% + 35px)",
      display: "flex",
      justifyContent: "center",
    },
    margin: {
      margin: theme.spacing(1),
    },
    withoutLabel: {
      marginTop: theme.spacing(3),
    },
  })
);

export const AddFoodDrinksPage = connectAddFoodDrinks<AddFoodDrinksPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();
  const [open, setOpen] = React.useState(true);
  const { snackId } = match.params;

  useEffect(() => {
    props.resetFood();
    if (snackId !== undefined) {
      api
        .get(`/V1/snacks/${snackId}`)
        .then(({ data }) => {
          props.updatePrice(data.price);
          props.updateTitle(data.title);
        })
        .catch(console.error);
    }
    props.fetchExistingSnacks();
  }, []);

  const goBackHandler = () => {
    history.goBack();
    props.resetFood();
  };
  const saveFoodDrinks = () => {
    if (!props.validate(props, !!snackId)) return;
    if (snackId !== undefined) {
      api
        .put(
          `/V1/snacks/${snackId}`,
          {
            price: props.food_price,
            title: props.food_title,
          },
          { headers: { successMsg: "Snacks updated successfully!" } }
        )
        .then((res) => {
          props.resetFood();
          history.goBack();
        })
        .catch(console.error);
    } else {
      api
        .post(
          "/V1/snacks",
          {
            price: props.food_price,
            title: props.food_title,
          },
          { headers: { successMsg: "Snacks added successfully!" } }
        )
        .then((res) => {
          props.resetFood();
          history.goBack();
        })
        .catch(console.error);
    }
  };
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
            {!!snackId?'Edit Food item. After changing form fields, click save to update Food item':'Add a new Food item. Fill in the form below and click "Save" to add a new Food item.'}
          </Alert>
        </Collapse>
        <Box p={3}>
          <Grid container spacing={3}>
            <Grid item xs={6}>
              <FormControl className={classes.formControl}>
                <TextField
                  required
                  label="Title"
                  variant="outlined"
                  name="title"
                  error={props.errors.has("title")}
                  helperText={props.errors.get("title")}
                  value={props.food_title}
                  onChange={(evt) => props.updateTitle(evt.target.value)}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Title is Required", props.errors)
                      : !snackId && props.existingSnacks.find(({ title }) => title === evt.target.value)
                      ? props.addError(evt.target.name, "Title already exists", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                />
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl className={classes.formControl}>
                <TextField
                  label="Price per attendee"
                  variant="outlined"
                  type="number"
                  name="price"
                  required
                  error={props.errors.has("price")}
                  helperText={props.errors.get("price")}
                  inputProps={{ min: 0 }}
                  InputProps={{
                    startAdornment: <InputAdornment position="start">&#8377;</InputAdornment>,
                  }}
                  value={props.food_price}
                  onChange={(evt) => props.updatePrice(parseInt(evt.target.value))}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Price is required", props.errors)
                      : parseInt(evt.target.value) <= 0
                      ? props.addError(evt.target.name, "Price shouldn't be non-positive", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                />
              </FormControl>
            </Grid>
          </Grid>
        </Box>
        <div className={classes.divButton}>
          <Button variant="contained" color="primary" className={classes.buttons} onClick={saveFoodDrinks}>
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
