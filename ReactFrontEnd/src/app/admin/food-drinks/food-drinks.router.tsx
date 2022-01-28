import React, { FunctionComponent } from "react";
import { Switch, Route, RouteComponentProps } from "react-router-dom";
import { FoodDrinksPage } from "./food-drinks.page";
import { AddFoodDrinksPage } from "./add-food-drinks/add-food-drinks.page";

export interface EquipmentsRouterProps extends RouteComponentProps {}

export const FoodDrinksRouter: FunctionComponent<EquipmentsRouterProps> = ({
  match,
}) => {
  return (
    <Switch>
      <Route exact path={match.path + "/"} component={FoodDrinksPage} />
      <Route path={match.path + "/add"} component={AddFoodDrinksPage} />
      <Route path={match.path + "/edit/:snackId"} component={AddFoodDrinksPage} />
    </Switch>
  );
};
