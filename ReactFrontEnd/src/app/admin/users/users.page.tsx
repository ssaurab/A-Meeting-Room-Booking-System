import React, { useEffect, useState } from "react";
import MaterialTable, { Column } from "material-table";
import { Link, RouteComponentProps } from "react-router-dom";
import {
  Typography,
  Button,
  CircularProgress,
  makeStyles,
  Theme,
  createStyles,
  MenuItem,
  Select,
  FormControl,
} from "@material-ui/core";
import { connectUsers } from "./users.selecter";
import IconButton from "@material-ui/core/IconButton";
import DeleteIcon from "@material-ui/icons/Delete";

export interface UsersPageProps extends RouteComponentProps {}

export interface UsersRow {
  id: number;
  role: string;
  name: string;
  phone: string;
  email: string;
  password: string;
  status: string;
}

interface TableData {
  columns: Array<Column<UsersRow>>;
  data: UsersRow[];
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

export const UsersPage = connectUsers<UsersPageProps>(({ match, history, ...props }) => {
  const classes = useStyles();

  useEffect(() => {
    if (!props.loading) props.updateLoading(true);
  }, []);

  if (props.loading) {
    props.fetchUsers();
  }

  const [data, setData] = useState(props.users);
  const [status, setStatus] = useState("all");

  useEffect(() => {
    switch (status) {
      case "all":
        setData(props.users);
        break;
      case "active":
        setData(props.users.filter(({ status }) => status === "active"));
        break;
      case "inactive":
        setData(props.users.filter(({ status }) => status === "inactive"));
        break;
    }
  }, [props.users, status]);

  const tableData: TableData = {
    columns: [
      {
        title: "UserName",
        field: "name",
        render: (rowData) => rowData.name.charAt(0).toUpperCase() + rowData.name.slice(1),
      },
      { title: "Email", field: "email" },
      {
        title: "Role",
        field: "role",
        render: (rowData) => rowData.role.charAt(0).toUpperCase() + rowData.role.slice(1),
      },
      {
        title: "Status",
        field: "status",
        render: (rowData) => (
          <Select value={rowData.status} onChange={(e) => props.editStatus(e.target.value as string, rowData, props.users)}>
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
          <IconButton aria-label="delete" onClick={() => props.deleteUser(rowData.id)}>
            <DeleteIcon />
          </IconButton>
        ),
      },
    ],
    data: data,
  };

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <Typography variant="h6" style={{ margin: 11 }}>
          Users
        </Typography>
        <Link to={match.url + "/add"}>
          <Button variant="contained" color="primary" style={{ margin: 10 }}>
            + Add User
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
