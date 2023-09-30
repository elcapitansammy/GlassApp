package be.kuleuven.glassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.glassapp.Model.Map;
import be.kuleuven.glassapp.obstacles.ArrivingBlock;
import be.kuleuven.glassapp.obstacles.Block;
import be.kuleuven.glassapp.obstacles.Blocktoactivate;
import be.kuleuven.glassapp.obstacles.EmptyBlock;
import be.kuleuven.glassapp.obstacles.Exit;
import be.kuleuven.glassapp.obstacles.InvisibleBlock;
import be.kuleuven.glassapp.obstacles.Obstacles;
import be.kuleuven.glassapp.obstacles.TeleportingBlock;

public class mapeditor extends AppCompatActivity {
    Map editmap;
    GridLayout board;
    Obstacles[][] grid;
    int currentx;
    int currenty;
    int locx;
    int locy;
    boolean playclicked=false;
    String mapname;
    private ImageButton addnormalblock;
    private ImageButton addinvisible;
    private ImageButton addteleport;
    private ImageButton addbutton;
    private ImageButton Exit;
    private ImageButton quit;
    private Button save;
    private ImageButton delete;
    private Button play;
    private TextView Text;
    private TextInputEditText name;

    private RequestQueue requestQueue;
    private static final String SUBMIT_URL = "https://studev.groept.be/api/a20sd609/insert_simple_Block";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapeditor);
        addnormalblock = (ImageButton) findViewById(R.id.NormalBlock);
        addinvisible = (ImageButton) findViewById(R.id.Invisible);
        addteleport = (ImageButton) findViewById(R.id.teletransport);
        addbutton = (ImageButton) findViewById(R.id.Button);
        Exit = (ImageButton) findViewById(R.id.setlocation);
        quit = (ImageButton) findViewById(R.id.quit);
        save = (Button) findViewById(R.id.save);
        delete = (ImageButton) findViewById(R.id.delete);
        play = (Button) findViewById(R.id.Play);
        Text= (TextView) findViewById(R.id.Text);
        Text.setText(" - choose a tile ");
        name= (TextInputEditText) findViewById(R.id.name);
        addnormalblock.setEnabled(false);
        addinvisible.setEnabled(false);
        addbutton.setEnabled(false);
        addteleport.setEnabled(false);
        editmap= new Map();
        editmap.initMap();
        grid = editmap.getMap();
        createGrid();
    }

    public void Play_Clicked(View caller){
        playclicked=true;
        mapname = String.valueOf(name.getText());
        sendMap(mapname);
    }

    public void createGrid(){
        board = findViewById(R.id.board);
        board.setRowCount(9);
        board.setColumnCount(12);
        int index=0;
        for (int i=0; i<editmap.getXsize();i++){
            for(int p=0; p<editmap.getYsize();p++){
                ImageButton tile= new ImageButton(this);
                tile.setId(index);
                index++;
                tile.setLayoutParams(new ViewGroup.LayoutParams(90,90));
                tile.setMaxHeight(90);
                tile.setMaxWidth(90);
                if (grid[i][p] instanceof EmptyBlock){
                    tile.setImageResource(R.drawable.stonegreen);
                }
                if (grid[i][p] instanceof Block){
                    tile.setImageResource(R.drawable.coblestor);
                }
                if (grid[i][p] instanceof InvisibleBlock) {
                    tile.setImageResource(R.drawable.invisiblee);
                }
                if (grid[i][p] instanceof TeleportingBlock){
                    tile.setImageResource(R.drawable.teletransport2);
                }
                if (grid[i][p] instanceof be.kuleuven.glassapp.obstacles.Button){
                    tile.setImageResource(R.drawable.buttont);
                }
                if (grid[i][p] instanceof Blocktoactivate){
                    tile.setImageResource(R.drawable.grey);
                }
                if (grid[i][p] instanceof ArrivingBlock){
                    tile.setImageResource(R.drawable.arriving2);
                }
                if (grid[i][p] instanceof Exit){
                    tile.setImageResource(R.drawable.win);
                }

                int finalI = i;
                int finalP=p;
                tile.setOnClickListener(e->{
                    currenty=finalI;
                    currentx=finalP;
                    Text.setText(" - choose Normal or Invisible Block \n or\n - For button/teletransport: long click to choose the location  to link to the block ");
                    onclickgrid();
                } );
                tile.setOnLongClickListener(e->{
                    locy=finalI;
                    locx=finalP;
                    onclickgrid();
                    Text.setText(" - Place teletransport or button block");
                    return true;
                } );
                board.addView(tile);

            }
        }
    }

    public mapeditor getMapeditor(){
        return this;
    }
    public void Button_Clicked(View caller){
        editmap.addButton(currenty,currentx);
        editmap.setButton(locx,locy,currenty,currentx);
        addnormalblock.setEnabled(false);
        addinvisible.setEnabled(false);
        addbutton.setEnabled(false);
        addteleport.setEnabled(false);
        board.removeAllViewsInLayout();
        Text.setText(" - choose a tile");
        createGrid();

    }

    public void Teletransport_Clicked(View caller){
        editmap.addTeleport(currenty,currentx);
        editmap.setTeleport(locx,locy,currenty,currentx);
        addnormalblock.setEnabled(false);
        addinvisible.setEnabled(false);
        addbutton.setEnabled(false);
        addteleport.setEnabled(false);
        board.removeAllViewsInLayout();
        Text.setText(" - choose a tile");
        createGrid();

    }

    public void onquit_Clicked(View caller) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onclickgrid(){
        addnormalblock.setEnabled(true);
        addinvisible.setEnabled(true);
        addbutton.setEnabled(true);
        addteleport.setEnabled(true);
    }



    public void submitentireMap(String mapId) {
        for (int i=0; i<editmap.getXsize();i++) {
            for (int p = 0; p < editmap.getYsize(); p++) {
                sendsingleBlock(grid[i][p].getType(),String.valueOf(i),String.valueOf(p),mapId,grid[i][p].getActionx(),grid[i][p].getActiony());
            }
        }
        if (playclicked==true){
            Intent intent = new Intent(this, mapview.class);
            intent.putExtra("MapName",mapname);
            startActivity(intent);
        }
    }

    public void sendsingleBlock(String BlockType, String locx2, String locy2,String idmap,String Actionx,String Actiony){
        String requestURL = SUBMIT_URL +"/"+grid [Integer.parseInt(locx2)][Integer.parseInt(locy2)].getType()+"/"+locx2+"/"+locy2+"/"+idmap;
        requestQueue= Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                get_blockid(locx2,locy2,BlockType,idmap,Actionx,Actiony);
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mapeditor.this, "Unable to place the block", Toast.LENGTH_LONG).show();
            }});
        requestQueue.add(submitRequest);
    }

    public void sendBlockParameters(String actionlocx,String actionlocy,String id){
        String requestURL = "https://studev.groept.be/api/a20sd609/Add_weird_block"+"/"+id+"/"+actionlocx+"/"+actionlocy;
        requestQueue= Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mapeditor.this, "weird placed", Toast.LENGTH_LONG).show();
            }}, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mapeditor.this, "Unable to place the map", Toast.LENGTH_LONG).show();
            }});
        requestQueue.add(submitRequest);
    }

    public void get_blockid(String locx, String locy, String type, String id,String actionx2, String actiony2){
        if ((type == "TeleportingBlock")||(type== "Button")){
            String requestURL = "https://studev.groept.be/api/a20sd609/get_blockid" + "/" + locx + "/"+locy+"/"+type+"/"+id;
            requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try{
                        for (int i=0; i< response.length();i++) {
                            JSONObject curobject= response.getJSONObject(i);
                            String fresponse = curobject.getString("BlockID");
                            String blockid = fresponse;
                            sendBlockParameters(actionx2,actiony2,blockid);
                        }
                    }
                    catch (JSONException e ){
                        Toast.makeText(mapeditor.this, "Unable to get the map", Toast.LENGTH_LONG).show();
                    }
                }}, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mapeditor.this, "Unable to place the order", Toast.LENGTH_LONG).show();
                }});
            requestQueue.add(submitRequest);
        }
        else{
            int x= 2;
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
                        submitentireMap(mapId);
                        Toast.makeText(mapeditor.this, "nice", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e ){
                    Toast.makeText(mapeditor.this, "Unable to get the map", Toast.LENGTH_LONG).show();
                }
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mapeditor.this, "Unable to place the order", Toast.LENGTH_LONG).show();
            }});
        requestQueue.add(submitRequest);
    }

    public void sendMap(String name){
        String requestURL = "https://studev.groept.be/api/a20sd609/Add_map/"+name;
        requestQueue= Volley.newRequestQueue(this);
        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mapeditor.this, "map submitted", Toast.LENGTH_SHORT).show();
                get_mapID(name);
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mapeditor.this, "Unable to place the map", Toast.LENGTH_LONG).show();
            }});
        requestQueue.add(submitRequest);
    }

    public void Submit_Clicked(View caller){
        sendMap(String.valueOf(name.getText()));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void NormalBlock_Clicked(View caller){
        editmap.addBlock("normal",currenty,currentx);
        addnormalblock.setEnabled(false);
        board.removeAllViewsInLayout();
        Text.setText(" - choose a tile");
        createGrid();

    }

    public void Remove_Clicked(View caller){
        if (grid[currenty][currentx] instanceof be.kuleuven.glassapp.obstacles.Exit){
            Exit.setEnabled(true);
        }
        editmap.removeBlock(currenty,currentx);
        board.removeAllViewsInLayout();
        Text.setText(" - choose a tile");
        createGrid();

    }

    public void Exit_Clicked(View caller){
        editmap.addExit(currenty,currentx);
        board.removeAllViewsInLayout();
        Text.setText(" - choose a tile");
        Exit.setEnabled(false);
        board.removeView(Exit);
        createGrid();

    }

    public void Invisible_Clicked(View caller){
        editmap.addBlock("Invisible",currenty,currentx);
        addinvisible.setEnabled(false);
        board.removeAllViewsInLayout();
        Text.setText(" - choose a tile");
        createGrid();

    }

}