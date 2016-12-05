package com.rhlabs.circle.fragments.overview_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.LocationListAdapter;
import com.rhlabs.circle.fragments.BaseMapFragment;
import com.rhlabs.circle.services.Callback;
import com.rhlabs.circle.services.ServiceClient;
import com.rhlabs.circle.services.WrappedResponse;
import com.rhlabs.circle.utils.EndlessScrollListener;
import com.rhlabs.protobufs.services.organization.actions.get_locations.GetLocationsResponseV1;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.services.registry.responses.Ext_responses;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

/**
 * Created by anju on 6/3/15.
 */
public class LocationsOverviewFragment extends BaseMapFragment {

    @InjectView(R.id.lvLocations)
    ListView mLocationsListView;

    @InjectView(R.id.tvLocationsCount)
    TextView mLocationsCountTextView;

    @InjectView(R.id.pbProgressBar)
    ProgressBar mProgressBar;

    private static final String ARG_LOCATIONS_1 = "locations";
    private static final String ARG_LOCATIONS_NEXT_REQUEST_2 = "locationsNextRequest";

    private ArrayList<LocationV1> mLocations;
    private OnLocationClickedListener mListener;
    private ServiceRequestV1 mLocationsNextRequest;
    private LocationListAdapter mLocationListAdapter;

    public static LocationsOverviewFragment newInstance(ArrayList<LocationV1> locations, ServiceRequestV1 locationsNextRequest) {
        LocationsOverviewFragment fragment = new LocationsOverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATIONS_1, locations);
        args.putSerializable(ARG_LOCATIONS_NEXT_REQUEST_2, locationsNextRequest);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationsOverviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLocations = (ArrayList<LocationV1>) getArguments().getSerializable(ARG_LOCATIONS_1);
            mLocationsNextRequest = (ServiceRequestV1) getArguments().getSerializable(ARG_LOCATIONS_NEXT_REQUEST_2);
        }
    }

    private void moveMapToIncludeAllMarkers() {
        if (mGoogleMap != null && mLocations != null && mLocations.size() > 0) {
            LatLngBounds.Builder markerBoundsBuilder = new LatLngBounds.Builder();
            for (LocationV1 locationV1 : mLocations) {
                LatLng position = new LatLng(Double.parseDouble(locationV1.latitude), Double.parseDouble(locationV1.longitude));
                markerBoundsBuilder.include(position);
            }

            LatLngBounds markerBounds = markerBoundsBuilder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(markerBounds, 0);
            mGoogleMap.animateCamera(cameraUpdate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_locations_overview, container, false);
        ButterKnife.inject(this, rootView);

        mLocationListAdapter = new LocationListAdapter(getActivity(), mLocations);
        mLocationsListView.setAdapter(mLocationListAdapter);
        mLocationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick((ListView) parent, view, position, id);
            }
        });

        addLocationsCount();

        if (mLocationsNextRequest != null) {
            mLocationsListView.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    loadMoreData();
                }
            });

            if (mLocations.size() == 0) {
                loadMoreData();
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
        }

        return rootView;
    }

    private void addLocationsCount() {
        if (mLocations != null) {
            if (mLocations.size() == 0) {
                mLocationsCountTextView.setVisibility(View.INVISIBLE);
            } else {
                mLocationsCountTextView.setVisibility(View.VISIBLE);

                if (mLocations.size() == 1) {
                    mLocationsCountTextView.setText(getResources().getString(R.string.one_location_count_label_text));
                } else {
                    mLocationsCountTextView.setVisibility(View.VISIBLE);
                    mLocationsCountTextView.setText(
                            String.format(getResources().getString(R.string.multiple_locations_count_label_text), mLocations.size())
                    );
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLocationClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLocationClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onLocationClicked(mLocations.get(position));
        }
    }

    public interface OnLocationClickedListener {
        void onLocationClicked(LocationV1 location);
    }

    protected void onMapLoaded() {
        moveMapToIncludeAllMarkers();
    }

    protected void onMapLayoutComplete() {
        moveMapToIncludeAllMarkers();
    }

    protected void addLocationMarkers() {
        if (mLocations != null && mGoogleMap != null) {
            for (LocationV1 locationV1 : mLocations) {
                LatLng position = new LatLng(Double.parseDouble(locationV1.latitude), Double.parseDouble(locationV1.longitude));
                mGoogleMap.addMarker(new MarkerOptions().position(position).title(locationV1.name));
            }
        }
    }

    private void loadMoreData() {
        if (mLocationsNextRequest != null && mLocationListAdapter != null) {
            ServiceClient.sendRequest(mLocationsNextRequest, Ext_responses.get_locations, new Callback<WrappedResponse>() {
                @Override
                public void success(WrappedResponse wrappedResponse) {
                    GetLocationsResponseV1 response = (GetLocationsResponseV1) wrappedResponse.getResponse();
                    if (response != null && response.locations.size() > 0) {
                        mLocationListAdapter.addAll(response.locations);
                        mLocationListAdapter.notifyDataSetChanged();
                        addLocationsCount();
                        addLocationMarkers();
                        moveMapToIncludeAllMarkers();
                    }

                    mLocationsNextRequest = wrappedResponse.getNextRequest();
                    if (mLocationsNextRequest == null) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Error loading more data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
