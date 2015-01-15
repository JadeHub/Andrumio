package com.andrumio.josh.mpd;

import java.util.List;

/**
 * Created by Josh on 5/01/2015.
 */
public interface IClient {

    public abstract class Callback
    {
        public void onConnectionError(String message) {}
        public void onConnected() {}
        public void onDisconnected() {}
    }

    void connect(Callback c);
    void disconnect();
    boolean isConnected();

    String getName();
    String getHostName();
    int getPort();

    List<IArtist> getArtistList();
    List<IAlbum> getAlbumList(String artist);
    List<ITrack> getTrackList(String album);

    List<ITrack> getArtistTrackList(String artist);
}


