import React, { useEffect } from "react";
import { api } from "../../app";
import {
  makeStyles,
  Theme,
  createStyles,
  Grid,
  Paper,
  Typography,
  AppBar,
  Toolbar,
  List,
  ListItem,
  ListItemText,
  Box,
  Divider,
  Link as QuickLink,
  CircularProgress,
} from "@material-ui/core";
import AssignmentIcon from "@material-ui/icons/Assignment";
import { red } from "@material-ui/core/colors";
import DateFnsUtils from "@date-io/date-fns";
import { MuiPickersUtilsProvider, KeyboardDatePicker } from "@material-ui/pickers";
import { format } from "date-fns";
import { Link, RouteComponentProps } from "react-router-dom";
import { connectDashboards } from "./dashboard.selector";
import { Print } from "@material-ui/icons";
import { formatRelative } from "date-fns";

export interface DashboardPageProps extends RouteComponentProps {}

interface BookingData {
  room: string;
  date: string;
  status: string;
  name: string;
  id: number;
}

export interface ReservationData {
  id: number;
  room: string;
  name: string;
  duration: string;
  status: string;
  date: string;
  total: number;
}
export interface ReservationDataFromApi {
  room: { name: string };
  client: { name: string };
  status: string;
  date: string;
  id: number;
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {},
    stats: {
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      padding: theme.spacing(2),
      textAlign: "center",
      color: theme.palette.text.secondary,
    },
    overview: {
      color: theme.palette.text.primary,
      height: "100%",
    },
    media: {
      height: 0,
      paddingTop: "56.25%", // 16:9
    },
    expand: {
      transform: "rotate(0deg)",
      marginLeft: "auto",
      transition: theme.transitions.create("transform", {
        duration: theme.transitions.duration.shortest,
      }),
    },
    expandOpen: {
      transform: "rotate(180deg)",
    },
    avatar: {
      backgroundColor: red[500],
    },
    dashboardCurrentDateTimeInfo: {
      textAlign: "right",
      align: "right",
    },
    loader: {
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
    },
    link: {
      textDecorationLine: "underline",
      color: "#3f51b5",
    },
  })
);

