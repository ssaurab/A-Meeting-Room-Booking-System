export const loginActions = {
  updateUser: (payload: { user: string }) => ({
    type: "UPDATE_USER" as const,
    payload,
  }),

  updatePassword: (payload: { password: string }) => ({
    type: "UPDATE_PASSWORD" as const,
    payload,
  }),

  setError: (payload: { error: string }) => ({
    type: "SET_ERROR" as const,
    payload,
  }),

  hideError: () => ({
    type: "HIDE_ERROR" as const,
  }),

  logIn: (user: {
    token: string;
    email: string;
    name: string;
    role: string;
    status: string;
  }) => ({
    type: "LOG_IN" as const,
    user,
  }),

  logOut: () => ({
    type: "LOG_OUT" as const,
  }),

  openSnackBar: (bool: boolean) => ({
    type: "SHOW_ERROR" as const,
    bool,
  }),
  setErrorMessage: (msg: string) => ({
    type: "ERROR_MESSAGE" as const,
    msg,
  }),
  setSnackBarType: (
    value: "error" | "success" | "info" | "warning" | undefined
  ) => ({
    type: "SET_SNACKBAR_TYPE" as const,
    value,
  }),
};

export type LoginActionTypes = ReturnType<
  typeof loginActions[keyof typeof loginActions]
>["type"];

export type LoginActions = ReturnType<
  typeof loginActions[keyof typeof loginActions]
>;
