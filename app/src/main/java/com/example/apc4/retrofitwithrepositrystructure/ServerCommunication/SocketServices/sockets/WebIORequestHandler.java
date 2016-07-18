package com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.SocketServices.sockets;

import android.content.Context;

import com.example.apc4.retrofitwithrepositrystructure.Constants.ApiTags;
import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.CommonResponse;
import com.example.apc4.retrofitwithrepositrystructure.ResponseModels.UserProfileResponse;
import com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.RestServices.IResponseCallBack;
import com.google.gson.Gson;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class WebIORequestHandler {

    private static Context mContext;
    private String mSocket = ApiTags.SOCKET_ACCESS_TOKEN;
    private static WebIORequestHandler mWebIORequestHandler = new WebIORequestHandler();

    public static WebIORequestHandler getInstance() {
        if (null == mWebIORequestHandler) {
            mWebIORequestHandler = new WebIORequestHandler();
        }
        return mWebIORequestHandler;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    private WebIORequestHandler() {
    }

    public static void connect() {
        try {
            WebIO.onConnect(mConnectionListener);

        } catch (Exception e) {
            e.printStackTrace();
        }


//        WebIO.on(Socket.EVENT_CONNECT, connectionListener);

    }


    private void emitWithJObject(MyGenericListener myGenericListener, final JSONObject json) {
        WebIO.on(mSocket, myGenericListener);
        if (!WebIO.emit(mSocket, json)) {
            WebIO.onConnect(new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    WebIO.emit(mSocket, json);

                }
            });
        } else {
            //TODO: Log error here

        }
    }

    public void getUserProfile(final JSONObject json, IResponseCallBack onResponseCallBack) {
        mSocket = ApiTags.GET_PROFILE;
        emitWithJObject(new MyGenericListener(mSocket, UserProfileResponse.class, onResponseCallBack),
                json);
    }


    private class MyGenericListener implements Emitter.Listener, SetDataModel {
        private Class<?> dataModelClass;
        private String mSocketName;
        private IResponseCallBack mOnResponseCallBack;

        public MyGenericListener(String socketName, Class<?> a, IResponseCallBack onResponseCallBack) {
            setDataModel(a);
            mOnResponseCallBack = onResponseCallBack;
            mSocketName = socketName;
        }

        @Override
        public void call(Object... args) {
            String serverResponse = args[0].toString();

            Gson gson = new Gson();
            try {
                Object commonResponse = gson.fromJson(serverResponse, dataModelClass);
                if (commonResponse instanceof UserProfileResponse) {
                    UserProfileResponse data = (UserProfileResponse) commonResponse;

                    if (data.isSuccess()) {
                            mOnResponseCallBack.onResponse(data);
                        WebIO.off(mSocketName, MyGenericListener.this);
                    } else {
                        String errorMessage = data.getMessage();
                        mOnResponseCallBack.onError(errorMessage);
                        WebIO.off(mSocketName, MyGenericListener.this);
                    }

                } else if (commonResponse instanceof CommonResponse) {
                    CommonResponse data = (CommonResponse) commonResponse;

                    if (data.isSuccess()) {
                        mOnResponseCallBack.onSuccess();
                        WebIO.off(mSocketName, MyGenericListener.this);
                    } else {
                        String errorMessage = data.getMessage();
                        mOnResponseCallBack.onError(errorMessage);
                        WebIO.off(mSocketName, MyGenericListener.this);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                mOnResponseCallBack.onError("Unable to process request.");
                WebIO.off(mSocketName, MyGenericListener.this);
            }
        }

        @Override
        public void setDataModel(Class<?> a) {
            dataModelClass = a;
        }
    }



    //Custom Listener is for listening specific events from server so we not have to wait( just like GCM Push Notification)

    private class Listener implements Emitter.Listener{
        private IResponseCallBack mOnResponseCallBack;

        public Listener(IResponseCallBack onResponseCallBack) {
            mOnResponseCallBack = onResponseCallBack;
        }

        @Override
        public void call(Object... args) {
            String serverResponse = args[0].toString();

            Gson gson = new Gson();
            try {
                UserProfileResponse tripStatusResponse = gson.fromJson(serverResponse, UserProfileResponse.class);
                if (tripStatusResponse.isSuccess()) {
                    mOnResponseCallBack.onResponse(tripStatusResponse);
                } else {
                    String errorMessage = tripStatusResponse.getMessage();
                    mOnResponseCallBack.onError(errorMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public static Emitter.Listener mConnectionListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            //Let the app know that socket is connected
            //socketConnected=true;
        }
    };

    private interface SetDataModel {
        public void setDataModel(Class<?> a);
    }


}
