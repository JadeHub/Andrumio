package com.andrumio.josh.andrumio.TrackList;

import android.content.Context;

import com.andrumio.josh.andrumio.SongDBLoaderBase;
import com.andrumio.josh.mpd.Client;
import com.andrumio.josh.mpd.IArtist;
import com.andrumio.josh.mpd.ITrack;

import java.util.List;

/**
 * Created by Josh on 11/01/2015.
 */
public class TrackAsyncLoader extends SongDBLoaderBase<ITrack> {

    private String _artist;

    public TrackAsyncLoader(Context context, Client client, String artist) {
        super(context, client);
        _artist = artist;
    }

    @Override
    protected List<ITrack> load()
    {

        return _client.getArtistTrackList(_artist);
    }
}


