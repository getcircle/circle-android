package com.rhlabs.circle.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.MembershipActionEvent;
import com.rhlabs.circle.services.GroupService;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.protobufs.services.group.actions.respond_to_membership_request.RespondToMembershipRequestRequestV1;
import com.rhlabs.protobufs.services.group.containers.GroupV1;
import com.rhlabs.protobufs.services.group.containers.MembershipRequestV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RetrofitError;

/**
 * Created by anju on 7/1/15.
 */
public class GroupMembershipRequestListAdapter extends ArrayAdapter<MembershipRequestV1> {

    private Context mContext;
    private Map<String, ProfileV1> mProfiles = new HashMap<>();
    private Map<String, GroupV1> mGroups = new HashMap<>();

    public GroupMembershipRequestListAdapter(Context context, ArrayList<MembershipRequestV1> requests) {
        super(context, R.layout.view_group_membership_request_item, requests);
        mContext = context;
    }

    static class ViewHolder {
        @InjectView(R.id.tvRequestMessage)
        TextView requestMessageTextView;

        @InjectView(R.id.btnApprove)
        Button approveButton;

        @InjectView(R.id.btnDeny)
        Button denyButton;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public void setProfiles(List<ProfileV1> profiles) {
        if (profiles != null && profiles.size() > 0) {
            for (ProfileV1 profile : profiles) {
                mProfiles.put(profile.id, profile);
            }
        }
    }

    public void setGroups(List<GroupV1> groups) {
        if (groups != null && groups.size() > 0) {
            for (GroupV1 group : groups) {
                mGroups.put(group.id, group);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder requestViewHolder;
        MembershipRequestV1 request = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_group_membership_request_item, parent, false);
            requestViewHolder = new ViewHolder(convertView);
            convertView.setTag(requestViewHolder);
        } else {
            requestViewHolder = (ViewHolder) convertView.getTag();
        }

        requestViewHolder.approveButton.setTag(R.id.tag_key_position, position);
        requestViewHolder.approveButton.setTag(R.id.tag_key_group_request_action, "approve");

        requestViewHolder.denyButton.setTag(R.id.tag_key_position, position);
        requestViewHolder.denyButton.setTag(R.id.tag_key_group_request_action, "deny");

        requestViewHolder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveOrDenyButtonTapped(view);
            }
        });

        requestViewHolder.denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveOrDenyButtonTapped(view);
            }
        });

        String personName = request.requester_profile_id;
        if (mProfiles.get(request.requester_profile_id) != null) {
            personName = mProfiles.get(request.requester_profile_id).full_name;
        }

        String groupName = request.group_id;
        if (mGroups.get(request.group_id) != null) {
            groupName = mGroups.get(request.group_id).name;
        }

        String requestMessage = String.format("<b>%s</b> requested to join <b>%s</b> group.", personName, groupName);
        requestViewHolder.requestMessageTextView.setText(Html.fromHtml(requestMessage));

        return convertView;
    }

    private void approveOrDenyButtonTapped(final View view) {
        final Button actionButton = (Button) view;
        actionButton.setEnabled(false);

        final int position = (int) view.getTag(R.id.tag_key_position);
        final String action = (String) view.getTag(R.id.tag_key_group_request_action);

        RespondToMembershipRequestRequestV1.ResponseActionV1 responseAction = RespondToMembershipRequestRequestV1.ResponseActionV1.APPROVE;
        if (action.equals("deny")) {
            responseAction = RespondToMembershipRequestRequestV1.ResponseActionV1.DENY;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "", "", true, false);
        final MembershipRequestV1 request = getItem(position);
        final RespondToMembershipRequestRequestV1.ResponseActionV1 finalResponseAction = responseAction;
        GroupService.respondToMembershipRequest(
                request.id,
                responseAction,
                new GroupService.RespondToMembershipRequestCallback() {
                    @Override
                    public void success(Map<String, String> errors) {
                        progressDialog.dismiss();
                        if (errors == null || errors.size() == 0) {
                            GroupMembershipRequestListAdapter.this.remove(request);
                            notifyDataSetChanged();
                            BusProvider.getMainBus().post(new MembershipActionEvent(
                                    request,
                                    finalResponseAction,
                                    getCount()
                            ));
                        } else {
                            Toast.makeText(
                                    mContext,
                                    "Error approving group membership request. Please try again.",
                                    Toast.LENGTH_SHORT
                            ).show();
                            actionButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                mContext,
                                "Error approving group membership request. Please try again.",
                                Toast.LENGTH_SHORT
                        ).show();
                        actionButton.setEnabled(true);
                    }
                });
    }
}
