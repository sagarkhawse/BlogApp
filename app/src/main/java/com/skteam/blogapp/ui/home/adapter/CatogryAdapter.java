/*
 * Created by Nitin on 10/23/20 1:43 PM
 * Core Techies
 * nitin@coretechies.com
 */

/*
 * Created by Nitin on 10/23/20 1:24 PM
 * Core Techies
 * nitin@coretechies.com
 */

package com.skteam.blogapp.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.skteam.blogapp.R;
import com.skteam.blogapp.restmodels.gteCatogry.ResItem;

import java.util.List;

public class CatogryAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<ResItem> items;
    private final int mResource;

    public CatogryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        CheckedTextView spinneText = view.findViewById(R.id.textSpinner);
        spinneText.setText(items.get(position).getTitle());
        return view;
    }
}