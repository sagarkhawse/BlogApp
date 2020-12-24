package com.skteam.blogapp.ui.home;

import androidx.lifecycle.Observer;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.HomeFragmentBinding;
import com.skteam.blogapp.restmodels.getBlogs.ResItem;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.home.adapter.BlogAdapter;
import com.skteam.blogapp.ui.home.adapter.CatogryAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> implements HomeNav {

    private HomeViewModel mViewModel;
    private static HomeFragment instance;
    private HomeFragmentBinding binding;
    private HomeViewModel viewModel;
    private Dialog internetDialog;
    private BlogAdapter blogAdapter;
    private PagedList<ResItem> getBlogList;
    private CatogryAdapter catogryAdapter;
    private HomeNav homeNav;
    private boolean isCalled=false;
    private int postionMain=-1;
    private List<com.skteam.blogapp.restmodels.gteCatogry.ResItem> getCatogry = new ArrayList<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public static HomeFragment getInstance() {
        return instance = instance == null ? new HomeFragment() : instance;
    }

    @Override
    public String toString() {
        return HomeFragment.class.getName();
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    public HomeViewModel getViewModel() {
        return viewModel = new HomeViewModel(getContext(), getSharedPre(), getBaseActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        viewModel.getCatogory();
        homeNav = this;
        blogAdapter = new BlogAdapter(getContext());
        binding.blogsRecycler.setAdapter(blogAdapter);
        if (getBlogList != null && getBlogList.size() > 0) {
            blogAdapter.submitList(getBlogList);
            postionMain=0;
        }
        binding.catogry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                if (getCatogry != null && getCatogry.size() > 0 && postionMain!=position ) {
                    postionMain=position;
                    viewModel.LoadPaging(homeNav, getCatogry.get(position).getCategoryId());
                    viewModel.getGeBarDtaList().observe(getBaseActivity(), resItems -> {
                        getBlogList = resItems;
                        blogAdapter.submitList(getBlogList);
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            getBaseActivity().getInternetDialog().dismiss();
            viewModel.getCatogory();
        } else {
            getBaseActivity().getInternetDialog().show();
        }
    }

    @Override
    public void isLoading(boolean value) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    showLoadingDialog("");
                } else {
                    hideLoadingDialog();
                }
            }
        });

    }

    @Override
    public void getMessage(String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                showCustomAlert(message);
            }
        });


    }


    @Override
    public void GetCatogory(List<com.skteam.blogapp.restmodels.gteCatogry.ResItem> res) {
        getCatogry = res;
        catogryAdapter = new CatogryAdapter(getContext(), R.layout.checked_spiiner, res);

        binding.catogry.setAdapter(catogryAdapter);


    }

    @Override
    public void StartHomeNow(com.skteam.blogapp.restmodels.signUp.ResItem resItem) {
        viewModel.getCatogory();
    }
}