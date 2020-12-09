package com.skteam.blogapp.ui.comment.pagination.datasource;
/*
* Ishant Sharma
* Android Developer
* ishant.sharma1947@gmail.com
* Paging JetPack
*/

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.skteam.blogapp.restmodels.commentresponse.CommentResponse;
import com.skteam.blogapp.restmodels.commentresponse.ResItem;
import com.skteam.blogapp.setting.AppConstance;
import com.skteam.blogapp.ui.comment.CommentNav;

public class CommentDataSource extends PageKeyedDataSource<Long, ResItem> {
    private long pageKey = 2;
    private long page = 1;
    private String UserId = "0";
    private String blogId = "0";
    private CommentNav homeNav;

    public CommentDataSource(String blogId,String userId, CommentNav homeNav) {
        this.homeNav = homeNav;
        this.UserId=userId;
        this.blogId=blogId;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, ResItem> callback) {
        homeNav.isLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.VIEW_ALL_COMMENT)
                .addBodyParameter("user_id",UserId)
                .addBodyParameter("blog_id", blogId)
                .addBodyParameter("page", String.valueOf(page))
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(CommentResponse.class, new ParsedRequestListener<CommentResponse>() {
                    @Override
                    public void onResponse(CommentResponse response) {
                        homeNav.isLoading(false);
                        if (response != null && response.getCode().equalsIgnoreCase("200")) {
                            if (response.getRes() != null && response.getRes().size() > 0) {
                                page++;

                                callback.onResult(response.getRes(), null, pageKey);
                            }
                        }else{
                            homeNav.getMessage("Server Not Resonding..");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        homeNav.isLoading(false);
                        homeNav.getMessage("Server not Responding");
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ResItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ResItem> callback) {
     homeNav.isLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.VIEW_ALL_COMMENT)
                .addBodyParameter("user_id",UserId)
                .addBodyParameter("blog_id", blogId)
                .addBodyParameter("page", String.valueOf(page))
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(CommentResponse.class, new ParsedRequestListener<CommentResponse>() {
                    @Override
                    public void onResponse(CommentResponse response) {
                        homeNav.isLoading(false);
                        if (response != null && response.getCode().equalsIgnoreCase("200")) {
                            if (response.getRes() != null && response.getRes().size() > 0) {
                                page++;
                                callback.onResult(response.getRes(), params.key + 1);
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        homeNav.isLoading(false);
                        homeNav.getMessage("Server not Responding");
                    }
                });

    }
}
