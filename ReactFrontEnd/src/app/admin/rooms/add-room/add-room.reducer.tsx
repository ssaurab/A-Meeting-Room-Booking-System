import { Reducer } from "@reduxjs/toolkit";
import { AddRoomActions } from "./add-room.actions";

export interface AddRoomState {
  title: string;
  image: string;
  capacity: number;
  description: string;
  pricePerHour: number;
  pricePerHalfDay: number;
  pricePerDay: number;
  layouts: { id: number; title: string; image: string; selected: boolean }[];
  status: string;
  errors: Map<string, string>;
  rooms :{title:string}[];
}

const initialState: AddRoomState = {
  title: "",
  image: "",
  capacity: 0,
  description: "",
  pricePerHour: 0,
  pricePerHalfDay: 0,
  pricePerDay: 0,
  layouts: [],
  status: "",
  errors: new Map(),
  rooms: []
};

export const addRoomReducer: Reducer<AddRoomState, AddRoomActions> = (state = initialState, action) => {
  switch (action.type) {
    case "ADD_ROOM_RESET_ADD_ROOM":
      return { ...initialState };
    case "ADD_ROOM_UPDATE_TITLE":
      return { ...state, title: action.payload.title };
    case "ADD_ROOM_UPDATE_IMAGE":
      return { ...state, image: action.payload.image };
    case "ADD_ROOM_UPDATE_CAPACITY":
      return { ...state, capacity: action.payload.capacity };
    case "ADD_ROOM_UPDATE_DESCRIPTION":
      return { ...state, description: action.payload.description };
    case "ADD_ROOM_UPDATE_STATUS":
      return { ...state, status: action.payload.status };
    case "ADD_ROOM_UPDATE_PRICE":
      switch (action.payload.per) {
        case "hour":
          return { ...state, pricePerHour: action.payload.price };
        case "half-day":
          return { ...state, pricePerHalfDay: action.payload.price };
        case "day":
          return { ...state, pricePerDay: action.payload.price };
      }
      break;
    case "ADD_ROOM_TOGGLE_PRICE":
      switch (action.payload.per) {
        case "hour":
          return { ...state, pricePerHour: state.pricePerHour ? 0 : 1 };
        case "half-day":
          return { ...state, pricePerHalfDay: state.pricePerHalfDay ? 0 : 1 };
        case "day":
          return { ...state, pricePerDay: state.pricePerDay ? 0 : 1 };
      }
      break;
    case "ADD_ROOM_SET_LAYOUTS":
      return { ...state, layouts: action.payload.layouts };
    case "ADD_ROOM_TOGGLE_LAYOUT":
      return {
        ...state,
        layouts: state.layouts.map((layout) => ({
          ...layout,
          selected: layout.id === action.payload.layoutId ? !layout.selected : layout.selected,
        })),
      };
    case "ADD_ROOM_UPDATE_ERRORS":
      return { ...state, errors: action.payload.errors };
    case "ADD_ROOM_SET_ROOMS":
      return {...state,rooms: action.payload}
    default:
      return state;
  }
};
