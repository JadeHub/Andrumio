package com.andrumio.josh.andrumio;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.andrumio.josh.mpd.IClient;

import java.util.List;

/**
 * Created by Josh on 10/01/2015.
 */

abstract public class SongDBLoaderBase<EntityType> extends AsyncTaskLoader<AsyncLoaderResult<List<EntityType>>> {

    protected IClient _client;
    private List<EntityType> _data;

    public SongDBLoaderBase(Context context) {
        super(context);
        _client = App.GetApp(context).getClient();
    }

    abstract protected List<EntityType> load();

    @Override
    public AsyncLoaderResult<List<EntityType>> loadInBackground()
    {
        try
        {
            if(_data == null)
                _data = load();
            return new AsyncLoaderResult<List<EntityType>>(_data);
        }
        catch(Exception e)
        {
            return new AsyncLoaderResult<List<EntityType>>(e);
        }
    }

    @Override
    protected void onStartLoading()
    {
        if(_data != null) {
            deliverResult(new AsyncLoaderResult<List<EntityType>>(_data));
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

