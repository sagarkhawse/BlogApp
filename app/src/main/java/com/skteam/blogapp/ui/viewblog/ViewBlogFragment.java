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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.ViewBlogFragmentBinding;
import com.skteam.blogapp.restmodels.getBlogs.ResItem;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.comment.CommentFragment;
import com.skteam.blogapp.ui.home.HomeActivity;

import static com.skteam.blogapp.setting.AppConstance.IMG_URL;

public class ViewBlogFragment extends BaseFragment<ViewBlogFragmentBinding, ViewBlogViewModel> implements ViewBlogNav {

    private ViewBlogViewModel viewmodel;
    private ViewBlogFragmentBinding binding;
    private static ViewBlogFragment instance;
    private static ResItem getVlogMain;
    private int action=0;
    private boolean isClickedLike=false;


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
        return viewmodel = new ViewBlogViewModel(getContext(), getSharedPre(), getActivity());
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
        viewmodel.setNavigator(this);
        ((HomeActivity)getContext()).getToolbar().toolbarLay.setVisibility(View.GONE);
        binding.tvTitle.setText(getVlogMain.getTitle().trim());
        Glide.with(getContext()).load(IMG_URL+getVlogMain.getImage()).into(binding.ivBlog);
        binding.tvDesc.setText(getVlogMain.getDescription().trim());
        if(getVlogMain.getLikeAction.equalsIgnoreCase("0")){
            binding.ivLike.setImageResource(R.drawable.ic_like);
            binding.ivLikeTop.setImageResource(R.drawable.ic_like);
        }else{
            binding.ivLike.setImageResource(R.drawable.ic_liked);
            binding.ivLikeTop.setImageResource(R.drawable.ic_liked);

        }
        getLifecycle().addObserver(binding.youtubePlayerView);

        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "S0Q4gqBUs7c";
                //youTubePlayer.loadVideo(videoId, 0);
            }
        });
       setClickListeners();

    }

    private void setClickListeners() {
        binding.ivBack.setOnClickListener(v -> {
            getVib().vibrate(100);
            ((HomeActivity)getContext()).onBackPressed();
        });
        binding.ivLike.setOnClickListener(v -> {
            getVib().vibrate(100);
            if(getSharedPre().isLoggedIn()){
                if(getVlogMain.getLikeAction.equalsIgnoreCase("0")){
                    //like
                    viewmodel.LikeTheBlog(getVlogMain.getId(),"1");
                    isClickedLike=true;
                    getVlogMain.setGetLikeAction("1");
                    action=1;
                    binding.ivLike.setImageResource(R.drawable.ic_liked);
                    binding.ivLikeTop.setImageResource(R.drawable.ic_liked);
                }else{
                    //unlike
                    action=0;
                    isClickedLike=true;
                    getVlogMain.setGetLikeAction("0");
                    binding.ivLike.setImageResource(R.drawable.ic_like);
                    binding.ivLikeTop.setImageResource(R.drawable.ic_like);
                    viewmodel.LikeTheBlog(getVlogMain.getId(),"0");
                }
            }else{
                //showBoottomSheet
                isClickedLike=false;
                ((HomeActivity)getContext()).getBottomSheet().bottomLay.setVisibility(View.VISIBLE);
            }
        });
        binding.ivLikeTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVib().vibrate(100);
                if(getSharedPre().isLoggedIn()){
                    if(getVlogMain.getLikeAction.equalsIgnoreCase("0")){
                        //like
                        viewmodel.LikeTheBlog(getVlogMain.getId(),"1");
                        isClickedLike=true;
                        getVlogMain.setGetLikeAction("1");
                        action=1;
                        binding.ivLike.setImageResource(R.drawable.ic_liked);
                        binding.ivLikeTop.setImageResource(R.drawable.ic_liked);
                    }else{
                        //unlike
                        action=0;
                        isClickedLike=true;
                        getVlogMain.setGetLikeAction("0");
                        binding.ivLike.setImageResource(R.drawable.ic_like);
                        binding.ivLikeTop.setImageResource(R.drawable.ic_like);
                        viewmodel.LikeTheBlog(getVlogMain.getId(),"0");
                    }
                }else{
                    //showBoottomSheet
                    isClickedLike=false;
                    ((HomeActivity)getContext()).getBottomSheet().bottomLay.setVisibility(View.VISIBLE);
                }
            }
        });
//
        binding.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVib().vibrate(100);
                if(getSharedPre().isLoggedIn()){
                    getBaseActivity().startFragment(CommentFragment.getInstance(getVlogMain.getId()),true,CommentFragment.getInstance(getVlogMain.getId()).toString(),true);
                }else{
                    ((HomeActivity)getContext()).getBottomSheet().bottomLay.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.ivCommentTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVib().vibrate(100);
                if(getSharedPre().isLoggedIn()){
                    getBaseActivity().startFragment(CommentFragment.getInstance(getVlogMain.getId()),true,CommentFragment.getInstance(getVlogMain.getId()).toString(),true);
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
        if (isConnected) {
            getBaseActivity(). getInternetDialog().dismiss();
        } else {
            getBaseActivity().getInternetDialog().show();
        }
    }

    @Override
    public void getMessage(String server_not_responding_) {
        showCustomAlert(server_not_responding_);
        if(isClickedLike&&action==0){
            binding.ivLike.setImageResource(R.drawable.ic_liked);
            binding.ivLikeTop.setImageResource(R.drawable.ic_liked);
            getVlogMain.setGetLikeAction("1");
        }else if(isClickedLike && action==1){
            binding.ivLike.setImageResource(R.drawable.ic_like);
            binding.ivLikeTop.setImageResource(R.drawable.ic_like);
            getVlogMain.setGetLikeAction("0");
        }

    }
}