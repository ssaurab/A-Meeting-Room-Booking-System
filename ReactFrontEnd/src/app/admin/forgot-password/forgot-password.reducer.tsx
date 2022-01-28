import { Reducer } from "@reduxjs/toolkit";
import { ForgotPasswordActions } from "./forgot-password.actions";

export interface ForgotPasswordState {
  form: {
    email: string;
  };
  touched: Set<string>;
}

const initialState: ForgotPasswordState = {
  form: { email: "" },
  touched: new Set(),
};

export const forgotPasswordReducer: Reducer<ForgotPasswordState, ForgotPasswordActions> = (state = initialState, action) => {
  switch (action.type) {
    case "FORGOT_PASS_UPDATE_FORM":
      const form = { ...state.form };
      form[action.payload.field] = action.payload.value;
      const touched = new Set(state.touched);
      touched.add(action.payload.field);
      return { ...state, form, touched };
    default:
      return state;
  }
};
