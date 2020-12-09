package com.skteam.blogapp.restmodels.likeApi;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LikeResponse{

	@SerializedName("msg")
	private List<MsgItem> msg;

	@SerializedName("code")
	private String code;

	public List<MsgItem> getMsg(){
		return msg;
	}

	public String getCode(){
		return code;
	}
}