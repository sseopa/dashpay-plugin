import { WebPlugin } from '@capacitor/core';
import { DashPayModulePlugin } from './definitions';

export class DashPayModuleWeb extends WebPlugin implements DashPayModulePlugin {
    constructor() {
        super({
            name: 'DashPayModule',
            platforms: ['web'],
        });
    }

    async echo(options: { value: string }): Promise<{ value: string }> {
        console.log('ECHO', options);
        return options;
    }

    async openOnlineForm(options: { strUri: string }): Promise<{ strUri: string }> {
        console.log('openOnlineForm', options);
        return options;
    }

    async getSerial(options: { value: string }): Promise<{ value: string }> {
        console.log('getSerial', options);
        return options;
    }
    async print(options: { printString: string,receiptLogo: string,EXTRA_ORIGINATING_URI: string, NewActivityLaunchOption: boolean }): Promise<{ results: any[] }> {
        console.log('print string: ', options.printString);
        console.log('EXTRA_ORIGINATING_URI: ', options.EXTRA_ORIGINATING_URI);

        return {
            results: [{
                firstName: 'Dummy',
                lastName: 'Entry',
                telephone: '123456'
            }]
        };
    }

    async pay(options: { REFERENCE_NUMBER: string, TRANSACTION_ID: string, OPERATOR_ID: string, ADDITIONAL_AMOUNT: string, AMOUNT: string, TRANSACTION_TYPE: string, EXTRA_ORIGINATING_URI: string, value: string }): Promise<{ value: string }> {
        //console.logoptions('print string: ', options.printString);
        //console.log('EXTRA_ORIGINATING_URI: ', options.EXTRA_ORIGINATING_URI);
        //version: 1.6

        return options;
    }
    async initiateDebicheck(options: { EXTRA_ORIGINATING_URI: string, TRANSACTION_TYPE: string, ACCOUNT_NUMBER: string, MAX_AUTH_COLLECTION: string, CONTACT_REFERENCE: string, DEBITOR_ID: string, FROM_ACC_TYPE: string, TO_ACC_TYPE: string,COMMS_TYPE: string,DISABLE_CASHIER_LOGIN: string,DISABLE_SLIP_PRINTING: string,value: string }): Promise<{ value: string }> {
        //console.logoptions('print string: ', options.printString);
        //console.log('EXTRA_ORIGINATING_URI: ', options.EXTRA_ORIGINATING_URI);
        //version: 1.6

        return options;
    }
}

const DashPayModule = new DashPayModuleWeb();

export { DashPayModule };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(DashPayModule);
