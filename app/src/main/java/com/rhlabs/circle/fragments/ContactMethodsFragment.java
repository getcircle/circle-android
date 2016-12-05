package com.rhlabs.circle.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.activities.BaseActivity;
import com.rhlabs.circle.adapters.ContactMethodsListAdapter;
import com.rhlabs.circle.models.ProfileContactMethod;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.protobufs.services.profile.containers.ContactMethodV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 7/10/15.
 */
public class ContactMethodsFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.lvContactMethods)
    ListView mContactMethodsListView;

    private ProfileV1 mProfile;
    private ArrayList<ContactMethodV1> mContactMethods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mProfile = (ProfileV1) getArguments().getSerializable(BaseActivity.INTENT_ARG_PROFILE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_methods, container, true);
        ButterKnife.inject(this, rootView);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContactMethods = new ArrayList<>();
        ContactMethodV1 workEmailContactMethod = new ContactMethodV1.Builder()
                .label(getResources().getString(R.string.generic_email_label))
                .value(mProfile.email)
                .contact_method_type(ContactMethodV1.ContactMethodTypeV1.EMAIL)
                .build();

        mContactMethods.add(workEmailContactMethod);
        mContactMethods.addAll(mProfile.contact_methods);
        ContactMethodsListAdapter contactMethodsListAdapter = new ContactMethodsListAdapter(
                getActivity(),
                mContactMethods
        );
        mContactMethodsListView.setAdapter(contactMethodsListAdapter);
        mContactMethodsListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ContactMethodV1 contactMethod = mContactMethods.get(i);
        ProfileContactMethod profileContactMethod = new ProfileContactMethod(
                contactMethod.contact_method_type,
                mProfile
        );
        BusProvider.getMainBus().post(profileContactMethod);
    }
}
