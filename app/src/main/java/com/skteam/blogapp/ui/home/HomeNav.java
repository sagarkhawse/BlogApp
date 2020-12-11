package com.skteam.blogapp.ui.home;

import com.skteam.blogapp.restmodels.gteCatogry.ResItem;

import java.util.List;

public interface HomeNav {
    void isLoading(boolean value);
    void getMessage(String message);

    void GetCatogory(List<ResItem> res);

    void StartHomeNow(com.skteam.blogapp.restmodels.signUp.ResItem resItem);
}
