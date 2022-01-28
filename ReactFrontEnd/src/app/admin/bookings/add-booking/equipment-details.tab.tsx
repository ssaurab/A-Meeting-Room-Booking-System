import MaterialTable from "material-table";
import React from "react";
import { connectAddBooking } from "./add-booking.selecter";
import { Paper, TextField, Typography } from "@material-ui/core";

interface EquipmentDetailsProps {}
interface EquipmentData {
  id: number;
  title: string;
  bookMultipleUnit: boolean;
  priceType: string;
  price: number;
  quantity: number;
  selected: boolean;
}

export const EquipmentDetailsTab = connectAddBooking<EquipmentDetailsProps>(
  (props) => {
    const toggleEquipment = (selEquipment: EquipmentData[]) => {
      props.updateBookingEquipment(
        props.booking.equipments.map((equipments) => ({
          ...equipments,
          selected: !!selEquipment.find((s) => s.id === equipments.id),
        }))
      );
    };
    const changeEquipmentQuantity = (id: number, quantity: number) => {
      props.updateBookingEquipment(
        props.booking.equipments.map((s) => ({
          ...s,
          quantity:
            s.id === id && s.bookMultipleUnit === true ? quantity : s.quantity,
        }))
      );
    };
    return (
      <div>
        <MaterialTable
          title="Equipments for the Booking"
          columns={[
            {
              title: "Item",
              field: "title",
              render: (equipment) =>
                equipment.id === -1 ? (
                  <Typography style={{ fontWeight: "bold" }}>
                    {equipment.title}
                  </Typography>
                ) : (
                  equipment.title
                ),
            },
            {
              title: "Quantity",
              field: "quantity",
              render: (equipment) =>
                equipment.id !== -1 &&
                (equipment.bookMultipleUnit ? (
                  <TextField
                    label="Number"
                    type="number"
                    variant="outlined"
                    size="small"
                    name={`equipment_quantity${equipment.id}`}
                    inputProps={{ min: 1 }}
                    value={equipment.quantity}
                    onChange={(evt) => {
                      changeEquipmentQuantity(equipment.id, +evt.target.value);
                      parseInt(evt.target.value) < 0
                        ? props.addError(
                            evt.target.name,
                            "quantity is negative",
                            props.errors
                          )
                        : props.deleteError(evt.target.name, props.errors);
                    }}
                    error={props.errors.has(
                      `equipment_quantity${equipment.id}`
                    )}
                    helperText={props.errors.get(
                      `equipment_quantity${equipment.id}`
                    )}
                    disabled={!equipment.selected}
                  />
                ) : (
                  <p> &nbsp;&nbsp;&nbsp;1</p>
                )),
            },
            {
              title: "Cost",
              field: "price",
              render: ({ id, price, priceType }) =>
                id === -1 ? (
                  <Typography style={{ fontWeight: "bold" }}>{`₹${price.toFixed(
                    2
                  )}`}</Typography>
                ) : (
                  <span>{`₹${price.toFixed(2)}/ ${priceType}`}</span>
                ),
            },
          ]}
          data={[
            ...props.booking.equipments,
            {
              bookMultipleUnit: false,
              id: -1,
              price: props.bookingCosts.equipmentPrice,
              priceType: "",
              title: "Total",
              quantity: -1,
              selected: false,
            },
          ].map((ele) => ({ ...ele, tableData: { checked: ele.selected } }))}
          components={{
            Container: (props) => <Paper {...props} elevation={0} />,
          }}
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
            selectionProps: (rowData: EquipmentData) => ({
              disabled: rowData.id === -1,
              color: "primary",
            }),
          }}
          onSelectionChange={toggleEquipment}
        />
        &nbsp;&nbsp;&nbsp;&nbsp;
      </div>
    );
  }
);
