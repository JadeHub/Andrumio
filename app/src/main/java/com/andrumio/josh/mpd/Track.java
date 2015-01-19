package com.andrumio.josh.mpd;

import android.nfc.Tag;

import java.util.HashMap;

/**
 * Created by Josh on 4/01/2015.
 */
public class Track implements ITrack {

    private final String _file;

    private final HashMap<String, String> _tags;

    public Track(HashMap<String, String> tags)
    {
        _tags = new HashMap<>(tags);

        if(hasTag(TagNames.FILE))
        {
            _file = getTag(TagNames.FILE);
        }
        else
        {
            throw new IllegalArgumentException("No File tag");
        }
    }

    @Override
    public String getFileName() {return _file;}

    @Override
    public String getAlbumName() {return getTag(TagNames.ALBUM);}

    @Override
    public String getTitle() {return getTag(TagNames.TITLE);}

    @Override
    public String toString() {

        return getTitle();
    }

    @Override
    public int getId()
    {
        if(hasTag(TagNames.ID))
        {
            return Integer.parseInt(getTag(TagNames.ID));
        }
        return -1;
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
