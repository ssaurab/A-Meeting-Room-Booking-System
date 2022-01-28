import React, { useEffect, useState } from "react";
import MaterialTable, { Column } from "material-table";
import {
  Select,
  MenuItem,
  Button,
  Typography,
  CircularProgress,
  makeStyles,
  Theme,
  createStyles,
  FormControl,
} from "@material-ui/core";
import { Link, RouteComponentProps } from "react-router-dom";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";
import { connectBookings } from "./bookings.selector";

export interface RoomsData {
  id: number;
  room: string;
  date: Date;
  total: number;
  name: string;
  status: string;
  roomId: number;
}

interface Row extends RoomsData {}

interface TableData {
  columns: Array<Column<Row>>;
  data: Row[];
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    loader: {
      display: "flex",
      justifyContent: "center",
      marginTop: "200px",
    },
    formControl: {
      margin: theme.spacing(1),
      minWidth: 120,
    },
  })
);

interface BookingsPageProps extends RouteComponentProps {}

export const BookingsPage = connectBookings<BookingsPageProps>(({ match, history, ...props }) => {
  const params = new URLSearchParams(props.location.search);
  useEffect(() => {
    if (!props.loading) props.updateLoading(true);
    if (!!params.get("roomId")) setStatus("FilterId");
    if (!!params.get("bookingId")) setStatus("bookingId");
  }, []);
  if (props.loading) {
    props.fetchBookings();
  }

  const [data, setData] = useState(props.bookings);
  const [status, setStatus] = useState("all");

  useEffect(() => {
    switch (status) {
      case "all":
        setData(props.bookings);
        break;
      case "pending":
        setData(props.bookings.filter(({ status }) => status === "pending"));
        break;
      case "confirmed":
        setData(props.bookings.filter(({ status }) => status === "confirmed"));
        break;
      case "cancelled":
        setData(props.bookings.filter(({ status }) => status === "cancelled"));
        break;
      case "FilterId":
        setData(props.bookings.filter(({ roomId }) => roomId === parseInt(params.get("roomId") as string)));
        break;
      case "bookingId":
        setData(props.bookings.filter(({ id }) => id === parseInt(params.get("bookingId") as string)));
    }
    // if (!!params.get("roomId"))
    //   setData(
    //     props.bookings.filter(
    //       ({ roomId }) => roomId === parseInt(params.get("roomId") as string)
    //     )
    //   );
  }, [props.bookings, status]);
  const classes = useStyles();

  const tableData: TableData = {
    columns: [
      {
        title: "Room",
        field: "room",
      },
      {
        title: "Date",
        field: "date",
        type: "date",
        defaultSort: "desc",
      },
      {
        title: "Name",
        field: "name",
      },
      {
        title: "Total Cost",
        field: "total",
        render: (rowData) => <p>&#8377; {rowData.total}</p>,
      },
      {
        title: "Status",
        field: "status",
        render: (rowData) => (
          <Select
            value={rowData.status}
            onChange={(evt) => props.editStatus(evt.target.value as string, rowData, props.bookings)}
          >
            <MenuItem value="confirmed">Confirmed</MenuItem>
            <MenuItem value="pending">Pending</MenuItem>
            <MenuItem value="cancelled">Cancelled</MenuItem>
          </Select>
        ),
      },
      {
        title: "Actions",
        sorting: false,
        type: "numeric",
        render: (rowData) => (
          <div>
            <Link to={match.url + `/edit/${rowData.id}`}>
              <IconButton aria-label="edit">
                <EditIcon />
              </IconButton>
            </Link>
            <IconButton aria-label="delete" onClick={() => props.deleteBooking(rowData.id)}>
              <DeleteIcon />
            </IconButton>
          </div>
        ),
      },
    ],

    data: data,
  };

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <Typography variant="h6" style={{ margin: 11 }}>
          Bookings
        </Typography>
        <Link to={match.url + "/add"}>
          <Button variant="contained" color="primary" style={{ margin: 10 }}>
            + Add Bookings
          </Button>
        </Link>
      </div>

      {props.loading ? (
        <div className={classes.loader}>
          <CircularProgress />
        </div>
      ) : (
        <MaterialTable
          title={(status==='bookingId'||status==='FilterId')?'':
            (<div style={{ display: "flex", alignItems: "center" }}>
              <Typography variant="subtitle1" style={{ margin: 11 }}>
                Showing :
              </Typography>
              <FormControl variant="outlined" className={classes.formControl} size="small">
                <Select
                  value={status}
                  onChange={(evt) => {
                    setStatus(evt.target.value as string);
                  }}
                >
                  <MenuItem value="all">All</MenuItem>
                  <MenuItem value="confirmed">Confirmed</MenuItem>
                  <MenuItem value="pending">Pending</MenuItem>
                  <MenuItem value="cancelled">Cancelled</MenuItem>
                </Select>
              </FormControl>
            </div>)
          }
          columns={tableData.columns}
          data={tableData.data}
          options={{
            selection: false,
            showSelectAllCheckbox: false,
            showTextRowsSelected: false,
            draggable: false,
            // showTitle: false,
          }}
        />
      )}
    </div>
  );
});
