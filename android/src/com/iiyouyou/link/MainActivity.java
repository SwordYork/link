package com.iiyouyou.link;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.baidu.mapapi.map.*;
import com.igexin.slavesdk.MessageManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import android.os.Handler;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();
    public BMapManager mBMapMan = null;
    public MapView mMapView = null;
    private GetLocReceiver getLocReceiver;
    private String LocRecIntent = "com.iiyouyou.link.GET_LOCATION_RECEIVER";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBMapMan=new BMapManager(getApplication());
        mBMapMan.init("QQqN1f0InbSO1stbhVCgoChe", null);
        setContentView(R.layout.map_main);
        MessageManager.getInstance().initialize(this.getApplicationContext());

        getLocReceiver = new GetLocReceiver(new Handler());
        registerReceiver(getLocReceiver, new IntentFilter(LocRecIntent));

        mLocClient = new LocationClient(this);     //声明LocationClient类
        mLocClient.registerLocationListener( myListener );    //注册监听函数
        mMapView=(MapView)findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(true);
        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        GetMyLocation();

        Button button = (Button) findViewById(R.id.talocation);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String myid = sharedPref.getString("myid","");
                String taid = sharedPref.getString("taid","");
                if(myid.isEmpty() || taid.isEmpty()){
                    Toast.makeText(getApplicationContext(),"请先设置id",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), SetInfo.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"请稍等片刻",Toast.LENGTH_LONG).show();
                    RequestLocation requestLocation = new RequestLocation(getApplicationContext(),taid);
                }
            }
        });

        Button setting_button = (Button) findViewById(R.id.setting_button);
        setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SetInfo.class));
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMyLocation();
            }
        }
        );

    }

    public void GetMyLocation() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        double latitude = Double.parseDouble(sharedPref.getString("my_latitude", "30"));
        double longitude = Double.parseDouble(sharedPref.getString("my_longitude", "120"));
        float radius = Float.parseFloat(sharedPref.getString("my_radius", "10"));
        float direction = Float.parseFloat(sharedPref.getString("my_direction", "2"));
        updateLocationOnMap(latitude, longitude, radius, direction);


        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
     //   option.setScanSpan(0);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(false);//返回的定位结果包含手机机头的方向
        mLocClient.setLocOption(option);
        mLocClient.start();
        if (mLocClient != null && mLocClient.isStarted())
            mLocClient.requestLocation();
        else
            Log.d("LocSDK3", "locClient is null or not started");
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("my_latitude", Double.toString(location.getLatitude()));
                editor.putString("my_longitude", Double.toString(location.getLongitude()));
                editor.putString("my_radius", Double.toString(location.getRadius()));
                editor.putString("my_direction",Double.toString(location.getDirection()));
                editor.commit();

                Intent Msg = new Intent();
                Msg.putExtra("latitude", Double.toString(location.getLatitude()));
                Msg.putExtra("longitude", Double.toString(location.getLongitude()));
                Msg.putExtra("radius",Double.toString(location.getRadius()));
                Msg.putExtra("direction",Double.toString(location.getDirection()));
                Msg.setAction(LocRecIntent);
                sendBroadcast(Msg);

        }
        public void onReceivePoi(BDLocation poiLocation) {

        }
    }





    @Override
    protected void onDestroy(){
        if(mBMapMan!=null){
            mBMapMan.destroy();
            mBMapMan=null;
        }
        if( mMapView!=null){
            mMapView.destroy();
            mMapView = null;
        }
        super.onDestroy();
    }
    @Override
    protected void onPause(){
        if(mMapView != null){
            mMapView.onPause();
        }
        if(mBMapMan!=null){
            mBMapMan.stop();
        }
        super.onPause();
        //unregisterReceiver(getLocReceiver);
        //unregisterReceiver(reqLocReceiver);
    }
    @Override
    protected void onResume(){
        if(mMapView != null){
            mMapView.onResume();
        }
        if(mBMapMan!=null){
            mBMapMan.start();
        }
        super.onResume();
        //registerReceiver(getLocReceiver, new IntentFilter(LocRecIntent));
        //registerReceiver(reqLocReceiver, new IntentFilter(ReqLocIntent));
    }

    public class GetLocReceiver extends BroadcastReceiver
    {
        private final Handler handler;

        public GetLocReceiver(Handler handler){
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            handler.post(new MyRunnable(intent));
        }
    }

    public class MyRunnable implements Runnable {
        private Intent intent;
        public MyRunnable(Intent _intent) {
            this.intent = _intent;
        }
        public void run() {
            double latitude = Double.parseDouble(intent.getStringExtra("latitude"));
            double longitude = Double.parseDouble(intent.getStringExtra("longitude"));
            float radius = Float.parseFloat(intent.getStringExtra("radius"));
            float direction = Float.parseFloat(intent.getStringExtra("direction"));
            updateLocationOnMap(latitude, longitude, radius, direction);
        }
    }

    private void updateLocationOnMap(double latitude, double longitude,float radius,float direction ){
        MapController mMapController=mMapView.getController();
        mMapController.setZoom(16);//设置地图zoom级别

        MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
        LocationData locData = new LocationData();
        locData.latitude = latitude;
        locData.longitude = longitude;
        locData.direction = direction;
        locData.accuracy = radius;
        myLocationOverlay.setData(locData);
        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(myLocationOverlay);
        mMapView.refresh();
        mMapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6),
                (int)(locData.longitude* 1e6)));
    }


}