export const DashboardPage = connectDashboards<DashboardPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();

  const [selectedDate, setSelectedDate] = React.useState<Date>(new Date());
  const [bookingsMadeToday, setBookingsMadeToday] = React.useState(0);
  const [bookingsForToday, setBookingsForToday] = React.useState(0);
  const [totalBookings, setTotalBookings] = React.useState(0);
  const [last3bookings, setLast3Bookings] = React.useState<BookingData[]>([]);
  const [currentTime, setCurrentTime] = React.useState(new Date().toLocaleString());

  useEffect(() => {
    props.fetchBookingsOnDay(changeDateFormat(new Date()));
    let secTimer = setInterval(() => {
      setCurrentTime(new Date().toLocaleString().split(",")[1]);
    }, 1000);

    api.get("/V2/bookings/countofbookingstoday").then(({ data }) => {
      setBookingsMadeToday(data[1]);
      setTotalBookings(data[0]);
    });

    api.get(`/V1/bookings/bookingson/${changeDateFormat(new Date())}`).then(({ data }) => {
      setBookingsForToday(data.length);
    });

    props.updateLoading("latestBookings", true);
    api.get("/V2/bookings?from=1&order=des&sort=id&to=3").then(({ data }) => {
      let transaformedData: BookingData[] = data
        .map(({ client, room, date, id, status }: ReservationDataFromApi) => ({
          room: room.name,
          name: client.name,
          date,
          status,
          id,
        }))
        .filter((b: BookingData) => b.date.localeCompare(changeDateFormat(new Date())) >= 0);
      setLast3Bookings(transaformedData);
      props.updateLoading("latestBookings", false);
    });

    return () => {
      clearInterval(secTimer);
    };
  }, []);

  const changeDateFormat = (date: Date | null) => {
    let d = date === null ? new Date() : new Date(date),
      month = "" + (d.getMonth() + 1),
      day = "" + d.getDate(),
      year = d.getFullYear();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;

    return [year, month, day].join("-");
  };

  const handleDateChange = function (date: Date | null) {
    if (!date) return;
    setSelectedDate(date);
    props.fetchBookingsOnDay(changeDateFormat(date));
  };

  const adminPath = match.url.split("/").slice(0, -1).join("/");
  const quickLinks = [
    { text: "+Add Booking", link: "/bookings/add" },
    { text: "+Add Room", link: "/rooms/add" },
    { text: "View Bookings", link: "/bookings" },
    { text: "View Rooms", link: "/rooms" },
    { text: "Manage Equipments", link: "/equipments" },
    ...(props.loggedInUser?.role === "administrator" ? [{ text: "Manage Users", link: "/users" }] : []),
  ];

  const formatDate = (date: Date) => {
    let text = formatRelative(date, new Date());
    text = text.charAt(0).toUpperCase() + text.slice(1);
    return text.includes("at") ? text.slice(0, text.lastIndexOf("at")) : text;
  };
  return (
    <div className={classes.root}>
      <Grid container spacing={3}>
        <Grid item xs={4}>
          <Paper className={classes.stats}>
            <AssignmentIcon style={{ fontSize: 45 }} />
            <Typography variant="h2">{bookingsMadeToday}</Typography>
            <Typography style={{ margin: 10 }}>Total Bookings made today</Typography>
          </Paper>
        </Grid>
        <Grid item xs={4}>
          <Paper className={classes.stats}>
            <AssignmentIcon style={{ fontSize: 45 }} />
            <Typography variant="h2">{bookingsForToday}</Typography>
            <Typography style={{ margin: 10 }}>Total Bookings for today</Typography>
          </Paper>
        </Grid>
        <Grid item xs={4}>
          <Paper className={classes.stats}>
            <AssignmentIcon style={{ fontSize: 45 }} />
            <Typography variant="h2">{totalBookings}</Typography>
            <Typography style={{ margin: 10 }}>Total Bookings made so Far</Typography>
          </Paper>
        </Grid>
        <Grid item xs={4}>
          <Paper className={classes.overview}>
            <AppBar position="static" color="transparent" elevation={1}>
              <Toolbar>
                <Typography variant="h6">Latest Bookings Made</Typography>
              </Toolbar>
            </AppBar>
            {props.loading.latestBookings ? (
              <div className={classes.loader} style={{ height: "calc(100% - 64px)" }}>
                <CircularProgress />
              </div>
            ) : (
              <List component="nav" className={classes.root} aria-label="mailbox folders">
                {last3bookings.map((data, i, { length }) => (
                  <ListItem divider={i + 1 !== length}>
                    <ListItemText>
                      {data.room}
                      <br />
                      Date: {format(new Date(data.date), "MM-dd-yyyy")}
                      <br />
                      Status: {data.status.charAt(0).toUpperCase() + data.status.slice(1)}
                      <br />
                      <Link to={match.url.replace("dashboard", "bookings") + `?bookingId=${data.id}`}>
                        <div className={classes.link}>{data.name}</div>
                      </Link>
                    </ListItemText>
                  </ListItem>
                ))}
              </List>
            )}
          </Paper>
        </Grid>
        <Grid item xs={4}>
          <Paper className={classes.overview}>
            <AppBar position="static" color="transparent" elevation={1}>
              <Toolbar style={{ justifyContent: "space-between" }}>
                <Typography variant="h6">Bookings For {formatDate(selectedDate)}</Typography>
                <Link to={match.path + `/print?date=${changeDateFormat(selectedDate)}`}>
                  <Print />
                </Link>
              </Toolbar>
            </AppBar>
            <Box p={2}>
              <Grid container>
                <Grid item xs={3}>
                  <Typography variant="h6">Date: </Typography>
                </Grid>
                <Grid item xs={8}>
                  <MuiPickersUtilsProvider utils={DateFnsUtils}>
                    <KeyboardDatePicker
                      disableToolbar
                      variant="inline"
                      format="yyyy-MM-dd"
                      value={selectedDate}
                      onChange={handleDateChange}
                      autoOk={true}
                      inputProps={{ disabled: true }}
                      KeyboardButtonProps={{
                        "aria-label": "change date",
                      }}
                    />
                  </MuiPickersUtilsProvider>
                </Grid>
              </Grid>
            </Box>
            <Divider />
            {props.loading.reservations ? (
              <div className={classes.loader} style={{ height: "calc(100% - 128px)" }}>
                <CircularProgress />
              </div>
            ) : !props.bookingsOnDay.length ? (
              <Typography style={{ padding: "24px 12px", textAlign: "center" }}>There are no bookings to show.</Typography>
            ) : (
              <List component="nav" className={classes.root} aria-label="mailbox folders">
                {props.bookingsOnDay.map((data, i, { length }) => (
                  <ListItem divider={i + 1 !== length}>
                    <ListItemText>
                      {data.room}
                      <br />
                      Time: {data.duration}
                      <br />
                      Status: {data.status.charAt(0).toUpperCase() + data.status.slice(1)}
                      <br />
                      <Link to={match.url.replace("dashboard", "bookings") + `?bookingId=${data.id}`}>
                        <div className={classes.link}>{data.name}</div>
                      </Link>
                    </ListItemText>
                  </ListItem>
                ))}
              </List>
            )}
          </Paper>
        </Grid>
        <Grid item xs={4}>
          <Paper className={classes.overview}>
            <AppBar position="static" color="transparent" elevation={1}>
              <Toolbar>
                <Typography variant="h6">Quick Links</Typography>
              </Toolbar>
            </AppBar>
            <List component="nav" className={classes.root} aria-label="mailbox folders">
              {quickLinks.map((quick, i, { length }) => (
                <ListItem divider={i + 1 !== length}>
                  <ListItemText>
                    <Link to={adminPath + quick.link}>
                      <QuickLink>{quick.text}</QuickLink>
                    </Link>
                  </ListItemText>
                </ListItem>
              ))}
            </List>
          </Paper>
        </Grid>
        <Grid item xs={8}>
          <Paper className={classes.overview}>
            <AppBar position="static" color="transparent" elevation={1}>
              <Toolbar>
                <Typography variant="h6">Current User: {props.loggedInUser?.name}</Typography>
              </Toolbar>
            </AppBar>
          </Paper>
        </Grid>
        <Grid item xs={4}>
          <Paper className={classes.overview}>
            <AppBar position="static" color="transparent" elevation={1}>
              <Toolbar>
                <Typography variant="h6">
                  {currentTime} | {new Date().toDateString()}
                </Typography>
              </Toolbar>
            </AppBar>
          </Paper>
        </Grid>
      </Grid>
    </div>
  );
});
