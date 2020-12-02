package com.skteam.blogapp.ui.splash;

import android.app.Activity;
import android.content.Context;

import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;

public class SpalshViewmodel extends BaseViewModel<SpalshNav> {
    public SpalshViewmodel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
}
