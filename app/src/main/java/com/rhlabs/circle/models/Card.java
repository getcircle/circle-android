package com.rhlabs.circle.models;

import com.rhlabs.circle.R;
import com.rhlabs.circle.views.CardContentView;
import com.rhlabs.circle.views.ContactsBarView;
import com.rhlabs.circle.views.GenericTextValueView;
import com.rhlabs.circle.views.GroupMembershipRequestsListView;
import com.rhlabs.circle.views.KeyValueListView;
import com.rhlabs.circle.views.LocationAddressView;
import com.rhlabs.circle.views.LocationListView;
import com.rhlabs.circle.views.ProfileListView;
import com.rhlabs.circle.views.ProfilesGroupView;
import com.rhlabs.circle.views.SearchCategoriesListView;
import com.rhlabs.circle.views.TeamsGridView;
import com.rhlabs.protobufs.soa.ServiceRequestV1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anju on 4/6/15.
 */
public class Card {

    public Integer cardIndex = 0;
    public Integer contentCount;
    public boolean addHeader = true;
    public Map<String, Object> metaData;
    public boolean showAllContent;
    public boolean showContentCount;
    public boolean addBottomMargin = true;
    public boolean isEditable = false;

    private ArrayList<?> mAllContent = new ArrayList<>();
    private ArrayList<?> mContent = new ArrayList<>();
    private Class<CardContentView> mContentClass;

    private ServiceRequestV1 mContentNextRequest;
    private Class mHeaderClass;
    private int mImageSource;
    private boolean mIsCardFullScreenWidth = false;
    private Integer mMaxVisibleItems;
    private String mTitle;
    private CardType mCardType;

    public enum CardType {
        AddNote("AddNote", 1),
        Anniversaries("Anniversaries", 2),
        Banners("Banners", 5),
        Birthdays("Birthdays", 6),
        Empty("Empty", 7),
        Education("Education", 8),
        ProfileImages("ProfileImages", 9),
        GroupRequests("GroupRequests", 10),
        KeyValue("KeyValue", 11),
        Offices("Offices", 14),
        OfficeAddress("Office Address", 15),
        NewHires("NewHires", 16),
        Profiles("Profiles", 18),
        Placeholder("Placeholder", 19),
        Position("Position", 20),
        Settings("Settings", 21),
        SocialConnectCTAs("SocialConnectCTAs", 22),
        SocialToggle("SocialToggle", 23),
        StatTile("StatTile", 24),
        QuickActions("QuickActions", 25),
        Team("Team", 26),
        TeamsGrid("TeamsGrid", 27),
        TextValue("TextValue", 28),
        SearchCategories("Stats", 30),
        Groups("Groups", 31),
        ContactsBar("ContactsBar", 32);

        private final String text;
        private final Integer id;

        /**
         * @param text Name used when logging card type
         */
        CardType(final String text, final Integer id) {
            this.text = text;
            this.id = id;
        }

        public static CardType fromString(String stringValue) {
            for (CardType cardType : values()) {
                if (cardType.toString().equals(stringValue)) {
                    return cardType;
                }
            }
            return null;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }

        public Integer cardTypeID() {
            return id;
        }

        public final class CardTypeInfo {
            private int imageSrc;
            private Class contentClass;
            private int resource;

            public int getResource() {
                return resource;
            }

            public CardTypeInfo(final int imageSrc, final Class contentClass, final int resource) {
                this.imageSrc = imageSrc;
                this.contentClass = contentClass;
                this.resource = resource;
            }
        }

        public CardTypeInfo infoByCardType() {
            switch (this) {
                case ProfileImages:
                    return new CardTypeInfo(R.drawable.ic_feedpeers, ProfilesGroupView.class, R.layout.card_profile_images);

                case Anniversaries:
                    return new CardTypeInfo(R.drawable.ic_feedwork, ProfileListView.class, R.layout.card_profile_item);

                case Birthdays:
                    return new CardTypeInfo(R.drawable.ic_feedbirthday, ProfileListView.class, R.layout.card_profile_item);

                case NewHires:
                    return new CardTypeInfo(R.drawable.ic_feednewhire, ProfileListView.class, R.layout.card_profile_item);

                case Profiles:
                    return new CardTypeInfo(R.drawable.ic_feedreports, ProfileListView.class, R.layout.card_profile_item);

                case ContactsBar:
                    return new CardTypeInfo(R.drawable.ic_feedpeers, ContactsBarView.class, R.layout.card_contactsbar);

                case GroupRequests:
                    return new CardTypeInfo(R.drawable.ic_feednotification, GroupMembershipRequestsListView.class, R.layout.card_group_membership_requests);

                case KeyValue:
                    return new CardTypeInfo(0, KeyValueListView.class, R.layout.card_key_value_list);

                case Offices:
                    return new CardTypeInfo(R.drawable.ic_feedlocation, LocationListView.class, R.layout.card_location_item);

                case OfficeAddress:
                    return new CardTypeInfo(R.drawable.ic_feedlocation, LocationAddressView.class, R.layout.card_location_address);

                case SearchCategories:
                    return new CardTypeInfo(0, SearchCategoriesListView.class, R.layout.card_search_categories);

                case TeamsGrid:
                    return new CardTypeInfo(R.drawable.ic_feedteam, TeamsGridView.class, R.layout.card_teams_grid);

                case TextValue:
                    return new CardTypeInfo(0, GenericTextValueView.class, R.layout.card_generic_text_value);
            }

            return null;
        }
    }

