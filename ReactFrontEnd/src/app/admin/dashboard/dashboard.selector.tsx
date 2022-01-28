import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { connectStore } from "../../store";
import { DashboardState } from "./dashboard.reducer";
import { DashboardActionsTypes, dashboardActions } from "./dashboard.actions";
import { ReservationData } from "./dashboard.page";
import { api } from "../../app";
import { LoginState } from "../login/login.reducer";

interface ReservationOnDayData {
  id: number;
  name: string;
  fromHour: Number;
  toHour: Number;
  room: { name: string };
  client: { name: string };
  status: string;
  total: Number;
  date: string;
}
const mapStateToProps = (state: { dashboardReducer: DashboardState; loginReducer: LoginState }) => ({
  ...state.dashboardReducer,
  loggedInUser: state.loginReducer.loggedInUser,
});

const mapDispatchToProps = (dispatch: Dispatch<Action<DashboardActionsTypes>>) => ({
  updateLoading: (key: keyof DashboardState["loading"], loading: boolean) =>
    dispatch(dashboardActions.updateLoading(key, loading)),

  fetchBookingsOnDay: (date: string) => {
    dispatch(dashboardActions.updateLoading("reservations", true));
    api.get(`/V1/bookings/bookingson/${date}`).then(({ data }) => {
      let bookingsData: ReservationData[] = data.map(
        ({ id, name, room, client, fromHour, toHour, status, date, total }: ReservationOnDayData) => ({
          id,
          room: room.name,
          name: client.name,
          duration: `${fromHour}:00 - ${toHour}:00`,
          status,
          date,
          fromHour: fromHour.toString() + ":00",
          toHour: toHour.toString() + ":00",
          total: Number(total),
        })
      );
      dispatch(dashboardActions.updateLoading("reservations", false));
      dispatch(dashboardActions.updateDashboardBookingOnDay({ bookingsOnDay: bookingsData }));
    });
  },
});

export const connectDashboards = connectStore(mapStateToProps, mapDispatchToProps);
