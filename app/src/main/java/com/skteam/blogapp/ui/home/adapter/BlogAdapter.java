package com.skteam.blogapp.ui.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.skteam.blogapp.R;
import com.skteam.blogapp.databinding.BlogItemBinding;
import com.skteam.blogapp.restmodels.getBlogs.ResItem;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.home.HomeActivity;
import com.skteam.blogapp.ui.viewblog.ViewBlogFragment;

import java.text.DecimalFormat;
import java.util.List;

import static com.skteam.blogapp.setting.AppConstance.IMG_URL;

public class BlogAdapter extends PagedListAdapter<ResItem, BlogAdapter.BlogViewHolder> {
    private Context context;
    private List<ResItem> blogLists;

    public BlogAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;

    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BlogItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.blog_item, parent, false);
        return new BlogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, final int position) {
        holder.OnBindView(getItem(position), position);

    }


    public class BlogViewHolder extends RecyclerView.ViewHolder {
        private BlogItemBinding binding;

        public BlogViewHolder(BlogItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void OnBindView(final ResItem item, int position) {
            binding.tvDate.setText(CommonUtils.dateFormat(item.getCreatedAt()));
            Glide.with(context).load(IMG_URL + item.getImage()).into(binding.ivBlog);
            binding.tvTitle.setText(item.getTitle().trim());
            if (item.getDescription().length()>=50){
                binding.tvDesc.setText(item.getDescription().trim().substring(0,50)+"...");
            }else{
                binding.tvDesc.setText(item.getDescription().trim());
            }

            binding.itemLayout.setOnClickListener(v -> ((HomeActivity) context).startFragment(ViewBlogFragment.newInstance(item), true, ViewBlogFragment.newInstance(item).toString()));

        }
    }

    private static DiffUtil.ItemCallback<ResItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ResItem>() {

        @Override
        public boolean areItemsTheSame(@NonNull ResItem oldItem, @NonNull ResItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ResItem oldItem, @NonNull ResItem newItem) {
            return true;
        }
    };
}