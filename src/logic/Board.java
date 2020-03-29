package logic;

import shape.*;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private static Board mainBoard;
    private Tile[][] board;
    private ArrayList<Shape> onBoardShapes;
    private Shape currentShape;
    private Shape nextShape;


    public Board(){
        // Initialize board
        board = new Tile[10][20];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                board[i][j] = new Tile(i,j,false);
            }
        }
        onBoardShapes = new ArrayList<>();
    }

    // Set current shape and next shape
    public void setShape(){
        if (currentShape == null){
            int random = getRandom();
            currentShape = setRandomShapeByRandom(random);
            random = getRandom();
            nextShape = setRandomShapeByRandom(random);
        }else{
            if (!currentShape.isMoving()){
                currentShape = nextShape;
                int random = getRandom();
                nextShape = setRandomShapeByRandom(random);
            }
        }
    }

    public int isThereACompleteRow(){
        int rowOccupiedTiles = 0;
        for (int i = 0; i < 20; i++) {
            int row = 0;
            for (int j = 0; j < 10; j++) {
                row = i;
                if (getBoard()[j][i].isOccupied()) rowOccupiedTiles++;
            }
            if (rowOccupiedTiles == 10){
                System.out.println("row: " + row);
                return row;
            }
            rowOccupiedTiles = 0;
        }
        return -1;
    }

    public void deleteRow(int row) {
        for (Shape shape: getOnBoardShapes()){
            for (Tile tile :
                    shape.getRotations().get(shape.rotate)) {
                if (tile.getY() == row) tile.setOccupied(false);
            }
        }
        for (int i = 0; i < 10; i++) {
            getBoard()[i][row].setOccupied(false);
        }
    }


    public void setShapeToTheBoard(Shape shapeToTheBoard){
        for (Tile t :
                shapeToTheBoard.getRotations().get(shapeToTheBoard.rotate)) {
            this.board[t.getX()][t.getY()].setOccupied(true);
        }
    }

    private static int getRandom(){
        Random random = new Random();
        return random.nextInt(7);
    }

    public Shape setRandomShapeByRandom(int rand){
        Shape shape = new Shape();
        if (rand == 0){
            shape = new Choob();
        }else if (rand == 1){
            shape = new LeftDuck();
        }else if(rand == 2){
            shape = new LeftLeg();
        }else if(rand == 3){
            shape = new RightDuck();
        }else if(rand == 4){
            shape = new RightLeg();
        }else if(rand == 5){
            shape = new T();
        }else if(rand == 6){
            shape = new Window();
        }
        return shape;
    }



    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public ArrayList<Shape> getOnBoardShapes() {
        return onBoardShapes;
    }

    public void setOnBoardShapes(ArrayList<Shape> onBoardShapes) {
        this.onBoardShapes = onBoardShapes;
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(Shape currentShape) {
        this.currentShape = currentShape;
    }

    public Shape getNextShape() {
        return nextShape;
    }

    public void setNextShape(Shape nextShape) {
        this.nextShape = nextShape;
    }

    public void moveAllOnBoardShapes(int deletedRow) {
            for (int i = deletedRow; i >= 0 ; i--) {
                for (int j = 0; j < 10; j++) {
                    if (board[j][i].isOccupied()){
                        for (Shape shape :
                                getOnBoardShapes()) {
                            for (Tile t :
                                    shape.getRotations().get(shape.rotate)) {
                                if (t.getX() == j && t.getY() == i) {
                                    t.setY(t.getY() + 1);
                                }
                            }
                        }
                        board[j][i].setOccupied(false);
                        board[j][i+1].setOccupied(true);
                    }
                }
            }
    }
}
