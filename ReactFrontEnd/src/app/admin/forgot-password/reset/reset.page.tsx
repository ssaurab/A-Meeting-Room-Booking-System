import React from "react";
import TextField from "@material-ui/core/TextField";
import Card from "@material-ui/core/Card/Card";
import CloseIcon from "@material-ui/icons/Close";
import { CardActions, Button, makeStyles, CardContent, Collapse, IconButton } from "@material-ui/core";
import { RouteComponentProps, useLocation } from "react-router-dom";
import { connectReset } from "./reset.selecter";
import Alert from "@material-ui/lab/Alert";

const useStyles = makeStyles({
  root: {
    width: "100%",
    marginTop: "130px",
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

export interface ResetPageProps extends RouteComponentProps {}

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

export const ResetPage = connectReset<ResetPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();

  const token = useQuery().get("token") ?? "";

  const showError = new Map([...props.errors.entries()].filter(([field]) => props.touched.has(field)));

  const resetPassword = () => {
    if (props.errors.size) return;
    props.reset(props.form.password, token).then(() => history.push(match.url.split("/").slice(0, -2).join("/")));
  };

  return (
    <div className={classes.root}>
      <h1>Reset Password</h1>
      <Card className={classes.card}>
        <CardContent>
          <Collapse in={props.showError} className={classes.error}>
            <Alert
              severity="error"
              action={
                <IconButton aria-label="close" color="inherit" size="small">
                  <CloseIcon fontSize="inherit" />
                </IconButton>
              }
            >
              {props.error}
            </Alert>
          </Collapse>
          <TextField
            className={classes.text}
            required
            label="New Password"
            type="password"
            value={props.form.password}
            variant="outlined"
            onChange={props.updateForm("password")}
            error={showError.has("password")}
            helperText={showError.get("password")}
          />
          <TextField
            className={classes.text}
            required
            label="Retype New Password"
            type="password"
            value={props.form.repassword}
            variant="outlined"
            onChange={props.updateForm("repassword")}
            error={showError.has("repassword")}
            helperText={showError.get("repassword")}
          />
        </CardContent>
        <CardActions className={classes.actions}>
          <Button variant="contained" color="primary" disabled={!!props.errors.size} onClick={resetPassword}>
            Reset
          </Button>
        </CardActions>
      </Card>
    </div>
  );
});
