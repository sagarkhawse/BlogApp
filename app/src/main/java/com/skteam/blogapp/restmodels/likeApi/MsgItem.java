package com.skteam.blogapp.restmodels.likeApi;

import com.google.gson.annotations.SerializedName;

public class MsgItem{

	@SerializedName("response")
	private String response;

	public String getResponse(){
		return response;
	}
}