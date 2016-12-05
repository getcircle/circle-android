package com.rhlabs.circle.fragments.detail_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.activities.BaseActivity;
import com.rhlabs.circle.activities.MapsActivity;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.models.KeyValueData;
import com.rhlabs.circle.models.ProfilesAPIResponse;
import com.rhlabs.circle.services.OrganizationService;
import com.rhlabs.circle.services.ProfileService;
import com.rhlabs.circle.utils.BusProvider;
import com.rhlabs.protobufs.services.organization.containers.AddressV1;
import com.rhlabs.protobufs.services.organization.containers.LocationV1;
import com.rhlabs.protobufs.services.profile.containers.ProfileV1;
import com.rhlabs.protobufs.soa.ServiceRequestV1;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by anju on 6/10/15.
 */
public class LocationDetailFragment extends BaseDetailFragment {

    private class LocationData {
        List<ProfileV1> profiles;
        ServiceRequestV1 profilesNextRequest;

        public LocationData(List<ProfileV1> profiles, ServiceRequestV1 profilesNextRequest) {
            this.profiles = profiles;
            this.profilesNextRequest = profilesNextRequest;
        }
    }

    private LocationV1 mLocationV1;
    private LocationData mLocationData;
    private static final String ARG_LOCATION_1 = "location";
    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public static LocationDetailFragment newInstance(LocationV1 locationV1) {
        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION_1, locationV1);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mLocationV1 = (LocationV1) getArguments().getSerializable(ARG_LOCATION_1);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mCompositeSubscription.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getMainBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getMainBus().unregister(this);
    }

    @Override
    protected String getViewTitle() {
        return mLocationV1.name;
    }

    @Override
    protected void loadData() {
        final Observable<LocationData> locationDetailObservable = Observable.zip(
                OrganizationService.getLocation(mLocationV1),
                ProfileService.getProfiles(mLocationV1),
                new Func2<LocationV1, ProfilesAPIResponse, LocationData>() {
                    @Override
                    public LocationData call(final LocationV1 locationV1, final ProfilesAPIResponse profilesAPIResponse) {
                        mLocationV1 = locationV1;
                        return new LocationData(
                                profilesAPIResponse.getProfiles(),
                                profilesAPIResponse.getNextRequest()
                        );
                    }
                }
        );

        mCompositeSubscription.add(
                locationDetailObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<LocationData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(LocationData locationData) {
                                mLocationData = locationData;
                                Log.d("TAG", locationData.toString());
                                loadCards();
                            }
                        })
        );
    }

    private void loadCards() {
        mCards.clear();
        showProgress(false);

        if (mLocationV1 != null && mLocationData != null) {

            // Hero image
            if (mLocationV1.image_url != null && !mLocationV1.image_url.isEmpty()) {
                ImageView backdropImageView = mDetailViewBackdropImageView;
                Picasso.with(getActivity()).load(mLocationV1.image_url).fit().centerCrop().into(backdropImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (mDetailViewBackdropImageOverlayView != null) {
                            mDetailViewBackdropImageOverlayView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            // Address card
            Card addressCard = new Card(Card.CardType.OfficeAddress, null);
            addressCard.setCardFullScreenWidth(true);
            addressCard.addHeader = false;
            List<LocationV1> locations = new ArrayList<>();
            locations.add(mLocationV1);
            addressCard.addContent(locations);
            mCards.add(addressCard);

            // People count card
            Card peopleCountCard = new Card(Card.CardType.KeyValue, null);
            peopleCountCard.setCardFullScreenWidth(true);
            peopleCountCard.addHeader = false;
            List<KeyValueData> keyValueDataList = new ArrayList<>();
            String keyValueTitle = getResources().getString(R.string.location_profile_count_plural_title);
            KeyValueData peopleCountData = new KeyValueData(
                    "people_count",
                    keyValueTitle,
                    "" + mLocationV1.profile_count,
                    KeyValueData.KeyValueDataType.LocationProfileCount
            );
            keyValueDataList.add(peopleCountData);
            peopleCountCard.addContent(keyValueDataList);
            mCards.add(peopleCountCard);

            mAdapter.setCards(mCards);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onKeyValueRowSelected(KeyValueData keyValueData) {
        switch (keyValueData.getKeyValueDataType()) {
            case LocationProfileCount:
                Card locationProfilesCard = new Card(Card.CardType.Profiles, "People @ " + mLocationV1.name);
                locationProfilesCard.addContent(mLocationData.profiles);
                locationProfilesCard.setContentNextRequest(mLocationData.profilesNextRequest);
                ((BaseActivity) getActivity()).onCardHeaderClicked(locationProfilesCard, null);
                break;

            default:
                break;
        }
    }

    @Subscribe
    public void onAddressClicked(AddressV1 address) {
        if (mLocationV1 != null) {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra(BaseActivity.INTENT_ARG_LOCATION, mLocationV1);
            getActivity().startActivity(intent);
        }
    }
}
