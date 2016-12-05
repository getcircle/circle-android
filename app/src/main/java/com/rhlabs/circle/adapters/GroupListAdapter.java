package com.rhlabs.circle.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.services.GroupService;
import com.rhlabs.circle.utils.ListViewUtils;
import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.services.group.containers.MembershipRequestStatusV1;
import com.rhlabs.protobufs.services.group.containers.MembershipRequestV1;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

/**
 * Created by anju on 6/29/15.
 */
public class GroupListAdapter extends ArrayAdapter<GroupV1> {

    private Context mContext;

    public GroupListAdapter(Context context, ArrayList<GroupV1> groups) {
        super(context, R.layout.view_group_item, groups);
        mContext = context;
    }

    private ListView mListView;

    static class ViewHolder {

        @InjectView(R.id.tvGroupName)
        TextView groupNameTextView;

        @InjectView(R.id.tvGroupMembersCount)
        TextView groupMembersCountTextView;

        @InjectView(R.id.tvGroupDescription)
        TextView groupDescriptionTextView;

        @InjectView(R.id.btnRequestToJoin)
        Button requestToJoinButton;

        @InjectView(R.id.pbProgressBar)
        ProgressBar progressBar;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        mListView = (ListView) parent;
        // Set and cache the view holder
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_group_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GroupV1 group = getItem(position);
        setGroup(viewHolder, group, position);

        return convertView;
    }

    private void setGroup(final ViewHolder viewHolder, GroupV1 groupV1, int position) {

        // Name and Member Count
        viewHolder.groupNameTextView.setText(groupV1.name);
        if (groupV1.members_count == 1) {
            viewHolder.groupMembersCountTextView.setText(groupV1.members_count + "" + " member");
        } else {
            viewHolder.groupMembersCountTextView.setText(groupV1.members_count + "" + " members");
        }

        // Description
        if (groupV1.group_description != null && !groupV1.group_description.isEmpty()) {
            viewHolder.groupDescriptionTextView.setText(groupV1.group_description);
            viewHolder.groupDescriptionTextView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.groupDescriptionTextView.setVisibility(View.GONE);
        }

        // Request to join button
        viewHolder.requestToJoinButton.setTag(position);
        viewHolder.requestToJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestToJoinButtonTapped(view, viewHolder.progressBar);
            }
        });

        if (!groupV1.is_member) {
            if (groupV1.has_pending_request) {
                viewHolder.requestToJoinButton.setText(R.string.group_access_request_pending);
                viewHolder.requestToJoinButton.setEnabled(false);
                viewHolder.requestToJoinButton.setVisibility(View.VISIBLE);
                viewHolder.requestToJoinButton.setAllCaps(false);
            } else if (groupV1.can_join) {
                viewHolder.requestToJoinButton.setText(R.string.join_group_button_title);
                viewHolder.requestToJoinButton.setEnabled(true);
                viewHolder.requestToJoinButton.setVisibility(View.VISIBLE);
                viewHolder.requestToJoinButton.setAllCaps(true);
            } else if (groupV1.can_request) {
                viewHolder.requestToJoinButton.setText(R.string.request_to_join_group_button_title);
                viewHolder.requestToJoinButton.setEnabled(true);
                viewHolder.requestToJoinButton.setVisibility(View.VISIBLE);
                viewHolder.requestToJoinButton.setAllCaps(true);
            } else {
                viewHolder.requestToJoinButton.setVisibility(View.GONE);
            }
        } else {
            viewHolder.requestToJoinButton.setVisibility(View.GONE);
        }
    }

    private void requestToJoinButtonTapped(final View view, final ProgressBar progressView) {
        final Button requestToJoinButton = (Button) view;
        requestToJoinButton.setEnabled(false);
        progressView.setVisibility(View.VISIBLE);

        final int position = (int) view.getTag();
        final GroupV1 groupV1 = getItem(position);
        final GroupV1.Builder updatedGroupBuilder = new GroupV1.Builder(groupV1);
        GroupService.joinGroup(groupV1, new GroupService.JoinGroupCallback() {
            @Override
            public void success(MembershipRequestV1 request, Map<String, String> errors) {
                progressView.setVisibility(View.INVISIBLE);
                if (request != null) {
                    if (request.status == MembershipRequestStatusV1.APPROVED) {
                        Snackbar.make(view, mContext.getString(R.string.group_join_confirmation), Snackbar.LENGTH_SHORT).show();
                        requestToJoinButton.setVisibility(View.INVISIBLE);
                        updatedGroupBuilder.is_member(true);
                        GroupV1 updatedGroup = updatedGroupBuilder.build();
                        GroupListAdapter.this.remove(groupV1);
                        GroupListAdapter.this.insert(updatedGroup, position);
                        ListViewUtils.updateRowWithObject(mListView, updatedGroup);
                    } else if (request.status == MembershipRequestStatusV1.PENDING) {
                        Snackbar.make(view, mContext.getString(R.string.group_access_request_submitted), Snackbar.LENGTH_SHORT).show();
                        requestToJoinButton.setVisibility(View.INVISIBLE);
                        updatedGroupBuilder.has_pending_request(true);
                        GroupV1 updatedGroup = updatedGroupBuilder.build();
                        GroupListAdapter.this.remove(groupV1);
                        GroupListAdapter.this.insert(updatedGroup, position);
                        ListViewUtils.updateRowWithObject(mListView, updatedGroup);
                    }
                } else {
                    Toast.makeText(mContext, "Error joining group.", Toast.LENGTH_SHORT).show();
                    requestToJoinButton.setEnabled(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Error joining group.", Toast.LENGTH_SHORT).show();
                requestToJoinButton.setEnabled(true);
            }
        });
    }
}
