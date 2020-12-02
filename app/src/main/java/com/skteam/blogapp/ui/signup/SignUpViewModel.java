/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.blogapp.ui.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;
import com.skteam.blogapp.setting.AppConstance;

import org.json.JSONObject;
import java.util.Arrays;
import java.util.List;

import static com.skteam.blogapp.setting.CommonUtils.getFacebookData;

public class SignUpViewModel extends BaseViewModel<SignUpNav> {

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    List<String> permissions = Arrays.asList("public_profile", "email","user_status");
    String Profile="";

    public SignUpViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getResources().getString(R.string.GOOGLE_SIGNIN_SECRET)).requestEmail().build();
        getGoogleClient();
       // registerFBCallBack();
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public GoogleSignInClient getGoogleClient() {
        if (googleSignInClient != null) {
            return googleSignInClient;
        } else {
            return googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        }

    }
    public void SignupNow(String name,String Email,String password) {
        getNavigator().setLoading(true);
        getNavigator().setLoading(true);
        getSharedPre().setIsRegister(true);
        getSharedPre().setUserEmail(Email);
        getSharedPre().setClientId("");
        getSharedPre().setName(name);
        getSharedPre().setClientProfile("");
        getSharedPre().setIsGoogleLoggedIn(false);
        getSharedPre().setIsFaceboobkLoggedIn(false);
        SignUpViaEmail(Email,password);
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
            String acountToken=account.getIdToken();
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
            firebaseAuthWithClient(acountToken, AppConstance.LOGIN_TYPE_GOOGLE);
        } catch (ApiException e) {
            Log.e("googleStatus", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    //signUpAPI
 /*   private void SignuViaClient(String name, String email, String profilePic,String userId,String clientType,String deviceVersion ,String type) {
        getNavigator().setLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.SIGN_UP)
                .addBodyParameter("user_id", userId)
                .addBodyParameter("name", name)
                .addBodyParameter("email", email)
                .addBodyParameter("app_version", deviceVersion)
                .addBodyParameter("verified", "1")
                .addBodyParameter("signup_type", clientType)
                .addBodyParameter("profile_pic", profilePic)
                .addBodyParameter("phone", "0")
                .addBodyParameter("gender", "")
                .addBodyParameter("date_of_birth", "")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(ResponseSignUp.class, new ParsedRequestListener<ResponseSignUp>() {
                    @Override
                    public void onResponse(ResponseSignUp response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                getNavigator().setMessage("Signup Successfully");
                                getNavigator().SignUpResponse(response.getRes().get(0),type);
                            } else {
                                getNavigator().setMessage("Please try again later!");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setLoading(false);
                        getNavigator().setMessage("Server not Responding");
                    }
                });
    }*/
    //facebook
    public void registerFBCallBack() {
        callbackManager = CallbackManager.Factory.create();
       // LoginManager.getInstance().logInWithReadPermissions(getActivity(),permissions);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String accessToken = loginResult.getAccessToken().getToken();
                        getSharedPre().setFaceBookAccessToken(accessToken);
                        Log.i("FBstatus accessToken", accessToken);
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                // Get facebook data from login
                                Bundle bFacebookData = getFacebookData(object);
                                if (bFacebookData != null) {
                                    Log.d("FBstatus", "Welcome" + " " + bFacebookData.getString("first_name"));
                                    String name=bFacebookData.getString("first_name","")+bFacebookData.getString("last_name","");
                                    String email=bFacebookData.getString("email","");
                                    String profilePic=bFacebookData.getString("profile_pic","");
                                    String facebookId=bFacebookData.getString("idFacebook");
                                    getSharedPre().setIsGoogleLoggedIn(false);
                                    getSharedPre().setIsFaceboobkLoggedIn(true);
                                    getSharedPre().setIsLoggedIn(false);
                                    getSharedPre().setIsRegister(true);
                                    getSharedPre().setUserEmail(email);
                                    getSharedPre().setClientProfile(profilePic);
                                    Profile=profilePic;
                                    getSharedPre().setName(name);
                                    getSharedPre().setClientId(facebookId);
                                    firebaseAuthWithClient(accessToken,AppConstance.LOGIN_TYPE_FB);
                                    //SignuViaClient(name,email , profilePic,getSharedPre().getUserId() , AppConstance.LOGIN_TYPE_FB,BuildConfig.VERSION_NAME);
                                } else{
                                    getNavigator().onLoginFail();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("FBstatus", "canceled");
                        getNavigator().onLoginFail();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e("FBstatus", exception.getMessage());
                        getNavigator().onLoginFail();
                    }
                });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    try {
                        getNavigator().onLoginFail();
                    } catch (NullPointerException ex) {

                    }
                }
            }
        };
        accessTokenTracker.startTracking();

    }
    //firebase login via email and pass
    public void SignUpViaEmail(String email,String pass){
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getNavigator().setLoading(false);
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            getSharedPre().setUserId(user.getUid());
                            user.sendEmailVerification();
                           // SignuViaClient(getSharedPre().getName(),user.getEmail() , "",user.getUid() , AppConstance.LOGIN_TYPE_EMAIL,BuildConfig.VERSION_NAME,AppConstance.LOGIN_TYPE_EMAIL);

                        } else {
                            getNavigator().setLoading(false);
                            getNavigator().setMessage("Authentication failed.");
                        }
                    }

                });
    }
    //firebase login via client
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
                credential =FacebookAuthProvider.getCredential(idToken);
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
            getNavigator().setLoading(true);
            String finalTypeFinal = typeFinal;
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                getNavigator().setLoading(false);
                                FirebaseUser user = mAuth.getCurrentUser();
                                getSharedPre().setUserId(user.getUid());
                              //  SignuViaClient(getSharedPre().getName(),user.getEmail(), Profile,getSharedPre().getUserId(),finalTypeFinal, BuildConfig.VERSION_NAME, finalTypeFinal);
                            } else {
                                // If sign in fails, display a message to the user.
                                getNavigator().setMessage("Authentication Failed");
                                getNavigator().setLoading(false);
                            }

                            // ...
                        }
                    });
        }else{
            getNavigator().setMessage("Client Error");
        }

    }

    public void signOut() {
        mAuth.signOut();
    }

}