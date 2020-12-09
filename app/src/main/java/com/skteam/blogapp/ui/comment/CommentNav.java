package com.skteam.blogapp.ui.comment;

import com.skteam.blogapp.restmodels.commentresponse.ResItem;

import java.util.List;

public interface CommentNav {
    void isLoading(boolean b);

    void getMessage(String server_not_responding_);

    void setCommentResponse(List<ResItem> res);

    void setCommentResponseFirstTime(List<ResItem> res);
}
