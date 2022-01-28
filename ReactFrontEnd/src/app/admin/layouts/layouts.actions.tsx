import { LayoutsState } from "./layouts.reducer";

export const layoutsActions = {
  updateLoading: (payload: { loading: boolean }) => ({
    type: "UPDATE_LAYOUTS_LOADING" as const,
    payload,
  }),

  updateShowError: (payload: { showError: boolean }) => ({
    type: "UPDATE_LAYOUTSHOWERROR" as const,
    payload,
  }),

  updateLayouts: (payload: { layouts: LayoutsState["layouts"] }) => ({
    type: "UPDATE_LAYOUTS" as const,
    payload,
  }),
};

export type LayoutActionTypes = ReturnType<typeof layoutsActions[keyof typeof layoutsActions]>["type"];

export type LayoutActions = ReturnType<typeof layoutsActions[keyof typeof layoutsActions]>;
