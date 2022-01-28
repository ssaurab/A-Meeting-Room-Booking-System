import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { connectStore } from "../../store";
import { BookingsState } from "./bookings.reducer";
import { BookingsActionTypes, bookingsActions } from "./bookings.action";
import { RoomsData } from "./bookings.page";
import { api } from "../../app";

interface BookingsShow {
  id: number;
  room: { name: string; id: number };
  date: Date;
  total: number;
  client: { name: string };
  status: string;
}
const mapStateToProps = (state: { bookingsReducer: BookingsState }) => ({
  ...state.bookingsReducer,
  // bookings: state.bookingsReducer.bookings.map((booking) => ({ ...booking })),
});

const mapDispatchToProps = (dispatch: Dispatch<Action<BookingsActionTypes>>) => ({
  updateLoading: (loading: boolean) => dispatch(bookingsActions.updateLoading({ loading })),
  updateBookings: (bookings: RoomsData[]) =>
    dispatch(
      bookingsActions.updateBookings({
        bookings: bookings.map(({ id, room, date, name, total, status, roomId }) => ({
          id,
          room,
          date,
          name,
          total,
          status,
          roomId,
        })),
      })
    ),
  fetchBookings: () =>
    api.get("/V1/bookings").then(({ data }) => {
      var bookingsData = data.map(({ id, room, date, total, client, status }: BookingsShow) => ({
        id,
        room: room.name,
        date,
        total,
        name: client.name,
        status,
        roomId: room.id,
      }));
      dispatch(bookingsActions.updateLoading({ loading: false }));
      dispatch(bookingsActions.updateBookings({ bookings: bookingsData }));
    }),

  deleteBooking: (id: number) =>
    api
      .delete(`/V1/bookings/${id}`, { headers: { successMsg: "Booking deleted successfully!" } })
      .then(() => {
        dispatch(bookingsActions.updateLoading({ loading: true }));
      })
      .catch((err) => console.error(err.message)),
  editStatus: (status: string, row: RoomsData, rows: RoomsData[]) => {
    api
      .patch(`/V2/bookings/${row.id}`, { status }, { headers: { successMsg: "Status updated successfully!" } })
      .then(({ data }) => {
        let updatedRows = rows.map((ele) => {
          return ele.id === row.id ? { ...ele, status } : ele;
        });
        dispatch(bookingsActions.updateBookings({ bookings: updatedRows }));
      })
      .catch((err) => console.error(err.message));
  },
});

export const connectBookings = connectStore(mapStateToProps, mapDispatchToProps);
