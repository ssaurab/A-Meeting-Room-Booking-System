import { Reducer } from "@reduxjs/toolkit";
import { SnackbarActions } from "./snackbar.actions";

export interface SnackbarState {
  open: boolean;
  message: string;
  status?: "error" | "success" | "info" | "warning";
  dontAutoClose?: boolean;
}

const initialState: SnackbarState = {
  open: false,
  message: "",
};

export const snackbarReducer: Reducer<SnackbarState, SnackbarActions> = (state = initialState, action) => {
  switch (action.type) {
    case "SNACKBAR_HIDE":
      return { ...state, open: false };
    case "SNACKBAR_SHOW":
      return {
        ...state,
        open: true,
        message: action.payload.message,
        status: action.payload.status,
        dontAutoClose: action.payload.dontAutoClose,
      };
    default:
      return state;
  }
};
