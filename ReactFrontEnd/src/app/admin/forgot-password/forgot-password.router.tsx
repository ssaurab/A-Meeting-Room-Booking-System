import React, { FunctionComponent } from "react";
import { Switch, Route, RouteComponentProps } from "react-router-dom";
import { ForgotPasswordPage } from "./forgot-password.page";
import { ResetPage } from "./reset/reset.page";

export const ForgotPasswordRouter: FunctionComponent<RouteComponentProps> = ({ match }) => {
  return (
    <Switch>
      <Route exact path={match.path} component={ForgotPasswordPage} />
      <Route path={match.path + "/reset"} component={ResetPage} />
    </Switch>
  );
};
