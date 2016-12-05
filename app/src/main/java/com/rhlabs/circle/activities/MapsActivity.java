package com.rhlabs.circle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rhlabs.circle.R;
import com.rhlabs.circle.utils.LocationUtils;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MapsActivity extends BaseActivity {

    private GoogleMap mMap;
    private LocationV1 mLocation;

    @InjectView(R.id.tvLocationAddress)
    TextView mLocationAddressTextView;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_maps);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        mLocation = (LocationV1) intent.getSerializableExtra(INTENT_ARG_LOCATION);
        if (mLocation == null) {
            Toast.makeText(this, "Location cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            setToolbarAndTitle(mToolbar, mLocation.name);
        }

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mMap = googleMap;
                            // Check if we were successful in obtaining the map.
                            if (mMap != null) {
                                setUpMap();
                            }
                        }
                    });
        }
    }

    private void setUpMap() {
        if (mMap != null && mLocation != null) {
            LatLng position = new LatLng(Double.parseDouble(mLocation.latitude), Double.parseDouble(mLocation.longitude));
            mMap.addMarker(new MarkerOptions().position(position).title(LocationUtils.officeName(mLocation)));
            mLocationAddressTextView.setText(LocationUtils.fullAddress(mLocation));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 12);
            mMap.moveCamera(cameraUpdate);
        }
    }
}
