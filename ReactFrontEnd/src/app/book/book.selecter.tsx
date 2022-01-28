import { Action } from "@reduxjs/toolkit";
import { format } from "date-fns";
import { Dispatch } from "react";
import { api, range } from "../app";
import { LandingActionTypes } from "../landing/landing.actions";
import { connectStore } from "../store";
import { bookActions, BookActionTypes } from "./book.actions";
import { BookState } from "./book.reducer";
import { ReturnData } from "./confirmation.step";

const mapStateToProps = (state: { bookReducer: BookState }) => ({
  ...state.bookReducer,
  clientErrors: ((props: BookState) => {
    const errors = new Map<string, string>();
    const stepErrors = [];

    if (!props.bookRoom.attendees) errors.set("attendees", "Attendees must be non-zero");
    else if (props.bookRoom.attendees < 0) errors.set("attendees", "Attendees must be a positive number");
    else if (props.bookRoom.attendees > props.room.capacity)
      errors.set("attendees", `Max capacity for the selected room is ${props.room.capacity}`);
    if (!props.bookRoom.from) errors.set("from", "From is Required");
    if (!props.bookRoom.to) errors.set("to", "To is Required");
    if (!props.bookRoom.date) errors.set("date", "Date is Required");
    stepErrors.push(new Map<string, string>(errors));
    if (!props.roomSetup.selectedLayout?.id) errors.set("layout", "Layout is not selected");
    props.roomSetup.availableEquipments
      .filter((e) => e.selected)
      .forEach(({ id, quantity }) => {
        if (!quantity) errors.set("equipment-" + id, "Quantity is required");
        else if (quantity < 1) errors.set("equipment-" + id, "Quantity must be at least 1");
      });
    stepErrors.push(new Map<string, string>(errors));
    props.foodDrinks.availableSnacks
      .filter((s) => s.selected)
      .forEach(({ id, count }) => {
        if (!count) errors.set("snacks-" + id, "No. of units is required");
        else if (count < 1) errors.set("snacks-" + id, "No. of units must be at least 1");
      });
    stepErrors.push(new Map<string, string>(errors));
    if (!props.checkout.clientDetails.title) errors.set("title", "Title is Required");
    if (!props.checkout.clientDetails.name) errors.set("name", "Name is Required");
    // Email check
    const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (!props.checkout.clientDetails.email) errors.set("email", "Email is Required");
    else if (!emailRegex.test(props.checkout.clientDetails.email)) errors.set("email", "Invalid email address");
    // Phone check
    const phRegex = /^\d{10}$/;
    if (!props.checkout.clientDetails.phone) errors.set("phone", "Phone No is Required");
    else if (!phRegex.test(props.checkout.clientDetails.phone)) errors.set("phone", "Invalid phone number");
    if (!props.checkout.clientDetails.company) errors.set("company", "Enter Company");
    if (!props.checkout.clientDetails.city) errors.set("city", "City is required");
    // Zip check
    const zipRegex = /^\d{6}$/;
    if (!props.checkout.clientDetails.zip) errors.set("zip", "Zip Code is required");
    else if (!zipRegex.test(props.checkout.clientDetails.zip)) errors.set("zip", "Zip Code should be only 6 digits");
    if (!props.checkout.clientDetails.country) errors.set("country", "Enter Country");
    if (!props.checkout.clientDetails.address) errors.set("address", "Address is required");
    if (!props.checkout.clientDetails.state) errors.set("state", "State is required");
    if (props.checkout.paymentDetails.method === "Credit card") {
      if (!props.checkout.paymentDetails.cardNumber) errors.set("cardNumber", "Enter Card Number");
      if (!props.checkout.paymentDetails.cvv) errors.set("cvv", "Enter cvv");
      if (!props.checkout.paymentDetails.expiryDate) errors.set("expirydate", "Expiry Date is required");
      if (!props.checkout.paymentDetails.nameOnCard) errors.set("nameOnCard", "Enter Name");
    }
    stepErrors.push(new Map<string, string>(errors));
    return { errors, stepErrors };
  })(state.bookReducer),
});

