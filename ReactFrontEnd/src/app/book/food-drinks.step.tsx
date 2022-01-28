import { Grid, TextField } from "@material-ui/core";
import MaterialTable, { Column } from "material-table";
import React from "react";
import { connectBook } from "./book.selecter";

export interface FoodDrinksStepProps {}

interface FoodDrinksData {
  id: number;
  title: string;
  price: number;
  selected: boolean;
  count: number;
}
interface TableData {
  columns: Array<Column<FoodDrinksData>>;
  data: Array<FoodDrinksData>;
}

export const FoodDrinksStep = connectBook<FoodDrinksStepProps>((props) => {
  const handleQuantityChange = (evt: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>, id: number) => {
    props.updateTouched(`snacks-${id}`);
    props.updateFoodDrinks("availableSnacks")(
      tableData.data.map((eq) => ({
        ...eq,
        count: eq.id === id ? parseInt(evt.target.value) : eq.count,
      }))
    );
  };
  const toggleFoodDrinks = (selEqs: FoodDrinksData[]) => {
    props.updateFoodDrinks("availableSnacks")(
      tableData.data.map((eq) => ({
        ...eq,
        selected: !!selEqs.find(({ id }) => id === eq.id),
      }))
    );
  };
  const tableData: TableData = {
    columns: [
      { title: "Item", field: "title" },
      {
        title: "Quantity",
        field: "count",
        render: ({ id, count, selected }) => (
          <TextField
            label="Units"
            type="number"
            variant="outlined"
            required
            size="small"
            inputProps={{ min: 1 }}
            error={viewError("snacks-" + id)}
            helperText={errorText("snacks-" + id)}
            defaultValue={count}
            disabled={!selected}
            onChange={(evt) => {
              handleQuantityChange(evt, id);
            }}
          />
        ),
      },
      {
        title: "Cost",
        field: "price",
        render: ({ price }) => <p>&#8377;{price} per Unit</p>,
      },
    ],
    data: props.foodDrinks.availableSnacks,
  };
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
          Food and Drinks
        </h1>
      </Grid>
      <Grid item xs={12}>
        <MaterialTable
          title="Food & Drinks"
          columns={tableData.columns}
          data={tableData.data}
          options={{
            search: false,
            sorting: false,
            selection: true,
            showSelectAllCheckbox: false,
            showTextRowsSelected: false,
            paging: false,
            showTitle: false,
            draggable: false,
          }}
          onSelectionChange={toggleFoodDrinks}
        />
      </Grid>
    </Grid>
  );
});
