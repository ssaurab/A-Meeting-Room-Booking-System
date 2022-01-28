export const addUserActions = {
  reset: () => ({
    type: "ADD_USER_RESET" as const,
  }),

  updateName: (payload: { name: string }) => ({
    type: "UPDATE_NAME" as const,
    payload,
  }),

  updateEmail: (payload: { email: string }) => ({
    type: "UPDATE_EMAIL" as const,
    payload,
  }),

  updatePhone: (payload: { phone: string }) => ({
    type: "UPDATE_PHONE" as const,
    payload,
  }),

  updatePassword: (payload: { password: string }) => ({
    type: "UPDATE_PASSWORD" as const,
    payload,
  }),

  updateRegistration: (payload: { date: string }) => ({
    type: "UPDATE_REGISTRATION" as const,
    payload,
  }),

  updateRole: (payload: { role: string }) => ({
    type: "UPDATE_ROLE" as const,
    payload,
  }),

  updateStatus: (payload: { status: string }) => ({
    type: "UPDATE_STATUS" as const,
    payload,
  }),

  updateErrors: (payload: { errors: Map<string, string> }) => ({
    type: "ADD_USER_UPDATE_ERRORS" as const,
    payload,
  }),
  setUsers: (payload: {name:string}[]) => ({
    type: "ADD_USER_SET_USERS" as const,
    payload,
  })
};

export type AddUserActionTypes = ReturnType<
  typeof addUserActions[keyof typeof addUserActions]
>["type"];

export type AddUserActions = ReturnType<
  typeof addUserActions[keyof typeof addUserActions]
>;
