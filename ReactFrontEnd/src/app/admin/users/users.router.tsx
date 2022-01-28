import React, { FunctionComponent } from "react";
import { Switch, Route, RouteComponentProps } from "react-router-dom";
import { UsersPage } from "./users.page";
import { AddUserPage } from "./add-user/add-user.page";

export interface UsersRouterProps extends RouteComponentProps {}

export const UsersRouter: FunctionComponent<UsersRouterProps> = ({ match }) => {
    return (
        <Switch>
            <Route exact path={match.path + "/"} component={UsersPage} />
            <Route path={match.path + "/add"} component={AddUserPage} />
            <Route path={match.path + "/edit/:userId"} component={AddUserPage} />
        </Switch>
    );
};
