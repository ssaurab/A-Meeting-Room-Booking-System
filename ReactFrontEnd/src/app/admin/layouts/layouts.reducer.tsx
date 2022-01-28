import { Reducer } from "@reduxjs/toolkit";
import { LayoutActions } from "./layouts.actions";

export interface LayoutsState {
  loading: boolean;
  layouts: {
    id: number;
    title: string;
    image: string;
    rooms: string[];
  }[];
  showError: boolean;
}

const initialState: LayoutsState = {
  loading: true,
  showError: false,
  layouts: [],
};

export const layoutsReducer: Reducer<LayoutsState, LayoutActions> = (state = initialState, action) => {
  switch (action.type) {
    case "UPDATE_LAYOUTS":
      return { ...state, layouts: action.payload.layouts };
    case "UPDATE_LAYOUTS_LOADING":
      return { ...state, loading: action.payload.loading };
    case "UPDATE_LAYOUTSHOWERROR":
      return { ...state, showError: action.payload.showError };
    default:
      return state;
  }
};
