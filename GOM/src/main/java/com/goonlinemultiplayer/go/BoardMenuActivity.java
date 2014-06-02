package com.goonlinemultiplayer.go;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.goonlinemultiplayer.go.models.Board;
import com.goonlinemultiplayer.go.utils.BoardListItemClickListener;
import com.goonlinemultiplayer.go.utils.GameHelper;
import com.goonlinemultiplayer.go.utils.GoogleApiClientFactory;
import com.goonlinemultiplayer.go.utils.RequestCodes;
import com.goonlinemultiplayer.go.utils.callbacks.CreateMatchCallback;
import com.goonlinemultiplayer.go.utils.callbacks.LoadMatchesCallback;

import java.util.ArrayList;

/**
 * Created by shane on 5/23/14.
 */
public class BoardMenuActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "BoardMenuActivity";

    // ******** States ********//
    boolean mIsInSignIn = false;

    // ******** Member Vars ********//
    GoogleApiClient mGoogleApiClient;

    ActionBar mActionBar;
    ListView mBoardList;
    ArrayAdapter<Board> mBoardListAdapter;

    ArrayList<Board> mBoards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_menu);
        mActionBar = getSupportActionBar();

        // Initialize GoogleApiClient
        mGoogleApiClient = GoogleApiClientFactory.create(this, this, this);

        mBoards = new ArrayList<Board>();
        mBoardList = (ListView) findViewById(R.id.board_list);
        mBoardListAdapter = new ArrayAdapter<Board>(this, R.layout.board_list_item, R.id.opponent_name, mBoards);
        mBoardList.setAdapter(mBoardListAdapter);
        mBoardList.setOnItemClickListener(new BoardListItemClickListener(this, mBoards));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!mGoogleApiClient.isConnected() && !mIsInSignIn) {
            Log.v(TAG, "Attempting to connect...");
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.board_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                Log.v(TAG, "New game item selected...");
                startActivityForResult(Games.TurnBasedMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 1, true), RequestCodes.RC_SELECT_OPPONENT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodes.RC_SIGN_IN:
                if(resultCode == RESULT_OK) {
                    Log.v(TAG, "Signed in successfully!");
                    // Connect again now that we are sure we can.
                    mGoogleApiClient.connect();
                } else {
                    Log.v(TAG, "User canceled sign in!");
                }
                mIsInSignIn = false;
                break;
            case RequestCodes.RC_SELECT_OPPONENT:
                if(resultCode == RESULT_OK) {
                    Log.v(TAG, "User selected opponenet...");
                    GameHelper.createMatch(mGoogleApiClient, data, new CreateMatchCallback(this));
                } else {
                    Log.v(TAG, "User cancelled find opponent request...");
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG, "Connected!");
        GameHelper.loadBoards(mGoogleApiClient, new LoadMatchesCallback(mBoards, mBoardListAdapter));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Intent i = new Intent(this, SignInActivity.class);
        startActivityForResult(i, RequestCodes.RC_SIGN_IN);
        mIsInSignIn = true;
    }
}
