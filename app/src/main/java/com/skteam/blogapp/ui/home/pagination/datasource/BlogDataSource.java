package com.skteam.blogapp.ui.home.pagination.datasource;
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
import com.skteam.blogapp.restmodels.getBlogs.BlogResponse;
import com.skteam.blogapp.restmodels.getBlogs.ResItem;
import com.skteam.blogapp.setting.AppConstance;
import com.skteam.blogapp.ui.home.HomeNav;

public class BlogDataSource extends PageKeyedDataSource<Long, ResItem> {
    private long pageKey = 2;
    private long page = 1;
    private String UserId = "0";
    private HomeNav homeNav;
    private String catId;

    public BlogDataSource(String userId,HomeNav homeNav,String catId) {
        this.homeNav = homeNav;
        this.UserId=userId;
        this.catId=catId;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, ResItem> callback) {
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.BLOGS)
                .addBodyParameter("user_id",UserId )
                .addBodyParameter("page", String.valueOf(page))
                .addBodyParameter("category_id",catId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(BlogResponse.class, new ParsedRequestListener<BlogResponse>() {
                    @Override
                    public void onResponse(BlogResponse response) {
                        homeNav.isLoading(false);
                        if (response != null && response.getCode().equalsIgnoreCase("200")) {
                            if (response.getRes() != null && response.getRes().size() > 0) {
                               page++;
                                callback.onResult(response.getRes(), null, pageKey);
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

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ResItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ResItem> callback) {
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.BLOGS)
                .addBodyParameter("user_id",UserId )
                .addBodyParameter("page", String.valueOf(page))
                .addBodyParameter("category_id",catId)
                .build()
                .getAsObject(BlogResponse.class, new ParsedRequestListener<BlogResponse>() {
                    @Override
                    public void onResponse(BlogResponse response) {
                        homeNav.isLoading(false);
                        if (response != null && response.getCode().equalsIgnoreCase("200")) {
                            if (response.getRes() != null && response.getRes().size() > 0) {
                              page++;
                                callback.onResult(response.getRes(), params.key + 1);
                            }else{
                                //homeNav.getMessage("No More Data");
                            }
                        }
                        else{
                            homeNav.getMessage("Server not Responding ");
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
