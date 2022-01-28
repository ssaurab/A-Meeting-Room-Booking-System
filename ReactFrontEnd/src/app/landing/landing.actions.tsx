export const LandingActions = {
  updateLoading: (payload: { loading: boolean }) => ({
    type: "UPDATE_LOADING" as "UPDATE_LOADING",
    payload,
  }),

  updateLandingRooms: (payload: {
    rooms: {
      id: number;
      image: string;
      name: string;
      description: string;
      capacity: number;
      pricePerHour: number;
      pricePerDay: number;
    }[];
  }) => ({
    type: "UPDATE_LANDINGROOMS" as "UPDATE_LANDINGROOMS",
    payload,
  }),
  setSnackBarType: (
    value: "error" | "success" | "info" | "warning" | undefined
  ) => ({
    type: "SET_CLIENT_SNACKBAR_TYPE" as const,
    value,
  }),
  openSnackBar: (bool: boolean) => ({
    type: "SHOW_CLIENT_ERROR" as const,
    bool,
  }),
  setErrorMessage: (msg: string) => ({
    type: "ERROR_CLIENT_MESSAGE" as const,
    msg,
  }),
};

export type LandingActionTypes = ReturnType<
  typeof LandingActions[keyof typeof LandingActions]
>["type"];

export type LandingActions = ReturnType<
  typeof LandingActions[keyof typeof LandingActions]
>;
