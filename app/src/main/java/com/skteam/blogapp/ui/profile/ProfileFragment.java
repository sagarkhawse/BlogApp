package com.skteam.blogapp.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.blogapp.BuildConfig;
import com.skteam.blogapp.R;
import com.skteam.blogapp.baseclasses.BaseFragment;
import com.skteam.blogapp.databinding.FragmentProfileBinding;
import com.skteam.blogapp.restmodels.uploadDp.ResItem;
import com.skteam.blogapp.setting.AppConstance;
import com.skteam.blogapp.setting.CommonUtils;
import com.skteam.blogapp.setting.FileAccess;
import com.skteam.blogapp.ui.home.HomeActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    String [] permissions={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
        viewmodel.getAllLoginInformation();
        // ((HomeActivity)getBaseActivity()).getToolbar().toolbarLay.setVisibility(View.GONE);
        binding.name.setText(getSharedPre().getName());
        binding.email.setText(getSharedPre().getUserEmail());
        if(gender.equalsIgnoreCase("male")){
            binding.femaleRadio.setChecked(false);
            binding.maleRadio.setChecked(true);
        }else{
            binding.femaleRadio.setChecked(true);
            binding.maleRadio.setChecked(false);
        }
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
                           if( hasPermissions(getContext(),permissions)) {
                               showImgDialog(getActivity(), false).show();
                           }
                        } else {
                            showCustomAlert("Please Give Permission First!");
                        }
                    }
                });
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.name.getText().toString().isEmpty()){
                  showCustomAlert("Name Not be Empty");
                }else if(binding.email.getText().toString().isEmpty()){
                    showCustomAlert("Email Not be Empty");
                }else if(binding.dob.getText().toString().isEmpty()){
                    showCustomAlert("Date of Birth Not be Empty");
                }else if(gender.isEmpty()){
                    showCustomAlert("Gender Not be Empty");
                }else if(binding.phone.getText().toString().isEmpty()){
                    showCustomAlert("phone Not be Empty");
                }
                else{
                    viewmodel.EditNow(binding.name.getText().toString(),binding.phone.getText().toString(),binding.dob.getText().toString(),gender);
                }

            }
        });
        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });


    }
    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        Calendar date = Calendar.getInstance();

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                binding.dob.setText(CommonUtils.CurrentTimeAsFormat2(date.getTimeInMillis(), "yyyy-MM-dd"));
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();

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

    @Override
    public void SetDataNow(com.skteam.blogapp.restmodels.signUp.ResItem resItem) {
        binding.email.setText(resItem.getEmail());
        binding.name.setText(resItem.getName());
        if(!resItem.getProfilePic().isEmpty()){
            Uri uri = Uri.parse(resItem.getProfilePic());
            String protocol = uri.getScheme();
            String server = uri.getAuthority();
            if (protocol != null && server != null) {
                Glide.with(getContext()).load(resItem.getProfilePic()).into(binding.userDp);
            } else {
                Glide.with(getContext()).load(AppConstance.IMG_URL + resItem.getProfilePic()).into(binding.userDp);
            }
        }

        if(resItem.getGender()!=null && resItem.getGender().isEmpty()){
            if(resItem.getGender().equalsIgnoreCase("male")){
                gender="male";
                binding.femaleRadio.setChecked(false);
                binding.maleRadio.setChecked(true);
            }else{
                gender="Female";
                binding.femaleRadio.setChecked(true);
                binding.maleRadio.setChecked(false);
            }
        }
        if(resItem.getDob()!=null && !resItem.getDob().isEmpty()){
            binding.dob.setText(resItem.getDob());
        }
    }

    @Override
    public void OkDone() {
        getBaseActivity().onBackPressed();
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
                                viewmodel.UploadProfile(finalFile).observe(getBaseActivity(), new Observer<List<ResItem>>() {
                                    @Override
                                    public void onChanged(List<ResItem> resItems) {
                                        getSharedPre().setClientProfile(resItems.get(0).getProfilePic());
                                    }
                                });
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
                        viewmodel.UploadProfile(finalFile).observe(getBaseActivity(), new Observer<List<ResItem>>() {
                            @Override
                            public void onChanged(List<ResItem> resItems) {
                                getSharedPre().setClientProfile(resItems.get(0).getProfilePic());
                            }
                        });

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
        ((HomeActivity) getBaseActivity()).getToolbar().toolbarLay.setVisibility(View.VISIBLE);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}