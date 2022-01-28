import { Reducer } from "@reduxjs/toolkit";
import { DashboardActions } from "./dashboard.actions";

export interface DashboardState {
  loading: {
    latestBookings: boolean;
    reservations: boolean;
  };
  bookingsOnDay: {
    id: number;
    room: string;
    name: string;
    duration: string;
    status: string;
    date: string;
    total: number;
  }[];
}

const initialState: DashboardState = {
  loading: {
    latestBookings: true,
    reservations: true,
  },
  bookingsOnDay: [],
};

export const dashboardReducer: Reducer<DashboardState, DashboardActions> = (state = initialState, action) => {
  switch (action.type) {
    case "UPDATE_BOOKINGSONDAY":
      return { ...state, bookingsOnDay: action.payload.bookingsOnDay };
    case "UPDATE_DASHBOARD_LOADING":
      const loading = { ...state.loading };
      loading[action.payload.key] = action.payload.loading;
      return { ...state, loading };
    default:
      return state;
  }
};
