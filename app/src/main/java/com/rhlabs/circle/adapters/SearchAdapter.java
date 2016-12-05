package com.rhlabs.circle.adapters;

import android.content.Context;

import com.rhlabs.circle.R;
import com.rhlabs.circle.interfaces.OnCardHeaderClickedListener;
import com.rhlabs.circle.models.Card;

import java.util.List;

/**
 * Created by anju on 6/26/15.
 */
public class SearchAdapter extends CardAdapter {
    public SearchAdapter(Context context, List<Card> cards) {
        super(context, cards);
    }

    public SearchAdapter(Context context, List<Card> cards, OnCardHeaderClickedListener cardHeaderEventListener) {
        super(context, cards, cardHeaderEventListener);
    }

    @Override
    protected void customizeViews(CardViewHolder cardViewHolder) {
        super.customizeViews(cardViewHolder);
        cardViewHolder.cardHeaderView.setBackgroundColor(mContext.getResources().getColor(R.color.app_view_background_color));
    }
}
