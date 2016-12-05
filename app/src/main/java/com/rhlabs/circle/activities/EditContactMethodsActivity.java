package com.rhlabs.circle.activities;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.common.AppPreferences;
import com.rhlabs.protobufs.services.profile.containers.ContactMethodV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 7/21/15.
 */
public class EditContactMethodsActivity extends BaseActivity {

    @InjectView(R.id.btnCancel)
    Button mCancelButton;

    @InjectView(R.id.btnNext)
    Button mDoneButton;

    @InjectView(R.id.tvViewTitle)
    TextView mViewTitleTextView;

    @InjectView(R.id.llContactMethodsFormContainer)
    LinearLayout mFormsContainer;

    List<ContactMethodFormSection> mFormSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_methods);
        ButterKnife.inject(this);
        configureCustomToolbar();
        configureForm();
    }

    private void configureCustomToolbar() {
        mViewTitleTextView.setText(getString(R.string.edit_contact_preferences));
        mDoneButton.setText(getString(R.string.generic_done_button_title));
        mCancelButton.setVisibility(View.VISIBLE);
    }

    private void configureForm() {
        ProfileV1 profile = AppPreferences.getLoggedInUserProfile();
        if (profile != null) {

            mFormSections = new ArrayList<>();

            ArrayList<ContactMethodFormItem> items = new ArrayList<>();

            // Phone numbers section
            // Cell phone
            ContactMethodFormItem cellPhoneItem = new ContactMethodFormItem(
                    getString(R.string.cell_phone_field_hint),
                    ContactMethodV1.ContactMethodTypeV1.CELL_PHONE
            );
            items.add(cellPhoneItem);

            // Work phone
            ContactMethodFormItem workPhoneItem = new ContactMethodFormItem(
                    getString(R.string.work_phone_field_hint),
                    ContactMethodV1.ContactMethodTypeV1.PHONE
            );
            items.add(workPhoneItem);
            ContactMethodFormSection phoneNumbersSection = new ContactMethodFormSection(
                    getString(R.string.phone_number_section_title),
                    R.drawable.ic_call,
                    items
            );
            mFormSections.add(phoneNumbersSection);

            // Emails section
            items.clear();

            // Work email
            ContactMethodFormItem workEmailItem = new ContactMethodFormItem(
                    getString(R.string.work_email_field_hint),
                    ContactMethodV1.ContactMethodTypeV1.EMAIL
            );
            workEmailItem.setEditable(false);
            items.add(workEmailItem);

            // Personal email
            ContactMethodFormItem personalEmailItem = new ContactMethodFormItem(
                    getString(R.string.personal_email_field_hint),
                    ContactMethodV1.ContactMethodTypeV1.EMAIL
            );
            items.add(personalEmailItem);

            ContactMethodFormSection emailsSection = new ContactMethodFormSection(
                    getString(R.string.emails_section_title),
                    R.drawable.ic_email,
                    items
            );
            mFormSections.add(emailsSection);

            // Add values
            HashMap<ContactMethodV1.ContactMethodTypeV1, String> contactMethodValues = new HashMap<>();
            for (ContactMethodV1 contactMethodV1 : profile.contact_methods) {
                contactMethodValues.put(contactMethodV1.contact_method_type, contactMethodV1.value);
            }

            for (ContactMethodFormSection section: mFormSections) {
                for (ContactMethodFormItem item: section.getItems()) {
                    item.setValue(contactMethodValues.get(item.getContactMethodTypeV1()));

                    if (item.getHintText().equals(getString(R.string.work_email_field_hint))) {
                        item.setEditable(false);
                        item.setValue(profile.email);
                    }
                }
            }

            // Render form
            renderForm();
        }
    }

    private void renderForm() {

        for (ContactMethodFormSection section: mFormSections) {

            View contactMethodSectionView = LayoutInflater.from(this).inflate(
                    R.layout.view_contact_method_section,
                    mFormsContainer,
                    false
            );

            ImageView sectionImageView = (ImageView) contactMethodSectionView.findViewById(
                    R.id.ivContactMethodSectionImage
            );
            sectionImageView.setImageResource(section.getImageResource());

            TextView sectionTitleTextView = (TextView) contactMethodSectionView.findViewById(
                    R.id.tvContactMethodSectionTitle
            );
            sectionTitleTextView.setText(section.getSectionTitle());

            mFormsContainer.addView(contactMethodSectionView);

            for (ContactMethodFormItem item : section.getItems()) {


            }
        }
    }

    public class ContactMethodFormItem {
        private String mHintText;
        private ContactMethodV1.ContactMethodTypeV1 mContactMethodTypeV1;
        private InputType mInputType;
        private boolean mEditable = true;
        private String mValue;

        public ContactMethodFormItem(String hintText, ContactMethodV1.ContactMethodTypeV1 contactMethodTypeV1) {
            mHintText = hintText;
            mContactMethodTypeV1 = contactMethodTypeV1;
        }

        public ContactMethodFormItem(String hintText, ContactMethodV1.ContactMethodTypeV1 contactMethodTypeV1, InputType inputType) {
            mHintText = hintText;
            mContactMethodTypeV1 = contactMethodTypeV1;
            mInputType = inputType;
        }

        public String getHintText() {
            return mHintText;
        }

        public ContactMethodV1.ContactMethodTypeV1 getContactMethodTypeV1() {
            return mContactMethodTypeV1;
        }

        public InputType getInputType() {
            return mInputType;
        }

        public void setInputType(InputType inputType) {
            mInputType = inputType;
        }

        public boolean isEditable() {
            return mEditable;
        }

        public void setEditable(boolean editable) {
            mEditable = editable;
        }

        public String getValue() {
            return mValue;
        }

        public void setValue(String value) {
            mValue = value;
        }
    }

    public class ContactMethodFormSection {
        String mSectionTitle;
        int mImageResource;
        ArrayList<ContactMethodFormItem> mItems;

        public ContactMethodFormSection(String sectionTitle, int imageResource, ArrayList<ContactMethodFormItem> items) {
            mSectionTitle = sectionTitle;
            mImageResource = imageResource;
            mItems = items;
        }

        public String getSectionTitle() {
            return mSectionTitle;
        }

        public int getImageResource() {
            return mImageResource;
        }

        public ArrayList<ContactMethodFormItem> getItems() {
            return mItems;
        }
    }
}
