package com.andrumio.josh.mpd;

import java.util.List;

/**
 * Created by Josh on 5/01/2015.
 */
public interface IClient {

    public interface Callback
    {
        void onConnectionError(String message);
        void onConnected();
        void onDisconnected();
    }

    void connect(Callback c);
    void disconnect();
    boolean isConnected();

    String getName();
    String getHostName();
    int getPort();

    List<IArtist> getArtistList();

    List<ITrack> getArtistTrackList(String artist);

    List<ITrack> getCurrentPlayList();

    TagSet getStatus();

    List<String> idle();

    void endIdle();
}


