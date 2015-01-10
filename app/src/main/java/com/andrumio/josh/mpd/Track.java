package com.andrumio.josh.mpd;

/**
 * Created by Josh on 4/01/2015.
 */
public class Track implements ITrack {

    private String _name;

    public Track(String name)
    {
        _name = name;
    }


    @Override
    public String GetName() {return _name;}

    @Override
    public String toString() {

        return GetName();
    }
}
