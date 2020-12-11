package com.skteam.blogapp.restmodels.uploadDp;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserUploadResponse{

	@SerializedName("res")
	private List<ResItem> res;

	@SerializedName("code")
	private String code;

	public List<ResItem> getRes(){
		return res;
	}

	public String getCode(){
		return code;
	}
}