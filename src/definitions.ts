declare module '@capacitor/core' {
  interface PluginRegistry {
    DashPayModule: DashPayModulePlugin;
  }
}

export interface DashPayModulePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  print(filter: string): Promise<{results: any[]}>;
}
