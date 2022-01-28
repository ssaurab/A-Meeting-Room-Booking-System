import { AddEquipmentState } from "./add-equipment.reducer";
import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { AddEquipmentActionTypes, addEquipmentActions } from "./add-equipment.actions";
import { connectStore } from "../../../store";
import { EquipmentsActionTypes } from "../equipments.actions";
import { api } from "../../../app";

const mapStateToProps = (state: { addEquipmentReducer: AddEquipmentState }) => ({
  ...state.addEquipmentReducer,
});

const mapDispatchToProps = (dispatch: Dispatch<Action<AddEquipmentActionTypes> | Action<EquipmentsActionTypes>>) => ({
  fetchEquipments: () =>
    api.get("/V1/equipments").then(({ data }) => dispatch(addEquipmentActions.setExisitng({ existingEquipments: data }))),

  updateTitle: (title: string) => dispatch(addEquipmentActions.updateTitle({ title })),

  updatePrice: (price: number) => dispatch(addEquipmentActions.updatePrice({ price })),

  updateBookMultipleUnit: (checked: boolean) =>
    dispatch(
      addEquipmentActions.updateBookMultipleUnit({
        bookMultipleUnit: checked,
      })
    ),

  updatePriceType: (priceType: string) =>
    dispatch(
      addEquipmentActions.updatePriceType({
        priceType: priceType,
      })
    ),

  resetEquipment: () => dispatch(addEquipmentActions.reset()),

  validate: (props: AddEquipmentState, isEdit: boolean) => {
    const errors = new Map<string, string>();

    if (!props.title) errors.set("title", "Title is Required");
    else if (!isEdit && props.existingEquipments.find((r) => r.title === props.title))
      errors.set("title", "Title already exists");
    if (!props.price) errors.set("price", "Price is Required");
    else if (props.price <= 0) errors.set("price", "Price shouldn't be non-positive");
    if (!props.priceType) errors.set("priceType", "Please indicate the price type");

    dispatch(addEquipmentActions.updateErrors({ errors }));
    return !errors.size;
  },
  addError: (name: string, errorString: string, errorProps: Map<string, string>) => {
    const errors = new Map(errorProps);
    errors.set(name, errorString);
    dispatch(addEquipmentActions.updateErrors({ errors }));
  },
  deleteError: (name: string, errorProps: Map<string, string>) => {
    const errors = new Map(errorProps);
    errors.delete(name);
    dispatch(addEquipmentActions.updateErrors({ errors }));
  },
});

export const connectAddEquipment = connectStore(mapStateToProps, mapDispatchToProps);
