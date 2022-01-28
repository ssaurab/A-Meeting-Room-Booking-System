export const snacksActions = {
  updateLoading: (payload: { loading: boolean }) => ({
    type: "UPDATE_FOODLOADING" as const,
    payload,
  }),

  updateShowError: (payload: { showError: boolean }) => ({
    type: "UPDATE_FOODSHOWERROR" as const,
    payload, 
  }), 

  updateSnacks: (payload: {
    snacks: {
      id: number;
      title: string;
      price: number;
    }[];
  }) => ({
    type: "UPDATE_SNACKS" as const,
    payload,
  }),
};

export type SnacksActionTypes = ReturnType<
  typeof snacksActions[keyof typeof snacksActions]
>["type"];

export type SnacksActions = ReturnType<
  typeof snacksActions[keyof typeof snacksActions]
>;
