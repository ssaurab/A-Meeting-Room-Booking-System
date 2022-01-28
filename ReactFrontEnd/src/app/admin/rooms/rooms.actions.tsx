export const roomsActions = {
  updateLoading: (payload: { loading: boolean }) => ({
    type: "UPDATE_ROOMS_LOADING" as const,
    payload,
  }),

  updateShowError: (payload: { showError: boolean }) => ({
    type: "UPDATE_ROOMSSHOWERROR" as const,
    payload,
  }),

  updateRooms: (payload: {
    rooms: {
      id: number;
      image: string;
      name: string;
      capacity: number;
      bookings: number;
      status: string;
    }[];
  }) => ({
    type: "UPDATE_ROOMS" as const,
    payload,
  }),
};

export type RoomActionTypes = ReturnType<
  typeof roomsActions[keyof typeof roomsActions]
>["type"];

export type RoomActions = ReturnType<
  typeof roomsActions[keyof typeof roomsActions]
>;
