package com.goonlinemultiplayer.go.utils;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created by shane on 6/1/14.
 */
public class GoogleApiClientFactory {

    public static GoogleApiClient create(Context c, GoogleApiClient.ConnectionCallbacks conn, GoogleApiClient.OnConnectionFailedListener fail){
        return new GoogleApiClient.Builder(c, conn, fail)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
    }
}
