import { DashboardState } from "./dashboard.reducer";

export const dashboardActions = {
  updateLoading: (key: keyof DashboardState["loading"], loading: boolean) => ({
    type: "UPDATE_DASHBOARD_LOADING" as const,
    payload: { key, loading },
  }),

  updateDashboardBookingOnDay: (payload: {
    bookingsOnDay: {
      id: number;
      room: string;
      name: string;
      duration: string;
      status: string;
      date: string;
      total: number;
    }[];
  }) => ({
    type: "UPDATE_BOOKINGSONDAY" as const,
    payload,
  }),
};

export type DashboardActionsTypes = ReturnType<typeof dashboardActions[keyof typeof dashboardActions]>["type"];

export type DashboardActions = ReturnType<typeof dashboardActions[keyof typeof dashboardActions]>;
