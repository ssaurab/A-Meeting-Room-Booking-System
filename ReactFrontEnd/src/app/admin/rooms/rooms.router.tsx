import React, { FunctionComponent } from "react";
import { Switch, Route, RouteComponentProps } from "react-router-dom";
import { RoomsPage } from "./rooms.page";
import { AddRoomPage } from "./add-room/add-room.page";

export interface RoomsRouterProps extends RouteComponentProps {}

export const RoomsRouter: FunctionComponent<RoomsRouterProps> = ({ match }) => {
  return (
    <Switch>
      <Route exact path={match.path + "/"} component={RoomsPage} />
      <Route path={match.path + "/add"} component={AddRoomPage} />
      <Route path={match.path + "/edit/:roomId"} component={AddRoomPage} />
    </Switch>
  );
};
