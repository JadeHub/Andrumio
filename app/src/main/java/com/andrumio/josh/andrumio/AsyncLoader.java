package com.andrumio.josh.andrumio;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

//import com.andrumio.josh.andrumio.ArtistList.ArtistAsyncLoader;


/**
 * Created by Josh on 15/01/2015.
 */

public class AsyncLoader<T> implements LoaderManager.LoaderCallbacks<AsyncLoaderResult<T>> {

    private final AsyncLoaderLoad<T> _loader;
    private final AsyncLoaderCallback<T> _callback;
    private final Context _context;


    public AsyncLoader(Context context, AsyncLoaderLoad<T> loader, AsyncLoaderCallback callback)
    {
        _context = context;
        _loader = loader;
        _callback = callback;
    }

    public void initLoader(LoaderManager lm)
    {
        lm.initLoader(0, null, this);
    }

    @Override
    public Loader<AsyncLoaderResult<T>> onCreateLoader(int id, Bundle args) {
        return new LoaderImpl<T>(_context, _loader);
    }

    @Override
    public void onLoadFinished(Loader<AsyncLoaderResult<T>> loader, AsyncLoaderResult<T> result) {

        if(result.Exception != null)
        {
            _callback.onLoadFailed(result.Exception);
        }
        else
        {
            _callback.onLoaded(result.Result);
        }
    }

    @Override
    public void onLoaderReset(Loader<AsyncLoaderResult<T>> loader) {

    }

}
