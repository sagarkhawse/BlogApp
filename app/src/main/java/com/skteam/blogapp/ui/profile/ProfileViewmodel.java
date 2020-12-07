package com.skteam.blogapp.ui.profile;

import android.app.Activity;
import android.content.Context;
import com.skteam.blogapp.baseclasses.BaseViewModel;
import com.skteam.blogapp.prefrences.SharedPre;

public class ProfileViewmodel extends BaseViewModel<ProfileNav> {

    public ProfileViewmodel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
}