    public Card(
            CardType cardType,
            String title,
            Integer contentCount,
            boolean showContentCount
    ) {
        this.mCardType = cardType;
        CardType.CardTypeInfo infoByCardType = this.mCardType.infoByCardType();
        if (infoByCardType != null) {
            this.mImageSource = infoByCardType.imageSrc;
            this.mContentClass = infoByCardType.contentClass;
        }
        this.mTitle = title;
        this.contentCount = contentCount;
        if (showContentCount) {
            this.showContentCount = showContentCount;
        }
    }

    public Card(CardType cardType, String title) {
        this(cardType, title, 0, false);
    }

    public Integer getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Integer cardIndex) {
        this.cardIndex = cardIndex;
    }

    public Integer getContentCount() {
        return contentCount;
    }

    public void setContentCount(Integer contentCount) {
        this.contentCount = contentCount;
    }

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
        this.metaData = metaData;
    }

    public boolean isShowAllContent() {
        return showAllContent;
    }

    public void setShowAllContent(boolean showAllContent) {
        this.showAllContent = showAllContent;
    }

    public boolean isShowContentCount() {
        return showContentCount;
    }

    public void setShowContentCount(boolean showContentCount) {
        this.showContentCount = showContentCount;
    }

    public ArrayList getAllContent() {
        return mAllContent;
    }

    private void setAllContent(ArrayList allContent) {
        this.mAllContent = allContent;
    }

    public boolean isHeaderAdded() {
        return addHeader;
    }

    public ArrayList getContent() {
        return mContent;
    }

    public Class getContentClass() {
        return mContentClass;
    }

    private void setContentClass(Class contentClass) {
        this.mContentClass = contentClass;
    }

    public Class getHeaderClass() {
        return mHeaderClass;
    }

    private void setHeaderClass(Class headerClass) {
        this.mHeaderClass = headerClass;
    }

    public int getImageSource() {
        return mImageSource;
    }

    public void setImageSource(int imageSource) {
        mImageSource = imageSource;
    }

    public String getTitle() {
        return mTitle;
    }

    private void setTitle(String title) {
        this.mTitle = title;
    }

    public CardType getCardType() {
        return mCardType;
    }

    private void setCardType(CardType cardType) {
        this.mCardType = cardType;
    }

    public Integer getCardTypeID() {
        return getCardType().id;
    }

    public void addContent(List content, int maxVisibleItems) {

        int totalNumberOfVisibleItems = Math.min(content.size(), maxVisibleItems == 0 ? content.size() : maxVisibleItems);
        List visibleContent = content.subList(0, totalNumberOfVisibleItems);
        resetContent();

        mContent.addAll(visibleContent);
        mAllContent.addAll(content);
        contentCount = content.size();
        mMaxVisibleItems = totalNumberOfVisibleItems;
    }

    public void addContent(List content) {
        this.addContent(content, 0);
    }

    public void resetContent() {
        mContent.clear();
        mAllContent.clear();
        contentCount = 0;
    }

    public boolean isContentAllContent() {
        return this.mAllContent.size() == this.mContent.size() && this.mContent.size() > 0;
    }

    public void setContentToVisibleItems() {
        if (mMaxVisibleItems > 0) {
            if (mContent.size() > mMaxVisibleItems) {
                mContent = (ArrayList<?>) mAllContent.subList(0, mMaxVisibleItems);
            }
        }
    }

    public void setContentToAllContent() {
        mContent = mAllContent;
    }

    public void toggleShowingFullContent() {
        if (isContentAllContent()) {
            setContentToVisibleItems();
        } else {
            setContentToAllContent();
        }
    }

    public String contentCountLabel() {
        if (showContentCount) {
            return contentCount == 1 ? "" : "All" + contentCount;
        }

        return "";
    }

    public static CardType.CardTypeInfo cardTypeInfoByID(Integer id) {
        for (CardType cardType : CardType.values()) {
            if (cardType.id == id) {
                return cardType.infoByCardType();
            }
        }

        return null;
    }


    public boolean isCardFullScreenWidth() {
        return mIsCardFullScreenWidth;
    }

    public void setCardFullScreenWidth(boolean isCardFullScreenWidth) {
        mIsCardFullScreenWidth = isCardFullScreenWidth;
    }

    public ServiceRequestV1 getContentNextRequest() {
        return mContentNextRequest;
    }

    public void setContentNextRequest(ServiceRequestV1 contentNextRequest) {
        mContentNextRequest = contentNextRequest;
    }
}
