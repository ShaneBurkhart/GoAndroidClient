package com.goonlinemultiplayer.gom.utils.network;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.goonlinemultiplayer.gom.SignInActivity;
import com.goonlinemultiplayer.gom.TempActivity;
import com.goonlinemultiplayer.gom.utils.DialogFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by shane on 5/22/14.
 */
public class SignInUserTask extends AsyncTask<String, Void, Integer>{
    private static final int SUCCESS = 0;
    private static final int ERROR = 1;
    private static final int AUTHORIZATION_REQUIRED = 2;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    SignInActivity a;
    String token;
    String boardData;

    ProgressDialog progressDialog;

    public SignInUserTask(SignInActivity a){
        this.a = a;
    }

    @Override
    protected void onPreExecute() {
        this.progressDialog = ProgressDialog.show(a, "Signing you in.", "Signing you into your Google account.");
    }

    @Override
    protected Integer doInBackground(String... strings) {
        if(strings.length < 1) return ERROR;
        String account = strings[0];

        // Try to get token
        try {
            System.out.println("Account: " + account);
            token = GoogleAuthUtil.getToken(a, account, SCOPE);
        } catch(UserRecoverableAuthException e) {
            // Need to get permission from user so we launch activity and redo
            a.showRequestAuthorization(e.getIntent());
            return AUTHORIZATION_REQUIRED;
        } catch(GoogleAuthException e) {
            // Not recoverable and there was a big error
            System.err.println(e);
            return ERROR;
        } catch(IOException e) {
            // Some kind of other issue
            System.err.println(e);
            return ERROR;
        }
        System.out.println("Token: " + token);

        // Send token to server
        if(!sendTokenToServer(token)) return ERROR;

        return SUCCESS;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer i) {
        progressDialog.dismiss();
        switch (i) {
            case SUCCESS:
                System.out.println("Authorization successful.");
                startGameActivity();
                a.finish();
                break;
            case AUTHORIZATION_REQUIRED:
                System.out.println("Authorization required.  Asking for permission in another intent.");
                break;
            case ERROR:
                showGoogleAuthErrorDialog();
                break;
        }
    }

    private void startGameActivity(){
        Intent i = new Intent(a, TempActivity.class);
        i.putExtra(SignInActivity.BOARDS_JSON_EXTRA, "{}");
        a.startActivity(i);
    }

    private boolean sendTokenToServer(String user_token){
        HttpClient httpclient = new DefaultHttpClient();
        HttpRequestBase request = RequestFactory.createSignInUserRequest(user_token);
        if(request == null) return false;
        try {
            HttpResponse response = httpclient.execute(request);
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    private void showGoogleAuthErrorDialog() {
        DialogFactory.showDefaultDialog(a, "There was an error.", "There was an error when trying to connect to your Google account.");
    }
}
