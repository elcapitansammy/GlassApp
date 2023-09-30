package be.kuleuven.glassapp.Model;

import android.opengl.ETC1;

import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.glassapp.obstacles.ArrivingBlock;
import be.kuleuven.glassapp.obstacles.Block;
import be.kuleuven.glassapp.obstacles.Blocktoactivate;
import be.kuleuven.glassapp.obstacles.Button;
import be.kuleuven.glassapp.obstacles.EmptyBlock;
import be.kuleuven.glassapp.obstacles.Exit;
import be.kuleuven.glassapp.obstacles.InvisibleBlock;
import be.kuleuven.glassapp.obstacles.Obstacles;
import be.kuleuven.glassapp.obstacles.TeleportingBlock;

public class Map {
    int xsize;
    int ysize;
    Obstacles[][] map;

    public Map(){
        xsize=9;
        ysize=12;
        map= new Obstacles[xsize][ysize];
    }

    public Obstacles[][] getMap() {
        return map;
    }

    public int getXsize() {
        return xsize;
    }

    public void setXsize(int xsize) {
        this.xsize = xsize;
    }

    public int getYsize() {
        return ysize;
    }

    public void setYsize(int ysize) {
        this.ysize = ysize;
    }

    public void initMap(){
        for (int i=0; i<xsize;i++){
            for(int p=0; p<ysize;p++){
                map[i][p]= new EmptyBlock();
            }
        }
        for (int i=0;i<xsize;i++){
            map[i][0]=new Block(true);
            map[i][ysize-1]=new Block(true);
        }

        for (int i=0;i<ysize;i++){
            map[0][i]=new Block(true);
            map[xsize-1][i]=new Block(true);
        }
    }

    public void addBlock(String type, int locx, int locy){
        if (type.equals("Invisible")){
            map[locx][locy]= new InvisibleBlock();
        }
        else{
            map[locx][locy]= new Block(true);
        }

    }

    public void addButton(int locx, int locy){
        map[locx][locy]= new Button(0,0);
    }

    public void setButton(int x,int y,int locx, int locy){
        map[locx][locy]= new Button(x,y);
        map[y][x]=new Blocktoactivate();
    }

    public void addTeleport(int locx, int locy){
        map[locx][locy]= new TeleportingBlock(0,0);
    }

    public void setTeleport(int x,int y,int locx, int locy){
        map[locx][locy]= new TeleportingBlock(x,y);
        map[y][x]=new ArrivingBlock();
    }
    public void addExit(int locx, int locy){
        map[locx][locy]= new Exit();
    }

    public void removeBlock(int locx, int locy){
        map[locx][locy]= new EmptyBlock();
    }
}
