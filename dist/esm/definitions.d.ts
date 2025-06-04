declare module '@capacitor/core' {
    interface CapacitorPlugins {
        DashPayModule: DashPayModulePlugin;
    }
}
export interface DashPayModulePlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    openOnlineForm(options: {
        strUri: string;
    }): Promise<{
        strUri: string;
    }>;
    getSerial(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    print(options: {
        printString: string;
        receiptLogo: string;
        EXTRA_ORIGINATING_URI: string;
        NewActivityLaunchOption: boolean;
    }): Promise<{
        results: any[];
    }>;
    pay(options: {
        REFERENCE_NUMBER: string;
        TRANSACTION_ID: string;
        OPERATOR_ID: string;
        ADDITIONAL_AMOUNT: string;
        AMOUNT: string;
        TRANSACTION_TYPE: string;
        EXTRA_ORIGINATING_URI: string;
        value: string;
    }): Promise<{
        value: string;
    }>;
    initiateDebicheck(options: {
        EXTRA_ORIGINATING_URI: string;
        TRANSACTION_TYPE: string;
        ACCOUNT_NUMBER: string;
        MAX_AUTH_COLLECTION: string;
        CONTACT_REFERENCE: string;
        DEBITOR_ID: string;
        FROM_ACC_TYPE: string;
        TO_ACC_TYPE: string;
        COMMS_TYPE: string;
        DISABLE_CASHIER_LOGIN: string;
        DISABLE_SLIP_PRINTING: string;
        value: string;
    }): Promise<{
        value: string;
    }>;
}
