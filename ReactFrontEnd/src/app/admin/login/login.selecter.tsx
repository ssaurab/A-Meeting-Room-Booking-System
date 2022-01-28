import { LoginState } from "./login.reducer";
import { Dispatch, ChangeEvent } from "react";
import { Action } from "@reduxjs/toolkit";
import { LoginActionTypes, loginActions } from "./login.actions";
import { connectStore } from "../../store";
import { api } from "../../app";

const mapStateToProps = (state: { loginReducer: LoginState }) => ({
  ...state.loginReducer,
});

const mapDispatchToProps = (dispatch: Dispatch<Action<LoginActionTypes>>) => ({
  updateUser: (e: ChangeEvent<HTMLInputElement>) =>
    dispatch(loginActions.updateUser({ user: e.target.value })),

  updatePassword: (e: ChangeEvent<HTMLInputElement>) =>
    dispatch(loginActions.updatePassword({ password: e.target.value })),

  hideError: () => dispatch(loginActions.hideError()),

  logIn: (username: string, password: string) => {
    api
      .post("/login", {
        name: username,
        password,
      })
      .then(({ data, headers }) => {
        dispatch(
          loginActions.logIn({
            token: headers.token,
            email: data.email,
            name: data.name,
            role: data.role,
            status: data.status,
          })
        );
        dispatch(loginActions.hideError());
      })
      .catch(({ response: { data } }) =>
        dispatch(loginActions.setError({ error: data.message }))
      );
  },

  logOut: () => dispatch(loginActions.logOut()),

  openSnackBar: (bool: boolean) => dispatch(loginActions.openSnackBar(bool)),
  setErrorMessage: (msg: string) => dispatch(loginActions.setErrorMessage(msg)),
  setSnackBarType: (
    type: "error" | "success" | "info" | "warning" | undefined
  ) => dispatch(loginActions.setSnackBarType(type)),
});

export const connectLogin = connectStore(mapStateToProps, mapDispatchToProps);
