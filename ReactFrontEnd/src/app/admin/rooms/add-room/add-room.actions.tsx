export const addRoomActions = {
  resetAddRoom: () => ({
    type: "ADD_ROOM_RESET_ADD_ROOM" as const,
  }),

  updateTitle: (payload: { title: string }) => ({
    type: "ADD_ROOM_UPDATE_TITLE" as const,
    payload,
  }),

  updateImage: (payload: { image: string }) => ({
    type: "ADD_ROOM_UPDATE_IMAGE" as const,
    payload,
  }),

  updateCapacity: (payload: { capacity: number }) => ({
    type: "ADD_ROOM_UPDATE_CAPACITY" as const,
    payload,
  }),

  updateDescription: (payload: { description: string }) => ({
    type: "ADD_ROOM_UPDATE_DESCRIPTION" as const,
    payload,
  }),

  updatePrice: (payload: { per: "hour" | "half-day" | "day"; price: number }) => ({
    type: "ADD_ROOM_UPDATE_PRICE" as const,
    payload,
  }),

  togglePrice: (payload: { per: "hour" | "half-day" | "day" }) => ({
    type: "ADD_ROOM_TOGGLE_PRICE" as const,
    payload,
  }),

  updateStatus: (payload: { status: string }) => ({
    type: "ADD_ROOM_UPDATE_STATUS" as const,
    payload,
  }),

  setLayouts: (payload: { layouts: { id: number; title: string; image: string; selected: boolean }[] }) => ({
    type: "ADD_ROOM_SET_LAYOUTS" as const,
    payload,
  }),

  toggleLayout: (payload: { layoutId: number }) => ({
    type: "ADD_ROOM_TOGGLE_LAYOUT" as const,
    payload,
  }),

  updateErrors: (payload: { errors: Map<string, string> }) => ({
    type: "ADD_ROOM_UPDATE_ERRORS" as const,
    payload,
  }),
  setRooms: (payload: {title:string}[]) => ({
    type: "ADD_ROOM_SET_ROOMS" as const,
    payload,
  })
};

export type AddRoomActionTypes = ReturnType<typeof addRoomActions[keyof typeof addRoomActions]>["type"];

export type AddRoomActions = ReturnType<typeof addRoomActions[keyof typeof addRoomActions]>;
