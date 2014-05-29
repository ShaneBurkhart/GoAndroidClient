package com.goonlinemultiplayer.gom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.goonlinemultiplayer.gom.models.Board;
import com.goonlinemultiplayer.gom.utils.SessionToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shane on 5/23/14.
 */
public class BoardMenuActivity extends ActionBarActivity {

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
            System.out.println("Session Token: " + SessionToken.get());
            boards = parseBoardJSON(data);
        } else {
            boards = new ArrayList<Board>();
        }

        boardList.setAdapter(new ArrayAdapter<Board>(this, R.layout.board_list_item, R.id.opponent_name, boards));
        boardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BoardMenuActivity.this, GameBoardActivity.class);
                startActivity(intent);
            }
        });

    }

    private ArrayList<Board> parseBoardJSON(String data) {
        ArrayList<Board> boards = new ArrayList<Board>();
        if(data.equals("")) return boards;
        try {
            JSONArray a = new JSONArray(data);
            for(int i = 0; i < a.length(); i++){
                JSONObject b = a.getJSONObject(i);
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
