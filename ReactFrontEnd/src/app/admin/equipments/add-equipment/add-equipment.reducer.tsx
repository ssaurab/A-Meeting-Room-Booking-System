import { Reducer } from "@reduxjs/toolkit";
import { AddEquipmentActions } from "./add-equipment.actions";

export interface AddEquipmentState {
  existingEquipments: {
    id: number;
    title: string;
  }[];
  bookMultipleUnit: boolean;
  id: number;
  price: number;
  priceType: string;
  title: string;
  errors: Map<string, string>;
}

const initialState: AddEquipmentState = {
  existingEquipments: [],
  bookMultipleUnit: false,
  id: 0,
  price: 0,
  priceType: "",
  title: "",
  errors: new Map(),
};

export const addEquipmentReducer: Reducer<AddEquipmentState, AddEquipmentActions> = (state = initialState, action) => {
  switch (action.type) {
    case "ADD_EQUIPMENT_RESET":
      return { ...initialState };
    case "ADD_EQUIPMENT_SET_EXISTING":
      return { ...state, existingEquipments: action.payload.existingEquipments };
    case "UPDATE_BOOKMULTIPLEUNIT":
      return { ...state, bookMultipleUnit: action.payload.bookMultipleUnit };
    case "UPDATE_EQUIPPRICE":
      return { ...state, price: action.payload.price };
    case "UPDATE_PRICETYPE":
      return { ...state, priceType: action.payload.priceType };
    case "UPDATE_EQUIPTITLE":
      return { ...state, title: action.payload.title };
    case "ADD_EQUIPMENT_UPDATE_ERRORS":
      return { ...state, errors: action.payload.errors };

    default:
      return state;
  }
};
