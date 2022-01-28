import DateFnsUtils from "@date-io/date-fns";
import { FormControl, FormHelperText } from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import ToggleButton from "@material-ui/lab/ToggleButton";
import { KeyboardDatePicker, MuiPickersUtilsProvider } from "@material-ui/pickers";
import React, { useEffect } from "react";
import { RouteComponentProps } from "react-router-dom";
import { range } from "../app";
import { connectBook } from "./book.selecter";

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    formControl: {
      marginTop: theme.spacing(2),
      marginBottom: theme.spacing(2),
      width: "100%",
    },
    selectEmpty: {
      marginTop: theme.spacing(2),
    },
    buttons: {
      margin: 10,
      width: 100,
    },
    divButton: {
      display: "flex",
      justifyContent: "center",
    },
    paper: {
      padding: theme.spacing(5),
      margin: theme.spacing(2),
      boxSizing: "border-box",
      display: "block",
    },
    toogleButton: {
      width: 120,
      marginRight: 10,
      background: "#eceff1",
      marginBottom: 10,
    },
    btnselected: {
      backgroundColor: theme.palette.primary.light + "!important",
      color: "white !important",
    },
  })
);

interface BookRoomPageState {
  duration: "Select Hours" | "Half-day Morning" | "Half-day Afternoon";
  date: Date | null;
}

export interface BookRoomStepProps extends RouteComponentProps<{ roomId: string }> {}

export const BookRoomStep = connectBook<BookRoomStepProps>((props) => {
  const classes = useStyles();

  useEffect(() => props.fetchBlockedSlots(props.bookRoom.date, props.match.params.roomId), [
    props.bookRoom.date,
    props.match.params.roomId,
  ]);

  const [state, setState] = React.useState<BookRoomPageState>({
    duration: "Select Hours",
    date: new Date(),
  });

  const handleChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    const duration = event.target.value as BookRoomPageState["duration"];
    let { from, to } = props.bookRoom;
    if (duration === "Half-day Morning") [from, to] = [8, 12];
    else if (duration === "Half-day Afternoon") [from, to] = [13, 17];
    setState((prevState) => ({
      ...prevState,
      duration,
    }));
    props.updateBookRoom("from")(from);
    props.updateBookRoom("to")(to);
  };

  var buttons: JSX.Element[] = [];
  for (let i = 8; i < 23; i++) {
    buttons.push(
      <ToggleButton value={i.toString()} classes={{ selected: classes.btnselected }}>
        {i}:00 - {i + 1}:00
      </ToggleButton>
    );
  }
  const showError = new Map([...props.clientErrors.errors.entries()].filter(([field]) => props.touched.has(field)));
  const viewError = (field: string) => (props.touched.has(field) ? showError.has(field) : props.errors.has(field));

  const errorText = (field: string) => (props.touched.has(field) ? showError.get(field) : props.errors.get(field));
  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <h1
          style={{
            textAlign: "center",
            fontSize: "1.3rem",
            textShadow: "0px 1px 1px",
          }}
        >
          Book Room
        </h1>
      </Grid>
      <Grid item xs={6}>
        <FormControl className={classes.formControl}>
          <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <KeyboardDatePicker
              autoOk
              minDate={new Date()}
              minDateMessage="Past Dates are not allowed"
              variant="inline"
              inputVariant="outlined"
              inputProps={{ disabled: true }}
              label="Date"
              required
              format="MM/dd/yyyy"
              value={props.bookRoom.date}
              onBlur={() => props.updateTouched("date")}
              onChange={props.updateBookRoom("date")}
              error={viewError("date")}
              helperText={errorText("date")}
            />
          </MuiPickersUtilsProvider>
        </FormControl>
      </Grid>
      <Grid alignContent="flex-start" item xs={6}>
        <FormControl variant="outlined" className={classes.formControl}>
          <InputLabel id="duration">Duration *</InputLabel>
          <Select
            labelId="duration"
            id="client-booking-duration"
            test-id="Duration"
            value={state.duration}
            label="Duration *"
            onChange={handleChange}
            onBlur={() => props.updateTouched("duration")}
          >
            <MenuItem value="Select Hours">Select Hours</MenuItem>
            <MenuItem value="Half-day Morning" disabled={range(8, 12).some((s) => props.bookRoom.blockedSlots.has(s))}>
              Half-day Morning(08:00 - 12:00)
            </MenuItem>
            <MenuItem value="Half-day Afternoon" disabled={range(13, 17).some((s) => props.bookRoom.blockedSlots.has(s))}>
              Half-day Afternoon(13:00 - 17:00)
            </MenuItem>
          </Select>
        </FormControl>
      </Grid>

      {state.duration === "Select Hours" && (
        <Grid alignContent="flex-start" item xs={6}>
          <FormControl variant="outlined" className={classes.formControl} required error={viewError("from")}>
            <InputLabel id="from-hour">From Hour</InputLabel>
            <Select
              labelId="from-hour"
              value={props.bookRoom.from}
              label="From Hour *"
              onChange={(e) => props.updateBookRoom("from")(+(e.target.value as number))}
              onBlur={() => props.updateTouched("from")}
            >
              <MenuItem value={0}></MenuItem>
              {range(8, 22).map((slot) => (
                <MenuItem
                  value={slot}
                  disabled={
                    props.bookRoom.blockedSlots.has(slot) ||
                    (!!props.bookRoom.to && slot >= props.bookRoom.to) ||
                    (!!props.bookRoom.to && range(slot, props.bookRoom.to).some((s) => props.bookRoom.blockedSlots.has(s)))
                  }
                >
                  {slot + ":00"}
                </MenuItem>
              ))}
            </Select>
            <FormHelperText>{errorText("from") || "Disabled Slots are not available"}</FormHelperText>
          </FormControl>
        </Grid>
      )}
      {state.duration === "Select Hours" && (
        <Grid alignContent="flex-start" item xs={6}>
          <FormControl variant="outlined" className={classes.formControl} required error={viewError("to")}>
            <InputLabel id="to-hour">To Hour</InputLabel>
            <Select
              labelId="to-hour"
              value={props.bookRoom.to}
              label="To Hour *"
              onChange={(e) => props.updateBookRoom("to")(+(e.target.value as number))}
              onBlur={() => props.updateTouched("to")}
            >
              <MenuItem value={0}></MenuItem>
              {range(9, 23).map((slot) => (
                <MenuItem
                  value={slot}
                  disabled={
                    props.bookRoom.blockedSlots.has(slot - 1) ||
                    (!!props.bookRoom.from && slot <= props.bookRoom.from) ||
                    (!!props.bookRoom.from &&
                      range(props.bookRoom.from, slot).some((s) => props.bookRoom.blockedSlots.has(s)))
                  }
                >
                  {slot + ":00"}
                </MenuItem>
              ))}
            </Select>
            <FormHelperText>{errorText("to") || "Disabled Slots are not available"}</FormHelperText>
          </FormControl>
        </Grid>
      )}
      <Grid item xs={6}>
        <FormControl variant="outlined" className={classes.formControl}>
          <TextField
            required
            label="Attendees"
            variant="outlined"
            type="number"
            id="attendees"
            inputProps={{
              min: 0,
              max: props.room.capacity,
            }}
            value={props.bookRoom.attendees}
            onChange={(e) => props.updateBookRoom("attendees")(+e.target.value)}
            error={viewError("attendees")}
            helperText={errorText("attendees")}
          />
        </FormControl>
      </Grid>
    </Grid>
  );
});
