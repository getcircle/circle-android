package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.utils.CircleColor;
import com.rhlabs.circle.utils.DrawableUtils;
import com.rhlabs.circle.utils.ProfileUtils;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 6/14/15.
 */
public class CircleImageView extends RelativeLayout {

    @InjectView(R.id.ivProfileImage)
    public ImageView imageView;

    @InjectView(R.id.tvProfileLetters)
    public TextView lettersView;

    private Context mContext;

    public CircleImageView(Context context) {
        super(context);
        init(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.circle_image_view, this, true);
        ButterKnife.inject(this);
    }

    public void setProfile(ProfileV1 profileV1) {
        // Load image letter view
        GradientDrawable gradientDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.circle_view);
        if (gradientDrawable != null) {
            gradientDrawable.setColor(CircleColor.appProfileBackgroundColor(profileV1));
            DrawableUtils.setBackgroudDrawable(
                    lettersView,
                    gradientDrawable
            );
        }

        lettersView.setText(
                profileV1.first_name.substring(0, 1) + profileV1.last_name.substring(0, 1)
        );
        lettersView.setVisibility(View.INVISIBLE);
        loadImage(ProfileUtils.getProfileImageURL(profileV1));

    }

    public void setLocation(LocationV1 locationV1) {
        // Load image letter view
        GradientDrawable gradientDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.circle_view);
        if (gradientDrawable != null) {
            gradientDrawable.setColor(CircleColor.appLocationBackgroundColor(locationV1));
            DrawableUtils.setBackgroudDrawable(
                    lettersView,
                    gradientDrawable
            );
        }

        lettersView.setText(locationV1.name.substring(0, 1));
        lettersView.setVisibility(View.INVISIBLE);
        loadImage(locationV1.image_url);
    }

    public void setTeam(TeamV1 teamV1) {
        GradientDrawable gradientDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.circle_view);
        if (gradientDrawable != null) {
            gradientDrawable.setColor(CircleColor.appTeamBackgroundColor(teamV1));
            DrawableUtils.setBackgroudDrawable(lettersView, gradientDrawable);
        }

        lettersView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        lettersView.setText(teamV1.name.substring(0, 1));
    }

    private void loadImage(String imageURL) {
        if (imageURL != null && !imageURL.isEmpty()) {
            Picasso.with(mContext).load(imageURL).fit().centerCrop().into(imageView, new Callback() {

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    lettersView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            lettersView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
