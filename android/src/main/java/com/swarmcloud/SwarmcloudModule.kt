package com.swarmcloud

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.p2pengine.sdk.P2pEngine
import com.p2pengine.core.p2p.P2pConfig
import com.p2pengine.core.tracking.TrackerZone
import com.p2pengine.core.p2p.P2pStatisticsListener
import com.p2pengine.core.utils.LogLevel
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import java.util.concurrent.TimeUnit
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.Arguments
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableArray

class SwarmcloudModule internal constructor(context: ReactApplicationContext) :
  SwarmcloudSpec(context) {

  var inited = false
  private var listenerCount = 0

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  override fun addListener(eventName: String) {
    listenerCount += 1
  }

  @ReactMethod
  override fun removeListeners(count: Double) {
    listenerCount -= count.toInt()
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  override fun getSDKVersion(): String {
    return P2pEngine.version
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  override fun getPeerId(): String? {
    return P2pEngine.instance?.peerId
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  override fun isConnected(): Boolean {
    return P2pEngine.instance?.isConnected ?: false
  }

  @ReactMethod
  override fun notifyPlaybackStalled(promise: Promise) {
    P2pEngine.instance?.notifyPlaybackStalled()
    promise.resolve(true)
  }

  @ReactMethod
  override fun restartP2p(promise: Promise) {
    P2pEngine.instance?.restartP2p(null)
    promise.resolve(true)
  }

  @ReactMethod
  override fun disableP2p(promise: Promise) {
    P2pEngine.instance?.disableP2p()
    promise.resolve(true)
  }

  @ReactMethod
  override fun stopP2p(promise: Promise) {
    P2pEngine.instance?.stopP2p()
    promise.resolve(true)
  }

  @ReactMethod
  override fun enableP2p(promise: Promise) {
    P2pEngine.instance?.enableP2p()
    promise.resolve(true)
  }

  @ReactMethod
  override fun shutdown(promise: Promise) {
    P2pEngine.instance?.shutdown()
    promise.resolve(true)
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  override fun setHttpHeadersForHls(headers: ReadableMap) {
    P2pEngine.instance?.setHttpHeadersForHls(toHashMap(headers))
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  override fun setHttpHeadersForDash(headers: ReadableMap) {
    P2pEngine.instance?.setHttpHeadersForDash(toHashMap(headers))
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  override fun parseStreamURL(url: String, videoId: String?): String {
    if (P2pEngine.instance == null) {
      return url
    }
    val parsedUrl = if (videoId != null) {
        P2pEngine.instance!!.parseStreamUrl(url, videoId)
    } else {
        P2pEngine.instance!!.parseStreamUrl(url)
    }
    return parsedUrl
  }

  @ReactMethod
  override fun init(token: String, args: ReadableMap, promise: Promise) {
      if (inited) {
        promise.reject("", "P2pEngine already init")
        return
      }
      inited = true
      var builder = P2pConfig.Builder()
       if (args.hasKey("logEnabled")) {
           var logEnabled = args.getBoolean("logEnabled");
            if (logEnabled) {
                val level: LogLevel = when (args.getInt("logLevel")) {
                    0 -> {
                        logEnabled = false
                        LogLevel.ERROR
                    }
                    1 -> LogLevel.DEBUG
                    2 -> LogLevel.INFO
                    3 -> LogLevel.WARN
                    4 -> LogLevel.ERROR
                    else -> LogLevel.ERROR
                }
                builder = builder.logEnabled(logEnabled).logLevel(level)
            }
       }
      if (args.hasKey("trackerZone")) {
          val zone: TrackerZone = when (args.getInt("trackerZone")) {
              0 -> TrackerZone.Europe
              1 -> TrackerZone.HongKong
              2 -> TrackerZone.USA
              3 -> TrackerZone.China
              else -> TrackerZone.Europe
          }
          builder = builder.trackerZone(zone)
      }
      if (args.hasKey("p2pEnabled")) {
          builder = builder.p2pEnabled(args.getBoolean("p2pEnabled"))
      }
      if (args.hasKey("isSetTopBox")) {
          builder = builder.isSetTopBox(args.getBoolean("isSetTopBox"))
      }
      if (args.hasKey("wifiOnly")) {
          builder = builder.wifiOnly(args.getBoolean("wifiOnly"))
      }
      if (args.hasKey("prefetchOnly")) {
          builder = builder.prefetchOnly(args.getBoolean("prefetchOnly"))
      }
      if (args.hasKey("downloadOnly")) {
          builder = builder.downloadOnly(args.getBoolean("downloadOnly"))
      }
      if (args.hasKey("useHttpRange")) {
          builder = builder.useHttpRange(args.getBoolean("useHttpRange"))
      }
      if (args.hasKey("logPersistent")) {
          builder = builder.logPersistent(args.getBoolean("logPersistent"))
      }
      if (args.hasKey("sharePlaylist")) {
          builder = builder.sharePlaylist(args.getBoolean("sharePlaylist"))
      }
      if (args.hasKey("fastStartup")) {
          builder = builder.fastStartup(args.getBoolean("fastStartup"))
      }
      if (args.hasKey("geoIpPreflight")) {
          builder = builder.geoIpPreflight(args.getBoolean("geoIpPreflight"))
      }
      if (args.hasKey("useStrictHlsSegmentId")) {
          builder = builder.useStrictHlsSegmentId(args.getBoolean("useStrictHlsSegmentId"))
      }
      if (args.hasKey("localPortHls")) {
          builder = builder.localPortHls(args.getInt("localPortHls"))
      }
      if (args.hasKey("localPortDash")) {
          builder = builder.localPortDash(args.getInt("localPortDash"))
      }
      if (args.hasKey("maxPeerConnections")) {
          builder = builder.maxPeerConnections(args.getInt("maxPeerConnections"))
      }
      if (args.hasKey("downloadTimeout")) {
          builder = builder.downloadTimeout(args.getInt("downloadTimeout"), TimeUnit.SECONDS)
      }
      if (args.hasKey("dcDownloadTimeout")) {
          builder = builder.dcDownloadTimeout(args.getInt("dcDownloadTimeout"), TimeUnit.SECONDS)
      }
      if (args.hasKey("memoryCacheCountLimit")) {
          builder = builder.memoryCacheCountLimit(args.getInt("memoryCacheCountLimit"))
      }
      if (args.hasKey("maxMediaFilesInPlaylist")) {
          builder = builder.maxMediaFilesInPlaylist(args.getInt("maxMediaFilesInPlaylist"))
      }
      if (args.hasKey("diskCacheLimit")) {
          builder = builder.diskCacheLimit(args.getInt("diskCacheLimit").toLong())
      }
      // if (args.hasKey("httpLoadTime")) {
      //    builder = builder.httpLoadTime(args.getInt("httpLoadTime").toLong())
      // }
      if (args.hasKey("playlistTimeOffset")) {
          builder = builder.insertTimeOffsetTag(args.getInt("playlistTimeOffset").toDouble())
      }
      if (args.hasKey("signalConfig")) {
          builder = builder.signalConfig(args.getString("signalConfig")!!, null)
      }
      if (args.hasKey("announce")) {
          builder = builder.announce(args.getString("announce")!!)
      }
      if (args.hasKey("tag")) {
          builder = builder.withTag(args.getString("tag")!!)
      } else {
          builder = builder.withTag("react-native")
      }
      if (args.hasKey("httpHeadersForHls")) {
          val headers = toHashMap(args.getMap("httpHeadersForHls"))
          builder = builder.httpHeadersForHls(headers)
      }
      if (args.hasKey("httpHeadersForDash")) {
          val headers = toHashMap(args.getMap("httpHeadersForDash"))
          builder = builder.httpHeadersForDash(headers)
      }
      if (args.hasKey("dashMediaFiles")) {
          val array = args.getArray("dashMediaFiles")!!
          val files: ArrayList<String> = ArrayList();
          for (i in 0 until array.size()) {
              files.add(array.getString(i))
          }
          builder = builder.dashMediaFiles(files)
      }

      val reactContext = getReactApplicationContext()

      P2pEngine.init(reactContext, token, builder.build())

      P2pEngine.instance!!.addP2pStatisticsListener(object : P2pStatisticsListener {
          override fun onHttpDownloaded(value: Int) {
              val params = Arguments.createMap().apply {
                  putInt("httpDownloaded", value)
              }
              sendEvent(getReactApplicationContext(), "stats", params)
          }

          override fun onP2pDownloaded(value: Int, speed: Int) {
              val params = Arguments.createMap().apply {
                  putInt("p2pDownloaded", value)
                  putInt("p2pDownloadSpeed", speed)
              }
              sendEvent(reactContext, "stats", params)
          }

          override fun onP2pUploaded(value: Int, speed: Int) {
              val params = Arguments.createMap().apply {
                  putInt("p2pUploaded", value)
              }
              sendEvent(reactContext, "stats", params)
          }

          override fun onPeers(peers: List<String>) {
              val array: WritableArray = Arguments.createArray()
              for (peer in peers) {
                  array.pushString(peer)
              }
              val params = Arguments.createMap().apply {
                  putArray("peers", array)
              }
              sendEvent(reactContext, "stats", params)
          }

          override fun onServerConnected(connected: Boolean) {
              val params = Arguments.createMap().apply {
                  putBoolean("serverConnected", connected)
              }
              sendEvent(reactContext, "stats", params)
          }
      })

      promise.resolve(true)
  }

  private fun toHashMap(map: ReadableMap?): HashMap<String, String> {
      val hashMap: HashMap<String, String> = HashMap()
      val iterator = map!!.keySetIterator()
      while (iterator.hasNextKey()) {
          val key = iterator.nextKey()
          when (map.getType(key)) {
              ReadableType.Boolean -> hashMap[key] = map.getBoolean(key).toString()
              ReadableType.Number -> hashMap[key] = map.getDouble(key).toString()
              ReadableType.String -> hashMap[key] = map.getString(key)!!
              else -> {}
          }
      }
      return hashMap
  }

  private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
      reactContext
          .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
          .emit(eventName, params)
  }

  companion object {
    const val NAME = "Swarmcloud"
  }
}
