/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.blogapp.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.skteam.blogapp.setting.AppConstance;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.skteam.blogapp.setting.CommonUtils.getFacebookData;


public class LoginViewModel extends BaseViewModel<LoginNav> {
    private String Profile="";
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    List<String> permissions = Arrays.asList("public_profile", "email","user_status");
    public LoginViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getResources().getString(R.string.GOOGLE_SIGNIN_SECRET)).requestEmail().build();
        getGoogleClient();
    }
    public GoogleSignInClient getGoogleClient() {
        if (googleSignInClient != null) {
            return googleSignInClient;
        } else {
            return googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        }

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



    public void LoginViaEmail(String email,String pass){
        getmAuth().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = getmAuth().getCurrentUser();
                        getSharedPre().setUserId(user.getUid());
                        getSharedPre().setUserEmail(user.getEmail());
                        getNavigator().setLoading(false);
                        getSharedPre().setIsLoggedIn(true);
                        getSharedPre().setIsRegister(true);
                       // LoginClient(user.getUid(),AppConstance.LOGIN_TYPE_EMAIL);
                    } else {
                        getNavigator().setLoading(false);
                        getNavigator().onLoginFail("Username or Password Mismatch!!");
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
            getNavigator().setLoading(true);
            String finalTypeFinal = typeFinal;
            getmAuth().signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            getNavigator().setLoading(false);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = getmAuth().getCurrentUser();
                                getSharedPre().setUserId(user.getUid());
                                getSharedPre().setUserEmail(user.getEmail());
                                getSharedPre().setIsLoggedIn(true);
                                getSharedPre().setIsRegister(true);
                               // LoginClient(getSharedPre().getName(),user.getEmail(),Profile,user.getUid(),finalTypeFinal, BuildConfig.VERSION_NAME);
                            } else {
                                // If sign in fails, display a message to the user.

                                getNavigator().onLoginFail("Authentication Failed");
                            }

                            // ...
                        }
                    });
        }else{
            getNavigator().onLoginFail("Client Error");
        }

    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void signOut() {
        getmAuth().signOut();
    }


    public void forgotPassword(String email) {
        getNavigator().setLoading(true);
        getmAuth().setLanguageCode("en");
        getmAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(getActivity(), task -> {
                    if(task.isSuccessful()){
                        getNavigator().setLoading(false);
                    }else{
                        getNavigator().setLoading(false);
                        getNavigator().onLoginFail("Please try again!!");
                    }
                })
                .addOnSuccessListener((OnSuccessListener<Void>) aVoid -> {
                    getNavigator().setLoading(false);
                    getNavigator().onLoginFail(getContext().getResources().getString(R.string.password_reset_link_will_be_sent_to_your_registered_email_address));
                })
                .addOnFailureListener(e -> {
            getNavigator().setLoading(false);
            getNavigator().onLoginFail(e.getMessage());
        });
    }
}
