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
import com.rhlabs.circle.adapters.ProfileListAdapter;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 5/8/15.
 */
public class ProfileListView extends CardContentView {

    @InjectView(R.id.lvProfile) ListView mProfileListView;

    private ProfileListAdapter mProfileListAdapter;
    private Context mContext;

    public ProfileListView(Context context) {
        super(context);
        init(context);
    }

    public ProfileListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProfileListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfileListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    public void setData(ArrayList<?> data) {
        int numberOfProfiles = data.size();

        ViewGroup.LayoutParams layoutParams = mProfileListView.getLayoutParams();
        int totalDividerHeight = mProfileListView.getDividerHeight() * numberOfProfiles;
        layoutParams.height = (int)(mContext.getResources().getDimension(R.dimen.profile_item_row_height) * numberOfProfiles) + totalDividerHeight;
        mProfileListView.setLayoutParams(layoutParams);
        mProfileListView.requestLayout();

        mProfileListAdapter.clear();
        mProfileListAdapter.setCardType(getCardType());
        mProfileListAdapter.addAll(data);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_profile_list, this, true);
        ButterKnife.inject(this);
        mContext = context;
        mProfileListAdapter = new ProfileListAdapter(context, new ArrayList<ProfileV1>());
        mProfileListView.setAdapter(mProfileListAdapter);
        mProfileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BusProvider.getMainBus().post(mProfileListAdapter.getItem(position));
            }
        });
    }

}
