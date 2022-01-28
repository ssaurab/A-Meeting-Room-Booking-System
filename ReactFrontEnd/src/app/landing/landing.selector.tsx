import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { connectStore } from "../store";
import { LandingState } from "./landing.reducer";
import { LandingActionTypes, LandingActions } from "./landing.actions";
import { LandingRow } from "./landing.page";
import { api } from "../app";

const mapStateToProps = (state: { landingReducer: LandingState }) => ({
  ...state.landingReducer,
});

const mapDispatchToProps = (
  dispatch: Dispatch<Action<LandingActionTypes>>
) => ({
  updateLoading: (loading: boolean) =>
    dispatch(LandingActions.updateLoading({ loading })),

  updateLandingRooms: (landingRooms: LandingRow[]) =>
    dispatch(
      LandingActions.updateLandingRooms({
        rooms: landingRooms.map(
          ({
            id,
            image,
            name,
            description,
            capacity,
            pricePerHour,
            pricePerDay,
          }) => ({
            id,
            image,
            name,
            description,
            capacity,
            pricePerHour,
            pricePerDay,
          })
        ),
      })
    ),

  fetchRooms: () =>
    api.get("/rooms").then(({ data }) => {
      const RoomsDisplay = data.filter(
        (data: {
          id: number;
          image: string;
          name: string;
          status: string;
          description: string;
          capacity: number;
          pricePerHour: number;
          pricePerDay: number;
        }) => data.status?.toLowerCase() === "active"
      );
      dispatch(LandingActions.updateLoading({ loading: false }));
      dispatch(LandingActions.updateLandingRooms({ rooms: RoomsDisplay }));
    }),
  openSnackBar: (bool: boolean) => dispatch(LandingActions.openSnackBar(bool)),
  setErrorMessage: (msg: string) =>
    dispatch(LandingActions.setErrorMessage(msg)),
  setSnackBarType: (
    type: "error" | "success" | "info" | "warning" | undefined
  ) => dispatch(LandingActions.setSnackBarType(type)),
});

export const connectLanding = connectStore(mapStateToProps, mapDispatchToProps);
