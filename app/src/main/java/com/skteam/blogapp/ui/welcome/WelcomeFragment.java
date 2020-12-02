/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.blogapp.ui.welcome;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.WelcomeFragmentBinding;
import com.skteam.blogapp.ui.home.HomeActivity;
import com.skteam.blogapp.ui.login.LoginFragment;
import com.skteam.blogapp.ui.signup.SignUpFragment;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class WelcomeFragment extends BaseFragment<WelcomeFragmentBinding, WelcomeViewModel> implements WelcomeNav {

    private WelcomeViewModel viewModel;
    private WelcomeFragmentBinding binding;
    private static WelcomeFragment instance;
    private Disposable disposable;

    public static WelcomeFragment newInstance() {
        instance = instance == null ? new WelcomeFragment() : instance;
        return instance;
    }

    @Override
    public String toString() {
        return WelcomeFragment.class.getSimpleName();
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.welcome_fragment;
    }

    @Override
    public WelcomeViewModel getViewModel() {
        return viewModel = new WelcomeViewModel(getContext(), getSharedPre(), getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.setNavigator(this);
        binding = getViewDataBinding();
        disposable = RxView.clicks(binding.loginBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                getVib().vibrate(100);
                getBaseActivity().startFragment(LoginFragment.newInstance(), true, LoginFragment.newInstance().toString());
            }
        });
        disposable = RxView.clicks(binding.createBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                getVib().vibrate(100);
                getBaseActivity().startFragment(SignUpFragment.newInstance(), true, SignUpFragment.newInstance().toString());
            }
        });
        disposable = RxView.clicks(binding.remindMeLater).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                getVib().vibrate(100);
                startActivity(new Intent(getBaseActivity(), HomeActivity.class));
                getBaseActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}