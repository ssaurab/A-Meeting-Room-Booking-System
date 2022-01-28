import { createStore, combineReducers, applyMiddleware } from "@reduxjs/toolkit";
import { connect, InferableComponentEnhancerWithProps } from "react-redux";
import { FunctionComponent } from "react";
import { loginReducer } from "./admin/login/login.reducer";
import { forgotPasswordReducer } from "./admin/forgot-password/forgot-password.reducer";
import { resetReducer } from "./admin/forgot-password/reset/reset.reducer";
import { bookReducer } from "./book/book.reducer";
import { addLayoutReducer } from "./admin/layouts/add-layout/add-layout.reducer";
import { layoutsReducer } from "./admin/layouts/layouts.reducer";
import { equipmentsReducer } from "./admin/equipments/equipments.reducer";
import { snacksReducer } from "./admin/food-drinks/food-drinks.reducer";
import { addFoodDrinksReducer } from "./admin/food-drinks/add-food-drinks/add-food-drinks.reducer";
import { addEquipmentReducer } from "./admin/equipments/add-equipment/add-equipment.reducer";
import { usersReducer } from "./admin/users/users.reducer";
import { addUserReducer } from "./admin/users/add-user/add-user.reducer";
import { bookingsReducer } from "./admin/bookings/bookings.reducer";
import { addRoomReducer } from "./admin/rooms/add-room/add-room.reducer";
import { roomsReducer } from "./admin/rooms/rooms.reducer";
import { landingReducer } from "./landing/landing.reducer";
import { addBookingReducer } from "./admin/bookings/add-booking/add-booking.reducer";
import { dashboardReducer } from "./admin/dashboard/dashboard.reducer";
import { composeWithDevTools } from "redux-devtools-extension";
import { snackbarReducer } from "./snackbar/snackbar.reducer";

const allReducers = {
  snackbarReducer,
  loginReducer,
  forgotPasswordReducer,
  resetReducer,
  bookReducer,
  addLayoutReducer,
  layoutsReducer,
  bookingsReducer,
  addBookingReducer,
  equipmentsReducer,
  addEquipmentReducer,
  snacksReducer,
  addFoodDrinksReducer,
  usersReducer,
  addUserReducer,
  addRoomReducer,
  roomsReducer,
  landingReducer,
  dashboardReducer,
};

const fullCombinedReducer = combineReducers(allReducers);

export const store = createStore(
  ((state, action) => {
    const nextState: any = { ...state };
    if (action.type === "RESET_REDUCER") {
      nextState[action.payload.reducer] = undefined;
    }
    return combineReducers(allReducers)(nextState, action);
  }) as typeof fullCombinedReducer,
  process.env.NODE_ENV !== "production" ? composeWithDevTools(applyMiddleware()) : undefined
);

export const resetStore = (reducer: keyof typeof allReducers) =>
  store.dispatch({ type: "RESET_REDUCER", payload: { reducer } });

export const connectStore = <TStateProps, TDispatchProps>(
  mapStateToProps: (...args: any) => TStateProps,
  mapDispatchToProps: (...args: any) => TDispatchProps
) =>
  connect(mapStateToProps, mapDispatchToProps) as <TOwnProps>(
    c: FunctionComponent<TOwnProps & TStateProps & TDispatchProps>
  ) => ReturnType<InferableComponentEnhancerWithProps<TStateProps & TDispatchProps, TOwnProps>>;
