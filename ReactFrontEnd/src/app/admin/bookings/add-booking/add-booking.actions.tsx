export const addBookingActions = {
  reset: () => ({
    type: "ADD_BOOKING_RESET" as const,
  }),

  updateBookingActions: (type: string, payload: any) => ({
    type,
    payload,
  }),
  updateClientActions: (type: string, payload: string | number) => ({
    type,
    payload,
  }),
  setAvailableRooms: (
    availableRooms: {
      id: number;
      imageUrl: string;
      roomtype: string;
      capacity: number;
      bookings: number;
      status: string;
    }[]
  ) => ({
    type: "SET_AVAILABLE_ROOMS_IN_BOOKING" as const,
    payload: availableRooms,
  }),

  setAvailableLayouts: (
    availableLayouts: {
      id: number;
      image: string;
      title: string;
    }[]
  ) => ({
    type: "SET_AVAILABLE_LAYOUTS_IN_BOOKING" as const,
    payload: availableLayouts,
  }),

  setBlockedSlots: (blockedSlots: Set<number>) => ({
    type: "SET_BLOCKED_SLOTS_IN_BOOKING" as const,
    payload: blockedSlots,
  }),

  updateErrors: (payload: { errors: Map<string, string> }) => ({
    type: "ADD_BOOKING_UPDATE_ERRORS" as const,
    payload,
  }),
  updateprevDate: (payload: { date: Date }) => ({
    type: "SET_BOOKED_DATE" as const,
    payload,
  }),
};

export type AddBookingActionTypes = ReturnType<
  typeof addBookingActions[keyof typeof addBookingActions]
>["type"];

export type AddBookingActions = ReturnType<
  typeof addBookingActions[keyof typeof addBookingActions]
>;
