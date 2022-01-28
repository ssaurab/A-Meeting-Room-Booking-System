import { AddRoomState } from "./add-room.reducer";
import { Dispatch } from "react";
import { Action } from "@reduxjs/toolkit";
import { AddRoomActionTypes, addRoomActions } from "./add-room.actions";
import { connectStore } from "../../../store";
import { api } from "../../../app";

const mapStateToProps = (state: { addRoomReducer: AddRoomState }) => ({
  ...state.addRoomReducer,
  bookFor: [
    // {
    //   prop: "pricePerDay" as "pricePerDay",
    //   title: "Multiple Days",
    //   per: "day" as "day",
    //   selected: !!state.addRoomReducer.pricePerDay,
    // },
    {
      prop: "pricePerHalfDay" as "pricePerHalfDay",
      title: "Half Day",
      per: "half-day" as "half-day",
      selected: !!state.addRoomReducer.pricePerHalfDay,
    },
    {
      prop: "pricePerHour" as "pricePerHour",
      title: "Hour",
      per: "hour" as "hour",
      selected: !!state.addRoomReducer.pricePerHour,
    },
  ],
});

const mapDispatchToProps = (
  dispatch: Dispatch<Action<AddRoomActionTypes>>
) => ({
  updateTitle: (title: string) =>
    dispatch(addRoomActions.updateTitle({ title: title })),

  updateImage: (image: string) =>
    dispatch(addRoomActions.updateImage({ image })),

  updateCapacity: (capacitys: number) => {
    const capacity = +capacitys;
    if (capacity <= 0) return;
    dispatch(addRoomActions.updateCapacity({ capacity }));
  },

  updateDescription: (description: string) =>
    dispatch(addRoomActions.updateDescription({ description: description })),

  updateBookFor: (_: any, child: any) =>
    dispatch(addRoomActions.togglePrice({ per: child.props.value })),

  updatePrice: (per: "hour" | "half-day" | "day") => (price1: number) => {
    const price = +price1;
    if (price <= 0) return;
    dispatch(addRoomActions.updatePrice({ per, price }));
  },

  updateStatus: (status: string) =>
    dispatch(addRoomActions.updateStatus({ status: status })),

  fetchLayouts: (layouts: AddRoomState["layouts"] = []) =>
    api
      .get("/V1/roomlayouts")
      .then((res: { data: { id: number; title: string; image: string }[] }) =>
        dispatch(
          addRoomActions.setLayouts({
            layouts: res.data.map(({ id, title, image }) => ({
              id,
              title,
              image,
              selected: !!layouts.find((l) => l.id === id),
            })),
          })
        )
      ),

  toggleLayout: (event: any, child: any) => {
    dispatch(addRoomActions.toggleLayout({ layoutId: child.props.value }));
  },

  validate: (props: AddRoomState, isEdit: boolean) => {
    const errors = new Map<string, string>();
    if (!isEdit && props.rooms.find(({ title }) => title === props.title))
      errors.set("title", "Title already exists");
    if (!props.title) errors.set("title", "Title is Required");
    if (!props.image) errors.set("image", "Image is Required");
    if (!props.capacity) errors.set("capacity", "Capacity must be non-zero");
    else if (props.capacity <= 0)
      errors.set("capacity", "Capacity must be positive");
    if (!props.description)
      errors.set("description", "Description is Required");
    if (!props.status) errors.set("status", "Status is Required");
    if (!(props.pricePerHour || props.pricePerHalfDay || props.pricePerDay))
      errors.set("bookFor", "Atleast 1 book for must be selected");
    if (!props.layouts.some((l) => l.selected))
      errors.set("layouts", "Atleast 1 layout must be selected");

    dispatch(addRoomActions.updateErrors({ errors }));
    return !errors.size;
  },
  addError: (
    name: string,
    errorString: string,
    errorProps: Map<string, string>
  ) => {
    const errors = new Map(errorProps);
    errors.set(name, errorString);
    dispatch(addRoomActions.updateErrors({ errors }));
  },
  deleteError: (name: string, errorProps: Map<string, string>) => {
    const errors = new Map(errorProps);
    errors.delete(name);
    dispatch(addRoomActions.updateErrors({ errors }));
  },

  resetFields: () => dispatch(addRoomActions.resetAddRoom()),
  fetchRooms: () => {
    api.get("/V1/rooms").then(({ data }) => {
      const roomShow = data.map(({ name }: { name: string }) => ({
        title: name,
      }));
      dispatch(addRoomActions.setRooms(roomShow));
    });
  },
});

interface SaveRoomData {
  capacity: number;
  description: string;
  id: number;
  image: string;
  layouts: {
    id: number;
  }[];
  name: string;
  pricePerDay: number | null;
  pricePerHalfDay: number | null;
  pricePerHour: number | null;
  status: string;
}

export const connectAddRoom = connectStore(mapStateToProps, mapDispatchToProps);
