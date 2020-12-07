/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.blogapp.ui.signup;


public interface SignUpNav {
    void onLoginFail();

    void setLoading(boolean b);

    //void SignUpResponse(Re re,String type);


    void setMessage(String server_not_responding);
}
