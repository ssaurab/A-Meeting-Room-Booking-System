import React, { useEffect } from "react";
import { Box, FormControl, InputLabel } from "@material-ui/core";
import { RouteComponentProps } from "react-router-dom";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import TextField from "@material-ui/core/TextField";
import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import { connectBook } from "./book.selecter";

interface personalDetails {
  title: string;
  name: string;
  email: string;
  phoneNo: string;
  notes: string;
}
interface billingAddress {
  company: string;
  address: string;
  city: string;
  state: string;
  zip: string;
  country: string;
}

interface paymentDetails {
  method: string;
  cardName: string;
  cardNumber: string;
  expDate: string;
  cvv: string;
}

interface productDetails {
  fieldName: string;
  description: string;
  pricePerUnit: number;
  totalPrice: number;
}

interface checkoutDetails extends personalDetails, billingAddress, paymentDetails {}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      flexGrow: 1,
      "& .MuiTextField-root": {
        margin: theme.spacing(0),
      },
    },
    formControl: {
      marginTop: theme.spacing(2),
      marginBottom: theme.spacing(2),
      width: "100%",
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

export interface CheckoutStepProps extends RouteComponentProps<{ roomId: string }> {}

export const CheckoutStep = connectBook<CheckoutStepProps>((props) => {
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
  const showError = new Map([...props.clientErrors.errors.entries()].filter(([field]) => props.touched.has(field)));
  const viewError = (field: string) => (props.touched.has(field) ? showError.has(field) : props.errors.has(field));

  const errorText = (field: string) => (props.touched.has(field) ? showError.get(field) : props.errors.get(field));

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
            Details and Payment
          </h1>
        </Grid>
      </Grid>
      <Paper className={classes.paper}>
        <Grid container spacing={2}>
          <Typography variant="subtitle1" gutterBottom>
            PERSONAL DETAILS
          </Typography>
          <Grid container spacing={3}>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                error={viewError("title")}
                helperText={errorText("title")}
                id="Title"
                name="title"
                label="Title"
                fullWidth
                variant="outlined"
                value={props.checkout.clientDetails.title}
                onChange={(e) => props.updateCheckoutClientDetails("title")(e.target.value)}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                error={viewError("name")}
                helperText={errorText("name")}
                id="Name"
                name="name"
                label="Name"
                fullWidth
                variant="outlined"
                value={props.checkout.clientDetails.name}
                onChange={(e) => props.updateCheckoutClientDetails("name")(e.target.value)}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                error={viewError("email")}
                helperText={errorText("email")}
                id="email"
                name="email"
                label="email"
                fullWidth
                variant="outlined"
                value={props.checkout.clientDetails.email}
                onChange={(e) => props.updateCheckoutClientDetails("email")(e.target.value)}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                error={viewError("phone")}
                helperText={errorText("phone")}
                id="phoneNo"
                name="phoneNo"
                label="Phone Number"
                fullWidth
                variant="outlined"
                value={props.checkout.clientDetails.phone}
                onChange={(e) => props.updateCheckoutClientDetails("phone")(e.target.value)}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="notes"
                name="notes"
                label="Notes"
                fullWidth
                variant="outlined"
                value={props.checkout.clientDetails.notes}
                onChange={(e) => props.updateCheckoutClientDetails("notes")(e.target.value)}
              />
            </Grid>
          </Grid>
        </Grid>
      </Paper>
      <Paper className={classes.paper}>
        <Grid container spacing={2}>
          <Typography variant="subtitle1" gutterBottom>
            BILLING ADDRESS
          </Typography>
          <Grid item xs={12}>
            <TextField
              required
              error={viewError("company")}
              helperText={errorText("company")}
              id="company"
              name="company"
              label="Company"
              fullWidth
              variant="outlined"
              value={props.checkout.clientDetails.company}
              onChange={(e) => props.updateCheckoutClientDetails("company")(e.target.value)}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              required
              error={viewError("address")}
              helperText={errorText("address")}
              id="address"
              name="address"
              label="Address"
              fullWidth
              variant="outlined"
              value={props.checkout.clientDetails.address}
              onChange={(e) => props.updateCheckoutClientDetails("address")(e.target.value)}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              required
              error={viewError("city")}
              helperText={errorText("city")}
              id="city"
              name="city"
              label="City"
              fullWidth
              variant="outlined"
              value={props.checkout.clientDetails.city}
              onChange={(e) => props.updateCheckoutClientDetails("city")(e.target.value)}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              required
              error={viewError("state")}
              helperText={errorText("state")}
              id="state"
              name="state"
              label="State/Province/Region"
              fullWidth
              variant="outlined"
              value={props.checkout.clientDetails.state}
              onChange={(e) => props.updateCheckoutClientDetails("state")(e.target.value)}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              required
              error={viewError("zip")}
              helperText={errorText("zip")}
              id="zip"
              name="zip"
              label="Zip / Postal code"
              fullWidth
              variant="outlined"
              value={props.checkout.clientDetails.zip}
              onChange={(e) => props.updateCheckoutClientDetails("zip")(e.target.value)}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              required
              error={viewError("country")}
              helperText={errorText("country")}
              id="country"
              name="country"
              label="Country"
              fullWidth
              variant="outlined"
              value={props.checkout.clientDetails.country}
              onChange={(e) => props.updateCheckoutClientDetails("country")(e.target.value)}
            />
          </Grid>
        </Grid>
      </Paper>
      <Paper className={classes.paper}>
        <Grid container spacing={2}>
          <Typography variant="subtitle1" gutterBottom>
            PAYMENT DETAILS
          </Typography>
          <Grid container spacing={0}>
            <FormControl variant="outlined" className={classes.formControl}>
              <InputLabel id="duration">Payment Method *</InputLabel>
              <Select
                value={props.checkout.paymentDetails.method}
                label="Payment Method *"
                onChange={(e) => props.updateCheckoutPaymentDetails("method")(e.target.value)}
              >
                <MenuItem value="Cash">Cash</MenuItem>
                <MenuItem value="Credit card">Credit card</MenuItem>
                <MenuItem value="PayPal">PayPal</MenuItem>
              </Select>
            </FormControl>
          </Grid>

          <Box display={"Credit card" === props.checkout.paymentDetails.method ? "block" : "none"}>
            <br />
            <Grid container spacing={3}>
              <Grid item xs={12} md={6}>
                <TextField
                  required
                  error={viewError("nameOnCard")}
                  helperText={errorText("nameOnCard")}
                  id="cardName"
                  label="Name on card"
                  fullWidth
                  variant="outlined"
                  autoComplete="cc-name"
                  value={props.checkout.paymentDetails.nameOnCard}
                  onChange={(e) => props.updateCheckoutPaymentDetails("nameOnCard")(e.target.value)}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                  required
                  error={viewError("cardNumber")}
                  helperText={errorText("cardNumber")}
                  id="cardNumber"
                  label="Card number"
                  fullWidth
                  variant="outlined"
                  autoComplete="cc-number"
                  value={props.checkout.paymentDetails.cardNumber}
                  onChange={(e) => props.updateCheckoutPaymentDetails("cardNumber")(e.target.value)}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                  required
                  error={viewError("expirydate")}
                  helperText={errorText("expirydate")}
                  id="expDate"
                  label="Expiry date"
                  fullWidth
                  variant="outlined"
                  autoComplete="cc-exp"
                  value={props.checkout.paymentDetails.expiryDate}
                  onChange={(e) => props.updateCheckoutPaymentDetails("expiryDate")(e.target.value)}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                  required
                  error={viewError("cvv")}
                  id="cvv"
                  label="CVV"
                  helperText="Last three digits on signature strip"
                  fullWidth
                  variant="outlined"
                  autoComplete="cc-csc"
                  value={props.checkout.paymentDetails.cvv}
                  onChange={(e) => props.updateCheckoutPaymentDetails("cvv")(e.target.value)}
                />
              </Grid>
            </Grid>
          </Box>
        </Grid>
      </Paper>
    </div>
  );
});
