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
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseActivity;
import com.skteam.blogapp.databinding.ActivityHomeBinding;
import com.skteam.blogapp.databinding.BottomSheetBinding;
import com.skteam.blogapp.databinding.NavHeaderMainBinding;
import com.skteam.blogapp.databinding.ToolbarBinding;
import com.skteam.blogapp.restmodels.gteCatogry.ResItem;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.ui.login.LoginFragment;
import com.skteam.blogapp.ui.profile.ProfileFragment;
import com.skteam.blogapp.ui.splash.SplashActivity;

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

    public BottomSheetBinding getBottomSheet() {
        return binding.bottomSheetId;
    }

    public ToolbarBinding getToolbar() {
        return binding.toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        binding.toolbar.title.setText("Blogs");
        if (getSharedPre().getUserId() == null) {
            getSharedPre().setUserId("007");
        }

        navigationViewHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_main, binding.navView, false);
        binding.navView.addHeaderView(navigationViewHeaderBinding.getRoot());
        if (getSharedPre().isLoggedIn()) {
            navigationViewHeaderBinding.btnLogout.setText(getString(R.string.logout));
            navigationViewHeaderBinding.navHeaderTitle.setText(getSharedPre().getName());
            navigationViewHeaderBinding.navHeaderSubtitle.setText(getSharedPre().getUserEmail());
        } else {
            navigationViewHeaderBinding.btnLogout.setText(getString(R.string.login));
            navigationViewHeaderBinding.navHeaderTitle.setText(getString(R.string.app_name));
            navigationViewHeaderBinding.navHeaderSubtitle.setText(getString(R.string.app_name) + "@" + getString(R.string.app_name) + ".com");
        }
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheetId.bottomLay);
        viewModel.getAllLoginInformation();
        BottomSheetCheck();
        ClickListners();
        if (savedInstanceState == null) {
            startFragment(HomeFragment.getInstance(), true, HomeFragment.getInstance().toString());
        }
    }

    private void ClickListners() {
        navigationViewHeaderBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSharedPre().isLoggedIn()) {
                    viewModel.getmAuth().signOut();
                    getSharedPre().Logout();
                    startActivity(new Intent(HomeActivity.this, SplashActivity.class));
                    finish();
                } else {
                    if (binding.drawer.isDrawerOpen(Gravity.LEFT)) {
                        binding.drawer.closeDrawer(Gravity.LEFT);
                    } else {
                        binding.drawer.openDrawer(Gravity.LEFT);
                    }
                    startFragment(LoginFragment.newInstance(), true, LoginFragment.newInstance().toString());
                }
            }
        });
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile: {
                        startFragment(ProfileFragment.newInstance(), true, ProfileFragment.newInstance().toString());
                        break;
                    }
                    case R.id.nav_share_app: {
                        break;
                    }
                    case R.id.nav_privacy_policy: {
                        break;
                    }
                    case R.id.blogs: {
                        break;
                    }
                }
                if (binding.drawer.isDrawerOpen(Gravity.LEFT)) {
                    binding.drawer.closeDrawer(Gravity.LEFT);
                } else {
                    binding.drawer.openDrawer(Gravity.LEFT);
                }
                return false;
            }
        });

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
            viewModel.getAllLoginInformation();
        } else {
            getInternetDialog().show();
        }
    }

    @Override
    public void isLoading(boolean value) {
        if (value) {
            showLoadingDialog("");
        } else {
            hideLoadingDialog();
        }

    }

    @Override
    public void getMessage(String message) {
        showCustomAlert(message);
    }

    @Override
    public void GetCatogory(List<ResItem> res) {

    }

    @Override
    public void StartHomeNow(com.skteam.blogapp.restmodels.signUp.ResItem resItem) {
        if (resItem != null) {
            if (getSharedPre().isLoggedIn()) {
                navigationViewHeaderBinding.btnLogout.setText(getString(R.string.logout));
                navigationViewHeaderBinding.navHeaderTitle.setText(getSharedPre().getName());
                navigationViewHeaderBinding.navHeaderSubtitle.setText(getSharedPre().getUserEmail());
            } else {
                navigationViewHeaderBinding.btnLogout.setText(getString(R.string.login));
                navigationViewHeaderBinding.navHeaderTitle.setText(getString(R.string.app_name));
                navigationViewHeaderBinding.navHeaderSubtitle.setText(getString(R.string.app_name) + "@" + getString(R.string.app_name) + ".com");
            }
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


}