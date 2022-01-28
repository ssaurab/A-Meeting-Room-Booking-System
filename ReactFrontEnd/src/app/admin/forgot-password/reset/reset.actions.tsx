import { ResetState } from "./reset.reducer";

export const resetActions = {
  updateForm: <T extends keyof ResetState["form"]>(field: T, value: ResetState["form"][T]) => ({
    type: "REST_PASS_UPDATE_FORM" as const,
    payload: { field, value },
  }),

  setError: (payload: { error: string }) => ({
    type: "REST_PASS_SET_ERROR" as const,
    payload,
  }),

  hideError: () => ({
    type: "REST_PASS_HIDE_ERROR" as const,
  }),
};

export type ResetActionTypes = ReturnType<typeof resetActions[keyof typeof resetActions]>["type"];

export type ResetActions = ReturnType<typeof resetActions[keyof typeof resetActions]>;
