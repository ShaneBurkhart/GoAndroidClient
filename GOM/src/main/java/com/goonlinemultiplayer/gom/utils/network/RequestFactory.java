package com.goonlinemultiplayer.gom.utils.network;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shane on 5/22/14.
 */
public class RequestFactory {

    public static final String BASE_URL = "http://192.168.1.40:3000";

    public static HttpRequestBase createSignInUserRequest(String token){
        // Create a new HttpClient and Post Header
        HttpPost httppost = new HttpPost(BASE_URL + "/user");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("access_token", token));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return httppost;
    }

}
