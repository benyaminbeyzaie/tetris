package shape;

import logic.Board;
import logic.Tile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Shape {
    public int deltaX = 0;
    public int plusYCounter = 0;
    public int deltaRot = 0;
    public int rotate = 0;
    ArrayList<ArrayList<Tile>> rotations;
    ArrayList<Tile> shape, shape1, shape2, shape3, defaultShape;
    BufferedImage tileImage;
    boolean isMoving;

    public boolean isAvailableDown(Tile[][] board){
        int allTiles = 0;
        int canMoveTiles = 0;
        for (Tile t : getRotations().get(rotate)) {
            if (t.isOccupied()){
                allTiles++;
                if ((t.getY() + 1 < 20) && !board[t.getX()][t.getY() + 1].isOccupied()){
                    canMoveTiles++;
                }
            }
        }
        if (allTiles == canMoveTiles) return true;
        return false;
    }

    public boolean isAvailableLeft(Tile[][] board){
        int allTiles = 0;
        int canMoveTiles = 0;
        for (Tile t : getRotations().get(rotate)) {
            allTiles++;
            if ((t.getX() - 1 >= 0) && !board[t.getX() - 1][t.getY()].isOccupied()) canMoveTiles++;
        }
        if (allTiles == canMoveTiles) return true;
        return false;
    }

    public boolean isAvailableRight(Tile[][] board){
        int allTiles = 0;
        int canMoveTiles = 0;
        for (Tile t : getRotations().get(rotate)) {
            allTiles++;
            if ((t.getX() + 1 < 10) && !board[t.getX() + 1][t.getY()].isOccupied()) canMoveTiles++;
        }
        if (allTiles == canMoveTiles) return true;
        return false;
    }

    public boolean isRotateLeft(Tile[][] board){
        int allTiles = 0;
        int canMoveTiles = 0;
        int temp;
        if (rotate == 3) temp = 0;
        else temp = rotate + 1;
        for (Tile t : getRotations().get(temp)) {
            allTiles++;
            if (t.getY() < 20 && t.getX() >= 0 && (t.getX()< 10) && !board[t.getX()][t.getY()].isOccupied()) canMoveTiles++;
        }
        if (allTiles == canMoveTiles) return true;
        return false;
    }

    public boolean isRotateRight(Tile[][] board){
        int allTiles = 0;
        int canMoveTiles = 0;
        int temp;
        if (rotate == 0) temp = 3;
        else temp = rotate - 1;
        for (Tile t : getRotations().get(temp)) {
            allTiles++;
            if (t.getY() < 20 && t.getX() >= 0 && (t.getX() < 10) && !board[t.getX()][t.getY()].isOccupied()) canMoveTiles++;
        }
        if (allTiles == canMoveTiles) return true;
        return false;
    }

    public void moveDownShape(){
        for (ArrayList<Tile> shape :
                this.getRotations()) {
            for (Tile t :
                    shape) {
                if (t.isOccupied()){
                    t.setY(t.getY() + 1);
                }
            }
        }
    }

    public void moveUpShape(){
        rotate = ((rotate - (deltaRot + 4)) + 4) % 4;
        for (ArrayList<Tile> shape :
                this.getRotations()) {
            for (Tile t :
                    shape) {
                if (t.isOccupied()){
                    t.setY(t.getY() - plusYCounter);
                    t.setX(t.getX() + deltaX);
                }
            }
        }
    }

    public void moveLeftShape(){
        for (ArrayList<Tile> shape :
                this.getRotations()) {
            for (Tile t :
                    shape) {
                if (t.isOccupied()){
                    t.setX(t.getX() - 1);
                }
            }
        }
    }

    public void moveRightShape(){
        for (ArrayList<Tile> shape :
                this.getRotations()) {
            for (Tile t :
                    shape) {
                if (t.isOccupied()){
                    t.setX(t.getX() + 1);
                }
            }
        }
    }

    public ArrayList<ArrayList<Tile>> getRotations() {
        return rotations;
    }

    public void setRotations(ArrayList<ArrayList<Tile>> rotations) {
        this.rotations = rotations;
    }

//    public ArrayList<Tile> getShape() {
//        return shape;
//    }

    public void setShape(ArrayList<Tile> shape) {
        this.shape = shape;
    }

    public BufferedImage getTileImage() {
        return tileImage;
    }

    public void setTileImage(BufferedImage tileImage) {
        this.tileImage = tileImage;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }
}
