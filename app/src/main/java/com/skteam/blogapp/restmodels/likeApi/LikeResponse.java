package com.skteam.blogapp.restmodels.likeApi;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LikeResponse{

	@SerializedName("msg")
	private List<MsgItem> msg;

	@SerializedName("code")
	private String code;

	@SerializedName("error_msg")
	private String error_msg;

	public List<MsgItem> getMsg(){
		return msg;
	}

	public String getError_msg() {
		return error_msg;
	}

	public String getCode(){
		return code;
	}
}