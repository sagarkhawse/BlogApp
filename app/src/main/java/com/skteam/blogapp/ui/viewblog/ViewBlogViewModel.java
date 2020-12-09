package com.skteam.blogapp.ui.viewblog;

import android.app.Activity;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;
import com.skteam.blogapp.restmodels.getBlogs.BlogResponse;
import com.skteam.blogapp.restmodels.likeApi.LikeResponse;
import com.skteam.blogapp.setting.AppConstance;

public class ViewBlogViewModel extends BaseViewModel<ViewBlogNav> {
    public ViewBlogViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }

    public void LikeTheBlog(String id, String action) {
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.LIKE_BLOG)
                .addBodyParameter("user_id", getSharedPre().getUserId())
                .addBodyParameter("blog_id", id)
                .addBodyParameter("action", action)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(LikeResponse.class, new ParsedRequestListener<LikeResponse>() {
                    @Override
                    public void onResponse(LikeResponse response) {
                        if (response != null && response.getCode().equalsIgnoreCase("200")) {

                        }else{
                            getNavigator().getMessage("Server not Responding ");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().getMessage("Server not Responding");
                    }
                });
    }

}