package com.andrumio.josh.andrumio.ArtistList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrumio.josh.andrumio.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Josh on 10/01/2015.
 */
public class ArtistListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _chars; // header titles

    // child data in format of header title, child title
    HashMap<String, List<String>> _letterToArtists;

    public ArtistListAdapter(Context context, List<String> chars,
                             HashMap<String, List<String>> letterToArtists) {
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
/*
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        float f = txtListChild.getTextSize();


        ImageView imgView = (ImageView)convertView.findViewById(R.id.imgView1);
        imgView.setImageResource(R.drawable.album);

        return convertView;*/

        View vi = convertView;

        if(convertView==null) {

            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_row, null);
        }

        ((TextView)vi.findViewById(R.id.txtHeadline)).setText(childText);
        ((TextView)vi.findViewById(R.id.txtSubline)).setText(childText);
        ((ImageView)vi.findViewById(R.id.list_image)).setImageResource(R.drawable.artist);

        return vi;
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
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

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


