package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.LocationListAdapter;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LocationListView extends CardContentView {

    @InjectView(R.id.lvLocations) ListView mLocationListView;

    private LocationListAdapter mLocationListAdapter;
    private Context mContext;

    public LocationListView(Context context) {
        super(context);
        init(context);
    }

    public LocationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LocationListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LocationListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    public void setData(ArrayList<?> data) {
        ArrayList<LocationV1> locations = new ArrayList<>();
        locations.addAll((ArrayList<? extends LocationV1>) data);
        int numberOfLocations = locations.size();

        ViewGroup.LayoutParams layoutParams = mLocationListView.getLayoutParams();
        int totalDividerHeight = mLocationListView.getDividerHeight() * numberOfLocations;
        layoutParams.height = (int)(mContext.getResources().getDimension(R.dimen.location_item_row_height) * numberOfLocations) + totalDividerHeight;
        mLocationListView.setLayoutParams(layoutParams);
        mLocationListView.requestLayout();

        mLocationListAdapter.clear();
        mLocationListAdapter.addAll(locations);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_location_list, this, true);
        ButterKnife.inject(this);
        mContext = context;
        mLocationListAdapter = new LocationListAdapter(context, new ArrayList<LocationV1>());
        mLocationListView.setAdapter(mLocationListAdapter);
        mLocationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BusProvider.getMainBus().post(mLocationListAdapter.getItem(position));
            }
        });
    }
}