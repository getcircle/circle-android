package com.rhlabs.circle.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.utils.DateUtils;
import com.rhlabs.circle.views.CircleImageView;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 5/8/15.
 */
public class ProfileListAdapter extends ArrayAdapter {

    private Context mContext;
    private Card.CardType mCardType;

    public ProfileListAdapter(Context context, ArrayList objects) {
        super(context, R.layout.view_profile_item, objects);
        mContext = context;
    }

    static class ViewHolder {

        @InjectView(R.id.tvProfileName)
        TextView profileNameTextView;

        @InjectView(R.id.tvProfileSecondaryInfo)
        TextView profileSecondaryInfoTextView;

        @InjectView(R.id.civProfileImageView)
        CircleImageView profileImageView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        // Set and cache the view holder
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_profile_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Object object = getItem(position);
        if (object instanceof ProfileV1) {
            setProfile(viewHolder, (ProfileV1) object);
        } else if (object instanceof TeamV1) {
            setTeam(viewHolder, (TeamV1) object);
        } else if (object instanceof LocationV1) {
            setLocation(viewHolder, (LocationV1) object);
        }

        return convertView;
    }

    public void setCardType(Card.CardType cardType) {
        mCardType = cardType;
    }

    private void setLocation(ViewHolder viewHolder, LocationV1 locationV1) {
        String numberOfPeopleString = mContext.getString(R.string.number_of_people_singular);
        if (locationV1.profile_count != null && locationV1.profile_count > 1) {
            numberOfPeopleString = String.format(
                    mContext.getString(R.string.number_of_people_plural),
                    locationV1.profile_count
            );
        }

        viewHolder.profileImageView.setLocation(locationV1);
        viewHolder.profileNameTextView.setText(locationV1.name);
        viewHolder.profileSecondaryInfoTextView.setText(numberOfPeopleString);
    }

    private void setTeam(ViewHolder viewHolder, TeamV1 teamV1) {
        String secondaryInfoString = "";

        if (teamV1.child_team_count != null && teamV1.child_team_count > 0) {
            if (teamV1.child_team_count == 1) {
                secondaryInfoString += mContext.getString(R.string.number_of_teams_singular);
            }
            else {
                secondaryInfoString += String.format(
                        mContext.getString(R.string.number_of_teams_plural),
                        teamV1.child_team_count
                );
            }

            secondaryInfoString += ", ";
        }

        if (teamV1.profile_count != null && teamV1.profile_count > 0) {
            if (teamV1.profile_count == 1) {
                secondaryInfoString += mContext.getString(R.string.number_of_people_singular);
            }
            else {
                secondaryInfoString += String.format(
                        mContext.getString(R.string.number_of_people_plural),
                        teamV1.profile_count
                );
            }
        }

        viewHolder.profileImageView.lettersView.setVisibility(View.INVISIBLE);
        viewHolder.profileImageView.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        viewHolder.profileImageView.imageView.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ic_feedreports)
        );
        viewHolder.profileNameTextView.setText(teamV1.name);
        viewHolder.profileSecondaryInfoTextView.setText(secondaryInfoString);
    }

    private void setProfile(final ViewHolder viewHolder, ProfileV1 profileV1) {
        viewHolder.profileImageView.setProfile(profileV1);

        String secondaryInfo = profileV1.title;
        if (mCardType != null) {
            switch (mCardType) {
                case Birthdays:
                    secondaryInfo = DateUtils.getFormattedBirthDate(profileV1.birth_date);
                    break;

                case NewHires:
                    secondaryInfo = DateUtils.getFormattedNewHireDate(profileV1.hire_date);
                    break;

                case Anniversaries:
                    secondaryInfo = DateUtils.getFormattedWorkAnniversaryDate(profileV1.hire_date);
                    break;

                default:
                    break;
            }
        }

        viewHolder.profileNameTextView.setText(profileV1.full_name);
        viewHolder.profileSecondaryInfoTextView.setText(secondaryInfo);
    }
}