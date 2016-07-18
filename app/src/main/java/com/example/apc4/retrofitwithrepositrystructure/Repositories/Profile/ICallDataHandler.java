package com.example.apc4.retrofitwithrepositrystructure.Repositories.Profile;

import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.UserProfileResponse;

public interface ICallDataHandler {

    void onUserProfileResponse(UserProfileResponse response);
    void onError(String error);
    void onSuccess();
}
