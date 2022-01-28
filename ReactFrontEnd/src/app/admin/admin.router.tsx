import React from "react";
import { Redirect, Route, RouteComponentProps, Switch } from "react-router-dom";
import { AdminPage } from "./admin.page";
import { ForgotPasswordRouter } from "./forgot-password/forgot-password.router";
import { LoginPage } from "./login/login.page";
import { connectLogin } from "./login/login.selecter";

export interface AdminRouterProps extends RouteComponentProps {}

export const AdminRouter = connectLogin<AdminRouterProps>((props) => {
  return (
    <Switch>
      <Route exact path={props.match.path} component={LoginPage} />
      <Route path={props.match.path + "/forget-password"} component={ForgotPasswordRouter} />
      <Route path="/*">{props.loggedInUser ? <AdminPage {...props} /> : <Redirect to={props.match.url} />}</Route>
    </Switch>
  );
});
