/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.blogapp.ui.splash;

import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseActivity;
import com.skteam.blogapp.databinding.SplashActivityBinding;
import com.skteam.blogapp.setting.CommonUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity<SplashActivityBinding, SplashViewModel> {
    private SplashActivityBinding binding;
    private SplashViewModel viewModel;
    private Dialog internetDialog;

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.splash_activity;
    }

    @Override
    public SplashViewModel getViewModel() {
        return viewModel = new SplashViewModel(this, getSharedPre(), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        printHashKey();
        getSharedPre().setUserId("007");
        getSharedPre().setIsLoggedIn(true);
        if (fragment == null) {
            //start splash fragment only first time
            startFragment(SplashFragment.newInstance());
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(this, false);
        }
        if (isConnected) {
            internetDialog.dismiss();
        } else {
            internetDialog.show();
        }
    }

    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.skteam.blogapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                String sha1 = Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT);
                Log.e("sha1", sha1);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}