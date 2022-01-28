export const usersActions = {
    updateLoading: (payload: { loading: boolean }) => ({
        type: "UPDATE_LOADING" as "UPDATE_LOADING",
        payload,
    }),

    updateUsers: (payload: {
        users: {
            id: number;
            role: string;
            name: string;
            phone: string;
            email: string;
            password:string;
            status: string,
        }[];
    }) => ({
        type: "UPDATE_USERS" as "UPDATE_USERS",
        payload,
    }),
};

export type UsersActionTypes = ReturnType<typeof usersActions[keyof typeof usersActions]>["type"];

export type UsersActions = ReturnType<typeof usersActions[keyof typeof usersActions]>;
