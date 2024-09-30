package com.swarmcloud

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReactMethod

abstract class SwarmcloudSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {

  abstract fun init(token: String, args: ReadableMap, promise: Promise)
  abstract fun setHttpHeadersForHls(headers: ReadableMap)
  abstract fun setHttpHeadersForDash(headers: ReadableMap)
  abstract fun getSDKVersion(): String
  abstract fun getPeerId(): String?
  abstract fun parseStreamURL(url: String, videoId: String?): String
  abstract fun isConnected(): Boolean
  abstract fun notifyPlaybackStalled(promise: Promise)
  abstract fun restartP2p(promise: Promise)
  abstract fun disableP2p(promise: Promise)
  abstract fun stopP2p(promise: Promise)
  abstract fun enableP2p(promise: Promise)
  abstract fun shutdown(promise: Promise)
  abstract fun addListener(eventName: String)
  abstract fun removeListeners(count: Double)
}
