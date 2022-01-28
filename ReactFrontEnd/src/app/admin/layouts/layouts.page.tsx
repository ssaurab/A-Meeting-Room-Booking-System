import React, { useEffect } from "react";
import MaterialTable, { Column } from "material-table";
import Button from "@material-ui/core/Button";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";
import { Link, RouteComponentProps } from "react-router-dom";
import { Typography, CircularProgress, makeStyles, Theme, createStyles } from "@material-ui/core";
import { connectLayouts } from "./layouts.selecter";
import EditIcon from "@material-ui/icons/Edit";

export interface LayoutsRow {
  id: number;
  image: string;
  title: string;
  rooms: Array<string>;
}

interface TableData {
  columns: Array<Column<LayoutsRow>>;
  data: Array<LayoutsRow>;
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    loader: {
      display: "flex",
      justifyContent: "center",
      marginTop: "200px",
    },
  })
);

export interface LayoutsPageProps extends RouteComponentProps {}

export const LayoutsPage = connectLayouts<LayoutsPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();

  useEffect(() => {
    if (!props.loading) props.updateLoading(true);
  }, []);

  if (props.loading) {
    props.fetchLayouts();
  }

  const tableData: TableData = {
    columns: [
      {
        title: "Image",
        field: "image",
        sorting: false,
        render: (rowData) => <img src={rowData.image} style={{ width: 100, borderRadius: "5%" }} alt={rowData.title} />,
      },
      { title: "Title", field: "title" },
      {
        title: "Rooms",
        sorting: false,
        field: "rooms",
        editable: "never",
        render: (rowData) =>
          rowData.rooms.map((ele) => (
            <span>
              {ele} <br />
            </span>
          )),
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
            <IconButton aria-label="delete" onClick={() => props.deleteLayout(rowData.id)}>
              <DeleteIcon />
            </IconButton>
          </div>
        ),
      },
    ],

    data: props.layouts,
  };

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <Typography variant="h6" style={{ margin: 11 }}>
          Room layouts
        </Typography>
        <Link to={match.url + "/add"}>
          <Button variant="contained" color="primary" style={{ margin: 10 }}>
            + Add Room Layout
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
              <Typography variant="h6">
                Room layouts
                <Link to={match.url + "/add"}>
                  <Button variant="contained" color="primary" style={{ margin: 10 }}>
                    Add Room Layout
                  </Button>
                </Link>
              </Typography>
            }
            columns={tableData.columns}
            data={tableData.data}
            options={{
              selection: false,
              showSelectAllCheckbox: false,
              showTextRowsSelected: false,
              draggable: false,
              showTitle: false,
            }}
          />
        </div>
      )}
    </div>
  );
});
