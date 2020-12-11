package com.skteam.blogapp.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseActivity;
import com.skteam.blogapp.databinding.ActivityHomeBinding;
import com.skteam.blogapp.databinding.BottomSheetBinding;
import com.skteam.blogapp.databinding.NavHeaderMainBinding;
import com.skteam.blogapp.databinding.ToolbarBinding;
import com.skteam.blogapp.restmodels.gteCatogry.ResItem;
import com.skteam.blogapp.setting.CommonUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.skteam.blogapp.setting.AppConstance.GOOGLE_REQUEST_CODE;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> implements HomeNav {
    private ActivityHomeBinding binding;
    private HomeViewModel viewModel;
    private Disposable disposable;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private NavHeaderMainBinding navigationViewHeaderBinding;
    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        return viewModel = new HomeViewModel(this, getSharedPre(), this);
    }
    public BottomSheetBinding getBottomSheet(){
        return binding.bottomSheetId;
    }

    public ToolbarBinding getToolbar(){
        return binding.toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        binding.toolbar.title.setText("Blogs");
        navigationViewHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_main, binding.navView, false);
        binding.navView.addHeaderView(navigationViewHeaderBinding.getRoot());
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheetId.bottomLay);
        BottomSheetCheck();

        disposable = RxView.clicks(binding.toolbar.back).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVibe().vibrate(100);
            if (binding.drawer.isDrawerOpen(Gravity.LEFT)) {
                binding.drawer.closeDrawer(Gravity.LEFT);
            } else {
                binding.drawer.openDrawer(Gravity.LEFT);
            }
        });
        disposable = RxView.clicks(binding.bottomSheetId.ivClose).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVibe().vibrate(100);
            getBottomSheet().bottomLay.setVisibility(View.GONE);
        });
        disposable = RxView.clicks(binding.bottomSheetId.loginViaGoogle.googleBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVibe().vibrate(100);
            CallGoogleApi();
        });
        if (savedInstanceState == null) {
            startFragment(HomeFragment.getInstance(), true, HomeFragment.getInstance().toString());
        }
    }

    private void BottomSheetCheck() {
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        binding.bottomSheetId.bottomLay.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        binding.bottomSheetId.bottomLay.setVisibility(View.GONE);
                        break;
                    default:
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (isConnected) {
            getInternetDialog().dismiss();
        } else {
            getInternetDialog().show();
        }
    }

    @Override
    public void isLoading(boolean value) {
        if(value){
          showLoadingDialog("");
        }else{
         hideLoadingDialog();
        }

    }

    @Override
    public void getMessage(String message) {

    }

    @Override
    public void StartHomeNow() {

    }

    @Override
    public void GetCatogory(List<ResItem> res) {

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

    public void startFragment(Fragment fragment,String name){
        startFragment(fragment,true,name);
    }

}