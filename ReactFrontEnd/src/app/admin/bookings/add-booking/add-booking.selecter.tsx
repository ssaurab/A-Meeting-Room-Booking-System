import { Action } from "@reduxjs/toolkit";
import { format } from "date-fns";
import { Dispatch } from "react";
import { api, range } from "../../../app";
import { connectStore } from "../../../store";
import { loginActions } from "../../login/login.actions";
import { addBookingActions, AddBookingActionTypes } from "./add-booking.actions";
import { AddBookingState } from "./add-booking.reducer";
import * as actionTypes from "./add-booking.types";

const mapStateToProps = (state: { addBookingReducer: AddBookingState }) => ({
  ...state.addBookingReducer,
  booking: {
    ...state.addBookingReducer.booking,
    room: state.addBookingReducer.availableRooms.find((r) => r.id === state.addBookingReducer.booking.room.id) ?? {
      id: 0,
      capacity: 0,
    },
  },
  bookingCosts: (() => {
    const numberOfHours =
      !!state.addBookingReducer.booking.toHour && !!state.addBookingReducer.booking.fromHour
        ? state.addBookingReducer.booking.toHour - state.addBookingReducer.booking.fromHour
        : 0;
    const equipmentPrice = state.addBookingReducer.booking.equipments
      .filter((eq) => eq.selected)
      .reduce((s, eq) => {
        const eqMultiply = eq.priceType === "perHour" ? numberOfHours : 1;
        return s + (eq.quantity > 0 ? eq.quantity : 0) * eq.price * eqMultiply;
      }, 0);
    const foodAndDrinkPrice = state.addBookingReducer.booking.snacks
      .filter((s) => s.selected)
      .reduce((sum, s) => sum + (s.quantity > 0 ? s.quantity : 0) * s.price, 0);
    const room = state.addBookingReducer.availableRooms.find((r) => r.id === state.addBookingReducer.booking.room.id);
    const roomPrice =
      state.addBookingReducer.booking.duration === "Select Hours"
        ? (room?.pricePerHour ?? 0) * numberOfHours
        : room?.pricePerHalfDay ?? 0;
    const subTotal = equipmentPrice + foodAndDrinkPrice + roomPrice;
    const tax = 0.1 * subTotal;
    const total = 1.1 * subTotal;
    const deposit = 0.1 * total;
    return {
      deposit,
      total,
      tax,
      subTotal,
      roomPrice,
      equipmentPrice,
      foodAndDrinkPrice,
    };
  })(),
});

