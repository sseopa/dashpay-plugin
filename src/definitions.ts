declare module '@capacitor/core' {
  interface PluginRegistry {
    DashPayModule: DashPayModulePlugin;
  }
}

export interface DashPayModulePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  getSerial(options: { value: string }): Promise<{ value: string }>;
  print(options: { printString: string,EXTRA_ORIGINATING_URI:string }): Promise<{results: any[]}>;
}
