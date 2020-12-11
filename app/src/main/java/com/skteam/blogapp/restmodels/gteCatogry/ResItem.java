package com.skteam.blogapp.restmodels.gteCatogry;

import com.google.gson.annotations.SerializedName;

public class ResItem{

	@SerializedName("category_id")
	private String categoryId;

	@SerializedName("title")
	private String title;

	public void setCategoryId(String categoryId){
		this.categoryId = categoryId;
	}

	public String getCategoryId(){
		return categoryId;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}
}