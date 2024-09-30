import { useState, useEffect, useRef } from 'react';
import {
  StyleSheet,
  TextInput,
  Button,
  SafeAreaView,
  ScrollView,
  StatusBar,
  useColorScheme,
} from 'react-native';
import Video, { type VideoRef } from 'react-native-video';
import {
  initP2pEngine,
  parseStreamURL,
  TrackerZone,
  LogLevel,
} from 'react-native-swarmcloud';
import { Colors, Header } from 'react-native/Libraries/NewAppScreen';
import Stats from './Stats';

export default function App() {
  const [text, setText] = useState(
    'https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/level_0.m3u8'
  );
  const [source, setSource] = useState({
    uri: text,
  });
  const videoRef = useRef<VideoRef>(null);

  const isDarkMode = useColorScheme() === 'dark';
  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  useEffect(() => {
    initP2pEngine('ZMuO5qHZg', {
      trackerZone: TrackerZone.Europe,
      logEnabled: true,
      logLevel: LogLevel.DEBUG,
    })
      .then(() => {
        play();
      })
      .catch((e) => console.error(e.toString()));
  }, []);

  const play = () => {
    if (!videoRef.current) {
      return;
    }
    // videoRef.current.resume();
    const url = parseStreamURL(text);
    setSource({ uri: url });
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}
      >
        <Header />
        <Video
          source={source}
          controls={true}
          ref={videoRef}
          // Callback when remote video is buffering
          // onBuffer={onBuffer}
          // Callback when video cannot be loaded
          // onError={onError}
          style={styles.backgroundVideo}
        />
      </ScrollView>
      <Stats></Stats>
      <TextInput
        editable
        value={text}
        onChangeText={setText}
        multiline
        placeholder="your m3u8/mpd here..."
        style={styles.textInput}
      />
      <Button onPress={play} title="Play" color="#841584" />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  backgroundVideo: {
    position: 'absolute',
    top: 0,
    left: 0,
    bottom: 0,
    right: 0,
  },
  textInput: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 5,
    padding: 10,
    fontSize: 18,
    marginBottom: 20,
  },
});
