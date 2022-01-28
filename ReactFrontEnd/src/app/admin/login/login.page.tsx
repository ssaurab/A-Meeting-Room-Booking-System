import { Button, CardActions, CardContent, makeStyles } from "@material-ui/core";
import Card from "@material-ui/core/Card/Card";
import TextField from "@material-ui/core/TextField";
import React from "react";
import { Link, Redirect, RouteComponentProps, useLocation } from "react-router-dom";
import { connectLogin } from "./login.selecter";

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
    flexFlow: "column",
    alignItems: "center",
    marginBottom: "10px",
  },
});

export interface LoginPageProps extends RouteComponentProps {}

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

export const LoginPage = connectLogin<LoginPageProps>(({ match, ...props }) => {
  const classes = useStyles();

  if (useQuery().has("logout")) {
    props.logOut();
    props.history.push(match.url);
  } else if (props.loggedInUser) {
    return <Redirect to={match.url + "/dashboard"} />;
  }

  return (
    <div className={classes.root}>
      <h1>Admin Login</h1>
      <Card className={classes.card}>
        <CardContent>
          <TextField
            className={classes.text}
            label="Username"
            value={props.user}
            onChange={props.updateUser}
            variant="outlined"
          />
          <br />
          <TextField
            className={classes.text}
            label="Password"
            value={props.password}
            onChange={props.updatePassword}
            variant="outlined"
            type="password"
          />
        </CardContent>
        <CardActions className={classes.actions}>
          <Button variant="contained" id="login" color="primary" onClick={() => props.logIn(props.user, props.password)}>
            Login
          </Button>
          <br />
          <Link to={match.url + "/forget-password"}>
            <Button variant="text" size="small" color="default">
              Forgot Password
            </Button>
          </Link>
        </CardActions>
      </Card>
    </div>
  );
});
