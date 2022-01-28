import { BookState } from "./book.reducer";

export const bookActions = {
  setRoom: (room: BookState["room"]) => ({
    type: "SET_ROOM" as const,
    payload: { room },
  }),
  updateBookRoom: (prop: keyof BookState["bookRoom"], value: any) => ({
    type: "UPDATE_BOOK_ROOM" as const,
    payload: { prop, value },
  }),
  updateRoomSetup: (prop: keyof BookState["roomSetup"], value: any) => ({
    type: "UPDATE_ROOM_SETUP" as const,
    payload: { prop, value },
  }),
  updateFoodDrinks: (prop: keyof BookState["foodDrinks"], value: any) => ({
    type: "UPDATE_FOOD_DRINKS" as const,
    payload: { prop, value },
  }),
  updateCheckoutClientDetails: (
    prop: keyof BookState["checkout"]["clientDetails"],
    value: any
  ) => ({
    type: "UPDATE_CHECKOUT_CLIENT" as const,
    payload: { prop, value },
  }),

  updateCheckoutPaymentDetails: (
    prop: keyof BookState["checkout"]["paymentDetails"],
    value: any
  ) => ({
    type: "UPDATE_CHECKOUT_PAYMENT" as const,
    payload: { prop, value },
  }),
  updateError: (error: BookState["error"]) => ({
    type: "UPDATE_ERROR" as const,
    payload: error,
  }),
  updateValErrors: (payload: { errors: Map<string, string> }) => ({
    type: "ADD_CLIENT_UPDATE_ERRORS" as const,
    payload,
  }),
  updateTouched: (prop: any) => ({
    type: "UPDATE_TOUCHED" as const,
    payload: { prop },
  }),
  initializeFields: ()=> ({
    type: "INITIAL_DATA" as const
  })
};

export type BookActionTypes = ReturnType<
  typeof bookActions[keyof typeof bookActions]
>["type"];

export type BookActions = ReturnType<
  typeof bookActions[keyof typeof bookActions]
>;
