package com.andrumio.josh.mpd;

/**
 * Created by Josh on 18/01/2015.
 */
public class Status {
    public final TagSet Tags;

    public Status(TagSet tags)
    {
        Tags = tags;
    }

    public int getPlaylistVersion()
    {
        if(Tags.hasTag(TagNames.PLAYLIST_ID))
            return Integer.parseInt(Tags.getTag(TagNames.PLAYLIST_ID));
        return -1;
    }
}
