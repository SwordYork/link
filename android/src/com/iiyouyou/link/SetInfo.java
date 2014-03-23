package com.iiyouyou.link;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by york on 2/22/14.
 */
public class SetInfo  extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Button button = (Button) findViewById(R.id.confirm_button);
        final TextView textView_myid = (TextView) findViewById(R.id.myid_editText);
        final TextView textView_taid = (TextView) findViewById(R.id.taid_editText);
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
        String myid = sharedPref.getString("myid","");
        String taid = sharedPref.getString("taid","");
        if(!myid.isEmpty()){
            textView_myid.setText(myid);
        }
        if(!taid.isEmpty()){
            textView_taid.setText(taid);
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("myid",textView_myid.getEditableText().toString() );
                editor.putString("taid",textView_taid.getEditableText().toString() );
                editor.commit();
                finish();
            }
        });
    }

}
