package com.skteam.blogapp.ui.comment;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;
import com.skteam.blogapp.restmodels.commentresponse.CommentResponse;
import com.skteam.blogapp.restmodels.commentresponse.ResItem;
import com.skteam.blogapp.restmodels.likeApi.LikeResponse;
import com.skteam.blogapp.restmodels.replyAllResponse.ReplyAllResponse;
import com.skteam.blogapp.setting.AppConstance;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.comment.pagination.adapter.GetReplyResponse;
import com.skteam.blogapp.ui.comment.pagination.datafactory.CommentDataFatory;
import com.skteam.blogapp.ui.comment.pagination.datasource.CommentDataSource;
import com.skteam.blogapp.ui.home.HomeNav;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.skteam.blogapp.setting.CommonUtils.CurrentTimeAsFormat;

public class CommentViewModel extends BaseViewModel<CommentNav> {
    private CommentDataFatory commentDataFatory;
    private MutableLiveData<CommentDataSource> sourceLiveData;
    private Executor executor;
    private LiveData<PagedList<ResItem>> getCommentBlogList;
    private MutableLiveData<List<com.skteam.blogapp.restmodels.replyAllResponse.ResItem>> GetAllReply=new MutableLiveData<>();
    public CommentViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }


    public LiveData<PagedList<ResItem>> getGetCommentDtaList() {
        return getCommentBlogList;
    }


    public void setCommentLike(String userId, String commentId, String action) {
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.LIKE_COMMENT)
                .addBodyParameter("user_id", userId)
                .addBodyParameter("comment_id", commentId)
                .addBodyParameter("action", action)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(LikeResponse.class, new ParsedRequestListener<LikeResponse>() {
                    @Override
                    public void onResponse(LikeResponse response) {
                        if (response != null && response.getCode().equalsIgnoreCase("200")) {

                        }else{

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    public void CommentNow(String blogIdMain, String userId, String comment) {
        String date= CommonUtils.CurrentTimeAsFormat(String.valueOf(System.currentTimeMillis()));

        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.COMMENT_NOW)
                .addBodyParameter("user_id", userId)
                .addBodyParameter("blog_id", blogIdMain)
                .addBodyParameter("comment", comment)
                .addBodyParameter("date", date)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("code")){
                            try {
                                if(response.getString("code").equalsIgnoreCase("200")){
                                    getNavigator().getMessageSuccess("Comment Added...");
                                }else{
                                    getNavigator().getMessage("Error:- "+response.getString("error_msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().getMessage(anError.getMessage());
                    }
                });
    }
    public void ReplyNow(String commentId, String userId, String comment) {
        String date= String.valueOf(System.currentTimeMillis());
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.REPLY_TO_COMMENT)
                .addBodyParameter("user_id", userId)
                .addBodyParameter("comment_id_replying_to", commentId)
                .addBodyParameter("comment", comment)
                .addBodyParameter("date", date)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response.has("code")){
                            try {
                                if(response.getString("code").equalsIgnoreCase("200")){
                                    getNavigator().replyDone();
                                }else{
                                    getNavigator().getMessage("Please Try Again..");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void getAllComments(String blogIdMain,int page){
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.VIEW_ALL_COMMENT)
                .addBodyParameter("user_id",getSharedPre().getUserId())
                .addBodyParameter("blog_id", blogIdMain)
                .addBodyParameter("page", String.valueOf(page))
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(CommentResponse.class, new ParsedRequestListener<CommentResponse>() {
                    @Override
                    public void onResponse(CommentResponse response) {
                        getNavigator().isLoading(false);
                        if (response != null && response.getCode().equalsIgnoreCase("200")) {
                            if (response.getRes() != null && response.getRes().size() > 0) {
                                if(page==1){
                                    getNavigator().setCommentResponseFirstTime(response.getRes());
                                }else{
                                    getNavigator().setCommentResponse(response.getRes());
                                }

                            }
                        }else{

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().isLoading(false);
                        getNavigator().getMessage("Server not Responding");
                    }
                });
    }

    public void getAllCommentsReply( String commentId, GetReplyResponse replyResponse){
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.VIEW_REPLY)
                .addBodyParameter("user_id",getSharedPre().getUserId())
                .addBodyParameter("comment_id", commentId)
                .addBodyParameter("page", "1")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(ReplyAllResponse.class, new ParsedRequestListener<ReplyAllResponse>() {
                    @Override
                    public void onResponse(ReplyAllResponse response) {
                        getNavigator().isLoading(false);
                        if (response != null && response.getCode().equalsIgnoreCase("200")) {
                            if (response.getRes() != null && response.getRes().size() > 0) {
                                replyResponse.addReplyResponse(response.getRes());
                            }else{
                                replyResponse.addReplyResponse(null);
                            }
                        }else{
                            getNavigator().getMessage("Server Not Resonding..");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().isLoading(false);
                        getNavigator().getMessage("Server not Responding");
                    }
                });
    }

}
