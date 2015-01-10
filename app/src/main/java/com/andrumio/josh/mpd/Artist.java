package com.andrumio.josh.mpd;

import java.util.ArrayList;
import java.util.List;

public class Artist implements IArtist {

    private String _name;


    public Artist(int id, String name)
    {
        _name = name;

    }

    public Artist(String name)
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
