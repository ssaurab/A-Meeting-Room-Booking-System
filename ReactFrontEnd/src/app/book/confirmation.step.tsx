import React, { useEffect } from "react";
import { RouteComponentProps } from "react-router-dom";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";
import { connectBook } from "./book.selecter";
// @ts-ignore
import * as CurrencyFormat from "react-currency-format";

export interface ReturnData {
  attendees: number;
  client: {
    address: string;
    city: string;
    company: string;
    country: string;
    email: string;
    id: number;
    name: string;
    notes: string;
    phone: string;
    state: string;
    title: string;
    zip: string;
  };
  date: string;
  deposit: number;
  equipmentPrice: number;
  equipments: {
    count: number;
    equipments: {
      bookMultipleUnit: true;
      id: number;
      price: number;
      priceType: string;
      title: string;
    };
  }[];
  foodAndDrinkPrice: number;
  fromHour: number;
  id: number;
  paymentMethod: string;
  room: {
    capacity: number;
    description: string;
    id: number;
    image: string;
    layouts: [
      {
        id: number;
        image: string;
        title: string;
      }
    ];
    name: string;
    pricePerDay: number;
    pricePerHalfDay: number;
    pricePerHour: number;
    status: string;
  };
  roomLayout: {
    id: number;
    image: string;
    title: string;
  };
  roomPrice: number;
  snacks: {
    count: number;
    snacks: {
      id: number;
      price: number;
      title: string;
    };
  }[];
  status: string;
  subTotal: number;
  tax: number;
  toHour: number;
  total: number;
}

interface equipments {
  count: number;
  equipments: {
    bookMultipleUnit: true;
    id: number;
    price: number;
    priceType: string;
    title: string;
  };
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      flexGrow: 1,
      "& .MuiTextField-root": {
        margin: theme.spacing(0),
      },
    },
    paper: {
      padding: theme.spacing(5),
      margin: theme.spacing(2, "auto"),
      maxWidth: 800,
    },
    material: {
      padding: theme.spacing(5),
      margin: theme.spacing(2, "auto"),
      maxWidth: 880,
    },
  })
);

export interface ConfirmationStepProps
  extends RouteComponentProps<{ roomId: string }> {}

