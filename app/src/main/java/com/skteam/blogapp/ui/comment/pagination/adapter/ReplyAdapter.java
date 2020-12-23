package com.skteam.blogapp.ui.comment.pagination.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skteam.blogapp.R;
import com.skteam.blogapp.databinding.ReplyLayItemBinding;
import com.skteam.blogapp.restmodels.replyAllResponse.ResItem;
import com.skteam.blogapp.setting.AppConstance;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.comment.CommentViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {
    private Context context;
    private List<ResItem> replyList = new ArrayList<>();
    private CommentViewModel viewModel;


    public ReplyAdapter(Context context, CommentViewModel commentViewModel) {
        this.context = context;
        this.viewModel = commentViewModel;
    }


    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReplyLayItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.reply_lay_item, parent, false);
        return new ReplyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        holder.OnBindView(replyList.get(position), position);
    }


    public void updateList(List<ResItem> res) {
        replyList = res;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(replyList!=null && replyList.size()>0){
            return replyList.size();
        }else{
            return 0;
        }

    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        private ReplyLayItemBinding binding;

        public ReplyViewHolder(ReplyLayItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void OnBindView(final ResItem item, int position) {
            if (item != null) {
                Uri uri = Uri.parse(item.getProfilePic());
                String protocol = uri.getScheme();
                String server = uri.getAuthority();
                if (protocol != null && server != null) {
                    Glide.with(context).load(item.getProfilePic()).into(binding.userDpReply);
                } else {
                    Glide.with(context).load(AppConstance.IMG_URL + item.getProfilePic()).into(binding.userDpReply);
                }
                binding.userCommentReply.setText(item.getComment().trim());
                binding.usernameReply.setText(item.getUserName());
                binding.likeCount.setText(item.getCommentLikesCount());
                if (item.getLiked().equalsIgnoreCase("0")) {
                    binding.likeImg.setImageResource(R.drawable.ic_like);
                } else {
                    binding.likeImg.setImageResource(R.drawable.ic_liked);
                }
                binding.dateReply.setText(CommonUtils.dateFormat(item.getDate()));
                binding.likeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getLiked().equalsIgnoreCase("0")) {
                            // viewModel.setReplyLike(viewModel.getSharedPre().getUserId(), item.getCommentId(), "1");
                            int count = Integer.parseInt(item.getCommentLikesCount());
                            item.setCommentLikesCount(String.valueOf(count + 1));
                            item.setLiked("1");
                            notifyItemChanged(position);
                        } else {
                            // viewModel.setReplyLike(viewModel.getSharedPre().getUserId(), item.getCommentId(), "0");
                            int count = Integer.parseInt(item.getCommentLikesCount());
                            if (count != 0) {
                                item.setCommentLikesCount(String.valueOf(count - 1));
                            }

                            item.setLiked("0");
                            notifyItemChanged(position);
                        }

                    }
                });


            }
        }
    }

}