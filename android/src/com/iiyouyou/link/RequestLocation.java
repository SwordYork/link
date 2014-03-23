package com.iiyouyou.link;

import android.content.Context;
import android.util.Log;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.ResourceBundle;


//RequestLocation requestLocation = new RequestLocation(context,"zsj");

/**
 * Created by york on 2/21/14.
 */
public class RequestLocation {
    private Context the_context;
    private String ta_id;

    public RequestLocation(Context context,String uid){
        the_context = context;
        ta_id = uid;
        sendRequest("/link/send_request.php");
    }

//    private String getHalfClientid(String pageUrl){
//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("user_id", user_id);
//        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
//        asyncHttpPost.execute(the_context.getString(R.string.server_root) + pageUrl);
//        return "";
//    }

    private void sendRequest(String pageUrl){
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("user_id", ta_id);
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
        asyncHttpPost.execute(the_context.getString(R.string.server_root) + pageUrl);
    }

}

