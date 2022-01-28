import {
  TextField,
  Grid,
  Paper,
  Collapse,
  IconButton,
  Box,
  MenuItem,
  FormHelperText,
  Select,
  InputLabel,
} from "@material-ui/core";
import React, { useEffect } from "react";
import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";
import FormControl from "@material-ui/core/FormControl";
import Button from "@material-ui/core/Button";
import CloseIcon from "@material-ui/icons/Close";
import Alert from "@material-ui/lab/Alert";
import { useHistory } from "react-router-dom";
import { connectAddUser } from "./add-user.selecter";
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

export interface AddUserPageProps {}

export const AddUserPage = connectAddUser<AddUserPageProps>((props) => {
  const classes = useStyles();
  const [open, setOpen] = React.useState(true);
  const history = useHistory();
  const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  const phoneRegex = /^\d{10}$/;
  const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])\w{8,18}$/;
  useEffect(() => {
    props.resetUsers();
    props.fetchUsers();
  }, []);
  const goBackHandler = () => {
    history.goBack();
  };
  const saveUser = () => {
    if (!props.validate(props)) return;

    api
      .post(
        "/V1/users",
        {
          role: props.role,
          name: props.name,
          phone: props.phone,
          email: props.email,
          password: props.password,
          status: props.status,
        },
        { headers: { successMsg: "User added successfully!" } }
      )
      .then((res) => {
        props.resetUsers();
        history.goBack();
      })
      .catch(console.error);
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
            Add a new User. Fill in the form below and click "Save" to add a new User.
          </Alert>
        </Collapse>
        <Box p={3}>
          <Grid container spacing={3}>
            <Grid item xs={6}>
              <FormControl className={classes.formControl}>
                <TextField
                  required
                  label="UserName"
                  variant="outlined"
                  name="name"
                  error={props.errors.has("name")}
                  helperText={props.errors.get("name")}
                  value={props.name}
                  onChange={props.updateName}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Name cannot be empty", props.errors)
                      : props.users.find(({ name }) => name === evt.target.value)
                      ? props.addError(evt.target.name, "UserName is already present", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                />
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl className={classes.formControl}>
                <TextField
                  required
                  label="Password"
                  type="password"
                  name="password"
                  error={props.errors.has("password")}
                  helperText={props.errors.get("password")}
                  variant="outlined"
                  value={props.password}
                  onChange={props.updatePassword}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Password is Required", props.errors)
                      : !passwordRegex.test(evt.target.value)
                      ? props.addError(
                          evt.target.name,
                          "Password must have a lowercase, an uppercase, a digit and be 8 to 18 characters long",
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
                  type="email"
                  name="email"
                  defaultValue=""
                  error={props.errors.has("email")}
                  helperText={props.errors.get("email")}
                  variant="outlined"
                  value={props.email}
                  onChange={props.updateEmail}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Email is Required", props.errors)
                      : !emailRegex.test(evt.target.value)
                      ? props.addError(evt.target.name, "Invalid email address", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                />
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined" required className={classes.formControl} error={props.errors.has("role")}>
                <InputLabel id="role">Role</InputLabel>
                <Select
                  labelId="role"
                  label="Role"
                  name="role"
                  value={props.role}
                  onChange={(evt) => props.updateRole(evt.target.value as string)}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Role is Required", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                >
                  <MenuItem value={"administrator"}>Administrator</MenuItem>
                  <MenuItem value={"editor"}>Editor</MenuItem>
                </Select>
                <FormHelperText>{props.errors.get("role")}</FormHelperText>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined" required className={classes.formControl} error={props.errors.has("status")}>
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
                  <MenuItem value={"active"}>Active</MenuItem>
                  <MenuItem value={"inactive"}>Inactive</MenuItem>
                </Select>
                <FormHelperText>{props.errors.get("status")}</FormHelperText>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl className={classes.formControl}>
                <TextField
                  required
                  label="Phone"
                  defaultValue=""
                  variant="outlined"
                  name="phone"
                  error={props.errors.has("phone")}
                  helperText={props.errors.get("phone")}
                  value={props.phone}
                  onChange={props.updatePhone}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Phone is Required", props.errors)
                      : !phoneRegex.test(evt.target.value)
                      ? props.addError(evt.target.name, "Phone number should be 10 digits", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                />
              </FormControl>
            </Grid>
          </Grid>
        </Box>
        <div className={classes.divButton}>
          <Button variant="contained" onClick={saveUser} color="primary" className={classes.buttons}>
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
