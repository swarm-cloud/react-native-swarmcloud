
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNSwarmcloudSpec.h"

@interface Swarmcloud : NSObject <NativeSwarmcloudSpec>
#else
#import <React/RCTBridgeModule.h>

@interface Swarmcloud : NSObject <RCTBridgeModule>
#endif

@end
