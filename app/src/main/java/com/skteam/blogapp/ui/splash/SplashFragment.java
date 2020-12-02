/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.blogapp.ui.splash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.iid.FirebaseInstanceId;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.FragmentSplashBinding;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.home.HomeActivity;
import com.skteam.blogapp.ui.welcome.WelcomeFragment;

import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashFragment extends BaseFragment<FragmentSplashBinding, SplashViewModel> implements SplashNav {
    private static SplashFragment instance;
    private static final int SPLASH_SCREEN_TIME_OUT=3;
    private FragmentSplashBinding binding;
    private Disposable disposable;
    private SplashViewModel viewModel;
    private Dialog internetDialog;
    public SplashFragment() {
        // Required empty public constructor
    }

    public static SplashFragment newInstance() {
        instance = instance == null ? new SplashFragment() : instance;
        return instance;
    }
    @Override
    public String toString() {
        return SplashFragment.class.getSimpleName();
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        return viewModel=new SplashViewModel(getContext(),getSharedPre(),getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        binding.companyName.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
       String tokenFinal=getSharedPre().getFirebaseDeviceToken() ;
        if (tokenFinal==null || tokenFinal.isEmpty()) {
            try {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w("Firebase", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            getSharedPre().setFirebaseToken(token);
                            getSharedPre().setUserId(FirebaseInstanceId.getInstance().getId());
                            disposable = Observable.timer(SPLASH_SCREEN_TIME_OUT, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(aLong -> StartIntent());
                        });
            } catch (Exception e) {
            }

        } else {
            disposable = Observable.timer(SPLASH_SCREEN_TIME_OUT, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> StartIntent());
        }
    }
    void StartIntent(){

        if(getSharedPre().isLoggedIn()){
           startActivity(new Intent(getBaseActivity(), HomeActivity.class));
           getBaseActivity().finish();
        }else if (getSharedPre().isRemindMeLater()){
            startActivity(new Intent(getBaseActivity(), HomeActivity.class));
            getBaseActivity().finish();
        }
        else{
            getBaseActivity().startFragment(WelcomeFragment.newInstance(),true,WelcomeFragment.newInstance().toString());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(getBaseActivity(), false);
        }
        if (isConnected) {
            internetDialog.dismiss();
        } else {
            internetDialog.show();
        }
    }
}

