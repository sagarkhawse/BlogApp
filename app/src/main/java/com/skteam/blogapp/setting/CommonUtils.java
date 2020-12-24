/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.blogapp.setting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.annotation.RequiresApi;

import com.skteam.blogapp.R;
import com.skteam.blogapp.setting.animation.MyBounceInterpolator;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


public class CommonUtils {
    public static ProgressDialog showLoadingDialog(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }


    public static Dialog InternetConnectionAlert(Context activity, boolean isCancelable) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_connection_dilaog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button turnOninternet = dialog.findViewById(R.id.turnOnInternet);
        if (isCancelable) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        dialog.setCancelable(false);
        turnOninternet.setOnClickListener(v -> {
            try {
                Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } catch (Exception e) {

            }
        });
        return dialog;
    }

    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }



    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return !Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phone) {
        String mobilePattern = "[0-9]{10}";
        return !Pattern.matches(mobilePattern, phone);
    }

    public static boolean isValidPin(String pin) {
        String pattern = "[0-9]{6}";
        return !Pattern.matches(pattern, pin);
    }

    public static boolean isValidName(String name) {
        String pattern = "[A-Za-z\\s]+";
        return !Pattern.matches(pattern, name);
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public static String timeStamp(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(time);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            long millis = TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime());
            long week = 0;
            if (days / 7 > 1) {
                week = days / 7;
            } else {
                week = days / 7;
            }
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);

            if (seconds < 60) {
                System.out.println(seconds + " sec ago");
                //  return seconds + " seconds ago";
                return "now";
            } else if (minutes < 60) {//
                System.out.println(minutes + " min ago");
                return minutes + " min ago";
            } else if (hours < 24) {
                System.out.println(hours + " hr ago");
                return hours + " hr ago";

            } else if (days < 8) {
                System.out.println(days + " days ago");
                return days + " days ago";
            } else if (week < 5) {
                System.out.println(week + " weeks ago");
                return week + " weeks ago";
            } else if (month < 12) {
                System.out.println(month + " months ago");
                return month + " months ago";
            } else {
                System.out.println(year + " Years ago");
                return year + " Years ago";
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return "few month ago";
    }

    public static void setAnimationBounse(Context context, View view) {
        final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce_button);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        view.startAnimation(myAnim);
    }

    public static String Base64Encode(String text) {
        byte[] encrpt = new byte[0];
        try {
            encrpt = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(encrpt, Base64.DEFAULT);
        return base64;
    }

    public static String Base64Decode(String base64) {
        byte[] decrypt = Base64.decode(base64, Base64.DEFAULT);
        String text = null;
        try {
            text = new String(decrypt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }
    public static Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            Log.d("JSON", "Error parsing JSON");
        }
        return null;
    }

    public static String CurrentTimeAsFormat(String curentTimeStamp)  {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);

        Date result= null;
        long miliSec= Long.parseLong(curentTimeStamp);
        try {
            result = new Date(miliSec);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return df.format(result);


    }
    public static String CurrentTimeAsFormat2(long curentTimeStamp,String format)  {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(curentTimeStamp);
        return formatter.format(calendar.getTime());
    }


    public static String dateFormat(String date) {
if(date!=null && !date.isEmpty()) {
    Calendar current_cal = Calendar.getInstance();

    Calendar date_cal = Calendar.getInstance();

    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    Date d = null;
    try {
        d = f.parse(date);
        date_cal.setTime(d);
        long difference = (current_cal.getTimeInMillis() - date_cal.getTimeInMillis()) / 1000;

        if (difference < 86400) {
            if (current_cal.get(Calendar.DAY_OF_YEAR) - date_cal.get(Calendar.DAY_OF_YEAR) == 0) {

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                return sdf.format(d);
            } else
                return "yesterday";
        } else if (difference < 172800) {
            return "yesterday";
        } else
            return (difference / 86400) + " day ago";
    } catch (ParseException e) {
        e.printStackTrace();
    }

}
return "";
    }

}
