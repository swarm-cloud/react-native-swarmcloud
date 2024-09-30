#import <React/RCTEventEmitter.h>
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNSwarmcloudSpec.h"

@interface Swarmcloud : RCTEventEmitter <NativeSwarmcloudSpec>
#else
#import <React/RCTBridgeModule.h>

@interface Swarmcloud : RCTEventEmitter <RCTBridgeModule>
#endif

@end
