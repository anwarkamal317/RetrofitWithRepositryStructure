package com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.RestServices;


import com.example.apc4.retrofitwithrepositrystructure.Constants.ApiTags;
import com.example.apc4.retrofitwithrepositrystructure.Constants.Fields;
import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.LoginResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface IRestClient {

    @FormUrlEncoded
    @POST(ApiTags.USER_LOGIN_API)
    Call<LoginResponse> login(@Field(Fields.Login.EMAIL) String email,
                              @Field(Fields.Login.PASSWORD) String password,
                              @Field(Fields.Login.DEVICE_TYPE) String deviceType,
                              @Field(Fields.Login.USER_STATUS) String userStatus,
                              @Field(Fields.Login.REG_ID) String gcmId);

}
