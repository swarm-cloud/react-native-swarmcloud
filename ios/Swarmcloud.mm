#import "Swarmcloud.h"
#import <SwarmCloudKit/SwarmCloudKit-Swift.h>
#import <React/RCTEventEmitter.h>

@implementation Swarmcloud

{
  bool inited;
  bool hasListeners;
}

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(notifyPlaybackStalled: (RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    [P2pEngine.shared notifyPlaybackStalled];
    resolve(@(YES));
}

RCT_EXPORT_METHOD(restartP2p: (RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    [P2pEngine.shared restartP2pWithContentId:nil];
    resolve(@(YES));
}

RCT_EXPORT_METHOD(disableP2p: (RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    [P2pEngine.shared disableP2p];
    resolve(@(YES));
}

RCT_EXPORT_METHOD(stopP2p: (RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    [P2pEngine.shared stopP2p];
    resolve(@(YES));
}

RCT_EXPORT_METHOD(enableP2p: (RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    [P2pEngine.shared enableP2p];
    resolve(@(YES));
}

RCT_EXPORT_METHOD(shutdown: (RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    [P2pEngine.shared shutdown];
    resolve(@(YES));
}

RCT_EXPORT_METHOD(init: (NSString *)token
                  config:(NSDictionary *)args
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)
{
    if (inited) {
        reject(@"event_failure", @"no event id returned", nil);
        return;
    }
    inited = YES;
    P2pConfig* config = [P2pConfig defaultConfiguration];
    
    if ([args objectForKey:@"logEnabled"]) {
        BOOL logEnabled = [[args objectForKey:@"logEnabled"] boolValue];
        if (logEnabled) {
            int logLevel = [[args objectForKey:@"logLevel"] intValue];
            LogLevel level = LogLevelERROR;
            switch (logLevel) {
                case 0:
                    logEnabled = false;
                    break;
                case 1:
                    level = LogLevelDEBUG;
                    break;
                case 2:
                    level = LogLevelINFO;
                    break;
                case 3:
                    level = LogLevelWARN;
                    break;
                default:
                    level = LogLevelERROR;
                    break;
            }
            config.debug = logEnabled;
            config.logLevel = level;
        }
    }
    if ([args objectForKey:@"trackerZone"]) {
        int zone = [[args objectForKey:@"trackerZone"] intValue];
        switch (zone) {
            case 0:
                config.trackerZone = TrackerZoneEurope;
                break;
            case 1:
                config.trackerZone = TrackerZoneHongKong;
                break;
            case 2:
                config.trackerZone = TrackerZoneUSA;
                break;
            case 3:
                config.trackerZone = TrackerZoneChina;
                break;
            default:
                config.trackerZone = TrackerZoneEurope;
                break;
        }
    }
    if ([args objectForKey:@"p2pEnabled"]) {
        config.p2pEnabled = [[args objectForKey:@"p2pEnabled"] boolValue];
    }
    if ([args objectForKey:@"wifiOnly"]) {
        config.wifiOnly = [[args objectForKey:@"wifiOnly"] boolValue];
    }
    if ([args objectForKey:@"prefetchOnly"]) {
        config.prefetchOnly = [[args objectForKey:@"prefetchOnly"] boolValue];
    }
    if ([args objectForKey:@"downloadOnly"]) {
        config.downloadOnly = [[args objectForKey:@"downloadOnly"] boolValue];
    }
    if ([args objectForKey:@"useHttpRange"]) {
        config.useHttpRange = [[args objectForKey:@"useHttpRange"] boolValue];
    }
    if ([args objectForKey:@"logPersistent"]) {
        config.logPersistent = [[args objectForKey:@"logPersistent"] boolValue];
    }
    if ([args objectForKey:@"sharePlaylist"]) {
        config.sharePlaylist = [[args objectForKey:@"sharePlaylist"] boolValue];
    }
    if ([args objectForKey:@"localPortHls"]) {
        config.localPortHls = [[args objectForKey:@"localPortHls"] boolValue];
    }
    if ([args objectForKey:@"fastStartup"]) {
        config.fastStartup = [[args objectForKey:@"fastStartup"] boolValue];
    }
    if ([args objectForKey:@"geoIpPreflight"]) {
        config.geoIpPreflight = [[args objectForKey:@"geoIpPreflight"] boolValue];
    }
    if ([args objectForKey:@"useStrictHlsSegmentId"]) {
        config.useStrictHlsSegmentId = [[args objectForKey:@"useStrictHlsSegmentId"] boolValue];
    }
    if ([args objectForKey:@"maxPeerConnections"]) {
        config.maxPeerConnections = [[args objectForKey:@"maxPeerConnections"] intValue];
    }
    if ([args objectForKey:@"maxMediaFilesInPlaylist"]) {
        config.maxMediaFilesInPlaylist = [[args objectForKey:@"maxMediaFilesInPlaylist"] intValue];
    }
    if ([args objectForKey:@"dcDownloadTimeout"]) {
        config.dcDownloadTimeout = [[args objectForKey:@"dcDownloadTimeout"] doubleValue];
    }
    if ([args objectForKey:@"httpLoadTime"]) {
        config.httpLoadTime = [[args objectForKey:@"httpLoadTime"] doubleValue];
    }
    if ([args objectForKey:@"diskCacheLimit"]) {
        config.diskCacheLimit = [[args objectForKey:@"diskCacheLimit"] intValue];
    }
    if ([args objectForKey:@"memoryCacheCountLimit"]) {
        config.memoryCacheCountLimit = [[args objectForKey:@"memoryCacheCountLimit"] intValue];
    }
    if ([args objectForKey:@"signalConfig"]) {
        config.signalConfig = [[SignalConfig alloc] initWithMainAddr:[args objectForKey:@"signalConfig"] backupAddr:nil];
    }
    if ([args objectForKey:@"announce"]) {
        config.announce = [args objectForKey:@"announce"];
    }
    if ([args objectForKey:@"mediaFileSeparator"]) {
        config.mediaFileSeparator = [args objectForKey:@"mediaFileSeparator"];
    }
    if ([args objectForKey:@"tag"]) {
        config.customLabel = [args objectForKey:@"tag"];
    } else {
        config.customLabel = @"react-native";
    }
    if ([args objectForKey:@"httpHeadersForHls"]) {
        NSDictionary *headers = [args objectForKey:@"httpHeadersForHls"];
        config.httpHeadersHls = headers;
    }
    if ([args objectForKey:@"hlsMediaFiles"]) {
        NSArray *files = [args objectForKey:@"hlsMediaFiles"];
        config.hlsMediaFiles = files;
    }
    
    config.logLevel = LogLevelDEBUG;
    [P2pEngine setupWithToken:token config:config];  // replace with your own token
    [self startMonitor];
    resolve(@(YES));
}

RCT_EXPORT_BLOCKING_SYNCHRONOUS_METHOD(parseStreamURL: (nonnull NSString *)url videoId:(nullable NSString *)videoId) {
    if (P2pEngine.token == nil) {
        return url;
    }
    return [P2pEngine.shared parseStreamUrl:url videoId:videoId];
}

RCT_EXPORT_BLOCKING_SYNCHRONOUS_METHOD(getSDKVersion) {
    return P2pEngine.VERSION;
}

RCT_EXPORT_BLOCKING_SYNCHRONOUS_METHOD(getPeerId) {
    if (P2pEngine.token == nil) {
        return @"";
    }
    return P2pEngine.shared.peerId;
}

RCT_EXPORT_BLOCKING_SYNCHRONOUS_METHOD(isConnected) {
    if (P2pEngine.token == nil) {
        return @(NO);
    }
    return @(P2pEngine.shared.isConnected);
}

RCT_EXPORT_METHOD(setHttpHeadersForHls: (nullable NSDictionary<NSString *,NSString *> *)headers) {
    [P2pEngine.shared setHttpHeadersForHlsWithHeaders:headers];
}

RCT_EXPORT_METHOD(setHttpHeadersForDash: (nullable NSDictionary<NSString *,NSString *> *)headers) {}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeSwarmcloudSpecJSI>(params);
}
#endif

-(void)startObserving {
    hasListeners = YES;
}

-(void)stopObserving {
    hasListeners = NO;
}

- (NSArray<NSString *> *)supportedEvents {
    return @[ @"stats" ];
}

- (void)startMonitor {
    P2pStatisticsMonitor* monitor = [[P2pStatisticsMonitor alloc] initWithQueue:dispatch_get_main_queue()];
    P2pEngine.shared.p2pStatisticsMonitor = monitor;
    
    monitor.onPeers = ^(NSArray<NSString *> * _Nonnull peers) {
        if (self->hasListeners) {
            [self sendEventWithName:@"stats" body:@{@"peers": peers}];
        }
        
    };
    monitor.onP2pUploaded = ^(NSInteger value) {
        if (self->hasListeners) {
            [self sendEventWithName:@"stats" body:@{@"p2pUploaded": @(value)}];
        }
    };
    monitor.onP2pDownloaded = ^(NSInteger value, NSInteger speed) {
        if (self->hasListeners) {
            [self sendEventWithName:@"stats" body:@{@"p2pDownloaded": @(value), @"p2pDownloadSpeed": @(speed)}];
        }
    };
    monitor.onHttpDownloaded = ^(NSInteger value) {
        if (self->hasListeners) {
            [self sendEventWithName:@"stats" body:@{@"httpDownloaded": @(value)}];
        }
    };
    monitor.onServerConnected = ^(BOOL connected) {
        if (self->hasListeners) {
            [self sendEventWithName:@"stats" body:@{@"serverConnected": @(connected)}];
        }
    };

}

@end
