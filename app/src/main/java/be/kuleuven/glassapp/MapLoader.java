package be.kuleuven.glassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class mapLoader extends AppCompatActivity {
    private ImageButton map_id_1;
    private RequestQueue requestQueue;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maploader);
        Intent intent = getIntent();
        map_id_1 = (ImageButton) findViewById(R.id.map_id_1);
    }


    public void get_mapName() {
        String requestURL = "https://studev.groept.be/api/a20sd609/get_mapName";
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONArray>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONArray response) {
                try {
                        @SuppressLint("WrongViewCast") LinearLayout layout = (LinearLayout) findViewById(R.id.map_id_1);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout row = new LinearLayout(be.kuleuven.glassapp.mapLoader.this);
                        row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        for (int j = 0; j < response.length(); j++)
                        {
                            Button btnTag = new Button(be.kuleuven.glassapp.mapLoader.this);
                            btnTag.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
                            JSONObject curobject = response.getJSONObject(j);
                            String label= curobject.getString("MapName");
                            btnTag.setText(""+ label);
                            //btnTag.setId(j +1);
                            row.addView(btnTag);
                        }
                        layout.addView(row);

                        //get_mapID(mapName);
                        Toast.makeText(be.kuleuven.glassapp.mapLoader.this, "mapSelected", Toast.LENGTH_SHORT).show();
                    } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(be.kuleuven.glassapp.mapLoader.this, "Unable to place the order", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(submitRequest);
    }

}