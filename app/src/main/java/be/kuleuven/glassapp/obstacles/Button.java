package be.kuleuven.glassapp.obstacles;

import org.json.JSONException;
import org.json.JSONObject;

public class Button extends Obstacles{

    int buttonlocx;
    int buttonlocy;
    boolean active;

    public Button( int butx, int buty){
        buttonlocx=butx;
        buttonlocy=buty;
        active=false;

    }
    public String getType(){
        return "Button";
    }

    public String getActionx() {
        return String.valueOf(buttonlocx);
    }

    public void setButtonlocx(int buttonlocx) {
        this.buttonlocx = buttonlocx;
    }

    public String getActiony() {
        return String.valueOf(buttonlocy);
    }

    public void setButtonlocy(int buttonlocy) {
        this.buttonlocy = buttonlocy;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
