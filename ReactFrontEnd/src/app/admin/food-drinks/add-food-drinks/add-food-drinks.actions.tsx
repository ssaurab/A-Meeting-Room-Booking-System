export const addFoodDrinksActions = {
  reset: () => ({
    type: "ADD_FOOD_RESET" as const,
  }),

  setExisting: (payload: {
    snacks: {
      id: number;
      price: number;
      title: string;
    }[];
  }) => ({
    type: "ADD_FOOD_SET_EXISTING_SNACKS" as const,
    payload,
  }),

  updateTitle: (payload: { title: string }) => ({
    type: "UPDATE_FOODTITLE" as const,
    payload,
  }),

  updatePrice: (payload: { price: number }) => ({
    type: "UPDATE_FOODPRICE" as const,
    payload,
  }),

  updateErrors: (payload: { errors: Map<string, string> }) => ({
    type: "ADD_FOOD_UPDATE_ERRORS" as const,
    payload,
  }),
};

export type AddFoodDrinksActionTypes = ReturnType<
  typeof addFoodDrinksActions[keyof typeof addFoodDrinksActions]
>["type"];

export type AddFoodDrinksActions = ReturnType<
  typeof addFoodDrinksActions[keyof typeof addFoodDrinksActions]
>;
