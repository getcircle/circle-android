package com.rhlabs.circle.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.KeyValueData;

import java.util.ArrayList;

/**
 * Created by anju on 6/12/15.
 */
public class KeyValueListAdapter extends ArrayAdapter<KeyValueData> {
    private Context mContext;

    public KeyValueListAdapter(Context context, ArrayList<KeyValueData> keyValueDataArrayList) {
        super(context, R.layout.view_key_value_item, keyValueDataArrayList);
        mContext = context;
    }

    static class KeyValueViewHolder {
        TextView keyNameTextView;
        TextView valueTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KeyValueViewHolder keyValueViewHolder;
        KeyValueData keyValueData = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_key_value_item, parent, false);
            keyValueViewHolder = new KeyValueViewHolder();
            keyValueViewHolder.keyNameTextView = (TextView) convertView.findViewById(R.id.tvKeyNameLabel);
            keyValueViewHolder.valueTextView = (TextView) convertView.findViewById(R.id.tvKeyValueText);
            convertView.setTag(keyValueViewHolder);
        }
        else {
            keyValueViewHolder = (KeyValueViewHolder) convertView.getTag();
        }

        keyValueViewHolder.keyNameTextView.setText(keyValueData.getKeyLabel());
        keyValueViewHolder.valueTextView.setText("" + keyValueData.getValue());
        if (keyValueData.getValueLabelGravity() != -1) {
            keyValueViewHolder.valueTextView.setGravity(keyValueData.getValueLabelGravity());
        }
        else {
            keyValueViewHolder.valueTextView.setGravity(Gravity.END);
        }

        if (keyValueData.getValueLabelTextColor() != -1) {
            keyValueViewHolder.valueTextView.setTextColor(keyValueData.getValueLabelTextColor());
        }
        else {
            keyValueViewHolder.valueTextView.setTextColor(mContext.getResources().getColor(R.color.app_dark_text_color));
        }

        return convertView;
    }

}
