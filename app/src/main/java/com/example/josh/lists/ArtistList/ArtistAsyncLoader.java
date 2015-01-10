package com.example.josh.lists.ArtistList;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.josh.lists.SongDBLoaderBase;
import com.josh.mpd.Album;
import com.josh.mpd.IArtist;
import com.josh.mpd.Client;

import java.util.List;

public class ArtistAsyncLoader extends SongDBLoaderBase<IArtist> {

    public ArtistAsyncLoader(Context context, Client client) {
        super(context, client);
    }

    @Override
    protected List<IArtist> load()
    {
        return _client.getArtistList();
    }
}

