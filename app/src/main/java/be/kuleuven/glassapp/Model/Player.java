package be.kuleuven.glassapp.Model;

public class Player {
    int positionx;
    int positiony;

    public Player(){
        positionx=4;
        positiony=1;
    }

    public int getPositionx() {
        return positionx;
    }

    public void setPositionx(int positionx) {
        this.positionx = positionx;
    }

    public int getPositiony() {
        return positiony;
    }

    public void setPositiony(int positiony) {
        this.positiony = positiony;
    }

    public void moveRight(){
        positionx++;
    }
    public void moveLeft(){
        positionx--;
    }
    public void moveUp(){
        positiony--;
    }
    public void moveDown(){
        positiony++;
    }
}
