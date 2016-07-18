package com.example.apc4.retrofitwithrepositrystructure.ResponseModels;

import com.example.apc4.retrofitwithrepositrystructure.DataModels.User;

/**
 * Created by APC4 on 7/18/2016.
 */
public class LoginResponse extends CommonResponse{
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
