package com.skteam.blogapp.restmodels.uploadDp;

import com.google.gson.annotations.SerializedName;

public class ResItem{

	@SerializedName("app_version")
	private String appVersion;

	@SerializedName("gender")
	private String gender;

	@SerializedName("created")
	private String created;

	@SerializedName("date_of_birth")
	private String dateOfBirth;

	@SerializedName("profile_pic")
	private String profilePic;

	@SerializedName("verified")
	private String verified;

	@SerializedName("signup_type")
	private String signupType;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("block")
	private String block;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	public String getAppVersion(){
		return appVersion;
	}

	public String getGender(){
		return gender;
	}

	public String getCreated(){
		return created;
	}

	public String getDateOfBirth(){
		return dateOfBirth;
	}

	public String getProfilePic(){
		return profilePic;
	}

	public String getVerified(){
		return verified;
	}

	public String getSignupType(){
		return signupType;
	}

	public String getUserId(){
		return userId;
	}

	public String getPhone(){
		return phone;
	}

	public String getName(){
		return name;
	}

	public String getBlock(){
		return block;
	}

	public String getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}
}