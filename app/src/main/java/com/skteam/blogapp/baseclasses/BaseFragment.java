/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.blogapp.baseclasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import com.skteam.blogapp.R;
import com.skteam.blogapp.application.MyApplication;
import com.skteam.blogapp.brodcastReceivers.ConnectionReceiver;
import com.skteam.blogapp.database.RoomDatabase;
import com.skteam.blogapp.databinding.CustomToastBinding;
import com.skteam.blogapp.prefrences.SharedPre;
import com.skteam.blogapp.setting.CommonUtils;


public abstract class BaseFragment<B extends ViewDataBinding, V extends BaseViewModel> extends Fragment implements ConnectionReceiver.ConnectionReceiverListener {
    private B mViewDataBinding;
    private V mViewModel;
    private BaseActivity mActivity;
    private ProgressDialog progressDialog;
    private SharedPre sharedPre;
    private RoomDatabase database;
    private Toast toast;
    private ConnectionReceiver.ConnectionReceiverListener connectionReceiverListener;
   private Vibrator vibe ;
    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }
    public Vibrator getVib(){
        if(vibe==null){
           vibe = (Vibrator) getBaseActivity().getSystemService(Context.VIBRATOR_SERVICE);
        }
        return vibe;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mViewModel = getViewModel();
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public SharedPre getSharedPre() {
        if(sharedPre!=null){
            return sharedPre;
        }else{
            sharedPre=SharedPre.getInstance(getContext());
            return sharedPre;
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         toast = new Toast(getContext());
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.setLifecycleOwner(this);
        mViewDataBinding.executePendingBindings();
        connectionReceiverListener = this;
        ((MyApplication) getBaseActivity().getApplicationContext()).setConnectionListener(connectionReceiverListener);
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public B getViewDataBinding() {
        return mViewDataBinding;
    }

    public void showLoadingDialog(String s) {
        if (progressDialog == null)
            progressDialog = CommonUtils.showLoadingDialog(getContext(), "Please wait");
        else
            progressDialog.show();
    }

    public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void showCustomAlert(String msg) {
        CustomToastBinding toastBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.custom_toast, null, false);
        toastBinding.toastText.setText(msg);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setView(toastBinding.getRoot());
        toast.show();
    }
}
