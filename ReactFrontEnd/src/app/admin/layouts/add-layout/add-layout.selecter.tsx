import { AddLayoutState } from "./add-layout.reducer";
import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { AddLayoutActionTypes, addLayoutActions } from "./add-layout.actions";
import { connectStore } from "../../../store";
import { LayoutActionTypes } from "../layouts.actions";
import { api } from "../../../app";

const mapStateToProps = (state: { addLayoutReducer: AddLayoutState }) => ({
  ...state.addLayoutReducer,
});

const mapDispatchToProps = (
  dispatch: Dispatch<Action<AddLayoutActionTypes> | Action<LayoutActionTypes>>
) => ({
  fetchExistingLayouts: () => {
    api
      .get("/V1/roomlayouts")
      .then(({ data }) =>
        dispatch(addLayoutActions.setExisting({ layouts: data }))
      );
  },

  updateTitle: (title: string) =>
    dispatch(addLayoutActions.updateTitle({ title: title })),

  updateImage: (_files: File[], [image]: string[]) =>
    dispatch(addLayoutActions.updateImage({ image })),

  resetLayouts: () => dispatch(addLayoutActions.reset()),

  validate: (props: AddLayoutState, isEdit: boolean) => {
    const errors = new Map<string, string>();

    if (!props.title) errors.set("title", "Title is Required");
    else if (
      !isEdit &&
      props.existingLayouts.find((l) => l.title === props.title)
    )
      errors.set("title", "Title already exists");
    if (!props.image) errors.set("image", "Image is Required");

    dispatch(addLayoutActions.updateErrors({ errors }));
    return !errors.size;
  },

  addError: (
    name: string,
    errorString: string,
    errorProps: Map<string, string>
  ) => {
    const errors = new Map(errorProps);
    errors.set(name, errorString);
    dispatch(addLayoutActions.updateErrors({ errors }));
  },
  deleteError: (name: string, errorProps: Map<string, string>) => {
    const errors = new Map(errorProps);
    errors.delete(name);
    dispatch(addLayoutActions.updateErrors({ errors }));
  },
});

export const connectAddLayout = connectStore(
  mapStateToProps,
  mapDispatchToProps
);
