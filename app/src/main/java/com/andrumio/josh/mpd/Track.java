package com.andrumio.josh.mpd;

import java.util.HashMap;

/**
 * Created by Josh on 4/01/2015.
 */
public class Track implements ITrack {

    private String _name;
    private String _album;

    public Track(String name)
    {
        _name = name;
    }

    public Track(HashMap<String, String> tags)
    {
        if(tags.containsKey("Title"))
        {
            _name = tags.get("Title");
        }
        else
        {
            throw new IllegalArgumentException("No Title tag");
        }

        if(tags.containsKey("Album"))
        {
            _album = tags.get("Album");
        }
    }


    @Override
    public String getAlbumName() {return  _album;}

    @Override
    public String getTitle() {return _name;}

    @Override
    public String toString() {

        return getTitle();
    }
}