const mapDispatchToProps = (dispatch: Dispatch<Action<AddBookingActionTypes>>) => ({
  resetForm: () => dispatch(addBookingActions.reset()),
  updateBookedDate: (date: Date) => dispatch(addBookingActions.updateprevDate({ date })),
  //Booking Details Actions Related
  updateBookingDate: (payload: any) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_DATE, payload)),
  editBookingDate: (payload: any) => {
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_DATE, payload));
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_FROMHOUR, 0));
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_TOHOUR, 0));
  },
  updateBookingAttendees: (payload: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_ATTENDEES, payload)),
  updateBookingStatus: (payload: string) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_STATUS, payload)),
  updateBookingDeposit: (payload: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_DEPOSIT, payload)),
  updateBookingEquipmentPrice: (payload: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_EQUIPMENT_PRICE, payload)),
  updateBookingFoodAndDrinkPrice: (payload: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_FOOD_AND_DRINK_PRICE, payload)),
  updateBookingPaymentMethod: (payload: string) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_PAYMENT_METHOD, payload)),
  updateBookingRoomPrice: (payload: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_ROOM_PRICE, payload)),
  updateBookingSubTotal: (payload: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_SUB_TOTAL, payload)),
  updateBookingTax: (payload: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_TAX, payload)),
  updateBookingDuration: (payload: string) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_DURATION, payload)),
  updateBookingTotal: (payload: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_TOTAL, payload)),
  updateBookingRoom: (room: AddBookingState["availableRooms"][0]) => {
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_ROOM, room));
    // Fetch available layouts for the room
    api.get(`/V1/rooms/${room.id}/layouts`).then(({ data }) => dispatch(addBookingActions.setAvailableLayouts(data)));
  },
  updateBookingFromHour: (hour: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_FROMHOUR, hour)),
  updateBookingToHour: (hour: number) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_TOHOUR, hour)),
  updateBookingRoomLayout: (layout: AddBookingState["availableLayouts"][0]) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_ROOM_LAYOUT, layout)),
  updateBookingSnacks: (snacks: any) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_SNACKS, snacks)),
  updateBookingEquipment: (equipments: any) =>
    dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_EQUIPMENT, equipments)),

  updateBookingCosts: (costs: {
    deposit: number;
    equipmentPrice: number;
    foodAndDrinkPrice: number;
    roomPrice: number;
    subTotal: number;
    tax: number;
    total: number;
  }) => dispatch(addBookingActions.updateBookingActions(actionTypes.UPDATE_BOOKING_COSTS, costs)),

  fetchRooms: () =>
    api
      .get("/V1/rooms")
      .then(({ data }) =>
        dispatch(addBookingActions.setAvailableRooms(data.filter((r: any) => r.status?.toLowerCase() === "active")))
      ),

  fetchSnacks: () =>
    api.get("/V1/snacks").then(({ data }) => {
      dispatch(
        addBookingActions.updateBookingActions(
          actionTypes.UPDATE_BOOKING_SNACKS,
          data.map((d: any) => ({ ...d, quantity: 1, selected: false }))
        )
      );
    }),

  fetchEquipment: () =>
    api.get("/V1/equipments").then(({ data }) => {
      dispatch(
        addBookingActions.updateBookingActions(
          actionTypes.UPDATE_BOOKING_EQUIPMENT,
          data.map((d: any) => ({ ...d, quantity: 1, selected: false }))
        )
      );
    }),

  fetchBlockedSlots: (date: Date, roomId: number, existing: number[]) => {
    api
      .get(`/V1/bookings/bookingslots/${format(date, "yyyy-MM-dd")}/${roomId}`)
      .then(({ data }: { data: { key: number; value: number }[] }) => {
        const hours = new Set<number>();
        data.forEach(({ key, value }) =>
          range(key, value)
            .filter((s) => !existing.includes(s))
            .forEach((s) => hours.add(s))
        );
        dispatch(addBookingActions.setBlockedSlots(hours));
      });
  },

  //CLIENT ACTIONS RELATED
  updateClientName: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_NAME, payload)),

  updateClientAddress: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_ADDRESS, payload)),
  updateClientCity: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_CITY, payload)),
  updateClientCompany: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_COMPANY, payload)),
  updateClientCountry: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_COUNTRY, payload)),
  updateClientEmail: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_EMAIL, payload)),
  updateClientNotes: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_NOTES, payload)),
  updateClientPhone: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_PHONE, payload)),
  updateClientState: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_STATE, payload)),
  updateClientZip: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_ZIP, payload)),
  updateClientTitle: (payload: string) =>
    dispatch(addBookingActions.updateClientActions(actionTypes.UPDATE_CLIENT_TITLE, payload)),

  validateBookingDetails: (props: ReturnType<typeof mapStateToProps>) => {
    const errors = new Map<string, string>();
    if (!props.booking.status) errors.set("status", "Status is Required");
    if (!props.booking.room?.id) errors.set("room", "Room is Required");
    if (!props.booking.roomLayout?.id) errors.set("roomLayout", "Room Layout is Required");
    if (!props.booking.attendees) errors.set("attendees", "Attendees must be non-zero");
    if (props.booking.attendees < 0) errors.set("attendees", "Attendees must be positive");
    if (props.booking.attendees > props.booking.room.capacity)
      errors.set("attendees", `Max capacity for selected room is ${props.booking.room.capacity}`);
    if (!props.booking.paymentMethod) errors.set("paymentMethod", "Payment Method is Required");
    if (!props.booking.fromHour) errors.set("fromHour", "From hour is Required");
    if (!props.booking.toHour) errors.set("toHour", "To hour is Required");

    if (errors.size || props.errors.size) dispatch(addBookingActions.updateErrors({ errors }));
    return !errors.size;
  },

  validateEquipmentDetails: (props: ReturnType<typeof mapStateToProps>) => {
    const errors = new Map<string, string>();
    props.booking.equipments.forEach(({ quantity, id, selected }) => {
      if (quantity < 0 && selected) {
        errors.set(`equipment_quantity${id}`, "quantity is negative");
      }
    });

    dispatch(addBookingActions.updateErrors({ errors }));
    return !errors.size;
  },
  validateFoodDetails: (props: ReturnType<typeof mapStateToProps>) => {
    const errors = new Map<string, string>();
    props.booking.snacks.forEach(({ quantity, id, selected }) => {
      if (quantity < 0 && selected) {
        errors.set(`food_quantity${id}`, "quantity is negative");
      }
    });

    dispatch(addBookingActions.updateErrors({ errors }));
    return !errors.size;
  },

  validateClientDetails: (props: ReturnType<typeof mapStateToProps>) => {
    const errors = new Map<string, string>();
    console.log("validateClientDetails");
    const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    const phoneRegex = /^\d{10}$/;
    const zipRegex = /^\d{6}$/;
    if (!props.client.title) errors.set("client.title", "Title is Required");
    if (!props.client.name) errors.set("client.name", "Name is Required");
    if (!props.client.email) errors.set("client.email", "Email is Required");
    else if (!emailRegex.test(props.client.email)) errors.set("client.email", "Invalid email address");
    if (!props.client.phone) errors.set("client.phone", "Phone is Required");
    else if (!phoneRegex.test(props.client.phone)) errors.set("client.phone", "Phone number should be 10 digits");
    if (!props.client.company) errors.set("client.company", "Company is Required");
    if (!props.client.address) errors.set("client.address", "Address is Required");
    if (!props.client.city) errors.set("client.city", "City is Required");
    if (!props.client.state) errors.set("client.state", "State is Required");
    if (!props.client.zip) errors.set("client.zip", "Zip is Required");
    else if (!zipRegex.test(props.client.zip)) errors.set("client.zip", "Postal Code should be 6 digits");
    if (!props.client.country) errors.set("client.country", "Country is Required");

    dispatch(addBookingActions.updateErrors({ errors }));
    return !errors.size;
  },

  addError: (name: string, errorString: string, errorProps: Map<string, string>) => {
    const errors = new Map(errorProps);
    errors.set(name, errorString);
    dispatch(addBookingActions.updateErrors({ errors }));
  },
  deleteError: (name: string, errorProps: Map<string, string>) => {
    const errors = new Map(errorProps);
    errors.delete(name);
    dispatch(addBookingActions.updateErrors({ errors }));
  },
  openSnackBar: (bool: boolean) => dispatch(loginActions.openSnackBar(bool)),
  setErrorMessage: (msg: string) => dispatch(loginActions.setErrorMessage(msg)),
  setSnackBarType: (type: "error" | "success" | "info" | "warning" | undefined) =>
    dispatch(loginActions.setSnackBarType(type)),
});

export const connectAddBooking = connectStore(mapStateToProps, mapDispatchToProps);
