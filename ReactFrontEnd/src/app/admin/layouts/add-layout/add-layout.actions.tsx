export const addLayoutActions = {
  reset: () => ({
    type: "ADD_LAYOUT_RESET" as const,
  }),

  setExisting: (payload: {
    layouts: {
      id: number;
      title: string;
      image: string;
      rooms: string[];
    }[];
  }) => ({
    type: "ADD_LAYOUT_SET_EXISTING_LAYOUTS" as const,
    payload,
  }),

  updateTitle: (payload: { title: string }) => ({
    type: "ADD_LAYOUT_UPDATE_TITLE" as const,
    payload,
  }),

  updateImage: (payload: { image: string }) => ({
    type: "ADD_LAYOUT_UPDATE_IMAGE" as const,
    payload,
  }),

  updateErrors: (payload: { errors: Map<string, string> }) => ({
    type: "ADD_LAYOUT_UPDATE_ERRORS" as const,
    payload,
  }),
};

export type AddLayoutActionTypes = ReturnType<typeof addLayoutActions[keyof typeof addLayoutActions]>["type"];

export type AddLayoutActions = ReturnType<typeof addLayoutActions[keyof typeof addLayoutActions]>;
