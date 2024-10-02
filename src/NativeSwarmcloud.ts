import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  parseStreamURL(url: string, videoId: string | undefined): string;
  getSDKVersion(): string;
  init(token: string, config: Object): Promise<void>;
  setHttpHeadersForHls(headers: Object): void;
  setHttpHeadersForDash(headers: Object): void;
  getPeerId(): string | undefined;
  isConnected(): boolean;
  notifyPlaybackStalled(): Promise<void>;
  restartP2p(): Promise<void>;
  disableP2p(): Promise<void>;
  stopP2p(): Promise<void>;
  enableP2p(): Promise<void>;
  shutdown(): Promise<void>;
  addListener(eventType: string): void;
  removeListeners(count: number): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('Swarmcloud');
