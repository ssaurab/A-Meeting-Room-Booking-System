import MaterialTable from "material-table";
import React from "react";
import { connectAddBooking } from "./add-booking.selecter";
import { Paper, TextField, Typography } from "@material-ui/core";

interface FoodsDrinksDetailsProps {}

interface FoodDrinksData {
  id: number;
  title: string;
  quantity: number;
  price: number;
  selected: boolean;
}

export const FoodsDrinksDetailsTab = connectAddBooking<FoodsDrinksDetailsProps>(
  (props) => {
    const toggleSnacks = (selSnacks: FoodDrinksData[]) => {
      props.updateBookingSnacks(
        props.booking.snacks.map((snack) => ({
          ...snack,
          selected: !!selSnacks.find((s) => s.id === snack.id),
        }))
      );
    };

    const changeSnackQuantity = (id: number, quantity: number) => {
      props.updateBookingSnacks(
        props.booking.snacks.map((s) => ({
          ...s,
          quantity: s.id === id ? quantity : s.quantity,
        }))
      );
    };
    return (
      <div>
        <MaterialTable
          title="Food & Drinks"
          columns={[
            {
              title: "Item",
              field: "title",
              render: (snack) =>
                snack.id === -1 ? (
                  <Typography style={{ fontWeight: "bold" }}>
                    {snack.title}
                  </Typography>
                ) : (
                  snack.title
                ),
            },
            {
              title: "Quantity",
              field: "quantity",
              render: (snack) =>
                snack.id !== -1 && (
                  <TextField
                    label="People"
                    type="number"
                    variant="outlined"
                    size="small"
                    name={`food_quantity${snack.id}`}
                    inputProps={{ min: 1 }}
                    value={snack.quantity}
                    onChange={(evt) => {
                      changeSnackQuantity(snack.id, +evt.target.value);
                      parseInt(evt.target.value) < 0
                        ? props.addError(
                            evt.target.name,
                            "quantity is negative",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                    error={props.errors.has(`food_quantity${snack.id}`)}
                    helperText={props.errors.get(`food_quantity${snack.id}`)}
                    disabled={!snack.selected}
                  />
                ),
            },
            {
              title: "Cost",
              field: "price",
              render: ({ id, price }) =>
                id === -1 ? (
                  <Typography style={{ fontWeight: "bold" }}>{`₹${price.toFixed(
                    2
                  )}`}</Typography>
                ) : (
                  <span>{`₹${price.toFixed(2)} / person`}</span>
                ),
            },
          ]}
          data={[
            ...props.booking.snacks,
            {
              id: -1,
              price: props.bookingCosts.foodAndDrinkPrice,
              title: "Total:",
              quantity: -1,
              selected: false,
            },
          ].map((ele) => ({ ...ele, tableData: { checked: ele.selected } }))}
          style={{ border: "1px solid rgb(228, 228, 228)" }}
          options={{
            headerStyle: {
              backgroundColor: "rgb(247, 247, 247)",
              fontWeight: "bold",
            },
            search: false,
            sorting: false,
            selection: true,
            showSelectAllCheckbox: false,
            showTextRowsSelected: false,
            paging: false,
            draggable: false,
            toolbar: false,
            selectionProps: (rowData: FoodDrinksData) => ({
              disabled: rowData.id === -1,
              color: "primary",
            }),
          }}
          onSelectionChange={toggleSnacks}
          components={{
            Container: (props) => <Paper {...props} elevation={0} />,
          }}
        />
        &nbsp;&nbsp;
      </div>
    );
  }
);
