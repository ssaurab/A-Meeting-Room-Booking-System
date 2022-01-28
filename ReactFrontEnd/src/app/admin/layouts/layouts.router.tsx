import React, { FunctionComponent } from "react";
import { Switch, Route, RouteComponentProps } from "react-router-dom";
import { LayoutsPage } from "./layouts.page";
import { AddLayoutPage } from "./add-layout/add-layout.page";

export interface LayoutsRouterProps extends RouteComponentProps {}

export const LayoutRouter: FunctionComponent<LayoutsRouterProps> = ({ match }) => {
  return (
    <Switch>
      <Route exact path={match.path + "/"} component={LayoutsPage} />
      <Route path={match.path + "/add"} component={AddLayoutPage} />
      <Route path={match.path + "/edit/:layoutId"} component={AddLayoutPage} />
    </Switch>
  );
};
