import React, { useEffect } from "react";
import MaterialTable, { Column } from "material-table";
import { Link, RouteComponentProps } from "react-router-dom";
import { Typography, Button, CircularProgress, makeStyles, Theme, createStyles } from "@material-ui/core";
import { connectEquipments } from "./equipments.selecter";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";

export interface EquipmentsPageProps extends RouteComponentProps {}

export interface EquipmentsRow {
  id: number;
  title: string;
  bookMultipleUnit: boolean;
  priceType: string;
  price: number;
}

interface TableData {
  columns: Array<Column<EquipmentsRow>>;
  data: EquipmentsRow[];
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

export const EquipmentsPage = connectEquipments<EquipmentsPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();

  useEffect(() => {
    if (!props.loading) props.updateLoading(true);
  }, []);

  if (props.loading) {
    props.fetchEquipments();
  }

  const tableData: TableData = {
    columns: [
      { title: "Title", field: "title" },
      {
        title: "Price",
        field: "price",
        sorting: false,
        editable: "never",
        render: (rowData) => {
          return rowData.bookMultipleUnit ? (
            <span> &#8377;{rowData.price} per Unit</span>
          ) : (
            <span> &#8377;{rowData.price} per Booking</span>
          );
        },
      },
      {
        title: "Book Multiple Units",
        field: "bookMultipleUnit",
        render: (rowData) =>
          rowData.bookMultipleUnit.toString().charAt(0).toUpperCase() + rowData.bookMultipleUnit.toString().slice(1),
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
            <IconButton aria-label="delete" onClick={() => props.deleteEquipment(rowData.id)}>
              <DeleteIcon />
            </IconButton>
          </div>
        ),
      },
    ],
    data: props.equipments,
  };

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <Typography variant="h6" style={{ margin: 11 }}>
          Equipment
        </Typography>
        <Link to={match.url + "/add"}>
          <Button variant="contained" color="primary" style={{ margin: 10 }}>
            + Add Equipment
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
                Equipments
                <Link to={match.url + "/add"}>
                  <Button variant="contained" color="primary" style={{ margin: 10 }}>
                    Add Equipment
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
