import React from "react";
import { connectSnackbar } from "./snackbar.selecter";
import Alert from "@material-ui/lab/Alert";
import { Snackbar } from "@material-ui/core";
import { api } from "../app";
import { readLoggedInUser } from "../admin/login/login.reducer";
import { store } from "../store";
import { loginActions } from "../admin/login/login.actions";

export interface SnackbarComponentProps {}

export const SnackbarComponent = connectSnackbar<SnackbarComponentProps>((props) => {
  // Add auth interceptor, but only once!
  if (!(api.interceptors.request as any).handlers?.length) {
    api.interceptors.request.use((request) => {
      const loggedInUser = readLoggedInUser();
      request.headers.authorization = loggedInUser?.token;
      return request;
    });
  }
  if (!(api.interceptors.response as any).handlers?.length) {
    api.interceptors.response.use(
      (response) => {
        if (response.config.headers?.successMsg) {
          props.showSnackbar(response.config.headers?.successMsg, "success", !!response.config.headers?.dontAutoClose);
        }
        return response;
      },
      (error) => {
        if (401 === error.response.status) {
          store.dispatch(loginActions.logOut());
          props.showSnackbar("Please login again!", "error");
        } else if (error.response.status >= 500) props.showSnackbar("Oops! Some error occured", "error");
        else props.showSnackbar(error.response.data?.message || "Oops! Some error occured", "error");
        throw error;
      }
    );
  }

  return (
    <Snackbar
      style={{ marginTop: 50 }}
      anchorOrigin={{ vertical: "top", horizontal: "center" }}
      open={props.open}
      autoHideDuration={props.dontAutoClose ? null : 5000}
      onClose={props.hideSnackbar}
    >
      <Alert onClose={props.hideSnackbar} variant="filled" severity={props.status}>
        {props.message}
      </Alert>
    </Snackbar>
  );
});
