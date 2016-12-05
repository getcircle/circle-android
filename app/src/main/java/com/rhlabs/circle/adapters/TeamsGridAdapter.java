package com.rhlabs.circle.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.utils.CircleColor;
import com.rhlabs.circle.utils.DrawableUtils;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 5/19/15.
 */
public class TeamsGridAdapter extends ArrayAdapter<TeamV1> {
    private Context mContext;

    public TeamsGridAdapter(Context context, ArrayList<TeamV1> teams) {
        super(context, R.layout.view_team_item, teams);
        mContext = context;
    }

    static class TeamViewHolder {
        @InjectView(R.id.tvTeamName)
        TextView teamNameTextView;
        @InjectView(R.id.tvTeamNameLetter)
        TextView teamNameLetterTextView;

        public TeamViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamViewHolder teamViewHolder;
        TeamV1 teamV1 = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_team_item, parent, false);
            teamViewHolder = new TeamViewHolder(convertView);
            convertView.setTag(teamViewHolder);
        } else {
            teamViewHolder = (TeamViewHolder) convertView.getTag();
        }

        GradientDrawable gradientDrawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.circle_view);
        if (gradientDrawable != null) {
            gradientDrawable.setColor(CircleColor.appTeamBackgroundColor(teamV1));
            DrawableUtils.setBackgroudDrawable(teamViewHolder.teamNameLetterTextView, gradientDrawable);
        }

        teamViewHolder.teamNameTextView.setText(teamV1.name);
        teamViewHolder.teamNameLetterTextView.setText(teamV1.name.substring(0, 1));
        return convertView;
    }
}
