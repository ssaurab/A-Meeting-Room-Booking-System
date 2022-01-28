import { Reducer } from "@reduxjs/toolkit";
import { RoomActions } from "./rooms.actions";

export interface RoomsState {
  loading: boolean;
  rooms: {
    id: number;
    image: string;
    name: string;
    capacity: number;
    bookings: number;
    status: string;
  }[];
  showError: boolean;
}

const initialState: RoomsState = {
  loading: true,
  showError: false,
  rooms: [],
};

export const roomsReducer: Reducer<RoomsState, RoomActions> = (
  state = initialState,
  action
) => {
  switch (action.type) {
    case "UPDATE_ROOMS":
      return { ...state, rooms: action.payload.rooms };
    case "UPDATE_ROOMS_LOADING":
      return { ...state, loading: action.payload.loading };
    case "UPDATE_ROOMSSHOWERROR":
      return { ...state, showError: action.payload.showError };
    default:
      return state;
  }
};
