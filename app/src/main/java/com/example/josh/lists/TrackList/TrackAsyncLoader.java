package com.example.josh.lists.TrackList;

import android.content.Context;

import com.example.josh.lists.SongDBLoaderBase;
import com.josh.mpd.Album;
import com.josh.mpd.Client;
import com.josh.mpd.ITrack;

import java.util.List;

/**
 * Created by Josh on 10/01/2015.
 */
public class TrackAsyncLoader extends SongDBLoaderBase<ITrack> {

    String _artist;

    public TrackAsyncLoader(Context context, Client client, String artist) {
        super(context, client);
        _artist = artist;
    }

    @Override
    public List<ITrack> load()
    {
        return _client.getTrackList(_artist);
    }
}