export const ConfirmationStep = connectBook<ConfirmationStepProps>((props) => {
  const classes = useStyles();

  useEffect(() => {
    const selectedRows1 = props.roomSetup.availableEquipments.filter((ele) => ele.selected === true);
    props.updateRoomSetup("selectedEquipments")(
      selectedRows1.map(({ id, title, price, priceType, quantity }) => ({
        id,
        title,
        price,
        priceType,
        count: quantity,
      }))
    );

    const selectedRows = props.foodDrinks.availableSnacks.filter((ele) => ele.selected === true);
    props.updateFoodDrinks("selectedSnacks")(
      selectedRows.map(({ id, title, price, count }) => ({
        id,
        title,
        price,
        count,
      }))
    );
  }, []);

  const priceForTime = () => {
    const time = +props.bookRoom.to - +props.bookRoom.from;
    if (time >= 8) {
      return +props.room.pricePerDay;
    } else if (time >= 4) {
      return +props.room.pricePerHalfDay;
    } else {
      return +props.room.pricePerHour;
    }
  };

  const priceType = () => {
    const time = +props.bookRoom.to - +props.bookRoom.from;
    if (time >= 8) {
      return " per day";
    } else if (time >= 4) {
      return " half-day";
    } else {
      return " per hour";
    }
  };

  const computeSubTotal = () => {
    let total = 0;
    props.roomSetup.selectedEquipments.forEach((equipment) => {
      total += equipment.count * equipment.price*(equipment.priceType === "perHour" ?(+props.bookRoom.to - +props.bookRoom.from):1);
    });

    props.foodDrinks.selectedSnacks.forEach((snack) => {
      total += snack.count * snack.price;
    });

    total += +props.bookRoom.to - +props.bookRoom.from < 4 ? priceForTime() *(+props.bookRoom.to - +props.bookRoom.from)
    : priceForTime()

    return total;
  };

  const computeTax = () => {
    let total = computeSubTotal();
    return 0.1 * total;
  };

  const computeTotal = () => {
    let subtotal = computeSubTotal();
    let tax = computeTax();

    return subtotal + tax;
  };

  return (
    <div className={classes.root}>
      <Grid container spacing={0}>
        <Grid item xs={12}>
          <h1
            style={{
              textAlign: "center",
              fontSize: "1.3rem",
              textShadow: "0px 1px 1px",
            }}
          >
            Booking Confirmation
          </h1>
        </Grid>
      </Grid>
      <Paper className={classes.paper}>
        <Grid container spacing={2}>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              <b>{props.room.name}</b>
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              <b>{props.room.capacity + " people"}</b>
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              <CurrencyFormat
                value={priceForTime() + ".00"}
                displayType={"text"}
                thousandSeparator={true}
                prefix={"\u20B9"}
              />
              {priceType()}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              <b>
                <CurrencyFormat
                  value={
                    +props.bookRoom.to - +props.bookRoom.from < 4
                      ? priceForTime() *
                          (+props.bookRoom.to - +props.bookRoom.from) +
                        ".00"
                      : priceForTime() + ".00"
                  }
                  displayType={"text"}
                  thousandSeparator={true}
                  prefix={"\u20B9"}
                />
              </b>
            </Typography>
          </Grid>
        </Grid>

        <Grid container spacing={2}>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              Date:
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              <b>{props.bookRoom.date.toDateString()}</b>
            </Typography>
          </Grid>
          <Grid item xs={3} sm={2}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={2}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
        </Grid>

        <Grid container spacing={2}>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              Attendees:
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              <b>{props.bookRoom.attendees}</b>
            </Typography>
          </Grid>
          <Grid item xs={3} sm={2}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={2}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
        </Grid>

        <Grid container spacing={2}>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              Layout:
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              <b>{props.roomSetup.selectedLayout.title}</b>
            </Typography>
          </Grid>
          <Grid item xs={3} sm={2}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={2}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
        </Grid>

        <Grid container spacing={2}>
          <Grid item xs={12} sm={12}>
            <Typography variant="body2" gutterBottom>
              Equipment:
            </Typography>
          </Grid>
        </Grid>
        {props.roomSetup.selectedEquipments.map((row) => (
          <Grid container item xs={12} sm={12}>
            <Grid item xs={3} sm={3}>
              <Typography variant="body2" gutterBottom>
                {" "}
              </Typography>
            </Grid>
            <Grid item xs={3} sm={3}>
              <Typography variant="body2" gutterBottom>
                <b>{row.title + " x " + row.count}</b>
              </Typography>
            </Grid>
            <Grid item xs={3} sm={3}>
              <Typography variant="body2" gutterBottom>
                <CurrencyFormat
                  value={row.price + ".00"}
                  displayType={"text"}
                  thousandSeparator={true}
                  prefix={"\u20B9"}
                />{" "}
                {row.priceType === "perHour" ? "per hour" : "per booking"}
              </Typography>
            </Grid>
            <Grid item xs={3} sm={3}>
              <Typography variant="body2" gutterBottom>
                <b>
                  <CurrencyFormat
                    value={row.count * row.price*(row.priceType === "perHour" ?(+props.bookRoom.to - +props.bookRoom.from):1) + ".00"}
                    displayType={"text"}
                    thousandSeparator={true}
                    prefix={"\u20B9"}
                  />
                </b>
              </Typography>
            </Grid>
          </Grid>
        ))}

        <Grid container spacing={2}>
          <Grid item xs={12} sm={12}>
            <Typography variant="body2" gutterBottom>
              Food and Drinks:
            </Typography>
          </Grid>
        </Grid>
        {props.foodDrinks.selectedSnacks.map((row) => (
          <Grid container item xs={12} sm={12}>
            <Grid item xs={3} sm={3}>
              <Typography variant="body2" gutterBottom>
                {" "}
              </Typography>
            </Grid>
            <Grid item xs={3} sm={3}>
              <Typography variant="body2" gutterBottom>
                <b>{row.title + " x " + row.count}</b>
              </Typography>
            </Grid>
            <Grid item xs={3} sm={3}>
              <Typography variant="body2" gutterBottom>
                <CurrencyFormat
                  value={row.price + ".00"}
                  displayType={"text"}
                  thousandSeparator={true}
                  prefix={"\u20B9"}
                />{" "}
                per person
              </Typography>
            </Grid>
            <Grid item xs={3} sm={3}>
              <Typography variant="body2" gutterBottom>
                <b>
                  <CurrencyFormat
                    value={row.count * row.price + ".00"}
                    displayType={"text"}
                    thousandSeparator={true}
                    prefix={"\u20B9"}
                  />
                </b>
              </Typography>
            </Grid>
          </Grid>
        ))}

        <Grid container item xs={12} sm={12}>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              Sub Total:
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="subtitle1" gutterBottom>
              <b>
                <CurrencyFormat
                  value={computeSubTotal() + ".00"}
                  displayType={"text"}
                  thousandSeparator={true}
                  prefix={"\u20B9"}
                />
              </b>
            </Typography>
          </Grid>
        </Grid>

        <Grid container item xs={12} sm={12}>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              Tax:
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="subtitle1" gutterBottom>
              <b>
                <CurrencyFormat
                  value={computeTax() + ".00"}
                  displayType={"text"}
                  thousandSeparator={true}
                  prefix={"\u20B9"}
                />
              </b>
            </Typography>
          </Grid>
        </Grid>

        <Grid container item xs={12} sm={12}>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              Total:
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="body2" gutterBottom>
              {" "}
            </Typography>
          </Grid>
          <Grid item xs={3} sm={3}>
            <Typography variant="subtitle1" gutterBottom>
              <b>
                <CurrencyFormat
                  value={computeTotal() + ".00"}
                  displayType={"text"}
                  thousandSeparator={true}
                  prefix={"\u20B9"}
                />
              </b>
            </Typography>
          </Grid>
        </Grid>
      </Paper>

      <Paper className={classes.paper}>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6}>
            <Typography variant="subtitle1" gutterBottom>
              <b>PERSONAL DETAILS </b>
            </Typography>
            <Typography gutterBottom variant="body2">
              <b>Title:</b>
              {" " + props.checkout.clientDetails.title}
            </Typography>
            <Typography gutterBottom variant="body2">
              <b>Name:</b> {" " + props.checkout.clientDetails.name}
            </Typography>
            <Typography gutterBottom variant="body2">
              <b>Email:</b>
              {" " + props.checkout.clientDetails.email}
            </Typography>
            <Typography gutterBottom variant="body2">
              <b>PhoneNo:</b>
              {" " + props.checkout.clientDetails.phone}
            </Typography>
          </Grid>

          <Grid item xs={12} sm={6}>
            <Typography variant="subtitle1" gutterBottom>
              <b>BILLING ADDRESS</b>
            </Typography>
            <Typography gutterBottom variant="body2">
              <b>Company:</b> {" " + props.checkout.clientDetails.company}
            </Typography>
            <Typography gutterBottom variant="body2">
              <b>Address:</b>
              {" " +
                props.checkout.clientDetails.address +
                ", " +
                props.checkout.clientDetails.city +
                ", " +
                props.checkout.clientDetails.state +
                ", " +
                props.checkout.clientDetails.country +
                ", " +
                props.checkout.clientDetails.zip +
                "."}
            </Typography>
          </Grid>

          <Grid item xs={12} sm={6}>
            <Typography variant="subtitle1" gutterBottom>
              <b>PAYMENT DETAILS</b>
            </Typography>
            <Typography gutterBottom variant="body2">
              <b>Payment Method:</b>{" "}
              {" " + props.checkout.paymentDetails.method}
            </Typography>
          </Grid>
        </Grid>
      </Paper>
    </div>
  );
});
