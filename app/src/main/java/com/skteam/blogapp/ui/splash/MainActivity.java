package com.skteam.blogapp.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;

import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseActivity;
import com.skteam.blogapp.databinding.ActivityMainBinding;
import com.skteam.blogapp.setting.CommonUtils;

public class MainActivity extends BaseActivity<ActivityMainBinding,SpalshViewmodel> implements SpalshNav {
private ActivityMainBinding binding;
private SpalshViewmodel spalshViewmodel;
private Dialog internetDialog;
    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public SpalshViewmodel getViewModel() {
        return spalshViewmodel=new SpalshViewmodel(this,getSharedPre(),this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        internetDialog=CommonUtils.InternetConnectionAlert(this,true);
        binding=getViewDataBinding();
        spalshViewmodel.setNavigator(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            internetDialog.show();
        }else{
            internetDialog.dismiss();
        }
    }
}