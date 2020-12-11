package com.skteam.blogapp.restmodels.editProdile;

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

	public void setAppVersion(String appVersion){
		this.appVersion = appVersion;
	}

	public String getAppVersion(){
		return appVersion;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}

	public void setDateOfBirth(String dateOfBirth){
		this.dateOfBirth = dateOfBirth;
	}

	public String getDateOfBirth(){
		return dateOfBirth;
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

	public void setSignupType(String signupType){
		this.signupType = signupType;
	}

	public String getSignupType(){
		return signupType;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setBlock(String block){
		this.block = block;
	}

	public String getBlock(){
		return block;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}