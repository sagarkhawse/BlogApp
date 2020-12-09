/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.blogapp.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.FragmentLoginBinding;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.home.HomeActivity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

import static com.skteam.blogapp.setting.AppConstance.GOOGLE_REQUEST_CODE;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel> implements LoginNav {
    private static LoginFragment instance;
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private Disposable disposable;
    private FirebaseAuth mAuth;
    private Dialog internetDialog;
    private List<String> permissions = Arrays.asList("public_profile", "email","user_status");
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public String toString() {
        return LoginFragment.class.getSimpleName();
    }

    public static LoginFragment newInstance() {
        instance = instance == null ? new LoginFragment() : instance;
        return instance;
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        return viewModel = new LoginViewModel(getContext(), getSharedPre(), getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.setNavigator(this);
        binding = getViewDataBinding();
        toolbar();
        SetClickListeners();

    }

    private void toolbar() {
        disposable = RxView.clicks(binding.toolbar.back).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVib().vibrate(100);
            getBaseActivity().onBackPressed();
        });
//        disposable = RxView.clicks(binding.toolbar.menu).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
//            getVib().vibrate(100);
//           showCustomAlert("Menu Click");
//        });
        binding.toolbar.title.setText("Login");
    }

    private void SetClickListeners() {
        disposable = RxView.clicks(binding.loginBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVib().vibrate(100);
            LoginNow();
        });

        disposable = RxView.clicks(binding.otherSigninOption.googleBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVib().vibrate(100);
            CallGoogleApi();
        });

        disposable=RxView.clicks(binding.tvForgetPass).throttleFirst(1000,TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                String email=binding.etEmail.getText().toString();
                if(!email.trim().isEmpty() && !CommonUtils.isValidEmail(email.trim())){
                    viewModel.forgotPassword(binding.etEmail.getText().toString());
                }else{
                    showCustomAlert("Incorrect E-mail Found");
                }

            }
        });
    }

    private void LoginNow() {
        setLoading(true);
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        if (email.isEmpty()) {
            showCustomAlert(getResources().getString(R.string.email_empty));
            setLoading(false);
        } else if (CommonUtils.isValidEmail(email)) {
            showCustomAlert(getResources().getString(R.string.valid_email));
            setLoading(false);
        } else if (password.isEmpty()) {
            showCustomAlert(getResources().getString(R.string.password_empty));
            setLoading(false);
        } else {
            viewModel.LoginViaEmail(email, password);
        }
    }

    private void CallGoogleApi() {
        showLoadingDialog("");
        Intent signInIntent = viewModel.getGoogleClient().getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GOOGLE_REQUEST_CODE: {
                hideLoadingDialog();
                viewModel.SignUpViaGoogle(data);
                break;
            }

        }
    }

    @Override
    public void onLoginFail(String message) {
        showCustomAlert(message);
    }

    @Override
    public void setLoading(boolean b) {
        if (b) {
            showLoadingDialog("");
        } else {
            hideLoadingDialog();
        }
    }

    @Override
    public void StartHomeNow() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
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