export const equipmentsActions = {
    updateLoading: (payload: { loading: boolean }) => ({
        type: "UPDATE_EQUIPLOADING" as const,
        payload,
    }),
    updateShowError: (payload: { showError: boolean }) => ({
        type: "UPDATE_EQUIPSHOWERROR" as const,
        payload,
    }), 
    updateEquipments: (payload: {
        equipments: {
            id: number;
            title: string;
            bookMultipleUnit: boolean,
            priceType: string,
            price: number;
        }[];
    }) => ({
        type: "UPDATE_EQUIPMENTS" as const,
        payload,
    }),
};

export type EquipmentsActionTypes = ReturnType<typeof equipmentsActions[keyof typeof equipmentsActions]>["type"];

export type EquipmentsActions = ReturnType<typeof equipmentsActions[keyof typeof equipmentsActions]>;
