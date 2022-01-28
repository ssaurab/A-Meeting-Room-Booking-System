import { AddUserState } from "./add-user.reducer";
import { Dispatch, ChangeEvent } from "react";
import { Action } from "@reduxjs/toolkit";
import { AddUserActionTypes, addUserActions } from "./add-user.actions";
import { connectStore } from "../../../store";
import { UsersActionTypes } from "../users.actions";
import { api } from "../../../app";

const mapStateToProps = (state: { addUserReducer: AddUserState }) => ({
  ...state.addUserReducer,
});

const mapDispatchToProps = (
  dispatch: Dispatch<Action<AddUserActionTypes> | Action<UsersActionTypes>>
) => ({
  updateName: (e: ChangeEvent<HTMLInputElement>) =>
    dispatch(addUserActions.updateName({ name: e.target.value })),

  updateRole: (role: string) =>
    dispatch(
      addUserActions.updateRole({
        role: role,
      })
    ),

  updateEmail: (e: ChangeEvent<HTMLInputElement>) =>
    dispatch(addUserActions.updateEmail({ email: e.target.value })),

  updatePassword: (e: ChangeEvent<HTMLInputElement>) =>
    dispatch(addUserActions.updatePassword({ password: e.target.value })),

  updatePhone: (e: ChangeEvent<HTMLInputElement>) =>
    dispatch(addUserActions.updatePhone({ phone: e.target.value })),

  updateRegistration: (e: ChangeEvent<HTMLInputElement>) =>
    dispatch(addUserActions.updateRegistration({ date: e.target.value })),

  updateStatus: (status: string) =>
    dispatch(
      addUserActions.updateStatus({
        status: status,
      })
    ),

  resetUsers: () => dispatch(addUserActions.reset()),

  validate: (props: AddUserState) => {
    const errors = new Map<string, string>();

    const emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    const phoneRegex = /^\d{10}$/;
    if (props.users.find(({ name }) => name === props.name))
      errors.set("name", "UserName is already present");
    if (!props.role) errors.set("role", "Role is Required");
    if (!props.name) errors.set("name", "Name cannot be empty");
    if (!props.phone) errors.set("phone", "Phone cannot be empty");
    else if (!phoneRegex.test(props.phone))
      errors.set("phone", "Phone number must have 10 digits");
    if (!props.email) errors.set("email", "Email cannot be empty");
    else if (!emailRegex.test(props.email))
      errors.set("email", "Invalid email address");
    if (!props.password) errors.set("password", "Password cannot be empty");
    if (props.password && props.password.length < 6)
      errors.set("password", "Password must have at least 6 characters ");
    if (!props.status) errors.set("status", "Status is required");

    dispatch(addUserActions.updateErrors({ errors }));
    return !errors.size;
  },
  addError: (
    name: string,
    errorString: string,
    errorProps: Map<string, string>
  ) => {
    const errors = new Map(errorProps);
    errors.set(name, errorString);
    dispatch(addUserActions.updateErrors({ errors }));
  },
  deleteError: (name: string, errorProps: Map<string, string>) => {
    const errors = new Map(errorProps);
    errors.delete(name);
    dispatch(addUserActions.updateErrors({ errors }));
  },
  fetchUsers: () => {
    api.get("/V1/users").then(({ data }) => {
      const users = data.map(({ name }: { name: string }) => ({ name: name }));
      dispatch(addUserActions.setUsers(users));
    });
  },
});

export const connectAddUser = connectStore(mapStateToProps, mapDispatchToProps);

// role: string;
//     name: string;
//     phone: string;
//     email: string;
//     password:string;
//     status: string,
