package be.kuleuven.glassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnPlus;
    private Button btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlus = (Button) findViewById(R.id.AddMap);
        btnLoad = (Button) findViewById(R.id.LoadMap);
    }

    public void onbtnPlus_Clicked(View caller) {
        Intent intent = new Intent(this, mapeditor.class);
        startActivity(intent);
    }

    public void onbtnLoad_Clicked(View caller) {
        Intent intent = new Intent(this, mapLoader.class);
        startActivity(intent);
    }

    //public void getMapNames(){

    //}
}