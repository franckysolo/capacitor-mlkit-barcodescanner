import { WebPlugin } from '@capacitor/core';
import { MlkitBarcodescannerPlugin } from './definitions';

export class MlkitBarcodescannerWeb extends WebPlugin implements MlkitBarcodescannerPlugin {
  constructor() {
    super({
      name: 'MlkitBarcodescanner',
      platforms: ['web'],
    });
  }

  // @TODO try dynamsoft-javascript-barcode for pwa implemntation
  async scanBarcode(): Promise<{ code: string }> {

    if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
      alert('heo')
      // try {
      //   let stream = await navigator.mediaDevices.getUserMedia({ video: true })
      //   let video = document.createElement('video')
      //   document.body.appendChild(video)
      //   video.srcObject = stream
      //   video.play()
      // } catch(e) {
      //   console.error(e)
      // }
    }

    return { code: 'test' }
  }
}

const MlkitBarcodescanner = new MlkitBarcodescannerWeb();

export { MlkitBarcodescanner };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(MlkitBarcodescanner);
