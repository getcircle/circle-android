package com.rhlabs.circle.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.SettingsListItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 7/19/15.
 */
public class SettingsListAdapter extends ArrayAdapter<SettingsListItem> {
    private Context mContext;

    public SettingsListAdapter(Context context, ArrayList<SettingsListItem> settingsListItems) {
        super(context, R.layout.view_setting_item, settingsListItems);
        mContext = context;
    }

    static class SettingsViewHolder {
        @InjectView(R.id.tvSettingsValue)
        TextView settingNameTextView;

        public SettingsViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        SettingsListItem settingListItem = getItem(position);
        return settingListItem.getViewType() == SettingsListItem.ViewType.Data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingsViewHolder settingViewHolder;
        SettingsListItem settingListItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.view_setting_item, parent, false);
            settingViewHolder = new SettingsViewHolder(convertView);
            convertView.setTag(settingViewHolder);
        }
        else {
            settingViewHolder = (SettingsViewHolder) convertView.getTag();
        }

        // Customize views
        if (settingListItem.getViewType() == SettingsListItem.ViewType.Data) {
            convertView.setBackgroundColor(Color.WHITE);
            settingViewHolder.settingNameTextView.setAllCaps(false);
            settingViewHolder.settingNameTextView.setTextColor(
                    mContext.getResources().getColor(R.color.app_dark_text_color)
            );
        }
        else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
            settingViewHolder.settingNameTextView.setAllCaps(true);
            settingViewHolder.settingNameTextView.setTextColor(
                    mContext.getResources().getColor(R.color.app_dark_text_color_secondary_info)
            );
        }

        switch (settingListItem.getDataType()) {
            case DisconnectAccount:
                settingViewHolder.settingNameTextView.setGravity(Gravity.CENTER);
                settingViewHolder.settingNameTextView.setTextColor(Color.RED);
                break;

            case SignOut:
                settingViewHolder.settingNameTextView.setGravity(Gravity.CENTER);
                break;

            default:
                settingViewHolder.settingNameTextView.setGravity(Gravity.START|Gravity.CENTER);
                break;
        }

        settingViewHolder.settingNameTextView.setText(settingListItem.getValue());
        return convertView;
    }
}