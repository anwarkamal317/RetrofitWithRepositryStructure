package com.example.apc4.retrofitwithrepositrystructure.Screens.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.apc4.retrofitwithrepositrystructure.Helpers.Utils;
import com.example.apc4.retrofitwithrepositrystructure.R;
import com.example.apc4.retrofitwithrepositrystructure.Repositories.User.IUserDataHandler;
import com.example.apc4.retrofitwithrepositrystructure.Repositories.User.UserDataHandler;
import com.example.apc4.retrofitwithrepositrystructure.Repositories.User.UserRepository;
import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.LoginResponse;

public class MainActivity extends AppCompatActivity {

    private MainActivity mCurrentActivity;
    private UserRepository mRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentActivity = this;
        mRepository = new UserRepository();

        performLogin();


    }

    private void performLogin(){
        if(Utils.isConnected(mCurrentActivity)) {
                mRepository.requestUserLogin(mCurrentActivity,
                        mCallBack, "Email",
                        "Password");
        }
    }

    private IUserDataHandler mCallBack = new UserDataHandler(){

        @Override
        public void onUserLogin(final LoginResponse loginResponse) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mCurrentActivity.finish();
                }
            });
        }
        @Override
        public void onError(final String error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    };



}
