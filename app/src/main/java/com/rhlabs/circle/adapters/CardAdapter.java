package com.rhlabs.circle.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rhlabs.circle.R;
import com.rhlabs.circle.interfaces.OnCardHeaderClickedListener;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.views.CardContentView;
import com.rhlabs.circle.views.CardHeaderView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 4/6/15.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    protected Context mContext;

    public interface CardHeaderEventListener {
        public abstract void onCardHeaderTapped(Card card, View view);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        protected
        @InjectView(R.id.card_view)
        CardView cardView;

        protected
        @InjectView(R.id.cardHeaderView)
        CardHeaderView cardHeaderView;

        protected
        @InjectView(R.id.cardContentView)
        CardContentView cardContentView;

        public CardViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            cardHeaderView.setBackgroundColor(Color.WHITE);
        }

        public void setCard(final Card card) {
            // Configure overall card
            if (card.isCardFullScreenWidth()) {
                cardView.setRadius(0);
                cardView.setCardElevation(0);
                CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );

                layoutParams.setMargins(0, 0, 0, card.addBottomMargin ? 50 : 1);
                cardView.setLayoutParams(layoutParams);
                cardHeaderView.setShowHeaderImage(false);
            }

            // Configure header view
            if (card.isHeaderAdded()) {
                cardHeaderView.setVisibility(View.VISIBLE);
                cardHeaderView.setCard(card);
            } else {
                cardHeaderView.setVisibility(View.GONE);
            }

            cardHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCardHeaderClickedListener != null) {
                        mOnCardHeaderClickedListener.onCardHeaderClicked(card, v);
                    }
                }
            });

            // Configure content view
            cardContentView.setCardType(card.getCardType());
            cardContentView.setMetaData(card.getMetaData());
            cardContentView.setData(card.getContent());
        }
    }

    private List<Card> mCards;
    private OnCardHeaderClickedListener mOnCardHeaderClickedListener;

    public CardAdapter(Context context, List<Card> cards) {
        mContext = context;
        mCards = cards;
    }

    public CardAdapter(Context context, List<Card> cards, OnCardHeaderClickedListener cardHeaderEventListener) {
        this(context, cards);
        mOnCardHeaderClickedListener = cardHeaderEventListener;
    }

    public List<Card> getCards() {
        return mCards;
    }

    public void setCards(List<Card> cards) {
        mCards = cards;
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mCards.size()) {
            return mCards.get(position).getCardTypeID();
        }

        return 0;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Card.CardType.CardTypeInfo cardTypeInfo = Card.cardTypeInfoByID(viewType);
        View cardItem = LayoutInflater.from(parent.getContext()).inflate(cardTypeInfo.getResource(), parent, false);
        return new CardViewHolder(cardItem);
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int position) {
        final Card card = mCards.get(position);
        customizeViews(cardViewHolder);
        cardViewHolder.setCard(card);
    }

    protected void customizeViews(CardViewHolder cardViewHolder) {
        // empty implementation..can be overridden by subclasses to customize views
    }
}
