import { Reducer } from "@reduxjs/toolkit";
import { LandingActions } from "./landing.actions";

export interface LandingState {
  loading: boolean;
  showSnackbar: boolean;
  errorMessage: string;
  snackbarType: "error" | "success" | "info" | "warning" | undefined;

  rooms: {
    id: number;
    image: string;
    name: string;
    description: string;
    capacity: number;
    pricePerHour: number;
    pricePerDay: number;
  }[];
}

const initialState: LandingState = {
  loading: true,
  showSnackbar: false,
  errorMessage: "",
  snackbarType: "success",
  rooms: [],
};

export const landingReducer: Reducer<LandingState, LandingActions> = (
  state = initialState,
  action
) => {
  switch (action.type) {
    case "UPDATE_LANDINGROOMS":
      return { ...state, rooms: action.payload.rooms };
    case "UPDATE_LOADING":
      return { ...state, loading: action.payload.loading };
    case "SHOW_CLIENT_ERROR":
      return { ...state, showSnackbar: action.bool };
    case "ERROR_CLIENT_MESSAGE":
      return { ...state, errorMessage: action.msg };
    case "SET_CLIENT_SNACKBAR_TYPE":
      return { ...state, snackbarType: action.value };
    default:
      return state;
  }
};
