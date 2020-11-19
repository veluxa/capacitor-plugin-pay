declare module '@capacitor/core' {
  interface PluginRegistry {
    Pay: PayPlugin;
  }
}

export interface PayPlugin {
  wxPayRequest(options: { mch_id: string, prepay_id: string, nonce: string, timestamp: number, sign: string }): Promise<any>;

  aliPayRequest(options: { orderInfo: string }): Promise<any>;
}
