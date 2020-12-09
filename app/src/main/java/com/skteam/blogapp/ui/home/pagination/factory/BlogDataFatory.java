package com.skteam.blogapp.ui.home.pagination.factory;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.skteam.blogapp.restmodels.getBlogs.ResItem;
import com.skteam.blogapp.ui.home.HomeNav;
import com.skteam.blogapp.ui.home.pagination.datasource.BlogDataSource;

import java.util.List;

public class BlogDataFatory extends DataSource.Factory {

    private MutableLiveData<BlogDataSource> mutableBlogs = new MutableLiveData<>();
    private BlogDataSource blogDataSource;

    public BlogDataFatory(HomeNav homeNav,String userId) {
        blogDataSource = new BlogDataSource(userId,homeNav);
    }

    @NonNull
    @Override
    public BlogDataSource create() {
        mutableBlogs.postValue(blogDataSource);
        return blogDataSource;
    }

    public MutableLiveData<BlogDataSource> GetBlogsLive() {
        return mutableBlogs;
    }
}
