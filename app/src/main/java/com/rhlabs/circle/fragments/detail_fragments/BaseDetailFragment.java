package com.rhlabs.circle.fragments.detail_fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.CardAdapter;
import com.rhlabs.circle.adapters.DetailViewCardAdapter;
import com.rhlabs.circle.fragments.BaseCardListFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 6/11/15.
 */
public abstract class BaseDetailFragment extends BaseCardListFragment {

    @InjectView(R.id.collapsingToolbar)
    protected CollapsingToolbarLayout mCollapsingToolbarLayout;

    @InjectView(R.id.ivDetailViewBackdropImage)
    protected ImageView mDetailViewBackdropImageView;

    @InjectView(R.id.ivDetailViewBackdropImageOverlay)
    protected FrameLayout mDetailViewBackdropImageOverlayView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);

        String viewTitle = getViewTitle();
        if (viewTitle != null) {
            mCollapsingToolbarLayout.setTitle(viewTitle);
        }

        return rootView;
    }

    @Override
    protected CardAdapter getCardAdapter() {
        return new DetailViewCardAdapter(getActivity(), mCards, mCallback);
    }

    @Override
    protected int getCardListLayout() {
        return R.layout.fragment_detail_base;
    }

    protected abstract String getViewTitle();
}
