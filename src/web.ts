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
  async getSerial(options: { value: string }): Promise<{ value: string }> {
    console.log('getSerial', options);
    return options;
  }
  async print(options: { printString: string,EXTRA_ORIGINATING_URI:string,NewActivityLaunchOption:boolean }): Promise<{ results: any[] }> {
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
}

const DashPayModule = new DashPayModuleWeb();

export { DashPayModule };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(DashPayModule);
