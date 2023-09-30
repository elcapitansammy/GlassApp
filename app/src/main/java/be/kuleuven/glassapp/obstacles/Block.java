package be.kuleuven.glassapp.obstacles;

public class Block extends Obstacles{
    boolean visible;

    public Block(boolean vis){
        visible=vis;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getType(){
        return "Block";
    }
}
