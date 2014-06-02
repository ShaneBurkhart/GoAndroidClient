package com.goonlinemultiplayer.go.utils.callbacks;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.goonlinemultiplayer.go.utils.GameHelper;

/**
 * Created by shane on 6/1/14.
 */
public class CreateMatchCallback implements ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>{
    private static final String TAG = "CreateMatchCallback";

    private Activity mActivity;

    public CreateMatchCallback(Activity a) {
        this.mActivity = a;
    }

    @Override
    public void onResult(TurnBasedMultiplayer.InitiateMatchResult initiateMatchResult) {
        Log.v(TAG, "Creating match callback...");
        switch (initiateMatchResult.getStatus().getStatusCode()) {
            case GamesStatusCodes.STATUS_OK:
                Log.v(TAG, "Created match successfully!");
                GameHelper.startPlayTurnActivityForResult(mActivity);
                break;
            default:
                Log.v(TAG, "Something happened when creating match.");
                Log.v(TAG, "Message: " + initiateMatchResult.getStatus().toString());
                break;
        }
    }
}
