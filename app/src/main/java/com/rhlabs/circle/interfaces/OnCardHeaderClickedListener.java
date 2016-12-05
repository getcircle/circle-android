package com.rhlabs.circle.interfaces;

import android.view.View;

import com.rhlabs.circle.models.Card;

public interface OnCardHeaderClickedListener {
    void onCardHeaderClicked(Card card, View view);
}
