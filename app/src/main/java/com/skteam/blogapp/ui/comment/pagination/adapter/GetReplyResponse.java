package com.skteam.blogapp.ui.comment.pagination.adapter;

import com.skteam.blogapp.restmodels.replyAllResponse.ResItem;

import java.util.List;

public interface GetReplyResponse {
    void addReplyResponse(List<ResItem> res);
}
