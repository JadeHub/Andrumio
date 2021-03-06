package com.andrumio.josh.mpd;

/**
 * Created by Josh on 4/01/2015.
 */
public interface ITrack {

    String getTitle();
    String getFileName();
    String getAlbumName();

    boolean hasTag(String tag);
    String getTag(String tag);

    int getId();
}
