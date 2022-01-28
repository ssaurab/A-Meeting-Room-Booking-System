import { Reducer } from "@reduxjs/toolkit";
import { UsersActions } from "./users.actions";

export interface UserState {
    loading: boolean;
    users: {
        id: number;
        role: string;
        name: string;
        phone: string;
        email: string;
        password:string;
        status: string,
    }[];
}


const initialState: UserState = {
    loading: true,
    users: [],
};

export const usersReducer: Reducer<UserState, UsersActions> = (state = initialState, action) => {
    switch (action.type) {
        case "UPDATE_USERS":
            return { ...state, users: action.payload.users };
        case "UPDATE_LOADING":
            return { ...state, loading: action.payload.loading };
        default:
            return state;
    }
};
