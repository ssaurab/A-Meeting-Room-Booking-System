import { AppBar, Button, Toolbar, Typography } from "@material-ui/core";
import Axios from "axios";
import React, { FunctionComponent } from "react";
import { BrowserRouter, Link, Route, Switch } from "react-router-dom";
import { AdminRouter } from "./admin/admin.router";
import "./app.css";
import { BookPage } from "./book/book.page";
import { LandingPage } from "./landing/landing.page";
import { SnackbarComponent } from "./snackbar/snackbar.component";

export const range = (start: number, end: number) =>
  Array(end - start)
    .fill(0)
    .map((_, i) => i + start);

export const api = Axios.create();

export const App: FunctionComponent = () => {
  return (
    <BrowserRouter>
      <SnackbarComponent />
      <AppBar position="static">
        <Toolbar className="app-navbar">
          <Link to="/">
            <Typography variant="h6" data-testid="app-title">
              Meeting Room Booking System
            </Typography>
          </Link>
          <Link to="/admin">
            <Button color="inherit" data-testid="admin-login-button">
              Admin Login
            </Button>
          </Link>
        </Toolbar>
      </AppBar>
      <Switch>
        <Route exact path="/" component={LandingPage} />
        <Route path="/book/:roomId" component={BookPage} />
        <Route path="/admin" component={AdminRouter} />
      </Switch>
    </BrowserRouter>
  );
};
