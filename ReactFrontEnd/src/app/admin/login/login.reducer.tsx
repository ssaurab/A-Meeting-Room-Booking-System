import { Reducer } from "@reduxjs/toolkit";
import { LoginActions } from "./login.actions";

export interface LoginState {
  loggedInUser: {
    token: string;
    email: string;
    name: string;
    role: string;
    status: string;
  } | null;
  user: string;
  password: string;
  showError: boolean;
  error: string;
  showSnackbar: boolean;
  errorMessage: string;
  snackbarType: "error" | "success" | "info" | "warning" | undefined;
}

export const readLoggedInUser = () => {
  let loggedInUser;
  try {
    const rawUser = localStorage.getItem("user");
    loggedInUser = rawUser ? JSON.parse(rawUser) : null;
  } catch {
    loggedInUser = null;
  }
  return loggedInUser;
};

const initialState: LoginState = {
  loggedInUser: readLoggedInUser(),
  user: "admin",
  password: "admin",
  showError: false,
  error: "",
  showSnackbar: false,
  errorMessage: "",
  snackbarType: "success",
};

export const loginReducer: Reducer<LoginState, LoginActions> = (
  state = initialState,
  action
) => {
  switch (action.type) {
    case "UPDATE_USER":
      return { ...state, user: action.payload.user };
    case "UPDATE_PASSWORD":
      return { ...state, password: action.payload.password };
    case "SET_ERROR":
      return { ...state, error: action.payload.error, showError: true };
    case "HIDE_ERROR":
      return { ...state, showError: false };
    case "LOG_IN":
      localStorage.setItem("user", JSON.stringify(action.user));
      return { ...state, loggedInUser: action.user };
    case "LOG_OUT":
      localStorage.removeItem("user");
      return { ...state, loggedInUser: null };
    case "SHOW_ERROR":
      return { ...state, showSnackbar: action.bool };
    case "ERROR_MESSAGE":
      return { ...state, errorMessage: action.msg };
    case "SET_SNACKBAR_TYPE":
      return { ...state, snackbarType: action.value };
    default:
      return state;
  }
};
