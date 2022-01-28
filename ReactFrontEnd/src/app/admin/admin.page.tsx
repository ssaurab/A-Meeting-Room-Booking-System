import React from "react";
import { Switch, Route, Link, RouteComponentProps, Redirect } from "react-router-dom";
import AppBar from "@material-ui/core/AppBar";
import CssBaseline from "@material-ui/core/CssBaseline";
import Divider from "@material-ui/core/Divider";
import Drawer from "@material-ui/core/Drawer";
import Hidden from "@material-ui/core/Hidden";
import IconButton from "@material-ui/core/IconButton";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import ListItemText from "@material-ui/core/ListItemText";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { makeStyles, useTheme, Theme, createStyles } from "@material-ui/core/styles";

/* Icon Imports */
import MenuIcon from "@material-ui/icons/Menu";
import SpeedIcon from "@material-ui/icons/Speed";
import AssignmentIcon from "@material-ui/icons/Assignment";
import MeetingRoom from "@material-ui/icons/MeetingRoom";
import BorderOuterIcon from "@material-ui/icons/BorderOuter";
import AddToQueueIcon from "@material-ui/icons/AddToQueue";
import FastfoodIcon from "@material-ui/icons/Fastfood";
import GroupIcon from "@material-ui/icons/Group";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";

/* Page Imports */
import { DashboardPage } from "./dashboard/dashboard.page";
import { BookingsRouter } from "./bookings/bookings.router";
import { RoomsRouter } from "./rooms/rooms.router";
import { LayoutRouter } from "./layouts/layouts.router";
import { EquipmentsRouter } from "./equipments/equipments.router";
import { FoodDrinksRouter } from "./food-drinks/food-drinks.router";
import { UsersRouter } from "./users/users.router";
import { LoginPage } from "./login/login.page";
import { connectLogin } from "./login/login.selecter";
import { PrintPage } from "./dashboard/dashboard.print";

const drawerWidth = 250;

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      display: "flex",
    },
    drawer: {
      [theme.breakpoints.up("sm")]: {
        width: drawerWidth,
        flexShrink: 0,
      },
    },
    appBar: {
      [theme.breakpoints.up("sm")]: {
        width: `calc(100% - ${drawerWidth}px)`,
        marginLeft: drawerWidth,
      },
    },
    menuButton: {
      marginRight: theme.spacing(2),
      [theme.breakpoints.up("sm")]: {
        display: "none",
      },
    },
    // necessary for content to be below app bar
    toolbar: theme.mixins.toolbar,
    drawerPaper: {
      width: drawerWidth,
    },
    content: {
      flexGrow: 1,
      padding: theme.spacing(3),
    },
  })
);

interface AdminPageProps extends RouteComponentProps {
  /**
   * Injected by the documentation to work in an iframe.
   * You won't need it on your project.
   */
  window?: () => Window;
}

export const AdminPage = connectLogin<AdminPageProps>(({ window, match, ...props }) => {
  const classes = useStyles();
  const theme = useTheme();
  const [mobileOpen, setMobileOpen] = React.useState(false);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const menus = [
    {
      text: "Dashboard",
      icon: <SpeedIcon />,
      route: "/dashboard",
      page: DashboardPage,
    },
    {
      text: "Bookings",
      icon: <AssignmentIcon />,
      route: "/bookings",
      page: BookingsRouter,
    },
    {
      text: "Rooms",
      icon: <MeetingRoom />,
      route: "/rooms",
      page: RoomsRouter,
    },
    {
      text: "Rooms Layout",
      icon: <BorderOuterIcon />,
      route: "/layout",
      page: LayoutRouter,
    },
    {
      text: "Equipments",
      icon: <AddToQueueIcon />,
      route: "/equipments",
      page: EquipmentsRouter,
    },
    {
      text: "Food & Drinks",
      icon: <FastfoodIcon />,
      route: "/food-drinks",
      page: FoodDrinksRouter,
    },
    {
      text: "Users",
      icon: <GroupIcon />,
      route: "/users",
      page: UsersRouter,
      role: "administrator",
    },
    {
      text: "Logout",
      icon: <ExitToAppIcon />,
      route: "?logout",
      page: LoginPage,
    },
  ].filter((menu) => !menu.role || menu.role === props.loggedInUser?.role);

  const drawer = (
    <div>
      <div className={classes.toolbar} />
      <Divider />
      <List>
        {menus.map(({ text, icon, route }) => (
          <Link to={match.url + route}>
            <ListItem button key={text}>
              <ListItemIcon>{icon}</ListItemIcon>
              <ListItemText primary={text} />
            </ListItem>
          </Link>
        ))}
      </List>
    </div>
  );

  const container = window !== undefined ? () => window().document.body : undefined;

  return (
    <div className={classes.root}>
      <CssBaseline />
      <AppBar position="fixed" className={classes.appBar}>
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            className={classes.menuButton}
          >
            <MenuIcon />
          </IconButton>
          <Link to="/">
            <Typography variant="h6" noWrap>
              Meeting Room Booking System
            </Typography>
          </Link>
        </Toolbar>
      </AppBar>
      <nav className={classes.drawer} aria-label="mailbox folders">
        {/* The implementation can be swapped with js to avoid SEO duplication of links. */}
        <Hidden smUp implementation="css">
          <Drawer
            container={container}
            variant="temporary"
            anchor={theme.direction === "rtl" ? "right" : "left"}
            open={mobileOpen}
            onClose={handleDrawerToggle}
            classes={{
              paper: classes.drawerPaper,
            }}
            ModalProps={{
              keepMounted: true, // Better open performance on mobile.
            }}
          >
            {drawer}
          </Drawer>
        </Hidden>
        <Hidden xsDown implementation="css">
          <Drawer
            classes={{
              paper: classes.drawerPaper,
            }}
            variant="permanent"
            open
          >
            {drawer}
          </Drawer>
        </Hidden>
      </nav>
      <main className={classes.content}>
        <Switch>
          <Route exact path={match.path + "/dashboard/print"} component={PrintPage}></Route>
          {menus.map(({ route, page }) => (
            <Route path={match.path + route} component={page}></Route>
          ))}
          <Route path="/*">
            <Redirect to={match.path + menus[0].route} />
          </Route>
        </Switch>
      </main>
    </div>
  );
});
