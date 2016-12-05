package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.utils.LocationUtils;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 6/10/15.
 */
public class LocationAddressView extends CardContentView {

    @InjectView(R.id.tvAddressStreet)
    TextView mStreetAddressTextView;

    @InjectView(R.id.tvAddressCityState)
    TextView mCityStateAddressTextView;

    public LocationAddressView(Context context) {
        super(context);
        init(context);
    }

    public LocationAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LocationAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LocationAddressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) layoutInflater.inflate(R.layout.view_location_address, this, true);
        ButterKnife.inject(this, view);
//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (mAddress != null) {
//                    BusProvider.getMainBus().post(mAddress);
//                }
//            }
//        });
    }

    @Override
    public void setData(ArrayList<?> data) {

        if (data.size() > 0 && data.get(0) instanceof LocationV1) {
            LocationV1 locationV1 = (LocationV1) data.get(0);
            mStreetAddressTextView.setText(LocationUtils.shortOfficeAddress(locationV1));
            mCityStateAddressTextView.setText(LocationUtils.cityRegionPostalCode(locationV1, false));
        }
    }
}
