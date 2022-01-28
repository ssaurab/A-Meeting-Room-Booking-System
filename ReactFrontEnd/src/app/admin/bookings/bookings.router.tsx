import React, { FunctionComponent } from "react";
import { Switch, Route, RouteComponentProps } from "react-router-dom";
import { BookingsPage } from "./bookings.page";
import { AddBookingPage } from "./add-booking/add-booking.page";

export const BookingsRouter: FunctionComponent<RouteComponentProps> = ({
  match,
}) => {
  return (
    <Switch>
      <Route exact path={match.path} component={BookingsPage} />
      <Route path={match.path + "/add"} component={AddBookingPage} />
      <Route
        path={match.path + "/edit/:bookingId"}
        component={AddBookingPage}
      />
    </Switch>
  );
};
