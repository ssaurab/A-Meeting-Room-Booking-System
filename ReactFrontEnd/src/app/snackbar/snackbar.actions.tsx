export const snackbarActions = {
  hide: () => ({
    type: "SNACKBAR_HIDE" as const,
  }),

  show: (message: string, status: "error" | "success" | "info" | "warning", dontAutoClose = false) => ({
    type: "SNACKBAR_SHOW" as const,
    payload: { message, status, dontAutoClose },
  }),
};

export type SnackbarActionTypes = ReturnType<typeof snackbarActions[keyof typeof snackbarActions]>["type"];

export type SnackbarActions = ReturnType<typeof snackbarActions[keyof typeof snackbarActions]>;
