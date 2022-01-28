import { Reducer } from "@reduxjs/toolkit";
import { SnacksActions } from "./food-drinks.action";

export interface SnackState {
  loading: boolean;
  snacks: {
    id: number;
    title: string;
    price: number;
  }[];
  showError : boolean;
}

const initialState: SnackState = {
  loading: true,
  showError : false,
  snacks: [],
};

export const snacksReducer: Reducer<SnackState, SnacksActions> = (
  state = initialState,
  action
) => {
  switch (action.type) {
    case "UPDATE_SNACKS":
      return { ...state, snacks: action.payload.snacks };
    case "UPDATE_FOODLOADING":
      return { ...state, loading: action.payload.loading };
    case "UPDATE_FOODSHOWERROR":
      return { ...state, showError: action.payload.showError };
    default:
      return state;
  }
};