const mapDispatchToProps = (dispatch: Dispatch<Action<BookActionTypes | LandingActionTypes>>) => ({
  fetchRoom: (roomId: string) => {
    api.get(`/rooms/${roomId}`).then(({ data }) => {
      const { layouts, ...room } = data;
      dispatch(bookActions.setRoom(room));
      dispatch(bookActions.updateRoomSetup("availableLayouts", layouts));
    });
  },
  updateTouched: (prop: any) => dispatch(bookActions.updateTouched(prop)),
  updateBookRoom: (prop: keyof BookState["bookRoom"]) => (value: any) => dispatch(bookActions.updateBookRoom(prop, value)),
  updateCheckoutClientDetails: (prop: keyof BookState["checkout"]["clientDetails"]) => (value: any) =>
    dispatch(bookActions.updateCheckoutClientDetails(prop, value)),

  updateCheckoutPaymentDetails: (prop: keyof BookState["checkout"]["paymentDetails"]) => (value: any) =>
    dispatch(bookActions.updateCheckoutPaymentDetails(prop, value)),

  postBooking: (prop: BookState) => {
    const data: ReturnData = {
      attendees: prop.bookRoom.attendees,
      client: prop.checkout.clientDetails,
      date: prop.bookRoom.date.toISOString().slice(0, 10),
      equipments: prop.roomSetup.selectedEquipments.map(({ count, ...equipment }) => ({
        count: count,
        equipments: equipment,
      })),
      fromHour: +prop.bookRoom.from,
      paymentMethod: prop.checkout.paymentDetails.method,
      equipmentPrice: (() => {
        let price = 0;
        prop.roomSetup.selectedEquipments.forEach((equipment) => {
          price += equipment.count * equipment.price;
        });
        return price;
      })(),
      foodAndDrinkPrice: (() => {
        let price = 0;
        prop.foodDrinks.selectedSnacks.forEach((snack) => {
          price += snack.count * snack.price;
        });
        return price;
      })(),
      id: 0,
      room: {
        capacity: prop.room.capacity,
        description: " ",
        id: prop.room.id,
        image: prop.room.image,
        layouts: [
          {
            id: prop.roomSetup.selectedLayout.id,
            image: prop.roomSetup.selectedLayout.image,
            title: prop.roomSetup.selectedLayout.title,
          },
        ],
        name: prop.room.name,
        pricePerDay: prop.room.pricePerDay,
        pricePerHalfDay: prop.room.pricePerHalfDay,
        pricePerHour: prop.room.pricePerHour,
        status: prop.room.status,
      },
      roomLayout: {
        id: prop.roomSetup.selectedLayout.id,
        image: prop.roomSetup.selectedLayout.image,
        title: prop.roomSetup.selectedLayout.title,
      },
      roomPrice: 1,
      snacks: prop.foodDrinks.selectedSnacks.map(({ count, ...snacks }) => ({
        count: count,
        snacks: snacks,
      })),
      toHour: +prop.bookRoom.to,
      status: "pending",
      subTotal: (() => {
        const time = +prop.bookRoom.to - +prop.bookRoom.from;
        let total = 0;
        if (time >= 8) {
          total = prop.room.pricePerDay;
        } else if (time >= 4) {
          total = prop.room.pricePerHalfDay;
        } else {
          total = prop.room.pricePerHour;
        }

        prop.roomSetup.selectedEquipments.forEach((equipment) => {
          total += equipment.count * equipment.price;
        });

        prop.foodDrinks.selectedSnacks.forEach((snack) => {
          total += snack.count * snack.price;
        });

        return total;
      })(),
      tax: (() => {
        const time = +prop.bookRoom.to - +prop.bookRoom.from;
        let total = 0;
        if (time >= 8) {
          total = prop.room.pricePerDay;
        } else if (time >= 4) {
          total = prop.room.pricePerHalfDay;
        } else {
          total = prop.room.pricePerHour;
        }

        prop.roomSetup.selectedEquipments.forEach((equipment) => {
          total += equipment.count * equipment.price;
        });

        prop.foodDrinks.selectedSnacks.forEach((snack) => {
          total += snack.count * snack.price;
        });

        return 0.1 * total;
      })(),
      total: (() => {
        const time = +prop.bookRoom.to - +prop.bookRoom.from;
        let total = 0;
        if (time >= 8) {
          total = prop.room.pricePerDay;
        } else if (time >= 4) {
          total = prop.room.pricePerHalfDay;
        } else {
          total = prop.room.pricePerHour;
        }

        prop.roomSetup.selectedEquipments.forEach((equipment) => {
          total += equipment.count * equipment.price;
        });

        prop.foodDrinks.selectedSnacks.forEach((snack) => {
          total += snack.count * snack.price;
        });

        return 1.1 * total;
      })(),
      deposit: (() => {
        const time = +prop.bookRoom.to - +prop.bookRoom.from;
        let total = 0;
        if (time >= 8) {
          total = prop.room.pricePerDay;
        } else if (time >= 4) {
          total = prop.room.pricePerHalfDay;
        } else {
          total = prop.room.pricePerHour;
        }

        prop.roomSetup.selectedEquipments.forEach((equipment) => {
          total += equipment.count * equipment.price;
        });

        prop.foodDrinks.selectedSnacks.forEach((snack) => {
          total += snack.count * snack.price;
        });

        return 1.1 * total * 0.1;
      })(),
    };

    api
      .post(`/bookings`, data, {
        headers: {
          successMsg: "Booking submitted successfully! Please check your email for further details.",
          dontAutoClose: true,
        },
      })
      .then((res) => {
        dispatch(bookActions.updateError(false));
      })
      .catch((err) => console.error(err.response.data));
  },
  updateError: (bool: boolean) => {
    dispatch(bookActions.updateError(bool));
  },
  getEquipmentsAndSnacks: () => {
    api.get(`/equipments`).then(({ data }) => {
      const newData = data.map(
        (row: { id: number; title: string; priceType: string; price: number; bookMultipleUnit: boolean }) => ({
          ...row,
          quantity: 1,
          selected: false,
        })
      );
      dispatch(bookActions.updateRoomSetup("availableEquipments", newData));
    });
    api.get("/snacks").then(({ data }) => {
      const newData = data.map((row: { id: number; title: string; price: number }) => ({
        ...row,
        count: 1,
        selected: false,
      }));
      dispatch(bookActions.updateFoodDrinks("availableSnacks", newData));
    });
  },

  updateRoomSetup: (prop: keyof BookState["roomSetup"]) => (value: any) =>
    dispatch(bookActions.updateRoomSetup(prop, value)),

  updateFoodDrinks: (prop: keyof BookState["foodDrinks"]) => (value: any) =>
    dispatch(bookActions.updateFoodDrinks(prop, value)),

  fetchBlockedSlots: (date: Date, roomId: string) => {
    api
      .get(`/bookings/bookingslots/${format(date, "yyyy-MM-dd")}/${roomId}`)
      .then(({ data }: { data: { key: number; value: number }[] }) => {
        const hours = new Set<number>();
        data.forEach(({ key, value }) => range(key, value).forEach((s) => hours.add(s)));
        dispatch(bookActions.updateBookRoom("blockedSlots", hours));
      });
  },
  validateBookRoom: [
    (props: BookState) => {
      const errors = new Map<string, string>();

      if (!props.bookRoom.attendees) errors.set("attendees", "Attendees must be non-zero");
      else if (props.bookRoom.attendees < 0) errors.set("attendees", "Attendees must be a positive number");
      else if (props.bookRoom.attendees > props.room.capacity)
        errors.set("attendees", `Max capacity for the selected room is ${props.room.capacity}`);
      if (!props.bookRoom.from) errors.set("from", "From is Required");
      if (!props.bookRoom.to) errors.set("to", "To is Required");
      if (!props.bookRoom.date) errors.set("date", "Date is Required");
      dispatch(bookActions.updateValErrors({ errors }));
      return !errors.size;
    },
    (props: BookState) => {
      const errors = new Map<string, string>();
      if (!props.roomSetup.selectedLayout?.id) errors.set("layout", "Layout is not selected");
      props.roomSetup.availableEquipments
        .filter((e) => e.selected)
        .forEach(({ id, quantity }) => {
          if (!quantity) errors.set("equipment-" + id, "Quantity is required");
          else if (quantity < 1) errors.set("equipment-" + id, "Quantity must be at least 1");
        });
      dispatch(bookActions.updateValErrors({ errors }));
      return !errors.size;
    },
    (props: BookState) => {
      const errors = new Map<string, string>();

      props.foodDrinks.availableSnacks
        .filter((s) => s.selected)
        .forEach(({ id, count }) => {
          if (!count) errors.set("snacks-" + id, "No. of units is required");
          else if (count < 1) errors.set("snacks-" + id, "No. of units must be at least 1");
        });

      dispatch(bookActions.updateValErrors({ errors }));
      return !errors.size;
    },
    (props: BookState) => {
      const errors = new Map<string, string>();

      if (!props.checkout.clientDetails.title) errors.set("title", "Title is Required");
      if (!props.checkout.clientDetails.name) errors.set("name", "Name is Required");
      // Email check
      const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      if (!props.checkout.clientDetails.email) errors.set("email", "Email is Required");
      else if (!emailRegex.test(props.checkout.clientDetails.email)) errors.set("email", "Invalid email address");
      // Phone check
      const phRegex = /^\d{10}$/;
      if (!props.checkout.clientDetails.phone) errors.set("phone", "Phone No is Required");
      else if (!phRegex.test(props.checkout.clientDetails.phone)) errors.set("phone", "Invalid phone number");
      if (!props.checkout.clientDetails.company) errors.set("company", "Enter Company");
      if (!props.checkout.clientDetails.city) errors.set("city", "City is required");
      // Zip check
      const zipRegex = /^\d{6}$/;
      if (!props.checkout.clientDetails.zip) errors.set("zip", "Zip Code is required");
      else if (!zipRegex.test(props.checkout.clientDetails.zip)) errors.set("zip", "Zip Code should be only 6 digits");
      if (!props.checkout.clientDetails.country) errors.set("country", "Enter Country");
      if (!props.checkout.clientDetails.address) errors.set("address", "Address is required");
      if (!props.checkout.clientDetails.state) errors.set("state", "State is required");
      if (props.checkout.paymentDetails.method === "Credit card") {
        if (!props.checkout.paymentDetails.cardNumber) errors.set("cardNumber", "Enter Card Number");
        if (!props.checkout.paymentDetails.cvv) errors.set("cvv", "Enter cvv");
        if (!props.checkout.paymentDetails.expiryDate) errors.set("expirydate", "Expiry Date is required");
        if (!props.checkout.paymentDetails.nameOnCard) errors.set("nameOnCard", "Enter Name");
      }

      dispatch(bookActions.updateValErrors({ errors }));
      return !errors.size;
    },
  ],
  initializeFields: () => dispatch(bookActions.initializeFields()),
});

export const connectBook = connectStore(mapStateToProps, mapDispatchToProps);
