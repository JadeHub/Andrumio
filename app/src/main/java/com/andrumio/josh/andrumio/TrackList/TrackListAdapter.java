package com.andrumio.josh.andrumio.TrackList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrumio.josh.andrumio.R;
import com.andrumio.josh.mpd.ITrack;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Josh on 11/01/2015.
 */
public class TrackListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _albums; // header titles

    //album name to track list
    HashMap<String, List<ITrack>> _albumToTracks;

    public TrackListAdapter(Context context, List<String> albums,
                            HashMap<String, List<ITrack>> albumToTracks) {
        this._context = context;
        _albums = albums;
        _albumToTracks = albumToTracks;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this._albumToTracks.get(this._albums.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

     /*   final ITrack track = (ITrack) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(track.getTitle());

        return convertView;*/

        final ITrack track = (ITrack) getChild(groupPosition, childPosition);
        View vi = convertView;

        if(convertView==null) {

            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_row, null);
        }

        ((TextView)vi.findViewById(R.id.txtHeadline)).setText(track.getTitle());
        ((TextView)vi.findViewById(R.id.txtSubline)).setText(track.getAlbumName());
        ((ImageView)vi.findViewById(R.id.list_image)).setImageResource(R.drawable.track3);

        return vi;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._albumToTracks.get(this._albums.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _albums.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return _albums.size();
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
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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
