import { WebPlugin } from '@capacitor/core';
import { PayPlugin } from './definitions';

export class PayWeb extends WebPlugin implements PayPlugin {
  constructor() {
    super({
      name: 'Pay',
      platforms: ['web'],
    });
  }

  async wxPayRequest(options: { mch_id: string, prepay_id: string, nonce: string, timestamp: number, sign: string }): Promise<any> {
    return options;
  }

  async aliPayRequest(options: { orderInfo: string }): Promise<any> {
    return options
  }
}

const Pay = new PayWeb();

export { Pay };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(Pay);
