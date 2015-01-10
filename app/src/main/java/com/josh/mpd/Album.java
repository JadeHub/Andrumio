package com.josh.mpd;

import java.util.ArrayList;
import java.util.List;

public class Album implements IAlbum {

    private String _name;

    public Album(String name)
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
