package com.goonlinemultiplayer.go.utils.callbacks;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchBuffer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.goonlinemultiplayer.go.models.Board;

import java.util.ArrayList;

/**
 * Created by shane on 6/1/14.
 */
public class LoadMatchesCallback implements ResultCallback<TurnBasedMultiplayer.LoadMatchesResult> {
    private static final String TAG = "LoadMatchesCallback";

    private ArrayList<Board> mData;
    private ArrayAdapter<Board> mAdapter;

    public LoadMatchesCallback(ArrayList<Board> data, ArrayAdapter<Board> adapter) {
        this.mData = data;
        this.mAdapter = adapter;
    }

    @Override
    public void onResult(TurnBasedMultiplayer.LoadMatchesResult result) {
        Log.v(TAG, "Got match listings.");
        Log.v(TAG, "Has Match Data: " + result.getMatches().hasData());
        Log.v(TAG, "Number My Turn: " + result.getMatches().getMyTurnMatches().getCount());
        Log.v(TAG, "Number Their Turn: " + result.getMatches().getTheirTurnMatches().getCount());
        Log.v(TAG, "Number Invitations: " + result.getMatches().getInvitations().getCount());
        TurnBasedMatchBuffer myTurn = result.getMatches().getMyTurnMatches();
        mData.clear();
        for(int i = 0; i < myTurn.getCount(); i++) {
            // TODO add boards to mData
            TurnBasedMatch match = myTurn.get(i);
            Board b = new Board();
            b.id = match.getDescription();
            mData.add(b);
        }
        mAdapter.notifyDataSetChanged();
    }
}
