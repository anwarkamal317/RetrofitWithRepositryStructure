package com.example.apc4.retrofitwithrepositrystructure.ServerCommunication.SocketServices.sockets;

import android.content.Context;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class WebIO {


/// http://172.16.0.192/
    public static final String SERVER = "http://52.36.93.28:3003";

    //Staging server
//    public static final String SERVER = "http://52.36.93.28:4003";


    private Socket mSocket;
    private static  WebIO mWebIO;

    public static Context getContext() {
        return mContext;
    }
    public static void setContext(Context context) {
        mContext = context;
    }

    private static Context mContext;
    private WebIO() {
        try {
            if(null == mContext) {
                mContext = getContext();
            }
          //  if(AppPreferences.isLoggedIn(mContext)) {
                IO.Options options = new IO.Options();
                options.query = "appName=Terminal&AppId=7002738&AppSecret=cI790Mf";

                options.timeout = 5*1000;

                mSocket = IO.socket(SERVER, options);
          //  }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            mWebIO = null;
        }
    }
    public synchronized static WebIO getInstance(){
        if(mWebIO == null){
            mWebIO = new WebIO();
        }
        return mWebIO;
    }

    public Socket getSocket() {
        return mSocket;
    }

    public static  void resetAccessToken(){
        WebIO.getInstance().getSocket().close();
        mWebIO= new WebIO();
    }

    public static void clearConnectionData(){
        WebIO.getInstance().getSocket().close();
        mWebIO = null;

    }

    public static void onConnect(Emitter.Listener connectCallBack) {

        try {
            if (mContext == null) {
               // mContext = App.getContext();
                mContext = getContext();
            }
            WebIO.getInstance().getSocket().on(Socket.EVENT_CONNECT, connectCallBack);
            WebIO.getInstance().getSocket().connect();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static boolean emit(String event,Object... params){
        if(!WebIO.getInstance().getSocket().connected()){
            return false;

        }

        WebIO.getInstance().getSocket().emit(event, params);
        return true;
    }

    public static void on(String EventName,Emitter.Listener callBack){

        WebIO.getInstance().getSocket().on(EventName, callBack);

    }
    public static void off(String EventName,Emitter.Listener callBack){

        WebIO.getInstance().getSocket().off(EventName, callBack);

    }

   public static void onError(Emitter.Listener callBack){
       WebIO.getInstance().getSocket().on(Socket.EVENT_ERROR, callBack)

               .on(Socket.EVENT_DISCONNECT, callBack);
   }

    public static void offError(Emitter.Listener callBack) {
        WebIO.getInstance().getSocket().off(Socket.EVENT_ERROR, callBack)
                .off(Socket.EVENT_DISCONNECT, callBack);
    }
}
