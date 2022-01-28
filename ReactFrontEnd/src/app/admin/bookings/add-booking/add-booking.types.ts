export interface Client {
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
}

export interface Booking {
  attendees: number;
  date: Date;
  status: string;
  deposit: number;
  equipmentPrice: number;
  foodAndDrinkPrice: number;
  paymentMethod: string;
  roomPrice: number;
  subTotal: number;
  tax: number;
  total: number;
  fromHour: number;
  toHour: number;
  duration: string;

  equipments: {
    bookMultipleUnit: boolean;
    id: number;
    price: number;
    priceType: string;
    title: string;
    quantity: number;
    selected: boolean;
  }[];
  room: {
    id: number;
  };
  roomLayout: {
    id: number;
  };
  snacks: {
    id: number;
    price: number;
    title: string;
    quantity: number;
    selected: boolean;
  }[];
  // totalCost: number,
}

export const UPDATE_BOOKING_DATE = "UPDATE_BOOKING_DATE";
export const UPDATE_BOOKING_ATTENDEES = "UPDATE_BOOKING_ATTENDEES";
export const UPDATE_BOOKING_STATUS = "UPDATE_BOOKING_STATUS";
export const UPDATE_BOOKING_DEPOSIT = "UPDATE_BOOKING_DEPOSIT";
export const UPDATE_BOOKING_EQUIPMENT_PRICE = "UPDATE_BOOKING_EQUIPMENT_PRICE";
export const UPDATE_BOOKING_FOOD_AND_DRINK_PRICE = "UPDATE_BOOKING_FOOD_AND_DRINK_PRICE";
export const UPDATE_BOOKING_PAYMENT_METHOD = "UPDATE_BOOKING_PAYMENT_METHOD";
export const UPDATE_BOOKING_ROOM_PRICE = "UPDATE_BOOKING_ROOM_PRICE";
export const UPDATE_BOOKING_SUB_TOTAL = "UPDATE_BOOKING_SUB_TOTAL";
export const UPDATE_BOOKING_TAX = "UPDATE_BOOKING_TAX";
export const UPDATE_BOOKING_SNACKS = "UPDATE_BOOKING_SNACKS";
export const UPDATE_BOOKING_FROMHOUR = "UPDATE_BOOKING_FROMHOUR";
export const UPDATE_BOOKING_DURATION = "UPDATE_BOOKING_DURATION";
export const UPDATE_BOOKING_TOHOUR = "UPDATE_BOOKING_TOHOUR";
export const UPDATE_BOOKING_TOTAL = "UPDATE_BOOKING_TOTAL";
export const UPDATE_BOOKING_COSTS = "UPDATE_BOOKING_COSTS";

export const UPDATE_BOOKING_ROOM = "UPDATE_BOOKING_ROOM";
export const UPDATE_BOOKING_ROOM_LAYOUT = "UPDATE_BOOKING_ROOM_LAYOUT";
export const UPDATE_BOOKING_EQUIPMENT = "UPDATE_BOOKING_EQUIPMENT";
// export const UPDATE_BOOKING_TOTAL_COST = "UPDATE_BOOKING_TOTAL_COST";

export const UPDATE_CLIENT_TITLE = "UPDATE_CLIENT_TITLE";
export const UPDATE_CLIENT_NAME = "UPDATE_CLIENT_NAME";
export const UPDATE_CLIENT_EMAIL = "UPDATE_CLIENT_EMAIL";
export const UPDATE_CLIENT_PHONE = "UPDATE_CLIENT_PHONE";
export const UPDATE_CLIENT_NOTES = "UPDATE_CLIENT_NOTES";
export const UPDATE_CLIENT_COMPANY = "UPDATE_CLIENT_COMPANY";
export const UPDATE_CLIENT_ADDRESS = "UPDATE_CLIENT_ADDRESS";
export const UPDATE_CLIENT_CITY = "UPDATE_CLIENT_CITY";
export const UPDATE_CLIENT_STATE = "UPDATE_CLIENT_STATE";
export const UPDATE_CLIENT_ZIP = "UPDATE_CLIENT_ZIP";
export const UPDATE_CLIENT_COUNTRY = "UPDATE_CLIENT_COUNTRY";
