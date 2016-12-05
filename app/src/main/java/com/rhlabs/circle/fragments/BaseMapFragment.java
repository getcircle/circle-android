package com.rhlabs.circle.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.rhlabs.circle.R;

/**
 * Created by anju on 6/25/15.
 */
public abstract class BaseMapFragment extends Fragment {

    protected GoogleMap mGoogleMap;
    protected SupportMapFragment mSupportMapFragment;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mGoogleMap == null) {
            mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.locationsMap);
            if (mSupportMapFragment != null) {
                mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        loadMap(googleMap);
                    }
                });

                final View mapView = mSupportMapFragment.getView();
                if (mapView != null) {
                    mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            onMapLayoutComplete();
                        }
                    });
                }
            } else {
                Toast.makeText(getActivity(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void loadMap(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mGoogleMap != null) {
            mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    BaseMapFragment.this.onMapLoaded();
                }
            });

            addLocationMarkers();
        } else {
            Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    protected abstract void addLocationMarkers();

    protected abstract void onMapLoaded();

    protected abstract void onMapLayoutComplete();
}
