package com.andrumio.josh.andrumio;

/**
 * Created by Josh on 15/01/2015.
 */
public interface AsyncLoaderCallback<T>
{
    void onLoaded(T result);
    void onLoadFailed(Exception e);
}
