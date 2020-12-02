package com.skteam.blogapp.ui.home;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;

public class HomeViewModel extends BaseViewModel<HomeNav> {
    public HomeViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
    // TODO: Implement the ViewModel
}