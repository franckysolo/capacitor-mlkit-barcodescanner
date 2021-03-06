declare module '@capacitor/core' {
  interface PluginRegistry {
    MlkitBarcodescanner: MlkitBarcodescannerPlugin;
  }
}

export interface MlkitBarcodescannerPlugin {
  scanBarcode(): Promise<{ code: string }>;
}
