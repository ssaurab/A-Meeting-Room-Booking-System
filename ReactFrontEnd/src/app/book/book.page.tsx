import React, { useEffect } from "react";
import { RouteComponentProps } from "react-router-dom";
import { CheckoutStep } from "./checkout.step";
import { ConfirmationStep } from "./confirmation.step";
import { BookRoomStep } from "./book-room.step";
import { RoomSetupStep } from "./room-setup.step";
import { FoodDrinksStep } from "./food-drinks.step";
import {
  makeStyles,
  Theme,
  createStyles,
  Stepper,
  Step,
  Button,
  StepButton,
  Grid,
  Paper,
  Typography,
} from "@material-ui/core";
import { connectBook } from "./book.selecter";
import { format } from "date-fns";

export interface BookPageProps extends RouteComponentProps<{ roomId: string }> {}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      width: "100%",
    },
    button: {
      marginRight: theme.spacing(1),
    },
    instructions: {
      marginTop: theme.spacing(1),
      marginBottom: theme.spacing(1),
    },
    divStepButtons: {
      width: "100%",
      display: "flex",
      justifyContent: "center",
      marginBottom: "15px",
    },
    paper: {
      padding: theme.spacing(5),
      margin: theme.spacing(2),
      width: "100%",
      boxSizing: "border-box",
      display: "block",
    },
    sideRoom: {
      width: "100%",
      display: "flex",
      justifyContent: "center",
    },
  })
);

export const BookPage = connectBook<BookPageProps>((props) => {
  const { roomId } = props.match.params;
  const { history } = props;
  if (!props.error) {
    history.push("/");
    props.updateError(true);
  }
  useEffect(() => {
    props.fetchRoom(roomId);
    props.getEquipmentsAndSnacks();
    props.initializeFields();
  }, []);

  const classes = useStyles();
  const [steps, setSteps] = React.useState([
    {
      title: "Book Room",
      component: <BookRoomStep {...props} />,
      completed: false,
    },
    {
      title: "Room Setup",
      component: <RoomSetupStep {...props} />,
      completed: false,
    },
    {
      title: "Food & Drinks",
      component: <FoodDrinksStep {...props} />,
      completed: false,
    },
    {
      title: "Checkout",
      component: <CheckoutStep {...props} />,
      completed: false,
    },
    {
      title: "Confirmation",
      component: <ConfirmationStep {...props} />,
      completed: false,
    },
  ]);
  const [activeStep, setActiveStep] = React.useState(0);
  const stepBack = () => setActiveStep(activeStep - 1);
  const stepNext = () => {
    if (!props.validateBookRoom[activeStep](props)) {
      window.scrollTo({ top: 0, behavior: "smooth" });
      return;
    }
    setSteps(
      steps.map((s, i) => ({
        ...s,
        completed: i === activeStep ? true : s.completed,
      }))
    );
    setActiveStep(activeStep + 1);
  };
  const stepJump = (toStep: number) => {
    if (toStep < activeStep && steps[toStep].completed) setActiveStep(toStep);
    else if (steps[activeStep].completed && steps[toStep - 1].completed) {
      if (!props.validateBookRoom[activeStep](props)) {
        window.scrollTo({ top: 0, behavior: "smooth" });
        steps.map((s, i) => ({
          ...s,
          completed: i === activeStep ? false : s.completed,
        }));
        return;
      }
      setSteps(
        steps.map((s, i) => ({
          ...s,
          completed: i === activeStep ? true : s.completed,
        }))
      );
      setActiveStep(toStep);
    }
  };
  return (
    <div className={classes.root}>
      <Stepper nonLinear activeStep={activeStep}>
        {steps.map(({ title, completed }, index) => (
          <Step>
            <StepButton
              onClick={() => stepJump(index)}
              completed={completed}
              style={{ backgroundColor: "rgba(0, 0, 0, 0.05)", borderRadius: 8 }}
            >
              {title}
            </StepButton>
          </Step>
        ))}
      </Stepper>
      <div>
        <Grid container>
          <Grid item md={4} className={classes.sideRoom}>
            <Paper className={classes.paper}>
              <img src={props.room.image} alt={props.room.name} className={classes.root} />
              <Typography variant="h6">
                Room: <b>{props.room.name}</b>
              </Typography>
              <Typography variant="subtitle1">
                Capacity: <b>{props.room.capacity}</b>
              </Typography>
              <Typography variant="subtitle1">
                Prices: <br />
                {!!props.room.pricePerHour && (
                  <b>
                    ₹{props.room.pricePerHour} per hour
                    <br />
                  </b>
                )}
                {!!props.room.pricePerHalfDay && <b>₹{props.room.pricePerHalfDay} per half-day</b>}
              </Typography>

              {activeStep > 0 && (
                <span>
                  <br />
                  <Typography variant="subtitle1">
                    Date: <b>{format(props.bookRoom.date, "MMMM d, yyyy")}</b>
                  </Typography>
                  <Typography variant="subtitle1">
                    Time: <b>{`${props.bookRoom.from}:00 - ${props.bookRoom.to}:00`}</b>
                  </Typography>
                  <Typography variant="subtitle1">
                    Attendees: <b>{props.bookRoom.attendees}</b>
                  </Typography>
                </span>
              )}

              {activeStep > 1 && (
                <span>
                  <br />
                  <Typography variant="subtitle1">
                    Room Layout: <b>{props.roomSetup.selectedLayout.title}</b>
                  </Typography>
                  {!!props.roomSetup.availableEquipments.filter((e) => e.selected).length && (
                    <Typography variant="subtitle1">
                      Equipments:
                      {props.roomSetup.availableEquipments
                        .filter((e) => e.selected)
                        .map((e) => (
                          <b>
                            <br />
                            {`${e.title} × ${e.quantity}`}
                          </b>
                        ))}
                    </Typography>
                  )}
                </span>
              )}
              {activeStep > 2 && !!props.foodDrinks.availableSnacks.filter((s) => s.selected).length && (
                <span>
                  <br />
                  <Typography variant="subtitle1">
                    Food & Drinks:
                    {props.foodDrinks.availableSnacks
                      .filter((s) => s.selected)
                      .map((s) => (
                        <b>
                          <br />
                          {`${s.title} × ${s.count}`}
                        </b>
                      ))}
                  </Typography>
                </span>
              )}
            </Paper>
          </Grid>
          <Grid item md={8}>
            <Paper className={classes.paper} style={{ width: "auto" }}>
              {steps[activeStep].component}
              <br />
              <div className={classes.divStepButtons}>
                <Button disabled={activeStep === 0} onClick={stepBack} className={classes.button}>
                  Back
                </Button>
                {activeStep < steps.length - 1 ? (
                  <Button variant="contained" color="primary" onClick={stepNext} className={classes.button} id="next_btn">
                    Next
                  </Button>
                ) : (
                  <Button
                    variant="contained"
                    id="confirm_btn"
                    color="secondary"
                    onClick={() => {
                      props.postBooking(props);
                    }}
                    className={classes.button}
                  >
                    Confirm
                  </Button>
                )}
              </div>
            </Paper>
          </Grid>
        </Grid>
      </div>
    </div>
  );
});
