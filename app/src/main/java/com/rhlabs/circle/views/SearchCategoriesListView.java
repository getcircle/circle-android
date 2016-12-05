package com.rhlabs.circle.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.adapters.SearchCategoriesListAdapter;
import com.rhlabs.circle.models.SearchCategory;
import com.rhlabs.circle.utils.BusProvider;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 6/28/15.
 */
public class SearchCategoriesListView extends CardContentView {
    @InjectView(R.id.lvSearchCategories)
    ListView mSearchCategoriesListView;

    private SearchCategoriesListAdapter mSearchCategoriesListAdapter;
    private Context mContext;

    public SearchCategoriesListView(Context context) {
        super(context);
        init(context);
    }

    public SearchCategoriesListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchCategoriesListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchCategoriesListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    public void setData(ArrayList<?> data) {
        ArrayList<SearchCategory> searchCategories = new ArrayList<>();
        searchCategories.addAll((ArrayList<? extends SearchCategory>) data);
        int numberOfItems = searchCategories.size();

        ViewGroup.LayoutParams layoutParams = mSearchCategoriesListView.getLayoutParams();
        int totalDividerHeight = mSearchCategoriesListView.getDividerHeight() * numberOfItems;
        layoutParams.height = (int)(mContext.getResources().getDimension(R.dimen.search_category_item_row_height) * numberOfItems) + totalDividerHeight;
        mSearchCategoriesListView.setLayoutParams(layoutParams);
        mSearchCategoriesListView.requestLayout();

        mSearchCategoriesListAdapter.clear();
        mSearchCategoriesListAdapter.addAll(searchCategories);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_stats_grid, this, true);
        ButterKnife.inject(this);

        mContext = context;
        mSearchCategoriesListAdapter = new SearchCategoriesListAdapter(context, new ArrayList<SearchCategory>());
        mSearchCategoriesListView.setAdapter(mSearchCategoriesListAdapter);
        mSearchCategoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusProvider.getMainBus().post(mSearchCategoriesListAdapter.getItem(position));
            }
        });
    }

}
