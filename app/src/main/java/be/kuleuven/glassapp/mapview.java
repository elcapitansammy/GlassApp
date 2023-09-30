package be.kuleuven.glassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import be.kuleuven.glassapp.Model.Map;
import be.kuleuven.glassapp.Model.Player;
import be.kuleuven.glassapp.obstacles.ArrivingBlock;
import be.kuleuven.glassapp.obstacles.Block;
import be.kuleuven.glassapp.obstacles.Blocktoactivate;
import be.kuleuven.glassapp.obstacles.EmptyBlock;
import be.kuleuven.glassapp.obstacles.Exit;
import be.kuleuven.glassapp.obstacles.InvisibleBlock;
import be.kuleuven.glassapp.obstacles.Obstacles;
import be.kuleuven.glassapp.obstacles.TeleportingBlock;

import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mapview extends AppCompatActivity {
    Map currentmap;
    GridLayout board;
    Obstacles[][] grid;
    String name;
    RequestQueue requestQueue;
    int placed=0;
    private Button quit;
    Player player;
    ConstraintLayout screen;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mapview);
            screen= (ConstraintLayout) findViewById(R.id.mapGrid);
            Intent intent= getIntent();
            name= intent.getExtras().getString("MapName");
            quit = (Button) findViewById(R.id.quit2);
            board = findViewById(R.id.board2);
            board.setRowCount(9);
            board.setColumnCount(12);
            currentmap= new Map();
            grid = currentmap.getMap();
            get_mapID(name);
            player= new Player();
            addListener();

    }
    public void addListener(){
        screen.setOnTouchListener(new OnSwipeTouchListener(mapview.this) {
            public void onSwipeTop() {
                board.removeAllViewsInLayout();
                move("Top");
                viewGrid();
            }
            public void onSwipeRight() {
                board.removeAllViewsInLayout();
                move("Right");
                viewGrid();
            }
            public void onSwipeLeft() {
                board.removeAllViewsInLayout();
                move("Left");
                viewGrid();
            }
            public void onSwipeBottom() {
                board.removeAllViewsInLayout();
                move("Down");
                viewGrid();
            }

        });
    }


    public void viewGrid() {
            for (int i=0; i<currentmap.getXsize();i++){
                for(int p=0; p<currentmap.getYsize();p++){
                    ImageView tile= new ImageView(this);
                    tile.setLayoutParams(new ViewGroup.LayoutParams(90,90));
                    tile.setMaxHeight(90);
                    tile.setMaxWidth(90);
                    if ((grid[i][p] instanceof EmptyBlock)){
                        tile.setImageResource(R.drawable.stonegreen);
                    }
                    if((grid[i][p] instanceof InvisibleBlock)){
                        tile.setImageResource(R.drawable.stonegreen);
                    }
                    if ((grid[i][p] instanceof Block)){
                        tile.setImageResource(R.drawable.coblestor);
                    }
                    if (grid[i][p] instanceof TeleportingBlock){
                        tile.setImageResource(R.drawable.teletransport2);
                    }
                    if (grid[i][p] instanceof be.kuleuven.glassapp.obstacles.Button){
                        tile.setImageResource(R.drawable.buttont);
                    }
                    if (grid[i][p] instanceof Blocktoactivate){
                        tile.setImageResource(R.drawable.stonegreen);
                    }
                    if (grid[i][p] instanceof ArrivingBlock){
                        tile.setImageResource(R.drawable.arriving2);
                    }
                    if (grid[i][p] instanceof Exit){
                        tile.setImageResource(R.drawable.win);
                    }
                    if ((i==player.getPositionx()) & (p==player.getPositiony())){
                        tile.setImageResource(R.drawable.player);
                    }
                    board.addView(tile);
                }
            }

    }

    public void get_mapID(String name){
            String requestURL = "https://studev.groept.be/api/a20sd609/get_map"+"/"+name;
            requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try{
                        for (int i=0; i< response.length();i++) {
                            JSONObject curobject= response.getJSONObject(i);
                            String fresponse = curobject.getString("MapID");
                            String mapId = fresponse;
                            getBlocks(mapId);
                            Toast.makeText(be.kuleuven.glassapp.mapview.this, "mapLoaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e ){
                        Toast.makeText(be.kuleuven.glassapp.mapview.this, "Unable to get the map", Toast.LENGTH_LONG).show();
                    }
                }}, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(be.kuleuven.glassapp.mapview.this, "Unable to place the order", Toast.LENGTH_LONG).show();
                }});
            requestQueue.add(submitRequest);
    }

    public void getBlocks(String mapid){
            String requestURL = "https://studev.groept.be/api/a20sd609/get_allparams/" + mapid;
            requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try{

                        for (int i=0; i< response.length();i++) {
                            JSONObject curobject= response.getJSONObject(i);
                            Log.e("mapview", curobject.toString());
                            String BlockType= curobject.getString("BlockType");
                            String BlockID = curobject.getString("BlockID");
                            String Locx=curobject.getString("LocX");
                            String Locy=curobject.getString("LocY");

                            if (BlockType.equals ("Button")){
                                String ALocx=curobject.getString("ActionLocX");
                                String ALocy=curobject.getString("ActionLocY");
                                currentmap.setButton(Integer.valueOf(ALocx),Integer.valueOf(ALocy),Integer.valueOf(Locx),Integer.valueOf(Locy));
                            }

                            if (BlockType.equals("Block")||BlockType.equals("Invisible")){
                                currentmap.addBlock(BlockType,Integer.valueOf(Locx),Integer.valueOf(Locy));
                            }

                            if( BlockType.equals("TeleportingBlock")){
                                String ALocx=curobject.getString("ActionLocX");
                                String ALocy=curobject.getString("ActionLocY");
                                currentmap.setTeleport(Integer.valueOf(ALocx),Integer.valueOf(ALocy),Integer.valueOf(Locx),Integer.valueOf(Locy));
                            }

                            if (BlockType.equals ("Exit")){
                                currentmap.addExit(Integer.valueOf(Locx),Integer.valueOf(Locy));
                            }

                            if (BlockType.equals("Empty")){
                                currentmap.removeBlock(Integer.valueOf(Locx),Integer.valueOf(Locy));
                            }
                            if (i>=108){
                                viewGrid();
                            }
                        }
                    }
                    catch (JSONException e ){
                        Toast.makeText(be.kuleuven.glassapp.mapview.this, "Unable to get the blocks", Toast.LENGTH_LONG).show();
                    }
                }}, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(be.kuleuven.glassapp.mapview.this, "Unable to place the order", Toast.LENGTH_LONG).show();
                }});
            requestQueue.add(submitRequest);
    }

    public void onquit2_Clicked(View caller) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void check(int x,int y){
        if (grid[x][y] instanceof TeleportingBlock){
            player.setPositionx(Integer.valueOf(grid[x][y].getActiony()));
            player.setPositiony(Integer.valueOf(grid[x][y].getActionx()));
        }
        if (grid[x][y] instanceof Exit){
            Toast.makeText(be.kuleuven.glassapp.mapview.this, "YOU WON", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (grid[x][y] instanceof be.kuleuven.glassapp.obstacles.Button){
            if (grid[x][y].isActive()==false) {
                currentmap.addBlock("block",Integer.valueOf(grid[x][y].getActiony()),Integer.valueOf(grid[x][y].getActionx()));
                grid[x][y].setActive(true);
            }
            else{
                currentmap.removeBlock(Integer.valueOf(grid[x][y].getActiony()),Integer.valueOf(grid[x][y].getActionx()));
                grid[x][y].setActive(false);
            }
        }
    }

    public void move(String direction){
        if(direction.equals("Down")){
            while (grid[player.getPositionx()+1][player.getPositiony()] instanceof EmptyBlock){
                player.moveRight();
                check(player.getPositionx()+1,player.getPositiony());
            }
        }
        if(direction.equals("Top")){
            while (grid[player.getPositionx()-1][player.getPositiony()] instanceof EmptyBlock){
                player.moveLeft();
                check(player.getPositionx()-1,player.getPositiony());
            }
        }
        if(direction.equals("Right")){
            while (grid[player.getPositionx()][player.getPositiony()+1] instanceof EmptyBlock){
                player.moveDown();
                check(player.getPositionx(),player.getPositiony()+1);
            }
        }
        if(direction.equals("Left")){
            while (grid[player.getPositionx()][player.getPositiony()-1] instanceof EmptyBlock){
                player.moveUp();
                check(player.getPositionx(),player.getPositiony()-1);
            }
        }


    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener (Context ctx){
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }
    }



}