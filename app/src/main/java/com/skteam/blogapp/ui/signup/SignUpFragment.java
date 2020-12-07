
package com.skteam.blogapp.ui.signup;

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
import com.skteam.blogapp.databinding.SignUpFragmentBinding;
import com.skteam.blogapp.setting.CommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.skteam.blogapp.setting.AppConstance.GOOGLE_REQUEST_CODE;

public class SignUpFragment extends BaseFragment<SignUpFragmentBinding, SignUpViewModel> implements SignUpNav {

    private SignUpViewModel viewModel;
    private SignUpFragmentBinding binding;
    private static SignUpFragment instance;

    private Disposable disposable;
    List<String> permissions = Arrays.asList("public_profile", "email","user_status");
    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.sign_up_fragment;
    }

    @Override
    public String toString() {
        return SignUpFragment.class.getSimpleName();
    }

    public static SignUpFragment newInstance() {
        instance = instance == null ? new SignUpFragment() : instance;
        return instance;
    }

    @Override
    public SignUpViewModel getViewModel() {
        return viewModel = new SignUpViewModel(getContext(), getSharedPre(), getBaseActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);

        SetClickListeners();
    }

    private void SetClickListeners() {

        disposable = RxView.clicks(binding.createBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVib().vibrate(100);
            SignupNow();
        });
        disposable = RxView.clicks(binding.otherSigninOption.googleBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVib().vibrate(100);
            CallGoogleApi();
        });

    }

    private void CallGoogleApi() {
        showLoadingDialog("");
        Intent signInIntent = viewModel.getGoogleClient().getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GOOGLE_REQUEST_CODE:{
                hideLoadingDialog();
                viewModel.SignUpViaGoogle(data);
                break;
            }

        }
    }

    private void SignupNow() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        if (name.isEmpty()) {
          showCustomAlert( "Name can't be empty.");
        } else if (email.isEmpty()) {
            showCustomAlert( getResources().getString(R.string.email_empty));
        }else if (CommonUtils.isValidEmail(email)) {
            showCustomAlert(  getResources().getString(R.string.valid_email));
        }else if(password.isEmpty()){
            showCustomAlert( getResources().getString(R.string.password_empty));
        }else{
            viewModel.SignupNow(name,email,password);
        }
    }

    @Override
    public void onLoginFail() {
        hideLoadingDialog();
        showCustomAlert("Login failed Please try again");
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
    public void setMessage(String message) {
        showCustomAlert(message);
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}