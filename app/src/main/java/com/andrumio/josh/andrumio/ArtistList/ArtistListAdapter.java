package com.andrumio.josh.andrumio.ArtistList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.andrumio.josh.andrumio.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Josh on 10/01/2015.
 */
public class ArtistListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Character> _chars; // header titles

    // child data in format of header title, child title
    HashMap<Character, List<String>> _letterToArtists;

    public ArtistListAdapter(Context context, List<Character> chars,
                             HashMap<Character, List<String>> letterToArtists) {
        this._context = context;
        _chars = chars;
        _letterToArtists = letterToArtists;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this._letterToArtists.get(this._chars.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._letterToArtists.get(this._chars.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._chars.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return _chars.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Character headerTitle = (Character) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.toString());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}


