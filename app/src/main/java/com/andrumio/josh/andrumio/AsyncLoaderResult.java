package com.andrumio.josh.andrumio;

import android.content.AsyncTaskLoader;

/**
 * Created by Josh on 15/01/2015.
 */
public class AsyncLoaderResult<T> {

    public final Exception Exception;
    public final T Result;

    public AsyncLoaderResult(T result)
    {
        Result = result;
        Exception = null;
    }

    public AsyncLoaderResult(Exception e)
    {
        Result = null;
        Exception = e;
    }
}
