export const bookingsActions = {
  updateLoading: (payload: { loading: boolean }) => ({
    type: "UPDATE_BOOKINGLOADING" as "UPDATE_BOOKINGLOADING",
    payload,
  }),
  updateBookings: (payload: {
    bookings: {
      id: number;
      room: string;
      date: Date;
      name: string;
      total: number;
      status: string;
      roomId: number;
    }[];
  }) => ({
    type: "UPDATE_BOOKINGS" as "UPDATE_BOOKINGS",
    payload,
  }),
};

export type BookingsActionTypes = ReturnType<
  typeof bookingsActions[keyof typeof bookingsActions]
>["type"];

export type BookingsActions = ReturnType<
  typeof bookingsActions[keyof typeof bookingsActions]
>;
