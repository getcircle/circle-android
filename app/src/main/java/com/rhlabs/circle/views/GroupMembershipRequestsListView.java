package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.GroupMembershipRequestListAdapter;
import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.services.group.containers.MembershipRequestV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 7/1/15.
 */
public class GroupMembershipRequestsListView  extends CardContentView {

    @InjectView(R.id.lvGroupMembershipRequests)
    ListView mGroupMembershipRequestsListView;

    private GroupMembershipRequestListAdapter mGroupMembershipRequestListAdapter;
    private Context mContext;

    public GroupMembershipRequestsListView(Context context) {
        super(context);
        init(context);
    }

    public GroupMembershipRequestsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GroupMembershipRequestsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GroupMembershipRequestsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    public void setMetaData(Map<String, Object> metaData) {
        if (metaData != null && metaData.size() > 0) {
            List<ProfileV1> profiles = (List<ProfileV1>) metaData.get("profiles");
            List<GroupV1> groups = (List<GroupV1>) metaData.get("groups");
            mGroupMembershipRequestListAdapter.setProfiles(profiles);
            mGroupMembershipRequestListAdapter.setGroups(groups);
        }
    }

    @Override
    public void setData(ArrayList<?> data) {
        ArrayList<MembershipRequestV1> requests = new ArrayList<>();
        requests.addAll((ArrayList<? extends MembershipRequestV1>) data);
        int numberOfRequests = requests.size();

        ViewGroup.LayoutParams layoutParams = mGroupMembershipRequestsListView.getLayoutParams();
        int totalDividerHeight = mGroupMembershipRequestsListView.getDividerHeight() * numberOfRequests;
        layoutParams.height = (int)(mContext.getResources().getDimension(R.dimen.request_item_row_height) * numberOfRequests) + totalDividerHeight;
        mGroupMembershipRequestsListView.setLayoutParams(layoutParams);
        mGroupMembershipRequestsListView.requestLayout();

        mGroupMembershipRequestListAdapter.clear();
        mGroupMembershipRequestListAdapter.addAll(requests);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_group_membership_request_list, this, true);
        ButterKnife.inject(this);
        mContext = context;
        mGroupMembershipRequestListAdapter = new GroupMembershipRequestListAdapter(context, new ArrayList<MembershipRequestV1>());
        mGroupMembershipRequestsListView.setAdapter(mGroupMembershipRequestListAdapter);
    }
}