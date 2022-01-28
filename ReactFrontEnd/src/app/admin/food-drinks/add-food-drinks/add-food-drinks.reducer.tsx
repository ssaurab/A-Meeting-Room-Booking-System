import { AddFoodDrinksActions } from "./add-food-drinks.actions";
import { Reducer } from "@reduxjs/toolkit";

export interface AddFoodDrinksState {
  existingSnacks: {
    id: number;
    price: number;
    title: string;
  }[];
  id: number;
  food_price: number;
  food_title: string;
  errors: Map<string, string>;
}

const initialState: AddFoodDrinksState = {
  existingSnacks: [],
  id: 0,
  food_price: 0,
  food_title: "",
  errors: new Map(),
};
export const addFoodDrinksReducer: Reducer<
  AddFoodDrinksState,
  AddFoodDrinksActions
> = (state = initialState, action) => {
  switch (action.type) {
    case "ADD_FOOD_RESET":
      return { ...initialState };
    case "UPDATE_FOODTITLE" as "UPDATE_FOODTITLE":
      return { ...state, food_title: action.payload.title };
    case "UPDATE_FOODPRICE" as "UPDATE_FOODPRICE":
      return { ...state, food_price: action.payload.price };
    case "ADD_FOOD_SET_EXISTING_SNACKS":
      return { ...state, existingSnacks: action.payload.snacks };
    case "ADD_FOOD_UPDATE_ERRORS":
      return { ...state, errors: action.payload.errors };
    default:
      return state;
  }
};
