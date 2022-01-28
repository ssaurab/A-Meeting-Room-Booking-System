import { TextField, Grid, Paper, Collapse, IconButton, Box, FormHelperText } from "@material-ui/core";
import React, { useEffect } from "react";
import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import Button from "@material-ui/core/Button";
import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import InputAdornment from "@material-ui/core/InputAdornment";
import CloseIcon from "@material-ui/icons/Close";
import Alert from "@material-ui/lab/Alert";
import { RouteComponentProps } from "react-router-dom";
import { connectAddEquipment } from "./add-equipment.selector";
import { api } from "../../../app";

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

export interface AddEquipmentPageProps extends RouteComponentProps<{ equipmentId: string }> {}

export const AddEquipmentPage = connectAddEquipment<AddEquipmentPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();
  const [open, setOpen] = React.useState(true);
  const { equipmentId } = match.params;

  useEffect(() => {
    props.resetEquipment();
    if (equipmentId !== undefined) {
      api
        .get(`/V1/equipments/${equipmentId}`)
        .then(({ data }) => {
          props.updateTitle(data.title);
          props.updateBookMultipleUnit(data.bookMultipleUnit);
          props.updatePriceType(data.priceType);
          props.updatePrice(data.price);
        })
        .catch(console.error);
    }
    props.fetchEquipments();
  }, []);

  const goBackHandler = () => {
    history.goBack();
    props.resetEquipment();
  };

  const saveEquipment = () => {
    if (!props.validate(props, !!equipmentId)) return;
    if (equipmentId !== undefined) {
      api
        .put(
          `/V2/equipments/${equipmentId}`,
          {
            bookMultipleUnit: props.bookMultipleUnit,
            price: props.price,
            priceType: props.priceType,
            title: props.title,
          },
          { headers: { successMsg: "Equipment updated successfully!" } }
        )
        .then((res) => {
          props.resetEquipment();
          history.goBack();
        })
        .catch(console.error);
    } else {
      api
        .post(
          "/V2/equipments",
          {
            bookMultipleUnit: props.bookMultipleUnit,
            price: props.price,
            priceType: props.priceType,
            title: props.title,
          },
          { headers: { successMsg: "Equipment added successfully!" } }
        )
        .then((res) => {
          props.resetEquipment();
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
           {!!equipmentId?'Edit equipment item. After changing form fields, click save to uppdate equipment.':'Add a new equipment item. Fill in the form below and click "Save" to add a new equipment.'} 
          </Alert>
        </Collapse>
        <Box p={3}>
          <Grid container spacing={3}>
            <Grid item xs={6}>
              <FormControl className={classes.formControl}>
                <TextField
                  required
                  error={props.errors.has("title")}
                  helperText={props.errors.get("title")}
                  label="Title"
                  name="title"
                  variant="outlined"
                  value={props.title}
                  onChange={(evt) => props.updateTitle(evt.target.value as string)}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Title is Required", props.errors)
                      : !equipmentId && props.existingEquipments.find(({ title }) => title === evt.target.value)
                      ? props.addError(evt.target.name, "Title already exists", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                />
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined" className={classes.formControl}>
                <FormControlLabel
                  control={
                    <Checkbox
                      name="units"
                      checked={props.bookMultipleUnit}
                      onChange={(event: React.ChangeEvent<{}>, checked: boolean) => props.updateBookMultipleUnit(checked)}
                    />
                  }
                  color="primary"
                  label="Allow Booking Multiple Units"
                />
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl className={classes.formControl}>
                <TextField
                  label="Price"
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
                  value={props.price}
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
            <Grid item xs={6}>
              <FormControl variant="outlined" required className={classes.formControl} error={props.errors.has("priceType")}>
                <InputLabel id="cost-per">Price Type</InputLabel>
                <Select
                  labelId="cost-per"
                  label="Price Type"
                  name="priceType"
                  value={props.priceType}
                  onChange={(evt) => props.updatePriceType(evt.target.value as string)}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Please indicate the price type", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                >
                  <MenuItem value={"perBooking"}>Per Booking</MenuItem>
                  <MenuItem value={"perHour"}>Per Hour</MenuItem>
                </Select>
                <FormHelperText>{props.errors.get("priceType")}</FormHelperText>
              </FormControl>
            </Grid>
          </Grid>
        </Box>
        <div className={classes.divButton}>
          <Button variant="contained" onClick={saveEquipment} color="primary" className={classes.buttons}>
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
