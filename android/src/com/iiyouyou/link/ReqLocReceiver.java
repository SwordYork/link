package com.iiyouyou.link;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.HashMap;

/**
 * Created by york on 2/23/14.
 */
public class ReqLocReceiver extends BroadcastReceiver {
    private Context context;
    public void onReceive(Context _context, Intent intent) {
        context = _context.getApplicationContext();
        ReqMyLocation();
    }

    public void ReqMyLocation() {

        LocationClient rmLocClient;
        MyReqLocationListener rmyListener = new MyReqLocationListener();
        rmLocClient = new LocationClient(context);     //声明LocationClient类
        rmLocClient.setAccessKey("QQqN1f0InbSO1stbhVCgoChe");
        rmLocClient.registerLocationListener( rmyListener );    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        //   option.setScanSpan(0);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(false);//返回的定位结果包含手机机头的方向
        rmLocClient.setLocOption(option);
        rmLocClient.start();
        if (rmLocClient != null && rmLocClient.isStarted())
            rmLocClient.requestLocation();
        else
            Log.d("LocSDK3", "locClient is null or not started");
    }


    public class MyReqLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            HashMap<String, String> Msg = new HashMap<String, String>();
            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
            String user_id = sharedPref.getString("taid","");
            if(user_id.isEmpty())
                return;
            Msg.put("user_id", user_id);
            Log.d("user_id",user_id);
            Msg.put("latitude", Double.toString(location.getLatitude()));
            Msg.put("longitude", Double.toString(location.getLongitude()));
            Msg.put("radius", Double.toString(location.getRadius()));
            Msg.put("direction", Double.toString(location.getDirection()));
            Log.d("result","MyListener");
            AsyncHttpPost asyncHttpPost = new AsyncHttpPost(Msg);
            asyncHttpPost.execute(context.getString(R.string.server_root) + "/link/response_loc.php");
        }
        public void onReceivePoi(BDLocation poiLocation) {

        }
    }
}
