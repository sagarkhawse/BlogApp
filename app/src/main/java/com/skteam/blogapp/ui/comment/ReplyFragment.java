package com.skteam.blogapp.ui.comment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.FragmentCommentBinding;
import com.skteam.blogapp.databinding.ReplyLayoutBinding;
import com.skteam.blogapp.restmodels.commentresponse.ResItem;
import com.skteam.blogapp.setting.AppConstance;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.setting.TimeAgo;
import com.skteam.blogapp.ui.comment.pagination.PaginationScrollListener;
import com.skteam.blogapp.ui.comment.pagination.adapter.CommentAdapter;
import com.skteam.blogapp.ui.comment.pagination.adapter.GetReplyResponse;
import com.skteam.blogapp.ui.comment.pagination.adapter.ReplyAdapter;

import java.util.List;

import io.reactivex.disposables.Disposable;


public class ReplyFragment extends BaseFragment<ReplyLayoutBinding, CommentViewModel> implements CommentNav {
    private ReplyLayoutBinding binding;
    private CommentViewModel viewModel;
    private static String commentId;
    private static ReplyFragment instance;
    private ReplyAdapter adapter;
    private static ResItem getComment;
    int page = 1;
    private Disposable disposable;

    public ReplyFragment() {
        // Required empty public constructor
    }

    @Override
    public String toString() {
        return "ReplyFragment";
    }

    public static ReplyFragment getInstance(ResItem comment) {
        commentId = comment.getCommentId();
        getComment = comment;
        return instance = instance == null ? new ReplyFragment() : instance;
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.reply_layout;
    }

    @Override
    public CommentViewModel getViewModel() {
        return viewModel = new CommentViewModel(getContext(), getSharedPre(), getBaseActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        binding.toolbar.title.setText("Reply");
        binding.toolbar.back.setImageDrawable(getResources().getDrawable(R.drawable.ic_left_arrow));
        binding.toolbar.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().onBackPressed();
            }
        });
        adapter = new ReplyAdapter(getContext(), viewModel);
        binding.replayRecyclerView.setAdapter(adapter);
        binding.inputComment.setHint(getContext().getResources().getString(R.string.replayAs) + " " + getSharedPre().getName());
        viewModel.getAllCommentsReply(getComment.getCommentId(), new GetReplyResponse() {
            @Override
            public void addReplyResponse(List<com.skteam.blogapp.restmodels.replyAllResponse.ResItem> res) {
                adapter.updateList(res);
            }
        });

        Uri uri = Uri.parse(getComment.getProfilePic());
        String protocol = uri.getScheme();
        String server = uri.getAuthority();
        if (protocol != null && server != null) {
            Glide.with(getContext()).load(getComment.getProfilePic()).into(binding.userDpComment);
        } else {
            Glide.with(getContext()).load(AppConstance.IMG_URL + getComment.getProfilePic()).into(binding.userDpComment);
        }
        Uri uri1 = Uri.parse(getSharedPre().getClientProfile());
        String protocol1 = uri1.getScheme();
        String server1 = uri1.getAuthority();
        if (protocol1 != null && server1 != null) {
            Glide.with(getContext()).load(getSharedPre().getClientProfile()).into(binding.userDpReply);
        } else {
            Glide.with(getContext()).load(AppConstance.IMG_URL + getSharedPre().getClientProfile()).into(binding.userDpReply);
        }
        binding.userComment.setText(getComment.getComment());
        if (getComment.getName() != null && !getComment.getName().isEmpty()) {
            binding.usernameComment.setText(getComment.getName());
        }
        binding.dateUserComment.setText(TimeAgo.getRelativeTime(getComment.getDate()));
        if (getComment.getLiked().equalsIgnoreCase("0")) {
            binding.likeImgUserComment.setImageResource(R.drawable.ic_like);
        } else {
            binding.likeImgUserComment.setImageResource(R.drawable.ic_liked);
        }
        binding.likeImgUserComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getComment.getLiked().equalsIgnoreCase("0")) {
                    viewModel.setCommentLike(getSharedPre().getUserId(), getComment.getCommentId(), "1");
                    binding.likeImgUserComment.setImageResource(R.drawable.ic_liked);
                    getComment.setLiked("1");
                } else {
                    viewModel.setCommentLike(getSharedPre().getUserId(), getComment.getCommentId(), "0");
                    binding.likeImgUserComment.setImageResource(R.drawable.ic_like);
                    getComment.setLiked("0");
                }
            }
        });

        binding.postReplyClick.setOnClickListener(v -> {
            getVib().vibrate(100);
            if (!binding.inputComment.getText().toString().trim().isEmpty()) {
                viewModel.ReplyNow(getComment.getCommentId(), getSharedPre().getUserId(), binding.inputComment.getText().toString().trim());

                binding.inputComment.setText("");
            } else {
                showCustomAlert("Comment not be Empty!!");
            }
        });


    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            getBaseActivity().getInternetDialog().dismiss();
        } else {
            getBaseActivity().getInternetDialog().show();
        }
    }

    @Override
    public void isLoading(boolean b) {

        if (b) {
            showLoadingDialog("");
        } else {
            hideLoadingDialog();
        }
    }

    @Override
    public void getMessage(String message) {
        showCustomAlert(message);
    }

    @Override
    public void setCommentResponse(List<ResItem> res) {

    }

    @Override
    public void setCommentResponseFirstTime(List<ResItem> res) {

    }

    @Override
    public void callReplyFragment(ResItem item,int postion) {

    }

    @Override
    public void replyDone() {
        viewModel.getAllCommentsReply(getComment.getCommentId(), new GetReplyResponse() {
            @Override
            public void addReplyResponse(List<com.skteam.blogapp.restmodels.replyAllResponse.ResItem> res) {
                adapter.updateList(res);
            }
        });
    }

    @Override
    public void getMessageSuccess(String s) {

    }
}