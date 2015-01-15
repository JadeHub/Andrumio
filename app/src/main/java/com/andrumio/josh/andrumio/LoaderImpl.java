package com.andrumio.josh.andrumio;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Josh on 15/01/2015.
 */
public class LoaderImpl<T> extends AsyncTaskLoader<AsyncLoaderResult<T>> {
    private T _data;
    private final AsyncLoaderLoad<T> _loader;

    public LoaderImpl(Context context, AsyncLoaderLoad<T> loader) {
        super(context);
        _loader = loader;
    }

    @Override
    public AsyncLoaderResult<T> loadInBackground()
    {
        try
        {
            if(_data == null)
                _data = _loader.Load();
            return new AsyncLoaderResult<T>(_data);
        }
        catch(Exception e)
        {
            return new AsyncLoaderResult<T>(e);
        }
    }

    @Override
    protected void onStartLoading()
    {
        if(_data != null) {
            deliverResult(new AsyncLoaderResult<T>(_data));
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
