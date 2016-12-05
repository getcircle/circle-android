package com.rhlabs.circle.models;

/**
 * Created by anju on 7/13/15.
 */
public class EditCardEvent {

    private Card mCard;

    public EditCardEvent(Card card) {
        mCard = card;
    }

    public Card getCard() {
        return mCard;
    }
}
