package com.skteam.blogapp.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;
import com.skteam.blogapp.restmodels.editProdile.EditProfileResponse;
import com.skteam.blogapp.restmodels.signUp.SignUpResponse;
import com.skteam.blogapp.restmodels.uploadDp.ResItem;
import com.skteam.blogapp.restmodels.uploadDp.UserUploadResponse;
import com.skteam.blogapp.setting.AppConstance;

import java.io.File;
import java.util.List;

public class ProfileViewmodel extends BaseViewModel<ProfileNav> {
private MutableLiveData<List<ResItem>> SaveUserFrofile=new MutableLiveData<>();
    public ProfileViewmodel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
    private MutableLiveData<List<ResItem>> SaveUserFrofile(File userDp){
        getNavigator().setLoading(true);
        AndroidNetworking.upload(AppConstance.API_BASE_URL + AppConstance.UPLOAD_DP)
                .addQueryParameter("user_id", getSharedPre().getUserId())
                .addMultipartFile("file",userDp)
                .setPriority(Priority.HIGH)

                .build()
                .getAsObject(UserUploadResponse.class, new ParsedRequestListener<UserUploadResponse>() {
                    @Override
                    public void onResponse(UserUploadResponse response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                SaveUserFrofile.postValue(response.getRes());
                            } else {
                                getNavigator().setMessage("Server Not Responding");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setLoading(false);
                    }
                });
        return SaveUserFrofile;
    }
    public LiveData<List<ResItem>>UploadProfile(File dp){
        return SaveUserFrofile(dp);
    }
    public void getAllLoginInformation(){
        if(getSharedPre().getUserId()!=null && !getSharedPre().getUserId().isEmpty()) {
            String userId=getSharedPre().getUserId();
            AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.SIGN_UP)
                    .addBodyParameter("user_id", userId)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsObject(SignUpResponse.class, new ParsedRequestListener<SignUpResponse>() {
                        @Override
                        public void onResponse(SignUpResponse response) {

                            if (response != null) {
                                if (response.getCode().equals("200")) {
                                    getSharedPre().setName(response.getRes().get(0).getName());
                                    getSharedPre().setUserEmail(response.getRes().get(0).getEmail());
                                    getSharedPre().setClientProfile(response.getRes().get(0).getProfilePic());
                                    getNavigator().SetDataNow(response.getRes().get(0));
                                } else {
                                    getNavigator().setMessage("Server Not Responding");
                                }
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            getNavigator().setMessage("Server Not Responding");
                        }
                    });
        }
    }
    public void EditNow(String name,String phone,String dob,String gender){
        getNavigator().setLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.UPLOAD_USER_DATA)
                .addBodyParameter("user_id", name)
                .addBodyParameter("name", getSharedPre().getUserId())
                .addBodyParameter("phone", phone)
                .addBodyParameter("date_of_birth", dob)
                .addBodyParameter("gender", gender)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(EditProfileResponse.class, new ParsedRequestListener<EditProfileResponse>() {
                    @Override
                    public void onResponse(EditProfileResponse response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                getSharedPre().setName(response.getRes().get(0).getName());
                                getSharedPre().setUserEmail(response.getRes().get(0).getEmail());
                                getSharedPre().setClientProfile(response.getRes().get(0).getProfilePic());
                                getNavigator().OkDone();
                            } else {
                                getNavigator().setMessage("Server Not Responding");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setMessage("Server Not Responding");
                        getNavigator().setLoading(false);
                    }
                });
    }

}
