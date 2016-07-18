package com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.RestServices;


import android.content.Context;

import com.example.apc4.retrofitwithrepositrystructure.Constants.ApiTags;
import com.example.apc4.retrofitwithrepositrystructure.Constants.HTTPStatus;
import com.example.apc4.retrofitwithrepositrystructure.Helpers.Utils;
import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.LoginResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RestRequestHandler {

    private Context mContext;
    private IRestClient mRestClient;
    private IResponseCallBack mResponseCallBack;
    public void sendUserLogin(final IResponseCallBack onResponseCallBack, String email, String password,
                              String deviceType, String userStatus, String regID) {
        Utils.printUrl("UrlLogin", ApiTags.USER_LOGIN_API + "&email=" + email +
                "&password=" + password +
                "&deviceType=" + deviceType +
                "&gcmId=" + regID);
        this.mResponseCallBack = onResponseCallBack;
        mRestClient = RestClient.getClient();
        Call<LoginResponse> restCall = mRestClient.login(email, password,
                deviceType, userStatus, regID);
        restCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                // Got success from server
                if (response.body().getCode() == HTTPStatus.OK) {
                    if(null != mResponseCallBack){
                        mResponseCallBack.onResponse(response.body());
                    }

                } else  {
                    //this case is for error
                    mResponseCallBack.onError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Utils.infoLog("LoginResponse", t.getMessage() + " ");
                mResponseCallBack.onError(t.getMessage());
            }
        });


    }

}