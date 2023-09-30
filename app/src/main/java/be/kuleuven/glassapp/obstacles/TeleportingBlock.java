package be.kuleuven.glassapp.obstacles;

import org.json.JSONException;
import org.json.JSONObject;

public class TeleportingBlock extends Obstacles{

    int telx;
    int tely;

    public TeleportingBlock( int xloc, int yloc){
        telx=xloc;
        tely=yloc;
    }
    public String getType(){
        return "TeleportingBlock";
    }

    public String getActionx() {
        return String.valueOf(telx);
    }

    public void setTelx(int telx) {
        this.telx = telx;
    }

    public String getActiony() {
        return String.valueOf(tely);
    }

    public void setTely(int tely) {
        this.tely = tely;
    }
}
