package com.andrumio.josh.mpd;

import java.util.HashMap;

/**
 * Created by Josh on 16/01/2015.
 */
public class TagSet {
    private HashMap<String, String> _tags;

    public TagSet(HashMap<String, String> tags)
    {
        _tags = new HashMap<>(tags);
    }

    public boolean hasTag(String tag)
    {
        return _tags.containsKey(tag);
    }

    public String getTag(String tag)
    {
        if(hasTag(tag))
            return _tags.get(tag);
        return "";
    }

    public boolean isIdentical(TagSet other)
    {
        if(other._tags.size() != _tags.size())
            return false;

        for(String tag : _tags.keySet())
        {
            if(!other.hasTag(tag))
                return false;
            if(getTag(tag) != other.getTag(tag))
                return false;
        }
        return true;
    }
}
