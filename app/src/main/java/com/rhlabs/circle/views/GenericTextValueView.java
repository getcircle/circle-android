package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.rhlabs.circle.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 6/12/15.
 */
public class GenericTextValueView extends CardContentView {

    @InjectView(R.id.tvTextValue)
    TextView mGenericTextView;

    public GenericTextValueView(Context context) {
        super(context);
        init(context);
    }

    public GenericTextValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GenericTextValueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GenericTextValueView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_text_value, this, true);
        ButterKnife.inject(this);
    }

    @Override
    public void setData(ArrayList<?> data) {

        if (data.size() > 0 && data.get(0) instanceof String) {
            String textValue = (String) data.get(0);
            mGenericTextView.setText(textValue);
        }
    }
}
