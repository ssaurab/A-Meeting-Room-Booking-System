import { Reducer } from "@reduxjs/toolkit";
import { AddUserActions } from "./add-user.actions";

export interface AddUserState {
  id: number;
  role: string;
  name: string;
  phone: string;
  email: string;
  password: string;
  status: string;
  errors: Map<string, string>;
  users: {name:string}[];
}

const initialState: AddUserState = {
  id: 0,
  role: "",
  name: "",
  phone: "",
  email: "",
  password: "",
  status: "",
  errors: new Map(),
  users:[]
};

export const addUserReducer: Reducer<AddUserState, AddUserActions> = (
  state = initialState,
  action
) => {
  switch (action.type) {
    case "ADD_USER_RESET":
      return { ...initialState };
    case "UPDATE_EMAIL":
      return { ...state, email: action.payload.email };
    case "UPDATE_ROLE":
      return { ...state, role: action.payload.role };
    case "UPDATE_NAME":
      return { ...state, name: action.payload.name };
    case "UPDATE_STATUS":
      return { ...state, status: action.payload.status };
    case "UPDATE_PHONE":
      return { ...state, phone: action.payload.phone };
    case "UPDATE_PASSWORD":
      return { ...state, password: action.payload.password };
    case "UPDATE_REGISTRATION":
      return { ...state, date: action.payload.date };
    case "ADD_USER_UPDATE_ERRORS":
      return { ...state, errors: action.payload.errors };
    case "ADD_USER_SET_USERS":
      return {...state,users: action.payload}
    default:
      return state;
  }
};
