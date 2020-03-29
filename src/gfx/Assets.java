package gfx;

import java.awt.image.BufferedImage;
import java.io.File;

public class Assets {
    public static BufferedImage sheet, one, two, three, four, five, six, seven;
    public static File backgroundMusic, dropMusic, rowMusic;
    public static void init(){
        sheet = ImageLoader.loadImage("res/pieces.png");
        one = sheet.getSubimage(0, 0, 40, 40);
        two = sheet.getSubimage(40,0,40,40);
        three = sheet.getSubimage(80,0,40,40);
        four = sheet.getSubimage(120,0,40,40);
        five = sheet.getSubimage(160,0,40,40);
        six = sheet.getSubimage(200,0,40,40);
        seven = sheet.getSubimage(240,0,40,40);
        backgroundMusic = new File("res\\Netherplace.wav");
    }
}
