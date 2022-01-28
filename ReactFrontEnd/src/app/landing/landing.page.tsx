import { Button, CircularProgress } from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import React, { useEffect } from "react";
// @ts-ignore
import * as CurrencyFormat from "react-currency-format";
import { Link } from "react-router-dom";
import { connectLanding } from "./landing.selector";

export interface LandingRow {
  id: number;
  image: string;
  name: string;
  description: string;
  capacity: number;
  pricePerHour: number;
  pricePerDay: number;
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    loader: {
      display: "flex",
      justifyContent: "center",
      marginTop: "200px",
    },
    paper: {
      padding: theme.spacing(5),
      margin: theme.spacing(2),
      maxWidth: 600,
      backgroundColor: "lavender",
    },
    image: {
      width: 500,
      height: 250,
    },
    img: {
      display: "block",
      maxWidth: "100%",
      maxHeight: "100%",
    },

    buttonGrid: {
      position: "relative",
    },
  })
);

export interface LandingPageProps {}

export const LandingPage = connectLanding<LandingPageProps>(({ ...props }) => {
  const classes = useStyles();
  useEffect(() => {
    if (!props.loading) props.updateLoading(true);
  }, []);
  if (props.loading) {
    props.fetchRooms();
  }
  return (
    <div>
      <h1
        style={{
          textAlign: "center",
          fontSize: "2rem",
          textShadow: "0px 1px 1px",
        }}
      >
        AVAILABLE ROOMS
      </h1>

      {props.loading ? (
        <div className={classes.loader}>
          <CircularProgress />
        </div>
      ) : (
        <div>
          <Grid container justify="center" alignItems="stretch">
            {props.rooms.map((rowData) => (
              <Grid item md={6} style={{ display: "flex", justifyContent: "center" }}>
                <Paper className={classes.paper}>
                  <Grid container spacing={5}>
                    <Grid item xs={12}>
                      <h1
                        style={{
                          margin: 0,
                          textAlign: "center",
                          fontSize: "1.5rem",
                          textShadow: "0px 1px 1px",
                        }}
                      >
                        {rowData.name}
                      </h1>
                    </Grid>
                    <Grid item xs={6}>
                      <img width="450" className={classes.img} src={rowData.image} alt={rowData.name} />
                      <Grid container direction="row">
                        <Grid item xs={6}>
                          <Typography gutterBottom variant="body2">
                            <br />
                            Capacity:
                          </Typography>
                          <Typography gutterBottom variant="body2">
                            <b>{rowData.capacity + " people"}</b>
                          </Typography>
                        </Grid>
                        <Grid item xs={6}>
                          <Typography gutterBottom variant="body2">
                            <br />
                            Price:
                          </Typography>
                          {!!rowData.pricePerHour && (
                            <Typography variant="body2" gutterBottom>
                              <b>
                                <CurrencyFormat
                                  value={rowData.pricePerHour + ".00"}
                                  displayType={"text"}
                                  thousandSeparator={true}
                                  prefix={"\u20B9"}
                                />
                                &nbsp;per hour
                              </b>
                            </Typography>
                          )}
                          {!!rowData.pricePerDay && (
                            <Typography variant="body2" gutterBottom>
                              <b>
                                <CurrencyFormat
                                  value={rowData.pricePerDay + ".00"}
                                  displayType={"text"}
                                  thousandSeparator={true}
                                  prefix={"\u20B9"}
                                />
                                &nbsp;per day
                              </b>
                              <br />
                            </Typography>
                          )}
                        </Grid>
                      </Grid>
                    </Grid>
                    <Grid
                      item
                      xs={6}
                      className={classes.buttonGrid}
                      style={{ display: "flex", flexFlow: "column", justifyContent: "space-between" }}
                    >
                      <Grid item>
                        <Typography variant="body2" gutterBottom style={{ whiteSpace: "pre-wrap" }}>
                          {rowData.description}
                        </Typography>
                      </Grid>
                      <Grid item style={{ textAlign: "center" }}>
                        <Link to={"/book/" + rowData.id}>
                          <Button variant="contained" color="primary" className="booking_btn">
                            Book This Room
                          </Button>
                        </Link>
                      </Grid>
                    </Grid>
                  </Grid>
                </Paper>
              </Grid>
            ))}
          </Grid>
          <Typography variant="h6" style={{ textAlign: "center", margin: 12 }}>
            Contact:{" "}
            <a href="mailto:roombookapp@gmail.com" style={{ color: "darkblue" }}>
              roombookapp@gmail.com
            </a>
          </Typography>
        </div>
      )}
    </div>
  );
});
