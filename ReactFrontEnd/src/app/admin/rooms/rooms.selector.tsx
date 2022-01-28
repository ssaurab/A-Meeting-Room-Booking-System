import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { connectStore } from "../../store";
import { RoomsState } from "./rooms.reducer";
import { RoomActionTypes, roomsActions } from "./rooms.actions";
import { RoomsRow } from "./rooms.page";
import { api } from "../../app";

const mapStateToProps = (state: { roomsReducer: RoomsState }) => ({
  ...state.roomsReducer,
});

const mapDispatchToProps = (dispatch: Dispatch<Action<RoomActionTypes>>) => ({
  updateLoading: (loading: boolean) => dispatch(roomsActions.updateLoading({ loading })),

  updateRooms: (rooms: RoomsRow[]) =>
    dispatch(
      roomsActions.updateRooms({
        rooms: rooms.map(({ id, image, name, capacity, bookings, status }) => ({
          id,
          image,
          name,
          capacity,
          bookings,
          status,
        })),
      })
    ),

  fetchRooms: () => {
    api.get("/V1/rooms").then(
      async (response: {
        data: {
          id: number;
          image: string;
          name: string;
          capacity: number;
          status: string;
        }[];
      }) => {
        const rooms = await Promise.all(
          response.data.map(async (room) => ({
            ...room,
            bookings: (await api.get(`/V2/bookings/countroom/${room.id}`)).data,
          }))
        );
        dispatch(roomsActions.updateLoading({ loading: false }));
        dispatch(roomsActions.updateRooms({ rooms }));
      }
    );
  },

  deleteRoom: (id: number) =>
    api
      .delete(`/V1/rooms/${id}`, { headers: { successMsg: "Room deleted successfully!" } })
      .then(() => {
        dispatch(roomsActions.updateLoading({ loading: true }));
      })
      .catch((error) => dispatch(roomsActions.updateShowError({ showError: true }))),

  hideError: () => dispatch(roomsActions.updateShowError({ showError: false })),
  editStatus: (status: string, row: RoomsRow, rows: RoomsRow[]) => {
    api
      .patch(`/V2/rooms/${row.id}`, { status }, { headers: { successMsg: "Status updated successfully!" } })
      .then(({ data }) => {
        let updateRows = rows.map((ele) => {
          return ele.id === row.id ? { ...ele, status } : ele;
        });
        dispatch(roomsActions.updateRooms({ rooms: updateRows }));
      })
      .catch((err) => console.error(err.message));
  },
});

export const connectRooms = connectStore(mapStateToProps, mapDispatchToProps);
