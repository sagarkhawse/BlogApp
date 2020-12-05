package com.skteam.blogapp.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseActivity;
import com.skteam.blogapp.databinding.ActivityHomeBinding;
import com.skteam.blogapp.databinding.BottomSheetBinding;
import com.skteam.blogapp.setting.CommonUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> implements HomeNav {
    private ActivityHomeBinding binding;
    private HomeViewModel viewModel;
    private Dialog internetDialog;
    private Disposable disposable;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior sheetBehavior;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        binding.toolbar.title.setText("Blogs");
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheetId.bottomLay);
        BottomSheetCheck();

        disposable = RxView.clicks(binding.toolbar.back).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVibe().vibrate(100);
            onBackPressed();
        });
        disposable = RxView.clicks(binding.toolbar.menu).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVibe().vibrate(100);
            showCustomAlert("Menu Click");
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
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(this, false);
        }
        if (isConnected) {
            internetDialog.dismiss();
        } else {
            internetDialog.show();
        }
    }

    @Override
    public void isLoading(boolean value) {

    }

    @Override
    public void getMessage(String message) {

    }
}