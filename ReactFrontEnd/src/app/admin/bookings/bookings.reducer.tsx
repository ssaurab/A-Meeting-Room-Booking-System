import { Reducer } from "@reduxjs/toolkit";
import { BookingsActions } from "./bookings.action";

export interface BookingsState {
  loading: boolean;
  bookings: {
    id: number;
    room: string;
    date: Date;
    name: string;
    total: number;
    status: string;
    roomId: number;
  }[];
}

const initialState: BookingsState = {
  loading: true,
  bookings: [],
};

export const bookingsReducer: Reducer<BookingsState, BookingsActions> = (
  state = initialState,
  action
) => {
  switch (action.type) {
    case "UPDATE_BOOKINGS":
      return { ...state, bookings: action.payload.bookings };
    case "UPDATE_BOOKINGLOADING":
      return { ...state, loading: action.payload.loading };
    default:
      return state;
  }
};
