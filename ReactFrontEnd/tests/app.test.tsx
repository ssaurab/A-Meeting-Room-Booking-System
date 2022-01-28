import React from "react";
import { cleanup } from "@testing-library/react";
import { App } from "../src/app/app";
import { renderWithStore } from "./setup";

describe("App NavBar", () => {
  afterEach(cleanup);

  it("should render the correct title", () => {
    const title = "Meeting Room Booking System";
    const component = renderWithStore(<App />);
    expect(component.getByTestId("app-title")).toHaveTextContent(title);
  });

  it("should render the admin login button", () => {
    const component = renderWithStore(<App />);
    expect(component.getByTestId("admin-login-button")).toBeDefined();
  });

  it("should open the login page when the login button is clicked", () => {
    const component = renderWithStore(<App />);
    component.getByTestId("admin-login-button").click();
    expect(window.location.pathname).toBe("/admin");
  });
});
