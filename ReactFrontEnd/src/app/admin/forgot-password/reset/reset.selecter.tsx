import { ResetState } from "../reset/reset.reducer";
import { ChangeEvent, Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { resetActions, ResetActionTypes } from "../reset/reset.actions";
import { connectStore } from "../../../store";
import { api } from "../../../app";

const mapStateToProps = (state: { resetReducer: ResetState }) => ({
  ...state.resetReducer,
  errors: ((props: ResetState) => {
    const errors = new Map<string, string>();

    const pwdRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])\w{8,18}$/;
    if (!props.form.password) errors.set("password", "New Password is Required");
    else if (props.form.password.length < 8 || props.form.password.length > 18)
      errors.set("password", "Password must be 8 to 18 characters long");
    else if (!pwdRegex.test(props.form.password))
      errors.set("password", "Password must have a lowercase, an uppercase, a digit.");
    if (!props.form.repassword) errors.set("repassword", "Please retype your password");
    else if (props.form.password !== props.form.repassword)
      errors.set("repassword", "Password does not match retyped password");

    return errors;
  })(state.resetReducer),
});

const mapDispatchToProps = (dispatch: Dispatch<Action<ResetActionTypes>>) => ({
  updateForm: (field: keyof ResetState["form"]) => (e: ChangeEvent<HTMLInputElement>) =>
    dispatch(resetActions.updateForm(field, e.target.value)),

  hideError: () => dispatch(resetActions.hideError()),

  reset: (password: string, token: string) =>
    api.post(
      "/users/resetPassword",
      { password, token },
      {
        headers: {
          successMsg: "Your password has been successfully reset. Please login.",
        },
      }
    ),
});

export const connectReset = connectStore(mapStateToProps, mapDispatchToProps);
