import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { connectStore } from "../../store";
import { EquipmentState } from "./equipments.reducer";
import { EquipmentsActionTypes, equipmentsActions } from "./equipments.actions";
import { EquipmentsRow } from "./equipments.page";
import { api } from "../../app";

const mapStateToProps = (state: { equipmentsReducer: EquipmentState }) => ({
  ...state.equipmentsReducer,
  equipments: state.equipmentsReducer.equipments.map((equipment) => ({
    ...equipment,
  })),
});

const mapDispatchToProps = (dispatch: Dispatch<Action<EquipmentsActionTypes>>) => ({
  updateLoading: (loading: boolean) => dispatch(equipmentsActions.updateLoading({ loading })),

  updateEquipments: (equipments: EquipmentsRow[]) =>
    dispatch(
      equipmentsActions.updateEquipments({
        equipments: equipments.map(({ id, title, bookMultipleUnit, price, priceType }) => ({
          id,
          title,
          bookMultipleUnit,
          price,
          priceType,
        })),
      })
    ),

  fetchEquipments: () =>
    api.get("/V1/equipments").then(({ data }) => {
      dispatch(equipmentsActions.updateLoading({ loading: false }));
      dispatch(equipmentsActions.updateEquipments({ equipments: data }));
      dispatch(equipmentsActions.updateShowError({ showError: false }));
    }),

  deleteEquipment: (id: number) =>
    api
      .delete(`/V1/equipments/${id}`, { headers: { successMsg: "Equipment deleted successfully!" } })
      .then(() => {
        dispatch(equipmentsActions.updateLoading({ loading: true }));
        dispatch(equipmentsActions.updateShowError({ showError: false }));
      })
      .catch((error) => dispatch(equipmentsActions.updateShowError({ showError: true }))),

  hideError: () => dispatch(equipmentsActions.updateShowError({ showError: false })),
});

export const connectEquipments = connectStore(mapStateToProps, mapDispatchToProps);
