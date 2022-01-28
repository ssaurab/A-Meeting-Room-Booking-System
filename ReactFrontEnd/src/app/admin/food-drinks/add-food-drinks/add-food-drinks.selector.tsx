import { AddFoodDrinksState } from "./add-food-drinks.reducer";
import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import {
  AddFoodDrinksActions,
  addFoodDrinksActions,
} from "./add-food-drinks.actions";
import { SnacksActionTypes } from "../food-drinks.action";
import { connectStore } from "../../../store";
import { api } from "../../../app";

const mapStateToProps = (state: {
  addFoodDrinksReducer: AddFoodDrinksState;
}) => ({
  ...state.addFoodDrinksReducer,
});

const mapDispatchToProps = (
  dispatch: Dispatch<AddFoodDrinksActions | Action<SnacksActionTypes>>
) => ({
  fetchExistingSnacks: () => {
    api
      .get("/V1/snacks")
      .then(({ data }) =>
        dispatch(addFoodDrinksActions.setExisting({ snacks: data }))
      );
  },

  updateTitle: (title: string) =>
    dispatch(addFoodDrinksActions.updateTitle({ title: title })),
  updatePrice: (price: number) =>
    dispatch(addFoodDrinksActions.updatePrice({ price: price })),
  resetFood: () => dispatch(addFoodDrinksActions.reset()),

  validate: (props: AddFoodDrinksState, isEdit: boolean) => {
    const errors = new Map<string, string>();

    if (!props.food_price) errors.set("price", "Price cannot be zero");
    else if (props.food_price <= 0)
      errors.set("price", "Price shouldn't be non-positive");
    if (!props.food_title) errors.set("title", "Please name the Food or Drink");
    else if (
      !isEdit &&
      props.existingSnacks.find((l) => l.title === props.food_title)
    )
      errors.set("title", "The food or drink already exists");

    dispatch(addFoodDrinksActions.updateErrors({ errors }));
    return !errors.size;
  },
  addError: (
    name: string,
    errorString: string,
    errorProps: Map<string, string>
  ) => {
    const errors = new Map(errorProps);
    errors.set(name, errorString);
    dispatch(addFoodDrinksActions.updateErrors({ errors }));
  },
  deleteError: (name: string, errorProps: Map<string, string>) => {
    const errors = new Map(errorProps);
    errors.delete(name);
    dispatch(addFoodDrinksActions.updateErrors({ errors }));
  },
});

export const connectAddFoodDrinks = connectStore(
  mapStateToProps,
  mapDispatchToProps
);
