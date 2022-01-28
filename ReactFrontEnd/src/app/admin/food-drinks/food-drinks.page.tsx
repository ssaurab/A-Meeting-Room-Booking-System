import React, { useEffect } from "react";
import MaterialTable, { Column } from "material-table";
import { Link, RouteComponentProps } from "react-router-dom";
import { Typography, Button, CircularProgress, makeStyles, Theme, createStyles } from "@material-ui/core";
import { connectSnacks } from "./food-drinks.selecter";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";

export interface SnacksRow {
  id: number;
  title: string;
  price: number;
}

interface TableData {
  columns: Array<Column<SnacksRow>>;
  data: SnacksRow[];
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

export interface SnacksPageProps extends RouteComponentProps {}

export const FoodDrinksPage = connectSnacks<SnacksPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();

  useEffect(() => {
    if (!props.loading) props.updateLoading(true);
  }, []);

  if (props.loading) {
    props.fetchSnacks();
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
          return <span> &#8377;{rowData.price} per Unit</span>;
        },
      },
      {
        title: "Actions",
        sorting: false,
        type: "numeric",
        render: (rowData) => (
          <div>
            <Link to={match.path + `edit/${rowData.id}`}>
              <IconButton
                aria-label="edit"
                // onClick={() => props.deleteSnack(rowData.id)}
              >
                <EditIcon />
              </IconButton>
            </Link>

            <IconButton aria-label="delete" onClick={() => props.deleteSnack(rowData.id)}>
              <DeleteIcon />
            </IconButton>
          </div>
        ),
      },
    ],
    data: props.snacks,
  };

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <Typography variant="h6" style={{ margin: 11 }}>
          Foods & Drinks
        </Typography>
        <Link to={match.url + "/add"}>
          <Button variant="contained" color="primary" style={{ margin: 10 }}>
            + Add Foods & Drinks
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
                Foods & Drinks
                <Link to={match.path + "add"}>
                  <Button variant="contained" color="primary" style={{ margin: 10 }}>
                    Add Food and Drinks
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
