package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.ProfileImagesData;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 4/16/15.
 */
public class ProfilesGroupView extends CardContentView {

    private ProfileImagesData mProfileImagesData;

    @InjectView(R.id.ivProfileImage1)
    CircleImageView mCircleImageView1;

    @InjectView(R.id.ivProfileImage2)
    CircleImageView mCircleImageView2;

    @InjectView(R.id.ivProfileImage3)
    CircleImageView mCircleImageView3;

    @InjectView(R.id.ivProfileImage4)
    CircleImageView mCircleImageView4;

    public ProfilesGroupView(Context context) {
        super(context);
        init(context);
    }

    public ProfilesGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProfilesGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfilesGroupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.view_profiles_group, this, true);
        ButterKnife.inject(rootView, this);
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProfileImagesData != null) {
                    BusProvider.getMainBus().post(mProfileImagesData);
                }
            }
        });
    }

    @Override
    public void setData(ArrayList<?> data) {

        ArrayList<CircleImageView> imageViews = new ArrayList<>(Arrays.asList(mCircleImageView1, mCircleImageView2, mCircleImageView3, mCircleImageView4));
        Integer currentIndex = 0;

        for (CircleImageView imageView : imageViews) {
            imageView.setVisibility(INVISIBLE);
        }

        List<ProfileImagesData> profileImagesDataList = (ArrayList<ProfileImagesData>) data;
        if (profileImagesDataList != null && profileImagesDataList.size() > 0) {
            mProfileImagesData = profileImagesDataList.get(0);
            if (mProfileImagesData != null) {
                for (ProfileV1 profileV1 : mProfileImagesData.getProfiles()) {
                    if (currentIndex < imageViews.size()) {
                        imageViews.get(currentIndex).setVisibility(VISIBLE);
                        imageViews.get(currentIndex).setProfile(profileV1);
                    } else {
                        break;
                    }

                    currentIndex++;
                }
            }
        }
    }
}
