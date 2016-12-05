package com.rhlabs.circle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.protobufs.services.profile.containers.ContactMethodV1;

import java.util.ArrayList;

/**
 * Created by anju on 7/10/15.
 */
public class ContactMethodsListAdapter extends ArrayAdapter<ContactMethodV1> {

    private Context mContext;

    public ContactMethodsListAdapter(Context context, ArrayList<ContactMethodV1> contactMethods) {
        super(context, R.layout.view_key_value_item, contactMethods);
        mContext = context;
    }

    static class KeyValueViewHolder {
        TextView keyNameTextView;
        TextView valueTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KeyValueViewHolder keyValueViewHolder;
        ContactMethodV1 contactMethod = getItem(position);

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

        keyValueViewHolder.keyNameTextView.setText(contactMethod.label);
        keyValueViewHolder.valueTextView.setText(contactMethod.value);
        return convertView;
    }
}
