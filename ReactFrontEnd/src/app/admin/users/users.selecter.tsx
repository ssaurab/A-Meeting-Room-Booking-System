import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { connectStore } from "../../store";
import { UserState } from "./users.reducer";
import { UsersActionTypes, usersActions } from "./users.actions";
import { UsersRow } from "./users.page";
import { api } from "../../app";

const mapStateToProps = (state: { usersReducer: UserState }) => ({
  ...state.usersReducer,
  users: state.usersReducer.users.map((user) => ({
    ...user,
  })),
});

const mapDispatchToProps = (dispatch: Dispatch<Action<UsersActionTypes>>) => ({
  updateLoading: (loading: boolean) => dispatch(usersActions.updateLoading({ loading })),

  updateUsers: (users: UsersRow[]) =>
    dispatch(
      usersActions.updateUsers({
        users: users.map(({ id, role, name, phone, email, password, status }) => ({
          id,
          role,
          name,
          phone,
          email,
          password,
          status,
        })),
      })
    ),

  fetchUsers: () =>
    api.get("/V1/users").then(({ data }) => {
      dispatch(usersActions.updateLoading({ loading: false }));
      dispatch(usersActions.updateUsers({ users: data }));
    }),

  deleteUser: (id: number) =>
    api.delete(`/V1/users/${id}`, { headers: { successMsg: "User deleted successfully!" } }).then(() => {
      dispatch(usersActions.updateLoading({ loading: true }));
    }),
  editStatus: (status: string, row: UsersRow, rows: UsersRow[]) => {
    api
      .patch(`/V2/users/${row.id}`, { status }, { headers: { successMsg: "Status updated successfully!" } })
      .then(({ data }) => {
        let updateRows = rows.map((ele) => {
          return ele.id === row.id ? { ...ele, status } : ele;
        });
        dispatch(usersActions.updateUsers({ users: updateRows }));
      })
      .catch((err) => console.error(err.message));
  },
});

export const connectUsers = connectStore(mapStateToProps, mapDispatchToProps);
