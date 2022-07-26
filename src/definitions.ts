declare module '@capacitor/core' {
  interface PluginRegistry {
    DashPayModule: DashPayModulePlugin;
  }
}

export interface DashPayModulePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  getSerial(options: { value: string }): Promise<{ value: string }>;
  print(options: { printString: string,EXTRA_ORIGINATING_URI:string,NewActivityLaunchOption:boolean }): Promise<{results: any[]}>;
  pay(options: { REFERENCE_NUMBER: string,TRANSACTION_ID: string,OPERATOR_ID: string,ADDITIONAL_AMOUNT:string,AMOUNT: string,TRANSACTION_TYPE: string,EXTRA_ORIGINATING_URI:string,value: string}): Promise<{value: string}>;
}
