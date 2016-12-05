package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.KeyValueListAdapter;
import com.rhlabs.circle.models.KeyValueData;
import com.rhlabs.circle.utils.BusProvider;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 6/12/15.
 */
public class KeyValueListView extends CardContentView {

    @InjectView(R.id.lvKeyValueData)
    ListView mKeyValueDataListView;

    private KeyValueListAdapter mKeyValueListAdapter;
    private Context mContext;

    public KeyValueListView(Context context) {
        super(context);
        init(context);
    }

    public KeyValueListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyValueListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KeyValueListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_keyvalue_list, this, true);
        ButterKnife.inject(this);
        mContext = context;
        mKeyValueListAdapter = new KeyValueListAdapter(context, new ArrayList<KeyValueData>());
        mKeyValueDataListView.setAdapter(mKeyValueListAdapter);
        mKeyValueDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BusProvider.getMainBus().post(mKeyValueListAdapter.getItem(position));
            }
        });
    }

    @Override
    public void setData(ArrayList<?> data) {
        ArrayList<KeyValueData> keyValueDataArrayList = new ArrayList<>();
        keyValueDataArrayList.addAll((ArrayList<? extends KeyValueData>) data);
        int numberOfItems = keyValueDataArrayList.size();

        ViewGroup.LayoutParams layoutParams = mKeyValueDataListView.getLayoutParams();
        int totalDividerHeight = mKeyValueDataListView.getDividerHeight() * numberOfItems;
        layoutParams.height = (int)(mContext.getResources().getDimension(R.dimen.key_value_data_row_height) * numberOfItems) + totalDividerHeight;
        mKeyValueDataListView.setLayoutParams(layoutParams);
        mKeyValueDataListView.requestLayout();

        mKeyValueListAdapter.clear();
        mKeyValueListAdapter.addAll(keyValueDataArrayList);
    }
}
