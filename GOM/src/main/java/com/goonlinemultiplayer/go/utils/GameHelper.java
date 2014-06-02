package com.goonlinemultiplayer.go.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.goonlinemultiplayer.go.GameBoardActivity;
import com.goonlinemultiplayer.go.utils.callbacks.CreateMatchCallback;
import com.goonlinemultiplayer.go.utils.callbacks.LoadMatchesCallback;

import java.util.ArrayList;

/**
 * Created by shane on 5/31/14.
 */
public class GameHelper {
    private static final String TAG = "GameHelper";

    public static void loadBoards(GoogleApiClient client, LoadMatchesCallback callback) {
        Games.TurnBasedMultiplayer.loadMatchesByStatus(client, TurnBasedMatch.MATCH_TURN_STATUS_ALL)
                .setResultCallback(callback);
    }

    public static void createMatch(GoogleApiClient client, Intent data, CreateMatchCallback callback) {
        // TODO Random opponent stuff.
        ArrayList<String> players = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.v(TAG, "Opponent Player Id: " + players.get(0));
        Games.TurnBasedMultiplayer.createMatch(client, createMatchConfig(players.get(0)))
                .setResultCallback(callback);
    }

    public static void startPlayTurnActivityForResult(Activity a){
        // TODO Add data to intent
        Intent i = new Intent(a, GameBoardActivity.class);
        a.startActivityForResult(i, RequestCodes.RC_PLAY_TURN);
    }

    private static TurnBasedMatchConfig createMatchConfig(String opponentId) {
        return TurnBasedMatchConfig.builder()
                .addInvitedPlayer(opponentId)
                .build();
    }

}
