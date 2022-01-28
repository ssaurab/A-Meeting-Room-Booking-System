import {
  makeStyles,
  Theme,
  createStyles,
  Paper,
  Collapse,
  IconButton,
  Grid,
  TextField,
  Select,
  MenuItem,
  Box,
  FormControl,
  InputLabel,
  FormHelperText,
} from "@material-ui/core";
import React from "react";
import CloseIcon from "@material-ui/icons/Close";
import Alert from "@material-ui/lab/Alert";
import { connectAddBooking } from "./add-booking.selecter";

export interface ClientDetailsProps {
  isEdit:boolean
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    formControl: {
      margin: theme.spacing(1),
      width: "100%",
    },
  })
);

export const ClientDetailsTab = connectAddBooking<ClientDetailsProps>(
  ({isEdit,...props}) => {
    const classes = useStyles();

    const [open, setOpen] = React.useState(true);

    const titleOptions = [
      "Mr.",
      "Mrs.",
      "Ms.",
      "Miss",
      "Dr.",
      "Prof.",
      "Rev.",
      "Other",
    ];
    const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    const phoneRegex = /^\d{10}$/;
    const zipRegex = /^\d{6}$/;
    const countryOptions = ["India", "US", "Singapore"];
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
              {isEdit?'Edit client details. After editing client details click save to update the booking ':'Add client details. Please fill in the client information before saving the booking.'}
            </Alert>
          </Collapse>
          <Box p={3}>
            <Grid container spacing={3}>
              <Grid item xs={6}>
                <FormControl
                  variant="outlined"
                  className={classes.formControl}
                  required
                  error={props.errors.has("client.title")}
                >
                  <InputLabel id="title-label">Title</InputLabel>
                  <Select
                    labelId="title-label"
                    value={props.client.title}
                    label="Title"
                    name="client.title"
                    onChange={(evt) => {
                      props.updateClientTitle(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "Title is Required",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  >
                    {titleOptions.map((title) => (
                      <MenuItem value={title}>{title}</MenuItem>
                    ))}
                  </Select>
                  <FormHelperText>
                    {props.errors.get("client.title")}
                  </FormHelperText>
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl className={classes.formControl}>
                  <TextField
                    required
                    label="Name"
                    name="client.name"
                    value={props.client.name}
                    error={props.errors.has("client.name")}
                    helperText={props.errors.get("client.name")}
                    variant="outlined"
                    onChange={(evt) => {
                      props.updateClientName(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "Name is Required",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl className={classes.formControl}>
                  <TextField
                    required
                    label="Email"
                    name="client.email"
                    value={props.client.email}
                    error={props.errors.has("client.email")}
                    helperText={props.errors.get("client.email")}
                    variant="outlined"
                    onChange={(evt) => {
                      props.updateClientEmail(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "Email is Required",
                            props.errors
                          )
                        : !emailRegex.test(evt.target.value)
                        ? props.addError(
                            evt.target.name,
                            "Invalid email address",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl className={classes.formControl}>
                  <TextField
                    required
                    label="Phone"
                    name="client.phone"
                    value={props.client.phone}
                    variant="outlined"
                    error={props.errors.has("client.phone")}
                    helperText={props.errors.get("client.phone")}
                    onChange={(evt) => {
                      props.updateClientPhone(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "Phone is Required",
                            props.errors
                          )
                        : !phoneRegex.test(evt.target.value)
                        ? props.addError(
                            evt.target.name,
                            "Phone number should be 10 digits",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <FormControl className={classes.formControl}>
                  <TextField
                    id="outlined-multiline-static"
                    label="Notes"
                    value={props.client.notes}
                    multiline
                    rows={3}
                    variant="outlined"
                    onChange={(evt) => {
                      props.updateClientNotes(evt.target.value as string);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl className={classes.formControl}>
                  <TextField
                    required
                    label="Company"
                    name="client.company"
                    value={props.client.company}
                    variant="outlined"
                    error={props.errors.has("client.company")}
                    helperText={props.errors.get("client.company")}
                    onChange={(evt) => {
                      props.updateClientCompany(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "Company is Required",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl className={classes.formControl}>
                  <TextField
                    required
                    label="Address"
                    name="client.address"
                    value={props.client.address}
                    error={props.errors.has("client.address")}
                    helperText={props.errors.get("client.address")}
                    variant="outlined"
                    onChange={(evt) => {
                      props.updateClientAddress(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "Address is Required",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl className={classes.formControl}>
                  <TextField
                    required
                    label="City"
                    name="client.city"
                    value={props.client.city}
                    variant="outlined"
                    error={props.errors.has("client.city")}
                    helperText={props.errors.get("client.city")}
                    onChange={(evt) => {
                      props.updateClientCity(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "City is Required",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl className={classes.formControl}>
                  <TextField
                    required
                    label="State"
                    value={props.client.state}
                    name="client.state"
                    variant="outlined"
                    error={props.errors.has("client.state")}
                    helperText={props.errors.get("client.state")}
                    onChange={(evt) => {
                      props.updateClientState(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "State is Required",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl className={classes.formControl}>
                  <TextField
                    required
                    label="Zip"
                    name="client.zip"
                    value={props.client.zip}
                    variant="outlined"
                    error={props.errors.has("client.zip")}
                    helperText={props.errors.get("client.zip")}
                    onChange={(evt) => {
                      props.updateClientZip(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "Zip is Required",
                            props.errors
                          )
                        : !zipRegex.test(evt.target.value)
                        ? props.addError(
                            evt.target.name,
                            "Postal Code should be 6 digits",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl
                  variant="outlined"
                  className={classes.formControl}
                  error={props.errors.has("client.country")}
                >
                  <InputLabel id="country-label">Country</InputLabel>
                  <Select
                    labelId="country-label"
                    value={props.client.country}
                    label="Country"
                    name="client.country"
                    onChange={(evt) => {
                      props.updateClientCountry(evt.target.value as string);
                    }}
                    onBlur={(evt) => {
                      !evt.target.value
                        ? props.addError(
                            evt.target.name,
                            "Country is Required",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                  >
                    {countryOptions.map((title) => (
                      <MenuItem value={title}>{title}</MenuItem>
                    ))}
                  </Select>
                  <FormHelperText>
                    {props.errors.get("client.country")}
                  </FormHelperText>
                </FormControl>
              </Grid>
            </Grid>
          </Box>
        </Paper>
      </div>
    );
  }
);
