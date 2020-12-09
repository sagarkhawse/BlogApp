package com.skteam.blogapp.restmodels.replyAllResponse;

import com.google.gson.annotations.SerializedName;

public class ResItem{

	@SerializedName("date")
	private String date;

	@SerializedName("comment_likes_count")
	private String commentLikesCount;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("user_name")
	private String userName;

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

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
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