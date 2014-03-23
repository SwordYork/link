package com.iiyouyou.link;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import java.util.HashMap;

import com.igexin.sdk.Consts;
import org.json.JSONObject;


/**
 * Created by york on 2/22/14.
 */
public class MsgReceiver extends BroadcastReceiver
{
    private String LocRecIntent = "com.iiyouyou.link.GET_LOCATION_RECEIVER";
    private String ReqLocIntent = "com.iiyouyou.link.REQ_LOCATION_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GexinSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(Consts.CMD_ACTION)) {
            case Consts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                if (payload != null) {
                    String data = new String(payload);
                    Log.d("result user",data);
                    if(data.equals("request_location")){
                        Log.d("GexinSdkDemo 2", "Got Payload:" + data);
                        Intent Msg = new Intent(ReqLocIntent);
                        context.sendBroadcast(Msg);
                    }
                    try{
                        JSONObject JsonData = new JSONObject(data);
                        Intent Msg = new Intent();
                        Msg.putExtra("latitude", JsonData.getString("latitude"));
                        Msg.putExtra("longitude", JsonData.getString("longitude"));
                        Msg.putExtra("radius",JsonData.getString("radius"));
                        Msg.putExtra("direction",JsonData.getString("direction"));
                        Msg.setAction(LocRecIntent);
                        context.sendBroadcast(Msg);
                    }
                    catch (Exception e){
                        Log.e("Json","Json no right..");
                    }
                }
                break;

            case Consts.GET_CLIENTID:
                String cid = bundle.getString("clientid");
                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("my_clientid", cid);
                editor.commit();
                String myid = sharedPref.getString("myid","");
                if(!myid.isEmpty()){
                    updateClientidToServer(myid, cid, context.getString(R.string.server_root) + "/link/update_clientid.php");
                }
                break;

            case Consts.BIND_CELL_STATUS:
                String cell = bundle.getString("cell");
//               Log.d("GexinSdkDemo", "BIND_CELL_STATUS:" + cell);
                break;
            case Consts.THIRDPART_FEEDBACK:
                // sendMessage接口调用回执
                String appid = bundle.getString("appid");
                String taskid = bundle.getString("taskid");
                String actionid = bundle.getString("actionid");
                String result = bundle.getString("result");
                long timestamp = bundle.getLong("timestamp");

//                Log.d("GexinSdkDemo", "appid:" + appid);
//                Log.d("GexinSdkDemo", "taskid:" + taskid);
//                Log.d("GexinSdkDemo", "actionid:" + actionid);
//                Log.d("GexinSdkDemo", "result:" + result);
//                Log.d("GexinSdkDemo", "timestamp:" + timestamp);
                break;
            default:
                break;
        }
    }

    public void updateClientidToServer(String user_id,String client_id,String serverUrl) {
        // Create a new HttpClient and Post Header
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("user_id", user_id);
        data.put("client_id", client_id);
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
        asyncHttpPost.execute(serverUrl);
    }
}