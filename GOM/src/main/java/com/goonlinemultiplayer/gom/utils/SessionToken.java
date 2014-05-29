package com.goonlinemultiplayer.gom.utils;

/**
 * Created by shane on 5/26/14.
 */
public class SessionToken {
    private static String sessionToken = null;

    private SessionToken(){}

    public static void set(String sessionToken){
        SessionToken.sessionToken = sessionToken;
    }

    public static String get(){
        return sessionToken;
    }
    public static boolean exists(){
        return sessionToken != null;
    }
}
