import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gfx.Assets;
import gfx.Constants;
import gfx.Display;
import logic.Board;
import logic.Tile;
import score.Score;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Game implements Runnable, KeyListener {

    private int upper = 60;
    private Display display;
    private Thread thread;
    private boolean running = false;
    private BufferStrategy bs;
    private Graphics g;
    private Graphics2D graphics2D;
    private int updateCounter = 0;
    private Score topScores;

    private Board board;
    private int random;
    private int score = 0;
    private int allDeletedRow = 0;

    private boolean isLost = false;

    // Main constructor
    Game() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Initialize display
    private void init() throws IOException {
        display = Display.getInstance();
        Assets.init();
        display.addKeyListener(this);
        board = new Board();
        readTopScores();
        playBackSound();
    }

    private void restart() throws IOException {
        board = new Board();
        readTopScores();
        isLost = false;
        score = 0;
        allDeletedRow = 0;
    }

    private void readTopScores() throws IOException {
        File file = new File(Constants.TOP_SCORE_FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        topScores = objectMapper.readValue(file, Score.class);
        Arrays.sort(topScores.getTopScores());
    }

    private void update() {
        board.setShape();
        updateCounter++;

        if (!isLost) {
            // Check if player lost
            if (board.getBoard()[4][3].isOccupied() ||
                    board.getBoard()[5][3].isOccupied() ||
                    board.getBoard()[6][3].isOccupied()) {
                if (topScores.getTopScores()[0] < score){
                    addScoreToTopScore(score);
                }
                playSound(Constants.FAIL);
                isLost = true;
            }

            // Check if there is complete rows
            int deletedRow = board.isThereACompleteRow();
            while (deletedRow != -1) {
                playSound(Constants.ROW_SOUND);
                score += 10;
                board.deleteRow(deletedRow);
                board.moveAllOnBoardShapes(deletedRow);
                allDeletedRow++;
                deletedRow = board.isThereACompleteRow();
            }

            if (updateCounter >= upper) {
                // if Shape can move move the shape
                if (board.getCurrentShape().isAvailableDown(board.getBoard())) {
                    board.getCurrentShape().moveDownShape();
                    board.getCurrentShape().plusYCounter++;
                } else {
                    board.getCurrentShape().setMoving(false);
                    board.setShapeToTheBoard(board.getCurrentShape());
                    board.getOnBoardShapes().add(board.getCurrentShape());
                    board.setShape();
                    score++;
                    playSound(Constants.DROP_SOUND);
                }
                updateCounter = 0;
            }
        }
    }

    private void addScoreToTopScore(int s) {
        topScores.getTopScores()[0] = s;
        Arrays.sort(topScores.getTopScores());
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(Constants.TOP_SCORE_FILE_PATH);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, topScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void render() {
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();

        graphics2D = (Graphics2D) bs.getDrawGraphics();
        // Clear Screen
        g.clearRect(0, 0, Constants.WIDTH, Constants.HEIGHTS);
        // Start drawing
        // Show Score
        g.setColor(new Color(0xA1A5A3));
        g.fillRect(400, 0, 200, Constants.HEIGHTS);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        showScore(graphics2D);
        showTopScores(graphics2D);


        graphics2D.setColor(new Color(0x000000));

        if (!isLost) {
            // Draw next shape
            graphics2D.setFont(new Font("Serif", Font.BOLD, 26));
            graphics2D.drawString("Next Shape: ", 430, 130);
            drawNextShape(board.getNextShape(), g);

            // Draw shapes which are not moving
            for (shape.Shape shape :
                    board.getOnBoardShapes()) {
                drawCurrentMovingShape(shape, g);
            }

            // Draw current moving shape
            drawCurrentMovingShape(board.getCurrentShape(), g);

            // Draw Lines
            drawLines(g);
        } else {
            showLostMassage(graphics2D);
        }
        // End drawing
        bs.show();
        g.dispose();
    }

    private void showLostMassage(Graphics2D graphics2D) {
        graphics2D.setFont(new Font("Serif", Font.BOLD, 34));
        graphics2D.drawString("You are lost", 110, 320);
        graphics2D.setFont(new Font("Serif", Font.BOLD, 20));
        graphics2D.drawString("Press ENTER to start again", 80, 350);
    }

    private void showTopScores(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(0x000000));
        graphics2D.drawString("Top scores ", 435, 350);
        graphics2D.setColor(new Color(0x3E3E3E));
        graphics2D.fillRect(435, 360, 90, 5);
        graphics2D.setColor(new Color(0x000000));
        for (int i = 0; i < 10; i++) {
            if (topScores.getTopScores()[9 - i] != 0) {
                graphics2D.drawString(String.valueOf(topScores.getTopScores()[9 - i]), 440, 390 + (i * 25));
            }
        }
    }

    private void showScore(Graphics2D graphics2D) {
        graphics2D.setFont(new Font("Serif", Font.BOLD, 28));
        graphics2D.drawString("Score: " + score, 435, 50);
        graphics2D.setFont(new Font("Serif", Font.BOLD, 18));
        graphics2D.setColor(new Color(0x9C1511));
        graphics2D.drawString("Deleted rows: " + allDeletedRow, 435, 80);
    }

    private void drawCurrentMovingShape(shape.Shape shape, Graphics g) {
        for (Tile t :
                shape.getRotations().get(shape.rotate)) {
            if (t.isOccupied()) {
                g.drawImage(shape.getTileImage(), 40 * t.getX(), (40 * t.getY()) - 160, null);
            }
        }
    }

    private void drawNextShape(shape.Shape shape, Graphics g) {
        for (Tile t :
                shape.getRotations().get(shape.rotate)) {
            if (t.isOccupied()) {
                g.drawImage(shape.getTileImage(), (t.getX() * 40) + 285, 40 * t.getY() + 140, null);
            }
        }
    }

    private void drawLines(Graphics g) {
        g.setColor(new Color(0xD8DCDE));
        for (int i = 0; i < 9; i++) {
            g.drawLine(40 + i * 40, 0, 40 + i * 40, Constants.HEIGHTS);
        }
        for (int i = 0; i < 15; i++) {
            g.drawLine(0, 40 + i * 40, Constants.CANVAS_WIDTH, 40 + i * 40);
        }
    }


    @Override
    public void run() {

        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        // Game loop
        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                update();
                render();
                ticks++;
                delta--;
            }
            if (timer >= 1000000000) {
                System.out.println("Frame per seconds: " + ticks);
                ticks = 0;
                timer = 0;
            }
        }
        stop();
    }

    synchronized void start() {
        // We don't want to start thread again!!
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // Start again
            try {
                restart();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            // Toobeh
            board.getCurrentShape().moveUpShape();
            board.getCurrentShape().plusYCounter = 0;
            board.getCurrentShape().deltaX = 0;
            board.getCurrentShape().deltaRot = 0;

        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            // Move Left
            if (board.getCurrentShape().isAvailableLeft(board.getBoard())) {
                board.getCurrentShape().moveLeftShape();
                board.getCurrentShape().deltaX++;
            }

        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            // Move Right
            if (board.getCurrentShape().isAvailableRight(board.getBoard())) {
                board.getCurrentShape().moveRightShape();
                board.getCurrentShape().deltaX--;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            // Rotate left
            if (board.getCurrentShape().isRotateLeft(board.getBoard())) {
                board.getCurrentShape().deltaRot++;
                board.getCurrentShape().rotate++;
                board.getCurrentShape().rotate += 4;
                board.getCurrentShape().rotate %= 4;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            // Rotate right
            if (board.getCurrentShape().isRotateRight(board.getBoard())) {
                board.getCurrentShape().deltaRot--;
                if (board.getCurrentShape().isAvailableRight(board.getBoard())) {
                    board.getCurrentShape().rotate--;
                    board.getCurrentShape().rotate += 4;
                    board.getCurrentShape().rotate %= 4;
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            // speed up!!
            upper = 15;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            // Back to the normal speed
            upper = 60;
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private static void playSound(final String url) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url));
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void playBackSound() {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Assets.backgroundMusic);
            clip.open(inputStream);
            clip.loop(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
