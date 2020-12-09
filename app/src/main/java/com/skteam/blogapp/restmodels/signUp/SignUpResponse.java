package com.skteam.blogapp.restmodels.signUp;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse{

	@SerializedName("res")
	private List<ResItem> res;

	@SerializedName("code")
	private String code;

	public void setRes(List<ResItem> res){
		this.res = res;
	}

	public List<ResItem> getRes(){
		return res;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	@Override
 	public String toString(){
		return 
			"SignUpResponse{" + 
			"res = '" + res + '\'' + 
			",code = '" + code + '\'' + 
			"}";
		}
}