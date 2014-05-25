package com.goonlinemultiplayer.gom;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.SignInButton;
import com.goonlinemultiplayer.gom.utils.network.SignInUserTask;


public class SignInActivity extends ActionBarActivity {
    public static final String BOARDS_JSON_EXTRA = "boards_json";

    RelativeLayout mainLayout;
    TextView text;
    SignInButton googleButton;

    String account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout) findViewById(R.id.main_container);
        text = (TextView) findViewById(R.id.text);
        googleButton = new SignInButton(this);

        googleButton.setSize(SignInButton.SIZE_WIDE);

        mainLayout.addView(googleButton);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAccountPicker();
                view.setClickable(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;
        if(data == null) return;

        switch (requestCode) {
            case ACCOUNT_PICKER_REQUEST_CODE:
                account = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                if(account != null) new SignInUserTask(this).execute(account);
                googleButton.setClickable(true);
                break;
            case REQUEST_AUTHORIZATION_REQUEST_CODE:
                if(account != null) new SignInUserTask(this).execute(account);
                googleButton.setClickable(true);
                break;
        }
    }

    public static final int ACCOUNT_PICKER_REQUEST_CODE = 1;
    public static final int REQUEST_AUTHORIZATION_REQUEST_CODE = 2;

    public void showRequestAuthorization(Intent i){
        startActivityForResult(i, REQUEST_AUTHORIZATION_REQUEST_CODE);
    }

    private void showAccountPicker(){
        Intent i = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(i, ACCOUNT_PICKER_REQUEST_CODE);
    }
}
