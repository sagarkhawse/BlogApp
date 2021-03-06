package com.skteam.blogapp.ui.comment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.FragmentCommentBinding;
import com.skteam.blogapp.restmodels.commentresponse.ResItem;
import com.skteam.blogapp.setting.AppConstance;
import com.skteam.blogapp.ui.comment.pagination.PaginationScrollListener;
import com.skteam.blogapp.ui.comment.pagination.adapter.CommentAdapter;
import com.skteam.blogapp.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;


public class CommentFragment extends BaseFragment<FragmentCommentBinding, CommentViewModel> implements CommentNav {
    private FragmentCommentBinding binding;
    private CommentViewModel viewModel;
    private static String blogIdMain;
    private static CommentFragment instance;
    private PagedList<ResItem> getAllComments;
    private CommentAdapter adapter;
    private List<ResItem> commentList=new ArrayList<>();
    int page=1;
    private Disposable disposable;

    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment getInstance(String blogId) {
        blogIdMain = blogId;
        return instance = instance == null ? new CommentFragment() : instance;
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_comment;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.toolbar.title.setText("Comments");
        binding.toolbar.back.setImageDrawable(getResources().getDrawable(R.drawable.ic_left_arrow));
        binding.toolbar.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().onBackPressed();
            }
        });
        adapter = new CommentAdapter(getContext(),commentList, viewModel);
        binding.recyclerView.setAdapter(adapter);
        loadFirstPage();
        Uri uri = Uri.parse(getSharedPre().getClientProfile());
        String protocol = uri.getScheme();
        String server = uri.getAuthority();
        if (protocol != null && server != null) {
            Glide.with(getContext()).load(getSharedPre().getClientProfile()).into(binding.userDp);
        } else {
            Glide.with(getContext()).load(AppConstance.IMG_URL + getSharedPre().getClientProfile()).into(binding.userDp);
        }

        binding.inputComment.setHint(getContext().getResources().getString(R.string.comment_as) + " " + getSharedPre().getName());
        binding.postClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.inputComment.getText().toString().trim().isEmpty()) {
                    viewModel.CommentNow(blogIdMain, getSharedPre().getUserId(), binding.inputComment.getText().toString().trim());
                    binding.inputComment.setText("");
                } else {
                    showCustomAlert("Comment not be Empty!!");
                }
            }
        });

        binding.recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });



    }
    private void loadNextPage() {
        viewModel.getAllComments(blogIdMain, page);
    }
    private void loadFirstPage() {
        // fetching dummy data
        page=1;
        viewModel.getAllComments(blogIdMain, page);
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
        commentList=res;
        adapter.updaeLIst(res);
    }

    @Override
    public void setCommentResponseFirstTime(List<ResItem> res) {
        page++;
        adapter.LoadFirstTime(res);
    }

    @Override
    public void callReplyFragment(ResItem item,int postion) {
        getBaseActivity().startFragment(ReplyFragment.getInstance(item),true,ReplyFragment.getInstance(item).toString());
    }

    @Override
    public void replyDone() {

    }

    @Override
    public void getMessageSuccess(String s) {
        loadFirstPage();
    }
}