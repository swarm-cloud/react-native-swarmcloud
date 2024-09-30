import {
  NativeModules,
  Platform,
  NativeEventEmitter,
  type EmitterSubscription,
} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-swarmcloud' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const SwarmcloudModule = isTurboModuleEnabled
  ? require('./NativeSwarmcloud').default
  : NativeModules.Swarmcloud;

const Swarmcloud = SwarmcloudModule
  ? SwarmcloudModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export const SwarmcloudEventEmitter = new NativeEventEmitter(Swarmcloud);

export function addEventListener(
  event: string,
  callback: (data: any) => void
): EmitterSubscription {
  let eventListener = SwarmcloudEventEmitter.addListener(event, (data) => {
    callback(data);
  });
  return eventListener;
}

export function parseStreamURL(
  url: string,
  videoId: string | undefined = undefined
): string {
  return Swarmcloud.parseStreamURL(url, videoId);
}

export function getSDKVersion(): string {
  return Swarmcloud.getSDKVersion();
}

export function getPeerId(): string | undefined {
  return Swarmcloud.getPeerId();
}

export function isConnected(): boolean {
  return Swarmcloud.isConnected();
}

export function notifyPlaybackStalled(): Promise<void> {
  return Swarmcloud.notifyPlaybackStalled();
}

export function restartP2p(): Promise<void> {
  return Swarmcloud.restartP2p();
}

export function disableP2p(): Promise<void> {
  return Swarmcloud.disableP2p();
}

export function stopP2p(): Promise<void> {
  return Swarmcloud.stopP2p();
}

export function enableP2p(): Promise<void> {
  return Swarmcloud.enableP2p();
}

export function shutdown(): Promise<void> {
  return Swarmcloud.shutdown();
}

export function setHttpHeadersForHls(headers: Object) {
  return Swarmcloud.setHttpHeadersForHls(headers);
}

export function setHttpHeadersForDash(headers: Object) {
  return Swarmcloud.setHttpHeadersForDash(headers);
}

export function initP2pEngine(token: string, config: Object): Promise<void> {
  return Swarmcloud.init(token, config);
}

export enum TrackerZone {
  Europe = 0,
  HongKong,
  USA,
  China,
}

export enum LogLevel {
  None = 0,
  DEBUG,
  INFO,
  WARN,
  ERROR,
}
