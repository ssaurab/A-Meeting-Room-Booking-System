import { ForgotPasswordState } from "../forgot-password/forgot-password.reducer";
import { ChangeEvent } from "react";
import { forgotPasswordActions } from "../forgot-password/forgot-password.actions";
import { connectStore, resetStore, store } from "../../store";
import { api } from "../../app";

const mapStateToProps = (state: ReturnType<typeof store["getState"]>) => ({
  ...state.forgotPasswordReducer,
  errors: ((props: ForgotPasswordState) => {
    const errors = new Map<string, string>();

    const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (!props.form.email) errors.set("email", "Email is Required");
    else if (!emailRegex.test(props.form.email)) errors.set("email", "Invalid email address");

    return errors;
  })(state.forgotPasswordReducer),
});

const mapDispatchToProps = (dispatch: typeof store["dispatch"]) => ({
  updateForm: (field: keyof ForgotPasswordState["form"]) => (e: ChangeEvent<HTMLInputElement>) =>
    dispatch(forgotPasswordActions.updateForm(field, e.target.value)),

  requestReset: (email: string) =>
    api
      .post(
        "/users/forgotPassword",
        { email },
        { headers: { successMsg: "Password reset link sent to " + email, dontAutoClose: true } }
      )
      .then(() => resetStore("forgotPasswordReducer")),
});

export const connectForgotPassword = connectStore(mapStateToProps, mapDispatchToProps);
