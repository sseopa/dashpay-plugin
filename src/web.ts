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
  async print(filter: string): Promise<{ results: any[] }> {
    console.log('filter: ', filter);
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
