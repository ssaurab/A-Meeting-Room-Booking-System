import {
  makeStyles,
  Theme,
  createStyles,
  Collapse,
  IconButton,
  Grid,
  TextField,
  Select,
  MenuItem,
  Box,
  FormControl,
  InputLabel,
  FormHelperText,
} from "@material-ui/core";
import React, { useEffect } from "react";
import CloseIcon from "@material-ui/icons/Close";
import Alert from "@material-ui/lab/Alert";
import DateFnsUtils from "@date-io/date-fns";
import { format } from "date-fns";
import { MuiPickersUtilsProvider, KeyboardDatePicker } from "@material-ui/pickers";
import { connectAddBooking } from "./add-booking.selecter";
import { range } from "../../../app";

export interface BookingDetailsProps {
  isEdit:boolean
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    formControl: {
      margin: theme.spacing(1),
      width: "100%",
      // height: 30
    },
  })
);

interface EquipmentData {
  id: number;
  title: string;
  bookMultipleUnit: boolean;
  priceType: string;
  price: number;
  quantity: number;
  selected: boolean;
}
interface FoodDrinksData {
  id: number;
  title: string;
  quantity: number;
  price: number;
  selected: boolean;
}

export const BookingDetailsTab = connectAddBooking<BookingDetailsProps>(({isEdit,...props}) => {
  const classes = useStyles();
  useEffect(() => {
    const exclude = [];
    if (
      props.prevBookedDate &&
      format(props.prevBookedDate, "yyyy-MM-dd") === format(props.booking.date, "yyyy-MM-dd") &&
      props.booking.date &&
      props.booking.room.id
    ) {
      exclude.push(...range(props.booking.fromHour, props.booking.toHour));
    }
    props.fetchBlockedSlots(props.booking.date, props.booking.room.id, exclude);
  }, [props.booking.date, props.booking.room.id, props.prevBookedDate]);

  const [open, setOpen] = React.useState(true);
  const paymentOptions = ["Authorize.net", "Wire Transfer", "Cash", "Credit Card", "PayPal"];

  const handleDurationChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    const duration = event.target.value as string;
    let { fromHour, toHour } = props.booking;
    if (duration === "Half-day Morning") [fromHour, toHour] = [8, 12];
    else if (duration === "Half-day Afternoon") [fromHour, toHour] = [13, 17];
    props.updateBookingDuration(duration);
    props.updateBookingFromHour(fromHour);
    props.updateBookingToHour(toHour);
  };

  return (
    <div>
      <Collapse in={open}>
        <Alert
          severity="info"
          action={
            <IconButton
              aria-label="close"
              color="inherit"
              size="small"
              onClick={() => {
                setOpen(false);
              }}
            >
              <CloseIcon fontSize="inherit" />
            </IconButton>
          }
        >
          {isEdit?'Edit booking details. After updating the booking details click next to update more details.':'Add booking details. Please fill in the booking information before saving the booking.'}
        </Alert>
      </Collapse>

      <Box p={3}>
        <Grid container spacing={3}>
          <Grid item xs={6}>
            <FormControl className={classes.formControl} error={props.errors.has("date")}>
              <MuiPickersUtilsProvider utils={DateFnsUtils}>
                <KeyboardDatePicker
                  required
                  autoOk
                  variant="inline"
                  inputVariant="outlined"
                  inputProps={{ disabled: true }}
                  label="Date"
                  name="date"
                  minDate={new Date()}
                  minDateMessage="Past Dates are not allowed"
                  format="MM/dd/yyyy"
                  value={props.booking.date}
                  onChange={props.editBookingDate}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "Date is Required", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                />
              </MuiPickersUtilsProvider>
              <FormHelperText>{props.errors.get("date")}</FormHelperText>
            </FormControl>
          </Grid>
          <Grid item xs={6}>
            <FormControl variant="outlined" className={classes.formControl} required error={props.errors.has("status")}>
              <InputLabel id="status-label">Status</InputLabel>
              <Select
                labelId="status-label"
                label="Status"
                name="status"
                value={props.booking.status}
                onChange={(evt) => {
                  props.updateBookingStatus(evt.target.value as string);
                }}
                onBlur={(evt) => {
                  !evt.target.value
                    ? props.addError(evt.target.name, "Status is Required", props.errors)
                    : props.deleteError(evt.target.name, props.errors);
                }}
              >
                {["Confirmed", "Cancelled", "Pending"].map((title) => (
                  <MenuItem value={title.toLowerCase()}>{title}</MenuItem>
                ))}
              </Select>
              <FormHelperText>{props.errors.get("status")}</FormHelperText>
            </FormControl>
          </Grid>

          <Grid item xs={6}>
            <FormControl variant="outlined" className={classes.formControl} required error={props.errors.has("room")}>
              <InputLabel id="room-label">Room</InputLabel>
              <Select
                labelId="room-label"
                value={props.booking.room.id}
                label="Room"
                name="room"
                onChange={(evt) =>
                  props.updateBookingRoom(props.availableRooms.find((r) => r.id === (evt.target.value as number))!)
                }
                onBlur={(evt) => {
                  !evt.target.value
                    ? props.addError(evt.target.name, "Room is Required", props.errors)
                    : props.deleteError(evt.target.name, props.errors);
                }}
              >
                {props.availableRooms.map((room) => (
                  <MenuItem value={room.id}>{room.name}</MenuItem>
                ))}
              </Select>
              <FormHelperText>{props.errors.get("room")}</FormHelperText>
            </FormControl>
          </Grid>
          <Grid item xs={6}>
            <FormControl
              variant="outlined"
              className={classes.formControl}
              disabled={!props.booking.room?.id}
              required
              error={props.errors.has("roomLayout")}
            >
              <InputLabel id="layout-label">Room Layout</InputLabel>
              <Select
                labelId="layout-label"
                value={props.booking.roomLayout.id}
                name="roomLayout"
                label="Room Layout"
                onChange={(evt) =>
                  props.updateBookingRoomLayout(props.availableLayouts.find((l) => l.id === (evt.target.value as number))!)
                }
                onBlur={(evt) => {
                  !evt.target.value
                    ? props.addError(evt.target.name, "Room Layout is Required", props.errors)
                    : props.deleteError(evt.target.name, props.errors);
                }}
              >
                {props.availableLayouts.map((layout) => (
                  <MenuItem value={layout.id}>{layout.title}</MenuItem>
                ))}
              </Select>
              <FormHelperText>{props.errors.get("roomLayout")}</FormHelperText>
            </FormControl>
          </Grid>
          <Grid item xs={6}>
            <FormControl className={classes.formControl}>
              <TextField
                required
                label="Attendees"
                type="number"
                name="attendees"
                variant="outlined"
                error={props.errors.has("attendees")}
                helperText={props.errors.get("attendees")}
                value={props.booking.attendees}
                inputProps={{
                  min: 0,
                  max: props.booking.room.capacity,
                }}
                disabled={!props.booking.room.id}
                onChange={(evt) => {
                  props.updateBookingAttendees(parseInt(evt.target.value));
                }}
                onBlur={(evt) => {
                  !evt.target.value
                    ? props.addError(evt.target.name, "Attendees must be non-zero", props.errors)
                    : parseInt(evt.target.value) <= 0
                    ? props.addError(evt.target.name, "Attendees must be more than zero", props.errors)
                    : parseInt(evt.target.value) > props.booking.room.capacity
                    ? props.addError(
                        evt.target.name,
                        `Max capacity for selected room is ${props.booking.room.capacity}`,
                        props.errors
                      )
                    : props.deleteError(evt.target.name, props.errors);
                }}
              />
            </FormControl>
          </Grid>
          <Grid item xs={6}>
            <FormControl
              variant="outlined"
              className={classes.formControl}
              required
              error={props.errors.has("paymentMethod")}
            >
              <InputLabel id="payment-label">Payment Mode</InputLabel>
              <Select
                labelId="payment-label"
                value={props.booking.paymentMethod}
                name="paymentMethod"
                onChange={(evt) => {
                  props.updateBookingPaymentMethod(evt.target.value as string);
                }}
                onBlur={(evt) => {
                  !evt.target.value
                    ? props.addError(evt.target.name, "Payment Method is Required", props.errors)
                    : props.deleteError(evt.target.name, props.errors);
                }}
                label="Payment"
              >
                {paymentOptions.map((title) => (
                  <MenuItem value={title}>{title}</MenuItem>
                ))}
              </Select>
              <FormHelperText>{props.errors.get("paymentMethod")}</FormHelperText>
            </FormControl>
          </Grid>

          <Grid alignContent="flex-start" item xs={6}>
            <FormControl variant="outlined" className={classes.formControl}>
              <InputLabel id="duration">Duration</InputLabel>
              <Select
                labelId="duration"
                id="duration"
                value={props.booking.duration}
                label="Duration"
                onChange={handleDurationChange}
              >
                <MenuItem value="Select Hours">Select Hours</MenuItem>
                <MenuItem value="Half-day Morning" disabled={range(8, 12).some((s) => props.blockedSlots.has(s))}>
                  Half-day Morning(08:00 - 12:00)
                </MenuItem>
                <MenuItem value="Half-day Afternoon" disabled={range(13, 17).some((s) => props.blockedSlots.has(s))}>
                  Half-day Afternoon(13:00 - 17:00)
                </MenuItem>
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={6}></Grid>

          {props.booking.duration === "Select Hours" && (
            <Grid alignContent="flex-start" item xs={6}>
              <FormControl variant="outlined" className={classes.formControl} required error={props.errors.has("fromHour")}>
                <InputLabel id="from-hour">From Hour</InputLabel>
                <Select
                  labelId="from-hour"
                  value={props.booking.fromHour}
                  label="From Hour"
                  name="fromHour"
                  onChange={(e) => props.updateBookingFromHour(+(e.target.value as number))}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "From hour is Required", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                >
                  <MenuItem value={0}></MenuItem>
                  {range(8, 22).map((slot) => (
                    <MenuItem
                      value={slot}
                      disabled={
                        props.blockedSlots.has(slot) ||
                        (!!props.booking.toHour && slot >= props.booking.toHour) ||
                        (!!props.booking.toHour && range(slot, props.booking.toHour).some((s) => props.blockedSlots.has(s)))
                      }
                    >
                      {slot + ":00"}
                    </MenuItem>
                  ))}
                </Select>
                <FormHelperText>{props.errors.get("fromHour")}</FormHelperText>
              </FormControl>
            </Grid>
          )}
          {props.booking.duration === "Select Hours" && (
            <Grid alignContent="flex-start" item xs={6}>
              <FormControl variant="outlined" className={classes.formControl} required error={props.errors.has("toHour")}>
                <InputLabel id="to-hour">To Hour</InputLabel>
                <Select
                  labelId="to-hour"
                  value={props.booking.toHour}
                  label="To Hour"
                  name="toHour"
                  onChange={(e) => props.updateBookingToHour(+(e.target.value as number))}
                  onBlur={(evt) => {
                    !evt.target.value
                      ? props.addError(evt.target.name, "To hour is Required", props.errors)
                      : props.deleteError(evt.target.name, props.errors);
                  }}
                >
                  <MenuItem value={0}></MenuItem>
                  {range(9, 23).map((slot) => (
                    <MenuItem
                      value={slot}
                      disabled={
                        props.blockedSlots.has(slot - 1) ||
                        (!!props.booking.fromHour && slot <= props.booking.fromHour) ||
                        (!!props.booking.fromHour &&
                          range(props.booking.fromHour, slot).some((s) => props.blockedSlots.has(s)))
                      }
                    >
                      {slot + ":00"}
                    </MenuItem>
                  ))}
                </Select>
                <FormHelperText>{props.errors.get("toHour")}</FormHelperText>
              </FormControl>
            </Grid>
          )}
        </Grid>
      </Box>
    </div>
  );
});
