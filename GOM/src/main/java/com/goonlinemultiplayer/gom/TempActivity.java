package com.goonlinemultiplayer.gom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by shane on 5/23/14.
 */
public class TempActivity extends Activity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(SignInActivity.BOARDS_JSON_EXTRA)){
            text.setText(extras.getString(SignInActivity.BOARDS_JSON_EXTRA));
        }
    }
}
