import { Reducer } from "@reduxjs/toolkit";
import { BookActions } from "./book.actions";

export interface BookState {
  error: boolean;
  touched: Set<string>;
  errors: Map<string, string>;
  room: {
    capacity: number;
    description: string;
    id: number;
    image: string;
    name: string;
    pricePerDay: number;
    pricePerHalfDay: number;
    pricePerHour: number;
    status: string;
  };
  bookRoom: {
    blockedSlots: Set<number>;
    date: Date;
    from: number;
    to: number;
    attendees: number;
  };
  roomSetup: {
    availableLayouts: {
      id: number;
      image: string;
      title: string;
    }[];
    selectedLayout: {
      id: number;
      image: string;
      title: string;
    };
    availableEquipments: {
      id: number;
      title: string;
      priceType: string;
      price: number;
      bookMultipleUnit: boolean;
      quantity: number;
      selected: boolean;
    }[];
    selectedEquipments: {
      id: number;
      title: string;
      bookMultipleUnit: true;
      priceType: string;
      price: number;
      count: number;
    }[];
  };
  foodDrinks: {
    availableSnacks: {
      id: number;
      title: string;
      price: number;
      selected: boolean;
      count: number;
    }[];
    selectedSnacks: {
      id: number;
      title: string;
      price: number;
      count: number;
    }[];
  };
  checkout: {
    clientDetails: {
      address: string;
      city: string;
      company: string;
      country: string;
      email: string;
      id: number;
      name: string;
      notes: string;
      phone: string;
      state: string;
      title: string;
      zip: string;
    };
    paymentDetails: {
      method: "Cash" | "Credit card" | "PayPal";
      nameOnCard: string;
      cardNumber: string;
      expiryDate: string;
      cvv: string;
    };
  };
}

const initialState: BookState = {
  error: true,
  errors: new Map(),
  touched: new Set(),
  room: {
    capacity: 0,
    description: "",
    id: 1,
    image: "",
    name: "",
    pricePerDay: 1,
    pricePerHalfDay: 1,
    pricePerHour: 1,
    status: "",
  },
  bookRoom: {
    blockedSlots: new Set<number>(),
    date: new Date(),
    from: 0,
    to: 0,
    attendees: 0,
  },
  roomSetup: {
    availableLayouts: [],
    selectedLayout: {
      id: 0,
      image: "",
      title: "",
    },
    availableEquipments: [],
    selectedEquipments: [],
  },
  foodDrinks: {
    availableSnacks: [],
    selectedSnacks: [],
  },
  checkout: {
    clientDetails: {
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
    paymentDetails: {
      method: "Cash",
      nameOnCard: "",
      cardNumber: "",
      expiryDate: "",
      cvv: "",
    },
  },
};

export const bookReducer: Reducer<BookState, BookActions> = (
  state = initialState,
  action
) => {
  switch (action.type) {
    case "SET_ROOM":
      return { ...state, room: action.payload.room };
    case "UPDATE_BOOK_ROOM": {
      const bookRoom: any = { ...state.bookRoom };
      bookRoom[action.payload.prop] = action.payload.value;
      const touched = new Set(state.touched);
      touched.add(action.payload.prop);
      return { ...state, bookRoom, touched };
    }
    case "UPDATE_ROOM_SETUP": {
      const roomSetup: any = { ...state.roomSetup };
      roomSetup[action.payload.prop] = action.payload.value;
      const touched = new Set(state.touched);
      touched.add(action.payload.prop);
      return { ...state, roomSetup, touched };
    }
    case "UPDATE_FOOD_DRINKS": {
      const foodDrinks: any = { ...state.foodDrinks };
      foodDrinks[action.payload.prop] = action.payload.value;
      const touched = new Set(state.touched);
      touched.add(action.payload.prop);
      return { ...state, foodDrinks, touched };
    }
    case "UPDATE_CHECKOUT_CLIENT": {
      const clientDetails: any = { ...state.checkout.clientDetails };
      clientDetails[action.payload.prop] = action.payload.value;
      const touched = new Set(state.touched);
      touched.add(action.payload.prop);
      return { ...state, checkout: { ...state.checkout, clientDetails }, touched };
    }
    case "UPDATE_CHECKOUT_PAYMENT": {
      const paymentDetails: any = { ...state.checkout.paymentDetails };
      paymentDetails[action.payload.prop] = action.payload.value;
      const touched = new Set(state.touched);
      touched.add(action.payload.prop);
      return { ...state, checkout: { ...state.checkout, paymentDetails }, touched };
    }
    case "UPDATE_TOUCHED": {
      const touched = new Set(state.touched);
      touched.add(action.payload.prop);
      return { ...state, touched };
    }
    case "UPDATE_ERROR":
      return { ...state, error: action.payload };
    case "ADD_CLIENT_UPDATE_ERRORS":
      return { ...state, errors: action.payload.errors };
    case "INITIAL_DATA":
      return initialState
    default:
      return state;
  }
};
