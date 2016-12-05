package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.Card;
import com.rhlabs.circle.models.EditCardEvent;
import com.rhlabs.circle.utils.BusProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 4/16/15.
 */
public class CardHeaderView extends LinearLayout {

    public Card mCard;

    @InjectView(R.id.tvCardTitle)
    protected TextView mCardHeaderTitleTextView;

    @InjectView(R.id.tvCardContentCount)
    protected TextView mCardHeaderCountTextView;

    @InjectView(R.id.ivCardHeaderImage)
    protected ImageView mCardHeaderImageView;

    @InjectView(R.id.btnCardHeaderEdit)
    protected Button mCardHeaderEditButton;

    private Context mContext;

    private boolean mShowHeaderImage = true;

    public CardHeaderView(Context context) {
        super(context);
        init(context);
    }

    public CardHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CardHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_card_header, this, true);
        ButterKnife.inject(this, view);
        mContext = context;
        mCardHeaderEditButton.setVisibility(GONE);
        mCardHeaderEditButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCard != null) {
                    EditCardEvent editCardEvent = new EditCardEvent(mCard);
                    BusProvider.getMainBus().post(editCardEvent);
                }
            }
        });
    }

    public Card getCard() {
        return mCard;
    }

    public void setCard(Card card) {
        this.mCard = card;

        // Image
        if (card.getImageSource() != 0 && mShowHeaderImage) {
            mCardHeaderImageView.setVisibility(VISIBLE);
            mCardHeaderImageView.setImageDrawable(ContextCompat.getDrawable(mContext, card.getImageSource()));
        }
        else {
            mCardHeaderImageView.setVisibility(GONE);
        }

        // Title
        mCardHeaderTitleTextView.setText(mCard.getTitle());

        // Content Count
        mCardHeaderEditButton.setVisibility(GONE);
        if (card.isEditable) {
            mCardHeaderEditButton.setVisibility(VISIBLE);
            mCardHeaderCountTextView.setVisibility(GONE);
        }
        else if (card.showContentCount) {
            if (card.contentCount == 1) {
                mCardHeaderCountTextView.setText("");
            } else {
                mCardHeaderCountTextView.setText("All (" + card.contentCount + ")");
            }
        }
    }

    public void setShowHeaderImage(boolean showHeaderImage) {
        mShowHeaderImage = showHeaderImage;
    }
}
