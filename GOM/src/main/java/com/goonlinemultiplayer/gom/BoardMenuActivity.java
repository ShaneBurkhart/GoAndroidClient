package com.goonlinemultiplayer.gom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.goonlinemultiplayer.gom.models.Board;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shane on 5/23/14.
 */
public class BoardMenuActivity extends Activity {

    ListView boardList;
    ArrayList<Board> boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_menu);

        boardList = (ListView) findViewById(R.id.board_list);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(SignInActivity.BOARDS_JSON_EXTRA)){
            String data = extras.getString(SignInActivity.BOARDS_JSON_EXTRA);
            System.out.println("Board Data: " + data);
            boards = parseBoardJSON(data);
        } else {
            boards = new ArrayList<Board>();
        }

    }

    private ArrayList<Board> parseBoardJSON(String data) {
        ArrayList<Board> boards = new ArrayList<Board>();
        if(data.equals("")) return boards;
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray bs = obj.getJSONArray("boards");
            for(int i = 0; i < bs.length(); i++){
                JSONObject b = bs.getJSONObject(i);
                Board t = new Board();
                t.id = b.getString("id");
                t.width = b.getInt("width");
                t.height = b.getInt("height");
                t.pieces = b.getString("pieces");
                boards.add(t);
            }
        } catch (JSONException e) {
            System.out.println(e);
            return boards;
        }
        return boards;
    }
}
