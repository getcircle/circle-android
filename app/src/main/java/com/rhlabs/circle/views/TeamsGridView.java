package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.TeamsGridAdapter;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.protobufs.services.organization.containers.TeamV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 5/19/15.
 */
public class TeamsGridView extends CardContentView {

    @InjectView(R.id.gvTeams) GridView mTeamsGridView;

    private TeamsGridAdapter mTeamsGridAdapter;
    private Context mContext;

    public TeamsGridView(Context context) {
        super(context);
        init(context);
    }

    public TeamsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TeamsGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TeamsGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    public void setData(ArrayList<?> data) {
        ArrayList<TeamV1> teams = new ArrayList<>();
        teams.addAll((ArrayList<? extends TeamV1>) data);
        int numberOfTeams = teams.size();

        ViewGroup.LayoutParams layoutParams = mTeamsGridView.getLayoutParams();
        layoutParams.height = (int)(mContext.getResources().getDimension(R.dimen.team_item_row_height) * (1 + ((numberOfTeams - 1) / 3)));
        mTeamsGridView.setLayoutParams(layoutParams);
        mTeamsGridView.requestLayout();

        mTeamsGridAdapter.clear();
        mTeamsGridAdapter.addAll(teams);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_teams_grid, this, true);
        ButterKnife.inject(this);
        mContext = context;
        mTeamsGridAdapter = new TeamsGridAdapter(context, new ArrayList<TeamV1>());
        mTeamsGridView.setAdapter(mTeamsGridAdapter);
        mTeamsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusProvider.getMainBus().post(mTeamsGridAdapter.getItem(position));
            }
        });
    }
}
