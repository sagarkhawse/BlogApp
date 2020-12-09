package com.skteam.blogapp.ui.comment.pagination.datafactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.skteam.blogapp.ui.comment.CommentNav;
import com.skteam.blogapp.ui.comment.pagination.datasource.CommentDataSource;
import com.skteam.blogapp.ui.home.HomeNav;
public class CommentDataFatory extends DataSource.Factory {

    private MutableLiveData<CommentDataSource> mutableCommentBlogs = new MutableLiveData<>();
    private CommentDataSource commentDataSource;

    public CommentDataFatory(CommentNav homeNav, String userId, String blogId) {
        commentDataSource = new CommentDataSource(blogId,userId,homeNav);
    }

    @NonNull
    @Override
    public CommentDataSource create() {
        mutableCommentBlogs.postValue(commentDataSource);
        return commentDataSource;
    }

    public MutableLiveData<CommentDataSource> GetBlogsCommentLive() {
        return mutableCommentBlogs;
    }
}
