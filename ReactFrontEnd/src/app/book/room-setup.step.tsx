import { Card, CardContent, CardHeader, Grid, TextField, Typography } from "@material-ui/core";
import { createStyles, makeStyles, Theme } from "@material-ui/core/styles";
import Alert from "@material-ui/lab/Alert";
import ToggleButton from "@material-ui/lab/ToggleButton";
import ToggleButtonGroup from "@material-ui/lab/ToggleButtonGroup";
import MaterialTable, { Column } from "material-table";
import React from "react";
import { connectBook } from "./book.selecter";

interface EquipmentData {
  id: number;
  title: string;
  priceType: string;
  price: number;
  bookMultipleUnit: boolean;
  quantity: number;
  selected: boolean;
}
interface TableData {
  columns: Array<Column<EquipmentData>>;
  data: Array<EquipmentData>;
}

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    toogleButton: {
      marginRight: 10,
      width: 160,
      background: "#FFFFFF",
      marginBottom: 10,
      height: 62,
      borderRadius: 3,
      border: 0,
    },
    btnselected: {
      backgroundColor: theme.palette.primary.light + "!important",
      color: "white !important",
    },
    image: {
      width: 45,
      height: 40,
      marginRight: 10,
    },
    btnText: {
      width: 82,
      height: 38,
    },
    paper: {
      padding: theme.spacing(5),
      margin: theme.spacing(2, "auto"),
      maxWidth: 800,
      boxSizing: "border-box",
      display: "block",
    },
  })
);
interface RoomSetupStepProps {}

export const RoomSetupStep = connectBook<RoomSetupStepProps>((props) => {
  const classes = useStyles();
  const handleSelctionLayout = (event: React.MouseEvent<HTMLElement>, newLayoutId: number) => {
    const selectedLayout = props.roomSetup.availableLayouts.find((l) => l.id === newLayoutId);
    props.updateRoomSetup("selectedLayout")(selectedLayout);
    props.updateTouched("layout");
  };

  const handleQuantityChange = (evt: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>, id: number) => {
    props.updateTouched(`equipment-${id}`);
    props.updateRoomSetup("availableEquipments")(
      tableData.data.map((eq) => ({
        ...eq,
        quantity: eq.id === id ? parseInt(evt.target.value) : eq.quantity,
      }))
    );
  };

  const layout = props.roomSetup.availableLayouts;

  const tableData: TableData = {
    columns: [
      { title: "Item", field: "title" },
      {
        title: "Quantity",
        field: "quantity",
        render: ({ id, quantity, selected, bookMultipleUnit }) =>
          bookMultipleUnit ? (
            <TextField
              label="Units"
              type="number"
              variant="outlined"
              required
              size="small"
              inputProps={{ min: 1 }}
              error={viewError("equipment-" + id)}
              helperText={errorText("equipment-" + id)}
              defaultValue={quantity}
              disabled={!selected}
              onChange={(evt) => {
                handleQuantityChange(evt, id);
              }}
            />
          ) : (
            <p>{quantity}</p>
          ),
      },
      {
        title: "Cost",
        field: "price",
        render: ({ price, priceType }) => (
          <p>
            &#8377;{price} {priceType === "perHour" ? "per Hour" : "per Booking"}
          </p>
        ),
      },
    ],
    data: props.roomSetup.availableEquipments,
  };

  const toggleEquipment = (selEqs: EquipmentData[]) => {
    props.updateRoomSetup("availableEquipments")(
      tableData.data.map((eq) => ({
        ...eq,
        selected: !!selEqs.find(({ id }) => id === eq.id),
      }))
    );
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
          Room Setup
        </h1>
      </Grid>
      <Grid item xs={12}>
        <Card elevation={2}>
          <CardHeader title={<Typography variant="h6">Layout</Typography>}></CardHeader>
          <CardContent>
            <ToggleButtonGroup value={props.roomSetup.selectedLayout?.id} onChange={handleSelctionLayout} exclusive>
              {layout.map((item) => (
                <ToggleButton value={item.id} className={classes.toogleButton} classes={{ selected: classes.btnselected }}>
                  <img src={item.image} alt={item.title} className={classes.image} />
                  <strong>{item.title}</strong>
                </ToggleButton>
              ))}
              {viewError("layout") && <Alert severity="error">{errorText("layout")}</Alert>}
            </ToggleButtonGroup>
          </CardContent>
        </Card>
      </Grid>
      <Grid item xs={12}>
        <MaterialTable
          title="Equipment"
          columns={tableData.columns}
          data={tableData.data}
          options={{
            search: false,
            sorting: false,
            selection: true,
            showSelectAllCheckbox: false,
            showTextRowsSelected: false,
            paging: false,
            draggable: false,
          }}
          onSelectionChange={toggleEquipment}
        />
      </Grid>
    </Grid>
  );
});
