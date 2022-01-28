import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { connectStore } from "../../store";
import { LayoutsState } from "./layouts.reducer";
import { LayoutActionTypes, layoutsActions } from "./layouts.actions";
import { LayoutsRow } from "./layouts.page";
import { api } from "../../app";

const mapStateToProps = (state: { layoutsReducer: LayoutsState }) => ({
  ...state.layoutsReducer,
});

const mapDispatchToProps = (dispatch: Dispatch<Action<LayoutActionTypes>>) => ({
  updateLoading: (loading: boolean) => dispatch(layoutsActions.updateLoading({ loading })),

  updateLayouts: (layouts: LayoutsRow[]) => dispatch(layoutsActions.updateLayouts({ layouts })),

  fetchLayouts: () => {
    const fetchRooms = (layoutId: number) =>
      api.get(`/V2/roomlayouts/${layoutId}/rooms`).then(({ data }) => data.map((r: any) => r.name));

    api.get("/V1/roomlayouts").then(async ({ data }) => {
      const rooms = await Promise.all(data.map((l: any) => fetchRooms(l.id)));
      dispatch(layoutsActions.updateLoading({ loading: false }));
      dispatch(layoutsActions.updateShowError({ showError: false }));

      dispatch(
        layoutsActions.updateLayouts({
          layouts: (data as any[]).map((l, i) => ({ ...l, rooms: rooms[i] })),
        })
      );
    });
  },

  deleteLayout: (id: number) =>
    api
      .delete(`/V1/roomlayouts/${id}`, { headers: { successMsg: "Room layout deleted successfully!" } })
      .then(() => {
        dispatch(layoutsActions.updateLoading({ loading: true }));
        dispatch(layoutsActions.updateShowError({ showError: false }));
      })
      .catch((error) => dispatch(layoutsActions.updateShowError({ showError: true }))),

  hideError: () => dispatch(layoutsActions.updateShowError({ showError: false })),
});

export const connectLayouts = connectStore(mapStateToProps, mapDispatchToProps);
