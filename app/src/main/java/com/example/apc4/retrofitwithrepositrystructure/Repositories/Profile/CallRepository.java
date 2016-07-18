package com.example.apc4.retrofitwithrepositrystructure.Repositories.Profile;

import android.content.Context;
import com.example.apc4.retrofitwithrepositrystructure.Constants.Fields;
import com.example.apc4.retrofitwithrepositrystructure.DataModels.User;
import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.UserProfileResponse;
import com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.RestServices.IResponseCallBack;
import com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.SocketServices.sockets.WebIORequestHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class CallRepository {

    private Context mContext;
    private static ICallDataHandler mUserCallback;
    private WebIORequestHandler mWebIOHandler;
    public CallRepository() {
        mWebIOHandler = WebIORequestHandler.getInstance();
    }

    public void getNearByDrivers(ICallDataHandler handler, Context context,
                                 String startAddress, String startLat,
                                 String startLng, String endAddress,
                                String endLat, String endLng) {
        mUserCallback = handler;
        mContext = context;
        User user =new User();
        JSONObject jsonObject = new JSONObject();
        /*try {
            jsonObject.put(Fields.NearByDrivers.ID, user.getId());
            jsonObject.put(Fields.NearByDrivers.TOKEN_ID, user.getToken_id());
            jsonObject.put(Fields.NearByDrivers.START_ADDRESS, startAddress);
            jsonObject.put(Fields.NearByDrivers.START_LAT, startLat);
            jsonObject.put(Fields.NearByDrivers.START_LNG, startLng);
            if(StringUtils.isNotBlank(endLat)){
                jsonObject.put(Fields.NearByDrivers.END_ADDRESS, endAddress);
                jsonObject.put(Fields.NearByDrivers.END_LAT, endLat);
                jsonObject.put(Fields.NearByDrivers.END_LNG, endLng);

            }
            jsonObject.put(Fields.NearByDrivers.VEHICLE_TYPE, AppPreferences.getSelectedVehicle(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        mWebIOHandler.getUserProfile(mCallBack, jsonObject);
    }


    public void setCallBack(ICallDataHandler handler) {
        mUserCallback = handler;
    }

    private IResponseCallBack mCallBack = new IResponseCallBack() {
        @Override
        public void onResponse(Object object) {
            if(object instanceof UserProfileResponse) {
                mUserCallback.onUserProfileResponse((UserProfileResponse) object);
            }
        }

        @Override
        public void onSuccess() {
            if(null != mUserCallback){
                mUserCallback.onSuccess();
            }

        }

        @Override
        public void onError(String error) {
            mUserCallback.onError(error);
        }

        @Override
        public void onSendCodeSuccess() {

        }
    };

}
