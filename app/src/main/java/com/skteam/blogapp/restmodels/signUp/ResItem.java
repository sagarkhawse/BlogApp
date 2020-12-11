package com.skteam.blogapp.restmodels.signUp;

import com.google.gson.annotations.SerializedName;

public class ResItem{

	@SerializedName("app_version")
	private String appVersion;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("name")
	private String name;

	@SerializedName("profile_pic")
	private String profilePic;

	@SerializedName("verified")
	private String verified;

	@SerializedName("email")
	private String email;

	@SerializedName("signup_type")
	private String signupType;
	@SerializedName("gender")
	private String gender;

	@SerializedName("date_of_birth")
	private String dob;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setAppVersion(String appVersion){
		this.appVersion = appVersion;
	}

	public String getAppVersion(){
		return appVersion;
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

	public void setVerified(String verified){
		this.verified = verified;
	}

	public String getVerified(){
		return verified;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setSignupType(String signupType){
		this.signupType = signupType;
	}

	public String getSignupType(){
		return signupType;
	}

	@Override
 	public String toString(){
		return 
			"ResItem{" + 
			"app_version = '" + appVersion + '\'' + 
			",user_id = '" + userId + '\'' + 
			",name = '" + name + '\'' + 
			",profile_pic = '" + profilePic + '\'' + 
			",verified = '" + verified + '\'' + 
			",email = '" + email + '\'' + 
			",signup_type = '" + signupType + '\'' + 
			"}";
		}
}