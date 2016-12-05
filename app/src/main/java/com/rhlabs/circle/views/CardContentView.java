package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.rhlabs.circle.models.Card;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by anju on 4/19/15.
 */
public abstract class CardContentView extends LinearLayout {

    public CardContentView(Context context) {
        super(context);
    }

    public CardContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected Card.CardType mCardType;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public abstract void setData(ArrayList<?> data);

    public void setMetaData(Map<String, Object> metaData) {

    }

    public Card.CardType getCardType() {
        return mCardType;
    }

    public void setCardType(Card.CardType cardType) {
        mCardType = cardType;
    }
}
