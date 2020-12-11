package com.skteam.blogapp.ui.profile;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;
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
        AndroidNetworking.upload(AppConstance.API_BASE_URL + AppConstance.SIGN_UP)
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
}
