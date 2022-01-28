// import { Reducer } from "@reduxjs/toolkit";
import { AddBookingActions } from "./add-booking.actions";
import * as actionTypes from "./add-booking.types";

export interface AddBookingState {
  availableRooms: {
    id: number;
    name: string;
    image: string;
    capacity: number;
    description: string;
    pricePerHour: number;
    pricePerHalfDay: number;
    pricePerDay: number;
    status: string;
  }[];
  availableLayouts: {
    id: number;
    image: string;
    title: string;
  }[];
  blockedSlots: Set<number>;
  booking: actionTypes.Booking;
  client: actionTypes.Client;
  errors: Map<string, string>;
  prevBookedDate: Date | null;
}

const initialState: AddBookingState = {
  availableRooms: [],
  availableLayouts: [],
  blockedSlots: new Set<number>(),
  booking: {
    attendees: 0,
    date: new Date(),
    status: "",
    deposit: 0,
    equipmentPrice: 0,
    foodAndDrinkPrice: 0,
    paymentMethod: "",
    roomPrice: 0,
    subTotal: 0,
    tax: 0,
    total: 0,
    duration: "Select Hours",
    equipments: [],
    room: {
      id: 0,
    },
    roomLayout: {
      id: 0,
    },
    snacks: [],
    fromHour: 0,
    toHour: 0,
  },

  client: {
    address: "",
    city: "",
    company: "",
    country: "",
    email: "",
    id: 0,
    name: "",
    notes: "",
    phone: "",
    state: "",
    title: "",
    zip: "",
  },
  prevBookedDate: null,
  errors: new Map(),
};

export const addBookingReducer = (
  state = initialState,
  action: AddBookingActions
) => {
  switch (action.type) {
    case "ADD_BOOKING_RESET":
      return { ...initialState };
    //booking details related actions
    case actionTypes.UPDATE_BOOKING_DATE:
      return { ...state, booking: { ...state.booking, date: action.payload } };
    case actionTypes.UPDATE_BOOKING_ATTENDEES:
      return {
        ...state,
        booking: { ...state.booking, attendees: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_STATUS:
      return {
        ...state,
        booking: { ...state.booking, status: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_DEPOSIT:
      return {
        ...state,
        booking: { ...state.booking, deposit: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_EQUIPMENT_PRICE:
      return {
        ...state,
        booking: { ...state.booking, equipmentPrice: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_FOOD_AND_DRINK_PRICE:
      return {
        ...state,
        booking: { ...state.booking, foodAndDrinkPrice: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_PAYMENT_METHOD:
      return {
        ...state,
        booking: { ...state.booking, paymentMethod: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_ROOM_PRICE:
      return {
        ...state,
        booking: { ...state.booking, roomPrice: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_SUB_TOTAL:
      return {
        ...state,
        booking: { ...state.booking, subTotal: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_TAX:
      return { ...state, booking: { ...state.booking, tax: action.payload } };
    case actionTypes.UPDATE_BOOKING_TOTAL:
      return { ...state, booking: { ...state.booking, total: action.payload } };
    case actionTypes.UPDATE_BOOKING_SNACKS:
      return {
        ...state,
        booking: { ...state.booking, snacks: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_EQUIPMENT:
      return {
        ...state,
        booking: { ...state.booking, equipments: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_FROMHOUR:
      return {
        ...state,
        booking: { ...state.booking, fromHour: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_TOHOUR:
      return {
        ...state,
        booking: { ...state.booking, toHour: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_ROOM:
      return { ...state, booking: { ...state.booking, room: action.payload } };
    case actionTypes.UPDATE_BOOKING_ROOM_LAYOUT:
      return {
        ...state,
        booking: { ...state.booking, roomLayout: action.payload },
      };
    case actionTypes.UPDATE_BOOKING_COSTS:
      return { ...state, booking: { ...state.booking, ...action.payload } };
    case actionTypes.UPDATE_BOOKING_DURATION:
      return {
        ...state,
        booking: { ...state.booking, duration: action.payload },
      };

    // client details related actions
    case actionTypes.UPDATE_CLIENT_NAME:
      return { ...state, client: { ...state.client, name: action.payload } };
    case actionTypes.UPDATE_CLIENT_ADDRESS:
      return { ...state, client: { ...state.client, address: action.payload } };
    case actionTypes.UPDATE_CLIENT_CITY:
      return { ...state, client: { ...state.client, city: action.payload } };
    case actionTypes.UPDATE_CLIENT_COMPANY:
      return { ...state, client: { ...state.client, company: action.payload } };
    case actionTypes.UPDATE_CLIENT_COUNTRY:
      return { ...state, client: { ...state.client, country: action.payload } };
    case actionTypes.UPDATE_CLIENT_EMAIL:
      return { ...state, client: { ...state.client, email: action.payload } };
    case actionTypes.UPDATE_CLIENT_NOTES:
      return { ...state, client: { ...state.client, notes: action.payload } };
    case actionTypes.UPDATE_CLIENT_PHONE:
      return { ...state, client: { ...state.client, phone: action.payload } };
    case actionTypes.UPDATE_CLIENT_STATE:
      return { ...state, client: { ...state.client, state: action.payload } };
    case actionTypes.UPDATE_CLIENT_TITLE:
      return { ...state, client: { ...state.client, title: action.payload } };
    case actionTypes.UPDATE_CLIENT_ZIP:
      return { ...state, client: { ...state.client, zip: action.payload } };
    case "SET_AVAILABLE_ROOMS_IN_BOOKING":
      return { ...state, availableRooms: action.payload };
    case "SET_AVAILABLE_LAYOUTS_IN_BOOKING":
      return { ...state, availableLayouts: action.payload };
    case "SET_BLOCKED_SLOTS_IN_BOOKING":
      return { ...state, blockedSlots: action.payload };
    case "ADD_BOOKING_UPDATE_ERRORS":
      return { ...state, errors: action.payload.errors };
    case "SET_BOOKED_DATE":
      return { ...state, prevBookedDate: action.payload.date };
    default:
      return state;
  }
};
