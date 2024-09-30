import { View, Text, StyleSheet } from 'react-native';
import {
  getSDKVersion,
  getPeerId,
  addEventListener,
} from 'react-native-swarmcloud';
import { useEffect, useState } from 'react';

const Stats = () => {
  const [connected, setConnected] = useState(false);
  const [offload, setOffload] = useState(0);
  const [upload, setUpload] = useState(0);
  const [httpDownLoaded, setHttpDownLoaded] = useState(0);
  const [peers, setPeers] = useState(0);
  useEffect(() => {
    let eventListener = addEventListener('stats', (_data) => {
      if (_data.serverConnected) {
        setConnected(true);
      } else if (_data.serverConnected === false) {
        setConnected(false);
      }
      if (_data.p2pDownloaded) {
        setOffload(offload + _data.p2pDownloaded);
      }
      if (_data.p2pUploaded) {
        setUpload(upload + _data.p2pUploaded);
      }
      if (_data.peers) {
        setPeers(_data.peers.length);
      }
      if (_data.httpDownloaded) {
        setHttpDownLoaded(httpDownLoaded + _data.httpDownloaded);
      }
    });
    return () => {
      eventListener.remove();
    };
  }, [httpDownLoaded, offload, upload]);

  return (
    <View style={styles.container}>
      <View style={styles.row}>
        <Text style={styles.text}>
          Offload: {(offload / 1024).toFixed(1)} MB
        </Text>
        <Text style={styles.text}>
          P2P Ration:{' '}
          {((offload / (httpDownLoaded + offload)) * 100).toFixed(1)}%
        </Text>
      </View>
      <View style={styles.row}>
        <Text style={styles.text}>Upload: {(upload / 1024).toFixed(1)} MB</Text>
        <Text style={styles.text}>Connected: {connected + ''}</Text>
      </View>
      <View style={styles.row}>
        <Text style={styles.text}>Peers: {peers}</Text>
        <Text style={styles.text}>Version: {getSDKVersion()}</Text>
      </View>
      <Text style={{ textAlign: 'center', fontSize: 18 }}>
        Peer ID: {getPeerId()}
      </Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  text: {
    fontSize: 18,
  },
});

export default Stats;
