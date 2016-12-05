package com.rhlabs.circle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rhlabs.circle.R;
import com.rhlabs.circle.models.SearchCategory;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by anju on 6/28/15.
 */
public class SearchCategoriesListAdapter extends ArrayAdapter<SearchCategory> {

    private Context mContext;

    public SearchCategoriesListAdapter(Context context, ArrayList<SearchCategory> searchCategories) {
        super(context, R.layout.view_search_category_item, searchCategories);
        mContext = context;
    }

    static class SearchCategoryViewHolder {
        @InjectView(R.id.tvSearchCategoryText)
        TextView searchCategoryTextView;

        @InjectView(R.id.ivSearchCategoryImage)
        ImageView searchCategoryImageView;

        public SearchCategoryViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchCategoryViewHolder searchCategoryViewHolder;
        SearchCategory searchCategory = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_search_category_item, parent, false);
            searchCategoryViewHolder = new SearchCategoryViewHolder(convertView);
            convertView.setTag(searchCategoryViewHolder);
        } else {
            searchCategoryViewHolder = (SearchCategoryViewHolder) convertView.getTag();
        }

        searchCategoryViewHolder.searchCategoryTextView.setText(searchCategory.getType().toString() + " (" + searchCategory.getCount() + ")");
        int imageSource = searchCategory.getImageResource();
        if (imageSource != 0) {
            searchCategoryViewHolder.searchCategoryImageView.setImageResource(imageSource);
        }
        return convertView;
    }
}
