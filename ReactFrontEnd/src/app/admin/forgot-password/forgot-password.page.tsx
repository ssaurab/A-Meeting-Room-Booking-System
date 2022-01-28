import { Button, CardActions, CardContent, makeStyles } from "@material-ui/core";
import Card from "@material-ui/core/Card/Card";
import TextField from "@material-ui/core/TextField";
import React, { useEffect } from "react";
import { RouteComponentProps } from "react-router-dom";
import { resetStore } from "../../store";
import { connectForgotPassword } from "./forgot-password.selecter";

const useStyles = makeStyles({
  root: {
    width: "100%",
    marginTop: "150px",
    display: "flex",
    flexFlow: "column",
    alignItems: "center",
  },
  card: {
    width: "90%",
    maxWidth: "400px",
  },
  error: {
    marginBottom: "12px",
  },
  text: {
    width: "100%",
    margin: "5px 0",
  },
  actions: {
    display: "flex",
    justifyContent: "center",
    marginBottom: "10px",
  },
});

export interface ForgotPasswordPageProps extends RouteComponentProps {}

export const ForgotPasswordPage = connectForgotPassword<ForgotPasswordPageProps>(({ match, ...props }) => {
  const classes = useStyles();

  const showError = new Map([...props.errors.entries()].filter(([field]) => props.touched.has(field)));

  const requestReset = () => {
    if (props.errors.size) return;
    props.requestReset(props.form.email);
  };

  useEffect(() => {
    resetStore("forgotPasswordReducer");
  }, []);

  return (
    <div className={classes.root}>
      <h1>Forget Password</h1>
      <Card className={classes.card}>
        <CardContent>
          <TextField
            className={classes.text}
            required
            label="Email"
            value={props.form.email}
            variant="outlined"
            onChange={props.updateForm("email")}
            error={showError.has("email")}
            helperText={showError.get("email")}
          />
        </CardContent>
        <CardActions className={classes.actions}>
          <Button variant="contained" color="primary" disabled={!!props.errors.size} onClick={requestReset}>
            Get Reset Link
          </Button>
        </CardActions>
      </Card>
    </div>
  );
});
