export const addEquipmentActions = {
  reset: () => ({
    type: "ADD_EQUIPMENT_RESET" as const,
  }),

  setExisitng: (payload: {
    existingEquipments: {
      id: number;
      title: string;
    }[];
  }) => ({
    type: "ADD_EQUIPMENT_SET_EXISTING" as const,
    payload,
  }),

  updateTitle: (payload: { title: string }) => ({
    type: "UPDATE_EQUIPTITLE" as const,
    payload,
  }),

  updatePrice: (payload: { price: number }) => ({
    type: "UPDATE_EQUIPPRICE" as const,
    payload,
  }),

  updateBookMultipleUnit: (payload: { bookMultipleUnit: boolean }) => ({
    type: "UPDATE_BOOKMULTIPLEUNIT" as const,
    payload,
  }),

  updatePriceType: (payload: { priceType: string }) => ({
    type: "UPDATE_PRICETYPE" as "UPDATE_PRICETYPE",
    payload,
  }),

  updateErrors: (payload: { errors: Map<string, string> }) => ({
    type: "ADD_EQUIPMENT_UPDATE_ERRORS" as const,
    payload,
  }),
};

export type AddEquipmentActionTypes = ReturnType<typeof addEquipmentActions[keyof typeof addEquipmentActions]>["type"];

export type AddEquipmentActions = ReturnType<typeof addEquipmentActions[keyof typeof addEquipmentActions]>;
