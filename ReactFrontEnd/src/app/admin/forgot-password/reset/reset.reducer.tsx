import { Reducer } from "@reduxjs/toolkit";
import { ResetActions } from "./reset.actions";

export interface ResetState {
  form: { password: string; repassword: string };
  touched: Set<string>;
  showError: boolean;
  error: string;
}

const initialState: ResetState = {
  form: { password: "", repassword: "" },
  touched: new Set(),
  showError: false,
  error: "",
};

export const resetReducer: Reducer<ResetState, ResetActions> = (state = initialState, action) => {
  switch (action.type) {
    case "REST_PASS_UPDATE_FORM":
      const form = { ...state.form };
      form[action.payload.field] = action.payload.value;
      const touched = new Set(state.touched);
      touched.add(action.payload.field);
      return { ...state, form, touched };
    case "REST_PASS_SET_ERROR":
      return { ...state, error: action.payload.error, showError: true };
    case "REST_PASS_HIDE_ERROR":
      return { ...state, showError: false };
    default:
      return state;
  }
};
