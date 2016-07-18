package com.example.apc4.retrofitwithrepositrystructure.Repositories.User;

import android.content.Context;

import com.example.apc4.retrofitwithrepositrystructure.Constants.Constants;
import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.CommonResponse;
import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.LoginResponse;
import com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.RestServices.IResponseCallBack;
import com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.RestServices.RestRequestHandler;
import com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.SocketServices.sockets.WebIORequestHandler;

/**
 * Created by APC4 on 7/18/2016.
 */
public class UserRepository {

    private Context mContext;
    private IUserDataHandler mUserCallback;
    private RestRequestHandler mRestRequestHandler;
    private WebIORequestHandler mWebIORequestHandler;

    public UserRepository() {
        mRestRequestHandler = new RestRequestHandler();
        mWebIORequestHandler = WebIORequestHandler.getInstance();
    }


    public void requestUserLogin(android.content.Context context, IUserDataHandler handler, String email, String password) {
        mContext = context;
        mUserCallback = handler;
        mRestRequestHandler.sendUserLogin(mDataCallback, email, password, Constants.DEVICE_TYPE, "1", "GCM_Key");

    }


    private IResponseCallBack mDataCallback = new IResponseCallBack() {
        @Override
        public void onResponse(Object object) {
             if (object instanceof LoginResponse) {
                 if (null != mUserCallback) {
                     mUserCallback.onUserLogin((LoginResponse) object);
                 }
             }else if (object instanceof CommonResponse) {
                if (null != mUserCallback) {


                }
            }
        }

        @Override
        public void onError(String error) {
            mUserCallback.onError(error);
        }

        @Override
        public void onSuccess() {
            if(null != mUserCallback){
                mUserCallback.onSuccess();
            }
        }

        @Override
        public void onSendCodeSuccess() {
            if(null != mUserCallback){
                mUserCallback.onSendCodeSuccess();
            }
        }
    };

}
