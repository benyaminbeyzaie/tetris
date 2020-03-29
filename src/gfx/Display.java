package gfx;

import logic.Board;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    private static Display mainDis;
    private String title;
    private int width, height;
    private Canvas canvas;

    public static Display getInstance(){
        if (mainDis == null) mainDis = new Display(Constants.TITLE, Constants.WIDTH, Constants.HEIGHTS);
        return mainDis;
    }

    private Display(String title, int width, int height){
        super(title);

        this.width = width;
        this.height = height;

        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(Constants.WIDTH, height));
        canvas.setFocusable(false);
        add(canvas);



        pack();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

}
