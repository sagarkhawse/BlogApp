package com.skteam.blogapp.ui.comment.pagination.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skteam.blogapp.R;
import com.skteam.blogapp.databinding.BlogItemBinding;
import com.skteam.blogapp.databinding.ItemCommentBinding;
import com.skteam.blogapp.restmodels.commentresponse.ResItem;
import com.skteam.blogapp.setting.AppConstance;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.setting.TimeAgo;
import com.skteam.blogapp.ui.comment.CommentViewModel;
import com.skteam.blogapp.ui.home.HomeActivity;
import com.skteam.blogapp.ui.viewblog.ViewBlogFragment;

import java.util.ArrayList;
import java.util.List;

import static com.skteam.blogapp.setting.AppConstance.IMG_URL;
import static com.skteam.blogapp.setting.CommonUtils.timeStamp;

public class CommentAdapter extends RecyclerView.Adapter< CommentAdapter.CommenteViewHolder> {
    private Context context;
    private List<ResItem> commentList=new ArrayList<>();
    private CommentViewModel viewModel;
    private int postion;

    public CommentAdapter(Context context, List<ResItem> commentList,CommentViewModel commentViewModel) {
        this.context = context;
        this.viewModel=commentViewModel;
        this.commentList=commentList;
    }

    @NonNull
    @Override
    public CommenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_comment, parent, false);
        return new CommenteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommenteViewHolder holder, final int position) {
        holder.OnBindView(commentList.get(position), position);
    }

    public void updaeLIst(List<ResItem> res) {
        postion=commentList.size();
        commentList.addAll(res);
        notifyItemChanged(postion);
    }
    public void LoadFirstTime(List<ResItem> res){
        commentList=res;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
    public void clear(){
        commentList.clear();
        notifyDataSetChanged();
    }

    public class CommenteViewHolder extends RecyclerView.ViewHolder {
        private ItemCommentBinding binding;

        public CommenteViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void OnBindView(final ResItem item, int position) {
            try {
                binding.date.setText(TimeAgo.getRelativeTime(item.getDate()));
                Uri uri = Uri.parse(item.getProfilePic());
                String protocol = uri.getScheme();
                String server = uri.getAuthority();
                if (protocol != null && server != null) {
                    Glide.with(context).load(item.getProfilePic()).into(binding.userDp);
                } else {
                    Glide.with(context).load(AppConstance.IMG_URL + item.getProfilePic()).into(binding.userDp);
                }
                binding.userComment.setText(item.getComment().trim());
                binding.username.setText(item.getName());
                binding.commentLikes.setText(item.getCommentLikesCount());
                if (item.getLiked().equalsIgnoreCase("0")) {
                    binding.likeImg.setImageResource(R.drawable.ic_like);
                } else {
                    binding.likeImg.setImageResource(R.drawable.ic_liked);
                }
                binding.viewReplies.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.getNavigator().callReplyFragment(item, position);
                    }
                });
                binding.reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.getNavigator().callReplyFragment(item, position);
                    }
                });
                viewModel.getAllCommentsReply(item.getCommentId(), new GetReplyResponse() {
                    @Override
                    public void addReplyResponse(List<com.skteam.blogapp.restmodels.replyAllResponse.ResItem> resItems) {
                        if (resItems != null) {
                            binding.replyLayout.setVisibility(View.VISIBLE);
                            Uri uri = Uri.parse(resItems.get(0).getProfilePic());
                            String protocol = uri.getScheme();
                            String server = uri.getAuthority();
                            if (protocol != null && server != null) {
                                Glide.with(context).load(resItems.get(0).getProfilePic()).into(binding.userDpReply);
                            } else {
                                Glide.with(context).load(AppConstance.IMG_URL + resItems.get(0).getProfilePic()).into(binding.userDpReply);
                            }

                            binding.userCommentReply.setText(resItems.get(0).getComment());
                            if (resItems.get(0).getUserName() != null && !resItems.get(0).getUserName().isEmpty()) {
                                binding.usernameReply.setText(resItems.get(0).getUserName());
                            }
                        } else {
                            binding.replyLayout.setVisibility(View.GONE);
                        }
                    }
                });


                binding.likeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getLiked().equalsIgnoreCase("0")) {
                            viewModel.setCommentLike(item.getUserId(), item.getCommentId(), "1");
                            int count = Integer.parseInt(item.getCommentLikesCount());
                            item.setCommentLikesCount(String.valueOf(count + 1));
                            item.setLiked("1");
                            notifyItemChanged(position);
                        } else {
                            viewModel.setCommentLike(item.getUserId(), item.getCommentId(), "0");
                            int count = Integer.parseInt(item.getCommentLikesCount());
                            if (count != 0) {
                                item.setCommentLikesCount(String.valueOf(count - 1));
                            }

                            item.setLiked("0");
                            notifyItemChanged(position);
                        }

                    }
                });

            }catch (Exception e){
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

}