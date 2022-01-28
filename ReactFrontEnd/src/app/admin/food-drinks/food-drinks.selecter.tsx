import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { connectStore } from "../../store";
import { SnackState } from "./food-drinks.reducer";
import { SnacksActionTypes, snacksActions } from "./food-drinks.action";
import { SnacksRow } from "./food-drinks.page";
import { api } from "../../app";

const mapStateToProps = (state: { snacksReducer: SnackState }) => ({
  ...state.snacksReducer,
  snacks: state.snacksReducer.snacks.map((snack) => ({
    ...snack,
  })),
});

const mapDispatchToProps = (dispatch: Dispatch<Action<SnacksActionTypes>>) => ({
  updateLoading: (loading: boolean) => dispatch(snacksActions.updateLoading({ loading })),

  updateSnacks: (snacks: SnacksRow[]) =>
    dispatch(
      snacksActions.updateSnacks({
        snacks: snacks.map(({ id, title, price }) => ({ id, title, price })),
      })
    ),

  fetchSnacks: () =>
    api.get("/V1/snacks").then(({ data }) => {
      dispatch(snacksActions.updateLoading({ loading: false }));
      dispatch(snacksActions.updateSnacks({ snacks: data }));
      dispatch(snacksActions.updateShowError({ showError: false }));
    }),

  deleteSnack: (id: number) =>
    api
      .delete(`/V1/snacks/${id}`, { headers: { successMsg: "Snack item deleted successfully!" } })
      .then(() => {
        dispatch(snacksActions.updateLoading({ loading: true }));
        dispatch(snacksActions.updateShowError({ showError: false }));
      })
      .catch((error) => dispatch(snacksActions.updateShowError({ showError: true }))),

  hideError: () => dispatch(snacksActions.updateShowError({ showError: false })),
});

export const connectSnacks = connectStore(mapStateToProps, mapDispatchToProps);
