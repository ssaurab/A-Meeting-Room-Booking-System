import { Reducer } from "@reduxjs/toolkit";
import { EquipmentsActions } from "./equipments.actions";

export interface EquipmentState {
  loading: boolean;
  equipments: {
    id: number;
    title: string;
    bookMultipleUnit: boolean;
    priceType: string;
    price: number;
  }[];
    showError : boolean;
}

const initialState: EquipmentState = {
  loading: true,
  showError : false,
  equipments: [],
};

export const equipmentsReducer: Reducer<EquipmentState, EquipmentsActions> = (
  state = initialState,
  action
) => {
  switch (action.type) {
    case "UPDATE_EQUIPMENTS":
      return { ...state, equipments: action.payload.equipments };
    case "UPDATE_EQUIPLOADING":
      return { ...state, loading: action.payload.loading };
    case "UPDATE_EQUIPSHOWERROR":
        return { ...state, showError: action.payload.showError };
    default:
      return state;
  }
};
