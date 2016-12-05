package com.rhlabs.circle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;

import java.util.ArrayList;

/**
 * Created by anju on 6/3/15.
 */
public class LocationListAdapter extends ArrayAdapter<LocationV1> {
    private Context mContext;

    public LocationListAdapter(Context context, ArrayList<LocationV1> locations) {
        super(context, R.layout.view_location_item, locations);
        mContext = context;
    }

    static class LocationViewHolder {
        TextView locationNameTextView;
        TextView locationSecondaryInfoTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LocationViewHolder locationViewHolder;
        LocationV1 locationV1 = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_location_item, parent, false);
            locationViewHolder = new LocationViewHolder();
            locationViewHolder.locationNameTextView = (TextView) convertView.findViewById(R.id.tvLocationName);
            locationViewHolder.locationSecondaryInfoTextView = (TextView) convertView.findViewById(R.id.tvLocationSecondaryInfo);
            convertView.setTag(locationViewHolder);
        }
        else {
            locationViewHolder = (LocationViewHolder) convertView.getTag();
        }

        locationViewHolder.locationNameTextView.setText(locationV1.name);
        if (locationV1.profile_count != null) {
            locationViewHolder.locationSecondaryInfoTextView.setText("" + locationV1.profile_count);
        }
        else {
            locationViewHolder.locationSecondaryInfoTextView.setText("");
        }

        return convertView;
    }
}
