import React, { useEffect, useState } from "react";
import MaterialTable, { Column } from "material-table";
import { Select, MenuItem, Button, FormControl } from "@material-ui/core";
import { RouteComponentProps, Link } from "react-router-dom";
import { Typography, CircularProgress, makeStyles, Theme, createStyles } from "@material-ui/core";
import { connectRooms } from "./rooms.selector";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";

export interface RoomsRow {
  id: number;
  image: string;
  name: string;
  capacity: number;
  bookings: number;
  status: string;
}

interface TableData {
  columns: Array<Column<RoomsRow>>;
  data: Array<RoomsRow>;
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
    link: {
      textDecorationLine: "underline",
      color: "#3f51b5",
    },
  })
);

interface RoomsPageProps extends RouteComponentProps {}

export const RoomsPage = connectRooms<RoomsPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();

  useEffect(() => {
    if (!props.loading) props.updateLoading(true);
  }, []);

  if (props.loading) {
    props.fetchRooms();
  }

  const [data, setData] = useState(props.rooms);
  const [status, setStatus] = useState("all");

  useEffect(() => {
    switch (status) {
      case "all":
        setData(props.rooms);
        break;
      case "active":
        setData(props.rooms.filter(({ status }) => status === "active"));
        break;
      case "inactive":
        setData(props.rooms.filter(({ status }) => status === "inactive"));
        break;
    }
  }, [props.rooms, status]);

  // const showBookings = () => {
  //   // eslint-disable-next-line @typescript-eslint/no-unused-expressions
  //   <Redirect to={match.path.replace("rooms", "bookings")} />;
  // };

  const tableData: TableData = {
    columns: [
      {
        title: "Image",
        field: "image",
        render: (rowData) => <img src={rowData.image} alt={rowData.name} style={{ width: 100, borderRadius: "5%" }} />,
      },
      {
        title: "Room",
        field: "name",
      },
      {
        title: "Capacity",
        field: "capacity",
        align: "center",
      },
      {
        title: "Bookings",
        field: "bookings",
        align: "center",
        render: (rowData) => (
          <Link to={match.url.replace("rooms", "bookings") + `?roomId=${rowData.id}`}>
            <div className={classes.link}>{rowData.bookings}</div>
          </Link>
        ),
      },

      {
        title: "Status",
        field: "status",
        render: (rowData) => (
          <Select value={rowData.status} onChange={(e) => props.editStatus(e.target.value as string, rowData, props.rooms)}>
            <MenuItem value="active">Active</MenuItem>
            <MenuItem value="inactive">Inactive</MenuItem>
          </Select>
        ),
      },

      {
        title: "Actions",
        sorting: false,
        type: "numeric",
        render: (rowData) => (
          <div>
            <Link to={match.path + `edit/${rowData.id}`}>
              <IconButton aria-label="edit">
                <EditIcon />
              </IconButton>
            </Link>
            <IconButton aria-label="delete" onClick={() => props.deleteRoom(rowData.id)}>
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
          Rooms
        </Typography>
        <Link to={match.url + "/add"}>
          <Button variant="contained" color="primary" style={{ margin: 10 }}>
            + Add Rooms
          </Button>
        </Link>
      </div>

      {props.loading ? (
        <div className={classes.loader}>
          <CircularProgress />
        </div>
      ) : (
        <div>
          <MaterialTable
            title={
              <div style={{ display: "flex", alignItems: "center" }}>
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
                    <MenuItem value="active">Active</MenuItem>
                    <MenuItem value="inactive">Inactive</MenuItem>
                  </Select>
                </FormControl>
              </div>
            }
            columns={tableData.columns}
            data={tableData.data}
            options={{
              selection: false,
              showSelectAllCheckbox: false,
              showTextRowsSelected: false,
              draggable: false,
            }}
          />
        </div>
      )}
    </div>
  );
});
