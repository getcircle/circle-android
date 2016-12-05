package com.rhlabs.circle.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.ProfileContactMethod;
import com.rhlabs.circle.utils.BusProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 7/9/15.
 */
public class ContactsBarView extends CardContentView implements View.OnClickListener {

    @InjectView(R.id.btnContact1)
    FrameLayout mContactButton1;

    @InjectView(R.id.btnContact2)
    FrameLayout mContactButton2;

    @InjectView(R.id.btnContact3)
    FrameLayout mContactButton3;

    @InjectView(R.id.btnContact4)
    FrameLayout mContactButton4;

    @InjectView(R.id.ivContact1)
    ImageView mContactImage1;

    @InjectView(R.id.ivContact2)
    ImageView mContactImage2;

    @InjectView(R.id.ivContact3)
    ImageView mContactImage3;

    @InjectView(R.id.ivContact4)
    ImageView mContactImage4;

    List<ProfileContactMethod> mProfileContactMethods;

    public ContactsBarView(Context context) {
        super(context);
        init(context);
    }

    public ContactsBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContactsBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ContactsBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout view = (LinearLayout) layoutInflater.inflate(R.layout.view_contactsbar, this, true);
        ButterKnife.inject(this, view);
        mContactButton1.setOnClickListener(this);
        mContactButton2.setOnClickListener(this);
        mContactButton3.setOnClickListener(this);
        mContactButton4.setOnClickListener(this);
    }

    @Override
    public void setData(ArrayList<?> data) {
        List<FrameLayout> contactButtons = new ArrayList<>(Arrays.asList(
                mContactButton1,
                mContactButton2,
                mContactButton3,
                mContactButton4
        ));
        List<ImageView> contactImages = new ArrayList<>(Arrays.asList(
                mContactImage1,
                mContactImage2,
                mContactImage3,
                mContactImage4
        ));
        int currentIndex = 0;
        for (ImageView contactImage: contactImages) {
            contactImage.setVisibility(INVISIBLE);
        }

        for (FrameLayout contactButton: contactButtons) {
            contactButton.setVisibility(INVISIBLE);
        }

        List<ProfileContactMethod> profileContactMethods = (ArrayList<ProfileContactMethod>) data;
        mProfileContactMethods = profileContactMethods;
        for (ProfileContactMethod profileContactMethod: profileContactMethods) {
            if (currentIndex < contactImages.size()) {
                contactImages.get(currentIndex).setImageResource(profileContactMethod.getContactMethodImage());
                contactImages.get(currentIndex).setTag(currentIndex);
                contactImages.get(currentIndex).setVisibility(VISIBLE);

                contactButtons.get(currentIndex).setTag(currentIndex);
                contactButtons.get(currentIndex).setVisibility(VISIBLE);
            }

            currentIndex++;
        }
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        if (mProfileContactMethods != null && position < mProfileContactMethods.size() ) {
            BusProvider.getMainBus().post(mProfileContactMethods.get(position));
        }
    }
}
