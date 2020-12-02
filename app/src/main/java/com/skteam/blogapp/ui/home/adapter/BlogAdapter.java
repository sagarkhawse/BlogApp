package com.skteam.blogapp.ui.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skteam.blogapp.restmodels.getBlogs.ResItem;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {
    private Context context;
    private List<ResItem> blogLists;

    public BlogAdapter(Context context, List<ResItem> blogsList) {
        this.context = context;
        blogLists=blogsList;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return blogLists.size();
    }

    class BlogViewHolder extends RecyclerView.ViewHolder {
        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
