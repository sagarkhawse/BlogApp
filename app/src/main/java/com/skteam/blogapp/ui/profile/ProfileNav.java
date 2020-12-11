package com.skteam.blogapp.ui.profile;

import com.skteam.blogapp.restmodels.signUp.ResItem;

public interface ProfileNav {
    void setLoading(boolean b);

    void setMessage(String message);

    void SetDataNow(ResItem resItem);

    void OkDone();
}
