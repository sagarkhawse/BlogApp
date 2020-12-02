package com.skteam.blogapp.ui.home;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;
import com.skteam.blogapp.restmodels.getBlogs.ResItem;
import com.skteam.blogapp.ui.home.pagination.datasource.BlogDataSource;
import com.skteam.blogapp.ui.home.pagination.factory.BlogDataFatory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeViewModel extends BaseViewModel<HomeNav> {
    private LiveData<PagedList<ResItem>> getBlogList;
    private BlogDataSource blogDataSource;
    private BlogDataFatory blogDataFatory;
    private LiveData<BlogDataSource> sourceLiveData;
    private Executor executor;
    public HomeViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
        //DataFacoryInstance
    }
    public void LoadPaging(HomeNav homeNav){
        blogDataFatory= new BlogDataFatory(homeNav);
        //GetLiveSource
        sourceLiveData=blogDataFatory.GetBlogsLive();
        //set PageList Config
        PagedList.Config config= (new PagedList.Config.Builder()).setEnablePlaceholders(false).setInitialLoadSizeHint(10)
                .setPageSize(5).setPrefetchDistance(4).build();
        //ThreadPool
        executor= Executors.newFixedThreadPool(5);
        //Sent LiveBarList
        getBlogList=(new LivePagedListBuilder<Long,ResItem>(blogDataFatory,config)).setFetchExecutor(executor).build();
    }


    public LiveData<PagedList<ResItem>> getGeBarDtaList() {
        return getBlogList;
    }
}