package com.goonlinemultiplayer.go.utils;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import com.goonlinemultiplayer.go.models.Board;

import java.util.ArrayList;

/**
 * Created by shane on 6/1/14.
 */
public class BoardListItemClickListener implements AdapterView.OnItemClickListener {
    private Activity mActivity;
    private ArrayList<Board> mData;

    public BoardListItemClickListener(Activity a, ArrayList<Board> data) {
        this.mActivity = a;
        this.mData = data;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        GameHelper.startPlayTurnActivityForResult(mActivity);
    }
}
