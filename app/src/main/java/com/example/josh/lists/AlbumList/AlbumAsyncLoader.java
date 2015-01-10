package com.example.josh.lists.AlbumList;

import android.content.Context;

import com.example.josh.lists.SongDBLoaderBase;
import com.josh.mpd.Album;
import com.josh.mpd.Artist;
import com.josh.mpd.Client;
import com.josh.mpd.IAlbum;

import java.util.List;

/**
 * Created by Josh on 9/01/2015.
 */
public class AlbumAsyncLoader extends SongDBLoaderBase<IAlbum> {

    String _artist;

    public AlbumAsyncLoader(Context context, Client client, String artist) {
        super(context, client);
        _artist = artist;
    }

    @Override
    public List<IAlbum> load()
    {
        return _client.getAlbumList(_artist);
    }
}

