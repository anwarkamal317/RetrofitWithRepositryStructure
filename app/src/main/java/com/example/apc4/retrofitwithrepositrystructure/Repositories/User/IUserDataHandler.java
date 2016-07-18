package com.example.apc4.retrofitwithrepositrystructure.Repositories.User;


import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.LoginResponse;

/**
 * Created by APC-10 on 2/18/2016.
 */
public interface IUserDataHandler {

    void onUserLogin(LoginResponse loginResponse);
    void onSuccess();
    void onSendCodeSuccess();
    void onError(String error);

}
