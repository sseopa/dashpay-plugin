import { WebPlugin } from '@capacitor/core';
import { DashPayModulePlugin } from './definitions';
export declare class DashPayModuleWeb extends WebPlugin implements DashPayModulePlugin {
    constructor();
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    print(filter: string): Promise<{
        results: any[];
    }>;
}
declare const DashPayModule: DashPayModuleWeb;
export { DashPayModule };
