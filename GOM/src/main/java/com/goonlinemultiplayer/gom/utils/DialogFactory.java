package com.goonlinemultiplayer.gom.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by shane on 5/22/14.
 */
public class DialogFactory {

    public static void showDefaultDialog(Activity a, String title, String message) {
        new AlertDialog.Builder(a)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
