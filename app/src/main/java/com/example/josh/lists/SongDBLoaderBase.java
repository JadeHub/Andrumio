package com.example.josh.lists;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.josh.mpd.Client;

import java.util.List;

/**
 * Created by Josh on 7/01/2015.
 */

abstract public class SongDBLoaderBase<EntityType> extends AsyncTaskLoader<List<EntityType>> {

    protected Client _client;
    private List<EntityType> _data;

    public SongDBLoaderBase(Context context, Client client) {
        super(context);
        _client = client;
    }

    abstract protected List<EntityType> load();

    @Override
    public List<EntityType> loadInBackground()
    {
        if(_data == null)
            _data = load();
        return _data;
    }

    @Override
    protected void onStartLoading()
    {
        if(_data != null) {
            deliverResult(_data);
        }
        if(takeContentChanged() || _data == null){
            forceLoad();
        }
    }

    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }
}
