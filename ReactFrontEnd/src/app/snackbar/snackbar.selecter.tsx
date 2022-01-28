import { snackbarActions } from "../snackbar/snackbar.actions";
import { connectStore, store } from "../store";

const mapStateToProps = (state: ReturnType<typeof store["getState"]>) => ({
  ...state.snackbarReducer,
});

const mapDispatchToProps = (dispatch: typeof store["dispatch"]) => ({
  hideSnackbar: () => dispatch(snackbarActions.hide()),
  showSnackbar: (message: string, status: "error" | "success" | "info" | "warning", dontAutoClose = false) =>
    dispatch(snackbarActions.show(message, status, dontAutoClose)),
});

export const connectSnackbar = connectStore(mapStateToProps, mapDispatchToProps);
