package com.skteam.blogapp.ui.viewblog;

import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.ViewBlogFragmentBinding;
import com.skteam.blogapp.restmodels.getBlogs.ResItem;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.home.HomeActivity;

import static com.skteam.blogapp.setting.AppConstance.IMG_URL;

public class ViewBlogFragment extends BaseFragment<ViewBlogFragmentBinding, ViewBlogViewModel> implements ViewBlogNav {

    private ViewBlogViewModel mViewModel;
    private ViewBlogFragmentBinding binding;
    private static ViewBlogFragment instance;
    private static ResItem getVlogMain;
    private Dialog internetDialog;


    public static ViewBlogFragment newInstance(ResItem getVlog) {
        getVlogMain = getVlog;
        return instance = instance == null ? new ViewBlogFragment() : instance;
    }

    @Override
    public String toString() {
        return ViewBlogFragment.class.getName();
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_blog_fragment;
    }

    @Override
    public ViewBlogViewModel getViewModel() {
        return mViewModel = new ViewBlogViewModel(getContext(), getSharedPre(), getActivity());
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
        mViewModel.setNavigator(this);
        ((HomeActivity)getContext()).getToolbar().toolbarLay.setVisibility(View.GONE);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getContext()).onBackPressed();
            }
        });
        binding.tvTitle.setText(getVlogMain.getTitle().trim());
        Glide.with(getContext()).load(IMG_URL+getVlogMain.getImage()).into(binding.ivBlog);
        binding.tvDesc.setText(getVlogMain.getDescription().trim());
        binding.ivLike.setOnClickListener(v -> {
            if(getSharedPre().isLoggedIn()){
                showCustomAlert("Blog Liked");
            }else{
                //showBoottomSheet
                ((HomeActivity)getContext()).getBottomSheet().bottomLay.setVisibility(View.VISIBLE);
            }
        });
//
        binding.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSharedPre().isLoggedIn()){
                    showCustomAlert("Create Comment Activity First");
                }else{
                    ((HomeActivity)getContext()).getBottomSheet().bottomLay.setVisibility(View.VISIBLE);
                }
            }
        });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeActivity)getContext()).getToolbar().toolbarLay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(getActivity(), false);
        }
        if (isConnected) {
            internetDialog.dismiss();
        } else {
            internetDialog.show();
        }
    }
}