/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.blogapp.ui.splash;
import android.app.Activity;
import android.content.Context;
import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;

public class SplashViewModel extends BaseViewModel<SplashNav> {
    public SplashViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }

}
