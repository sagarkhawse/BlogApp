package com.skteam.blogapp.restmodels.commentresponse;

import com.google.gson.annotations.SerializedName;

public class ResItem{

	@SerializedName("date")
	private String date;

	@SerializedName("blog_id")
	private String blogId;

	@SerializedName("comment_likes_count")
	private String commentLikesCount;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("name")
	private String name;

	@SerializedName("profile_pic")
	private String profilePic;

	@SerializedName("comment")
	private String comment;

	@SerializedName("comment_id")
	private String commentId;

	@SerializedName("liked")
	private String liked;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setBlogId(String blogId){
		this.blogId = blogId;
	}

	public String getBlogId(){
		return blogId;
	}

	public void setCommentLikesCount(String commentLikesCount){
		this.commentLikesCount = commentLikesCount;
	}

	public String getCommentLikesCount(){
		return commentLikesCount;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setProfilePic(String profilePic){
		this.profilePic = profilePic;
	}

	public String getProfilePic(){
		return profilePic;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setCommentId(String commentId){
		this.commentId = commentId;
	}

	public String getCommentId(){
		return commentId;
	}

	public void setLiked(String liked){
		this.liked = liked;
	}

	public String getLiked(){
		return liked;
	}
}