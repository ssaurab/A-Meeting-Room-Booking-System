import React, { FunctionComponent } from "react";
import { Switch, Route, RouteComponentProps } from "react-router-dom";
import { EquipmentsPage } from "./equipments.page";
import { AddEquipmentPage } from "./add-equipment/add-equipment.page";

export interface EquipmentsRouterProps extends RouteComponentProps {}

export const EquipmentsRouter: FunctionComponent<EquipmentsRouterProps> = ({
  match,
}) => {
  return (
    <Switch>
      <Route exact path={match.path + "/"} component={EquipmentsPage} />
      <Route path={match.path + "/add"} component={AddEquipmentPage} />
      <Route
        path={match.path + "/edit/:equipmentId"}
        component={AddEquipmentPage}
      />
    </Switch>
  );
};
