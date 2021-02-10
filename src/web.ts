import { WebPlugin } from '@capacitor/core';
import { MlkitBarcodescannerPlugin } from './definitions';

export class MlkitBarcodescannerWeb extends WebPlugin implements MlkitBarcodescannerPlugin {
  constructor() {
    super({
      name: 'MlkitBarcodescanner',
      platforms: ['web'],
    });
  }


  async scanBarcode(): Promise<{ value: string }> {
    return { value: 'test' }
  }
}

const MlkitBarcodescanner = new MlkitBarcodescannerWeb();

export { MlkitBarcodescanner };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(MlkitBarcodescanner);
