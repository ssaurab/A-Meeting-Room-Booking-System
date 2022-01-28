import { ForgotPasswordState } from "./forgot-password.reducer";

export const forgotPasswordActions = {
  updateForm: <T extends keyof ForgotPasswordState["form"]>(field: T, value: ForgotPasswordState["form"][T]) => ({
    type: "FORGOT_PASS_UPDATE_FORM" as const,
    payload: { field, value },
  }),
};

export type ForgotPasswordActionTypes = ReturnType<typeof forgotPasswordActions[keyof typeof forgotPasswordActions]>["type"];

export type ForgotPasswordActions = ReturnType<typeof forgotPasswordActions[keyof typeof forgotPasswordActions]>;
