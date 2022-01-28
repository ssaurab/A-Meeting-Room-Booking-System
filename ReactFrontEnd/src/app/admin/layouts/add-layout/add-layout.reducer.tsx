import { Reducer } from "@reduxjs/toolkit";
import { AddLayoutActions } from "./add-layout.actions";

export interface AddLayoutState {
  existingLayouts: {
    id: number;
    title: string;
    image: string;
    rooms: string[];
  }[];
  title: string;
  image: string;
  errors: Map<string, string>;
}

const initialState: AddLayoutState = {
  existingLayouts: [],
  title: "",
  image: "",
  errors: new Map(),
};

export const addLayoutReducer: Reducer<AddLayoutState, AddLayoutActions> = (state = initialState, action) => {
  switch (action.type) {
    case "ADD_LAYOUT_RESET":
      return { ...initialState };
    case "ADD_LAYOUT_SET_EXISTING_LAYOUTS":
      return { ...state, existingLayouts: action.payload.layouts };
    case "ADD_LAYOUT_UPDATE_TITLE":
      return { ...state, title: action.payload.title };
    case "ADD_LAYOUT_UPDATE_IMAGE":
      return { ...state, image: action.payload.image };
    case "ADD_LAYOUT_UPDATE_ERRORS":
      return { ...state, errors: action.payload.errors };
    default:
      return state;
  }
};
