package com.andrumio.josh.mpd;

import java.util.HashMap;

/**
 * Created by Josh on 4/01/2015.
 */
public class Track implements ITrack {

    private final static String FILE_TAG = "file";
    private final static String ALBUM_TAG = "Album";
    private final static String ARTIST_TAG = "Artist";
    private final static String TITLE_TAG = "Title";

    private final String _file;

    private final HashMap<String, String> _tags;

    public Track(HashMap<String, String> tags)
    {
        _tags = new HashMap<>(tags);

        if(hasTag(FILE_TAG))
        {
            _file = getTag(FILE_TAG);
        }
        else
        {
            throw new IllegalArgumentException("No File tag");
        }
    }

    @Override
    public String getFileName() {return _file;}

    @Override
    public String getAlbumName() {return getTag(ALBUM_TAG);}

    @Override
    public String getTitle() {return getTag(TITLE_TAG);}

    @Override
    public String toString() {

        return getTitle();
    }

    @Override
    public boolean hasTag(String tag)
    {
        return _tags.containsKey(tag);
    }

    @Override
    public String getTag(String tag)
    {
        if(hasTag(tag))
            return _tags.get(tag);
        return "";
    }
}
