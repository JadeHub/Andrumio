package com.andrumio.josh.andrumio.ArtistList;

import android.content.Context;

import com.andrumio.josh.andrumio.SongDBLoaderBase;
import com.andrumio.josh.mpd.Client;
import com.andrumio.josh.mpd.IArtist;

import java.util.List;

/**
 * Created by Josh on 10/01/2015.
 */
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

