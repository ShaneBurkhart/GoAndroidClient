package com.goonlinemultiplayer.go;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.goonlinemultiplayer.go.utils.GoogleApiClientFactory;
import com.goonlinemultiplayer.go.utils.RequestCodes;

/**
 * Created by shane on 6/1/14.
 */
public class SignInActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{
    private static final String TAG = "SignInActivity";

    GoogleApiClient mGoogleApiClient;

    SignInButton mGoogleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // Get button and set size
        mGoogleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);
        mGoogleSignInButton.setOnClickListener(this);

        // Initialize GoogleApi Client
        mGoogleApiClient = GoogleApiClientFactory.create(this, this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCodes.RC_LOGIN_RESOLUTION) {
            if(resultCode == RESULT_OK) {
                // Try connecting again after we have resolved.
                mGoogleApiClient.connect();
            } else {
                // TODO Show error dialog
            }
        }
    }

    @Override
    public void onClick(View view) {
        Log.v(TAG, "Button clicked");
        if(view.getId() == R.id.sign_in_button){
            Log.v(TAG, "Sign in button clicked.");
            mGoogleSignInButton.setClickable(false);
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG, "GoogleApiClient connection success.");
        Intent result = new Intent();
        if(bundle != null) {
            result.putExtras(bundle);
        }
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "GoogleApiClient connection suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(TAG, "GoogleApiClient connection failed.");
        Log.v(TAG, "Problem: " + connectionResult.toString());
        Log.v(TAG, "Resolvable: " + connectionResult.hasResolution());
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RequestCodes.RC_LOGIN_RESOLUTION);
                return;
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, e.toString());
            }
        }
        // TODO Show error dialog
    }
}
