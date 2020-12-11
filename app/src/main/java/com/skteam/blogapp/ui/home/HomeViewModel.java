package com.skteam.blogapp.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.skteam.blogapp.BuildConfig;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;
import com.skteam.blogapp.restmodels.getBlogs.ResItem;
import com.skteam.blogapp.restmodels.gteCatogry.CatResponse;
import com.skteam.blogapp.restmodels.likeApi.LikeResponse;
import com.skteam.blogapp.restmodels.signUp.SignUpResponse;
import com.skteam.blogapp.setting.AppConstance;
import com.skteam.blogapp.ui.home.pagination.datasource.BlogDataSource;
import com.skteam.blogapp.ui.home.pagination.factory.BlogDataFatory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeViewModel extends BaseViewModel<HomeNav> {
    private LiveData<PagedList<ResItem>> getBlogList;
    private BlogDataSource blogDataSource;
    private BlogDataFatory blogDataFatory;
    private LiveData<BlogDataSource> sourceLiveData;
    private Executor executor;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    private String Profile="";
    private PagedList.Config config;
    public HomeViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getResources().getString(R.string.GOOGLE_SIGNIN_SECRET)).requestEmail().build();
        getGoogleClient();

    }
    public void getCatogory(){
        GetCategory();
    }
    public void LoadPaging(HomeNav homeNav,String catId){
        blogDataFatory= new BlogDataFatory(homeNav,getSharedPre().getUserId(),catId);
        //GetLiveSource
        sourceLiveData=blogDataFatory.GetBlogsLive();
        //set PageList Config
         config= (new PagedList.Config.Builder()).setEnablePlaceholders(false).setInitialLoadSizeHint(10)
                .setPageSize(5).setPrefetchDistance(4).build();
        //ThreadPool
        executor= Executors.newFixedThreadPool(5);
        //Sent LiveBarList
        getBlogList=(new LivePagedListBuilder<Long,ResItem>(blogDataFatory,config)).setFetchExecutor(executor).build();
    }


    public LiveData<PagedList<ResItem>> getGeBarDtaList() {
        return getBlogList;
    }
    public GoogleSignInClient getGoogleClient() {
        if (googleSignInClient != null) {
            return googleSignInClient;
        } else {
            return googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        }

    }
    private void GetCategory() {
        AndroidNetworking.get(AppConstance.API_BASE_URL + AppConstance.GET_CATOGRY)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(CatResponse.class, new ParsedRequestListener<CatResponse>() {
                    @Override
                    public void onResponse(CatResponse response) {
                        if (response != null && response.getCode().equalsIgnoreCase("200")) {
                            getNavigator().GetCatogory(response.getRes());

                        }else{
                            getNavigator().getMessage("Server not Responding ");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        getNavigator().getMessage("Server not Responding");
                    }
                });
    }
    //google
    public void SignUpViaGoogle(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String email = account.getEmail();
            String name = account.getDisplayName();
            String gSocialId = account.getId();
            String profilePic = "";
            if (account.getPhotoUrl() != null)
                profilePic = account.getPhotoUrl().toString();
            Log.d("googleStatus", "" + email + " " + name + " " + " " + profilePic);
            String first = "", last = "";
            if (!TextUtils.isEmpty(name) && name.split("\\w+").length > 1) {
                first = name.substring(0, name.lastIndexOf(' '));
                last = name.substring(name.lastIndexOf(" ") + 1);
            } else
                first = name;
            getSharedPre().setIsGoogleLoggedIn(true);
            getSharedPre().setIsFaceboobkLoggedIn(false);
            getSharedPre().setIsLoggedIn(true);
            getSharedPre().setIsRegister(true);
            getSharedPre().setUserEmail(email);
            getSharedPre().setName(name);
            getSharedPre().setClientId(gSocialId);
            Profile=profilePic;
            getSharedPre().setClientProfile(profilePic);
            firebaseAuthWithClient(account.getIdToken(), AppConstance.LOGIN_TYPE_GOOGLE);
        } catch (ApiException e) {
            Log.e("googleStatus", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    private void firebaseAuthWithClient(String idToken,String type) {
        AuthCredential credential=null;
        String typeFinal =null;
        switch(type){
            case AppConstance.LOGIN_TYPE_GOOGLE:{
                credential = GoogleAuthProvider.getCredential(idToken, null);
                Profile=getSharedPre().getClientProfile();
                typeFinal=AppConstance.LOGIN_TYPE_GOOGLE;
                break;
            }
            case AppConstance.LOGIN_TYPE_FB:{
                credential = FacebookAuthProvider.getCredential(idToken);
                Profile=getSharedPre().getClientProfile();
                typeFinal=AppConstance.LOGIN_TYPE_FB;
                break;
            }
            default:{
                credential=null;
                Profile="";
            }
        }
        if(credential!=null){
            getNavigator().isLoading(true);
            String finalTypeFinal = typeFinal;
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            getNavigator().isLoading(false);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                getSharedPre().setUserId(user.getUid());
                                getSharedPre().setUserEmail(user.getEmail());
                                getSharedPre().setIsLoggedIn(true);
                                getSharedPre().setIsRegister(true);
                                 LoginClient(getSharedPre().getName(),user.getEmail(),Profile,user.getUid(),finalTypeFinal, BuildConfig.VERSION_NAME);
                            } else {
                                // If sign in fails, display a message to the user.

                                getNavigator().getMessage("Authentication Failed");
                            }

                            // ...
                        }
                    });
        }else{

        }

    }

    private void LoginClient(String name, String email, String profile, String uid, String finalTypeFinal, String versionName) {
        getNavigator().isLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.SIGN_UP)
                .addBodyParameter("user_id", uid)
                .addBodyParameter("name", name)
                .addBodyParameter("email", email)
                .addBodyParameter("app_version", versionName)
                .addBodyParameter("verified", "1")
                .addBodyParameter("signup_type", finalTypeFinal)
                .addBodyParameter("profile_pic", profile)
                .addBodyParameter("phone", "0")
                .addBodyParameter("gender", "")
                .addBodyParameter("date_of_birth", "")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(SignUpResponse.class, new ParsedRequestListener<SignUpResponse>() {
                    @Override
                    public void onResponse(SignUpResponse response) {
                        getNavigator().isLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                getSharedPre().setName(response.getRes().get(0).getName());
                                getSharedPre().setUserEmail(response.getRes().get(0).getEmail());
                                getNavigator().StartHomeNow();
                            } else {
                                getNavigator().getMessage("Server Not Responding");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().isLoading(false);
                        getNavigator().getMessage("Server Not Responding");
                    }
                });
    }

    public void getAllLoginInformation(){
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.SIGN_UP)
                .addBodyParameter("user_id", getSharedPre().getUserId())
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(SignUpResponse.class, new ParsedRequestListener<SignUpResponse>() {
                    @Override
                    public void onResponse(SignUpResponse response) {
                        getNavigator().isLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                getSharedPre().setName(response.getRes().get(0).getName());
                                getSharedPre().setUserEmail(response.getRes().get(0).getEmail());
                                getSharedPre().setClientProfile(response.getRes().get(0).getProfilePic());
                                getNavigator().StartHomeNow();
                            } else {
                                getNavigator().getMessage("Server Not Responding");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().isLoading(false);
                        getNavigator().getMessage("Server Not Responding");
                    }
                });
    }
}