import { Button, Card, CardActions, CardContent, CardHeader, Grid, Paper, Typography } from "@material-ui/core";
import Box from "@material-ui/core/Box";
import { makeStyles } from "@material-ui/core/styles";
import Tab from "@material-ui/core/Tab";
import Tabs from "@material-ui/core/Tabs";
import React, { FunctionComponent, useEffect } from "react";
import { RouteComponentProps } from "react-router-dom";
import { api } from "../../../app";
import { connectAddBooking } from "./add-booking.selecter";
import { BookingDetailsTab } from "./booking-details.tab";
import { ClientDetailsTab } from "./client-details.tab";
import { EquipmentDetailsTab } from "./equipment-details.tab";
import { FoodsDrinksDetailsTab } from "./foods&drinks-details.tab";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    backgroundColor: theme.palette.background.paper,
  },
  divButton: {
    width: "calc(100% + 35px)",
    display: "flex",
    justifyContent: "center",
  },
  buttons: {
    margin: 12,
  },
  grid: {},
}));

interface AddBookingPageProps extends RouteComponentProps<{ bookingId: string }> {}

export const AddBookingPage = connectAddBooking<AddBookingPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();
  const [value, setValue] = React.useState(0);
  const { bookingId } = match.params;

  useEffect(() => {
    props.resetForm();
    props.fetchRooms();
    if (!!bookingId) {
      Promise.all([api.get(`/V2/bookings/${bookingId}`), api.get("/V1/equipments"), api.get("/V1/snacks")]).then(
        ([{ data: bookingData }, { data: equipmentsData }, { data: snacksData }]) => {
          props.updateBookingDate(new Date(bookingData.date));
          props.updateBookingStatus(bookingData.status);
          props.updateBookingRoom(
            (({ id, name, image, capacity, description, pricePerHour, pricePerHalfDay, pricePerDay, status }) => ({
              id,
              name,
              image,
              capacity,
              description,
              pricePerHour,
              pricePerHalfDay,
              pricePerDay,
              status,
            }))(bookingData.room)
          );
          props.updateBookingRoomLayout(bookingData.roomLayout);
          props.updateBookingAttendees(bookingData.attendees);
          props.updateBookingPaymentMethod(bookingData.paymentMethod);
          bookingData.fromHour === 8 && bookingData.toHour === 12
            ? props.updateBookingDuration("Half-day Morning")
            : bookingData.fromHour === 13 && bookingData.toHour === 17
            ? props.updateBookingDuration("Half-day Afternoon")
            : props.updateBookingDuration("Select Hours");
          props.updateBookingFromHour(bookingData.fromHour);
          props.updateBookingToHour(bookingData.toHour);
          const updatedEquipments = equipmentsData.map((ele: any) => {
            const equip = bookingData.equipments.find(
              ({ equipments }: { equipments: { id: number } }) => equipments.id === ele.id
            );

            return !equip
              ? { ...ele, quantity: 1, selected: false }
              : {
                  id: equip.equipments.id,
                  price: equip.equipments.price,
                  priceType: equip.equipments.priceType,
                  title: equip.equipments.title,
                  quantity: equip.count,
                  selected: true,
                  bookMultipleUnit: equip.equipments.bookMultipleUnit,
                };
          });
          props.updateBookingEquipment(updatedEquipments);

          const updatedSnacks = snacksData.map((ele: any) => {
            const snacksSelected = bookingData.snacks.find(({ snacks }: { snacks: { id: number } }) => snacks.id === ele.id);

            return !snacksSelected
              ? { ...ele, quantity: 1, selected: false }
              : {
                  id: snacksSelected.snacks.id,
                  price: snacksSelected.snacks.price,
                  title: snacksSelected.snacks.title,
                  quantity: snacksSelected.count,
                  selected: true,
                };
          });
          props.updateBookingSnacks(updatedSnacks);

          props.updateClientAddress(bookingData.client.address);
          props.updateClientCity(bookingData.client.city);
          props.updateClientCompany(bookingData.client.company);
          props.updateClientCountry(bookingData.client.country);
          props.updateClientEmail(bookingData.client.email);
          props.updateClientName(bookingData.client.name);
          props.updateClientNotes(bookingData.client.notes);
          props.updateClientPhone(bookingData.client.phone);
          props.updateClientState(bookingData.client.state);
          props.updateClientTitle(bookingData.client.title);
          props.updateClientZip(bookingData.client.zip);
          props.updateBookedDate(new Date(bookingData.date));
        }
      );
    } else {
      props.fetchEquipment();
      props.fetchSnacks();
    }
  }, []);
  const validatePage = () => {
    let flag = false;
    switch (value) {
      case 0:
        flag = props.validateBookingDetails(props);
        break;
      case 1:
        flag = props.validateEquipmentDetails(props);
        break;
      case 2:
        flag = props.validateFoodDetails(props);
        break;
      default:
        flag = true;
    }
    return flag;
  };

  const handleChange = (event: React.ChangeEvent<{}>, newValue: number) => {
    if (validatePage()) setValue(newValue);
    else window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const goNextHandler = () => {
    if (validatePage()) setValue(value + 1);
    else window.scrollTo({ top: 0, behavior: "smooth" });
  };
  function formatDate(date: string) {
    var d = new Date(date),
      month = "" + (d.getMonth() + 1),
      day = "" + d.getDate(),
      year = d.getFullYear();

    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;

    return [year, month, day].join("-");
  }
  const saveBookingDetails = () => {
    if (!props.validateClientDetails(props)) {
      window.scrollTo({ top: 0, behavior: "smooth" });
      return;
    }
    const thePostData = {
      client: props.client,
      attendees: props.booking.attendees,
      date: formatDate(props.booking.date.toString().slice(0, 15)),
      equipments: props.booking.equipments
        .filter((eq) => eq.selected)
        .map((eq) => ({
          count: eq.quantity,
          equipments: eq,
        })),
      paymentMethod: props.booking.paymentMethod,
      room: props.booking.room,
      roomLayout: props.booking.roomLayout,
      snacks: props.booking.snacks
        .filter((s) => s.selected)
        .map((s) => ({
          count: s.quantity,
          snacks: s,
        })),
      status: props.booking.status,
      fromHour: props.booking.fromHour,
      toHour: props.booking.toHour,
      ...props.bookingCosts,
    };
    if (bookingId) {
      api
        .put(`/V2/bookings/${bookingId}`, thePostData, { headers: { successMsg: "Booking updated successfully!" } })
        .then(() => history.goBack())
        .catch(console.error);
    } else {
      api
        .post("/V1/bookings", thePostData, { headers: { successMsg: "Booking added successfully!" } })
        .then((res) => {
          if (res.status === 201) {
            props.setErrorMessage("Booking success");
            props.openSnackBar(true);
            props.setSnackBarType("success");
          }
          history.goBack();
        })
        .catch(console.error);
    }
  };
  return (
    <div className={classes.root}>
      <Paper square>
        <Tabs value={value} onChange={handleChange} indicatorColor="primary">
          <Tab label="Booking Details" />
          <Tab label="Equipments " />
          <Tab label="Food & Drinks" />
          <Tab label="Client Details" />
        </Tabs>
      </Paper>
      <TabPanel value={value} index={0}>
        <BookingDetailsTab isEdit={!!bookingId} />
      </TabPanel>
      <TabPanel value={value} index={1}>
        <EquipmentDetailsTab />
      </TabPanel>
      <TabPanel value={value} index={2}>
        <FoodsDrinksDetailsTab />
      </TabPanel>
      <TabPanel value={value} index={3}>
        <ClientDetailsTab isEdit={!!bookingId} />
      </TabPanel>
      <Card elevation={0} style={{ borderTop: "1px solid rgb(228, 228, 228)" }}>
        <CardHeader title="Cost details" style={{ paddingLeft: 30 }} />
        <CardContent style={{ paddingLeft: 60 }}>
          <Grid
            container
            spacing={3}
            // style={{ justifyContent:  }}
          >
            {value >= 0 && (
              <>
                <Grid item xs={3} className={classes.grid}>
                  <Typography style={{ fontWeight: "bold" }}>Room price</Typography>
                </Grid>
                <Grid item xs={3} style={{ justifyContent: "space-around" }}>
                  <Typography>₹{props.bookingCosts.roomPrice.toFixed(2)}</Typography>
                </Grid>
              </>
            )}
            {value >= 1 && (
              <>
                <Grid item xs={3} className={classes.grid}>
                  <Typography style={{ fontWeight: "bold" }}>Equpiment price</Typography>
                </Grid>
                <Grid item xs={3} style={{ justifyContent: "space-around" }}>
                  <Typography>₹{props.bookingCosts.equipmentPrice.toFixed(2)}</Typography>
                </Grid>
              </>
            )}
            {value >= 2 && (
              <>
                <Grid item xs={3} className={classes.grid}>
                  <Typography style={{ fontWeight: "bold" }}>Food & drinks price</Typography>
                </Grid>
                <Grid item xs={3} style={{ justifyContent: "space-around" }}>
                  <Typography>₹{props.bookingCosts.foodAndDrinkPrice.toFixed(2)}</Typography>
                </Grid>
              </>
            )}
            <Grid item xs={3} className={classes.grid}>
              <Typography style={{ fontWeight: "bold" }}>Sub-Total</Typography>
            </Grid>
            <Grid item xs={3} style={{ justifyContent: "space-around" }}>
              <Typography>₹{props.bookingCosts.subTotal.toFixed(2)}</Typography>
            </Grid>
            <Grid item xs={3} className={classes.grid}>
              <Typography style={{ fontWeight: "bold" }}>Tax </Typography>
            </Grid>
            <Grid item xs={3} style={{ justifyContent: "space-around" }}>
              <Typography> ₹{props.bookingCosts.tax.toFixed(2)}</Typography>
            </Grid>
            <Grid item xs={3} className={classes.grid}>
              <Typography style={{ fontWeight: "bold" }}>Deposit</Typography>
            </Grid>
            <Grid item xs={3} style={{ justifyContent: "space-around" }}>
              <Typography>₹{props.bookingCosts.deposit.toFixed(2)}</Typography>
            </Grid>
            <Grid item xs={3} className={classes.grid}>
              <Typography style={{ fontWeight: "bold" }}>Total </Typography>
            </Grid>
            <Grid item xs={3} style={{ justifyContent: "space-around" }}>
              <Typography>₹{props.bookingCosts.total.toFixed(2)} </Typography>
            </Grid>
          </Grid>
        </CardContent>
        <CardActions>
          <div className={classes.divButton}>
            {value === 3 ? (
              <Button variant="contained" color="primary" className={classes.buttons} onClick={saveBookingDetails}>
                Save
              </Button>
            ) : (
              <Button variant="contained" color="primary" className={classes.buttons} onClick={goNextHandler}>
                Next
              </Button>
            )}
            <Button
              variant="contained"
              color="secondary"
              className={classes.buttons}
              onClick={() => {
                history.goBack();
                props.resetForm();
              }}
            >
              Cancel
            </Button>
          </div>
        </CardActions>
      </Card>
    </div>
  );
});

interface TabPanelProps {
  children?: React.ReactNode;
  index: any;
  value: any;
}

const TabPanel: FunctionComponent<TabPanelProps> = ({ children, value, index, ...other }) => {
  return (
    <div role="tabpanel" hidden={value !== index} {...other}>
      {value === index && <Box p={3}>{children}</Box>}
    </div>
  );
};
