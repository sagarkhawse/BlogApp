package com.skteam.blogapp.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.blogapp.BuildConfig;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.FragmentProfileBinding;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.setting.FileAccess;
import com.skteam.blogapp.ui.home.HomeActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

import static com.skteam.blogapp.setting.AppConstance.CAMERA_REQUEST;
import static com.skteam.blogapp.setting.AppConstance.SELECT_IMAGE;


public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewmodel> implements ProfileNav {
    private FragmentProfileBinding binding;
    private ProfileViewmodel viewmodel;
    private static ProfileFragment instance;
    private Dialog internetDialog;
    private String gender = "male";
    private Disposable disposable;
    private Dialog dialog;
    private String mCameraFileName;
    private Uri tempUri;
    private File finalFile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        instance = instance == null ? new ProfileFragment() : instance;
        return instance;
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public ProfileViewmodel getViewModel() {
        return viewmodel = new ProfileViewmodel(getContext(), getSharedPre(), getBaseActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewmodel.setNavigator(this);
        ((HomeActivity)getBaseActivity()).getToolbar().toolbarLay.setVisibility(View.GONE);
        binding.maleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gender = "male";
            }
        });
        binding.femaleRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gender = "female";
            }
        });
        disposable = RxView.clicks(binding.userDp).compose(getBaseActivity().getRxPermissions().ensure(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)).observeOn(AndroidSchedulers.mainThread())
                .throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            showImgDialog(getActivity(), false).show();
                        } else {
                            showCustomAlert("Please Give Permission First!");
                        }
                    }
                });
        disposable = RxView.clicks(binding.ivBack).observeOn(AndroidSchedulers.mainThread())
                .throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        getBaseActivity().onBackPressed();
                    }
                });

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(getBaseActivity(), false);
        }
        if (isConnected) {
            internetDialog.dismiss();
        } else {
            internetDialog.show();
        }
    }

    @Override
    public void setLoading(boolean b) {
        if (b) {
            showLoadingDialog("");
        } else {
            hideLoadingDialog();
        }
    }

    @Override
    public void setMessage(String s) {
        showCustomAlert(s);
    }

    public Dialog showImgDialog(Context activity, boolean isCancelable) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isCancelable) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.upload_img_lay);
        ImageView camera = dialog.findViewById(R.id.img_camera);
        ImageView gallery = dialog.findViewById(R.id.img_gallery);
        ImageView close = dialog.findViewById(R.id.closeAttach);
        close.setOnClickListener(v -> dialog.dismiss());
        camera.setOnClickListener(v -> {
            dialog.dismiss();
            captureFromCamera();
        });
        gallery.setOnClickListener(v -> {
            Intent pictureActionIntent = null;
            pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pictureActionIntent, SELECT_IMAGE);
            dialog.dismiss();
        });
        dialog.setCancelable(isCancelable);
        return dialog;
    }

    private void captureFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getBaseActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                finalFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getBaseActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        finalFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return finalFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case SELECT_IMAGE: {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            try {
                                Uri selectedImage = data.getData();
                                String file = FileAccess.getPath(getBaseActivity(), selectedImage);
                                mCameraFileName = file;
                                finalFile = new File(file);
                                Glide.with(getContext()).load(selectedImage)
                                        .placeholder(R.drawable.ic_user)
                                        .into(binding.userDp);

                               // binding.userDp.setImageURI(selectedImage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        //  Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case CAMERA_REQUEST: {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            tempUri = data.getData();

                        }
                        Glide.with(getContext()).load(finalFile)
                                .placeholder(R.drawable.ic_user)
                                .into(binding.userDp);

                        if (tempUri == null && mCameraFileName != null) {
                            tempUri = Uri.fromFile(new File(mCameraFileName));
                        }
                        if (mCameraFileName != null) {
                            finalFile = new File(mCameraFileName);
                        }

                        //
                        if (!finalFile.exists()) {
                            finalFile.mkdir();
                        }

                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        //  Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default: {

                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeActivity)getBaseActivity()).getToolbar().toolbarLay.setVisibility(View.VISIBLE);
    }
}