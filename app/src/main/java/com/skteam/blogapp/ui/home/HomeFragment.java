package com.skteam.blogapp.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.HomeFragmentBinding;
import com.skteam.blogapp.restmodels.getBlogs.ResItem;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.home.adapter.BlogAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> implements HomeNav {

    private HomeViewModel mViewModel;
    private static HomeFragment instance;
    private HomeFragmentBinding binding;
    private HomeViewModel viewModel;
    private Dialog internetDialog;
    private BlogAdapter blogAdapter;
    private List<ResItem> blogsList=new ArrayList<>();

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
        blogAdapter=new BlogAdapter(getContext(),blogsList);
        binding.blogsRecycler.setAdapter(blogAdapter);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(getBaseActivity(), false);
        }
        if (isConnected) {
            internetDialog.dismiss();
        } else {
            internetDialog.show();
        }
    }
}