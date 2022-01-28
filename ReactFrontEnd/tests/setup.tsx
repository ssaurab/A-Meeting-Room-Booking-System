import React from "react";
import "@testing-library/jest-dom/extend-expect";
import { render } from "@testing-library/react";
import { Provider } from "react-redux";
import { store } from "../src/app/store";

export const renderWithStore = (component: JSX.Element, reduxStore = store) =>
  render(<Provider store={reduxStore}>{component}</Provider>);

console.error = (..._: any[]) => void 0;
