import { Button, Typography } from "@material-ui/core";
import MaterialTable, { Column } from "material-table";
import React, { useState } from "react";
import { RouteComponentProps } from "react-router-dom";
import { connectDashboards } from "./dashboard.selector";

export interface PrintPageProps extends RouteComponentProps {}
export interface BookingData {
  id: number;
  room: string;
  name: string;
  duration: string;
  status: string;
  date: string;
  total: number;
}

interface Row extends BookingData {}

interface TableData {
  columns: Array<Column<Row>>;
  data: Row[];
}

export const PrintPage = connectDashboards<PrintPageProps>(({ match, history, ...props }) => {
  const [data] = useState(props.bookingsOnDay);
  const [showButton, setShowButton] = useState(true);
  const print = () => {
    setShowButton(false);
    setTimeout(function () {
      window.print();
      setShowButton(true);
    }, 500);
  };

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
        title: "From",
        field: "fromHour",
      },
      {
        title: "To",
        field: "toHour",
      },
      {
        title: "Total",
        field: "total",
      },
      {
        title: "Status",
        field: "status",
      },
    ],

    data: data,
  };

  return (
    <div>
      <MaterialTable
        title={
          <div style={{ display: "flex", alignItems: "center" }}>
            <Typography variant="h4" style={{ margin: 11 }}>
              Bookings for {new URLSearchParams(props.location.search).get("date")}
            </Typography>
            <Button
              style={{ visibility: showButton ? "visible" : "hidden", margin: 10 }}
              variant="contained"
              color="primary"
              onClick={print}
            >
              Print
            </Button>
          </div>
        }
        columns={tableData.columns}
        data={tableData.data}
        options={{
          selection: false,
          showSelectAllCheckbox: false,
          showTextRowsSelected: false,
          draggable: false,
          paging: false,
          search: false,
          // showTitle: false,
        }}
      />
    </div>
  );
});
