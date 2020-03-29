package shape;

import gfx.Assets;
import logic.Tile;

import java.util.ArrayList;

public class Choob extends Shape {
    public Choob() {
        isMoving = true;
        rotations = new ArrayList<>();
        tileImage = Assets.one;

        shape = new ArrayList<>();
        shape.add(new Tile(5, 0, true));
        shape.add(new Tile(5, 1, true));
        shape.add(new Tile(5, 2, true));
        shape.add(new Tile(5, 3, true));
        rotations.add(shape);

        shape1 = new ArrayList<>();
        shape1.add(new Tile(4, 3, true));
        shape1.add(new Tile(5, 3, true));
        shape1.add(new Tile(6, 3, true));
        shape1.add(new Tile(7, 3, true));
        rotations.add(shape1);

        shape2 = new ArrayList<>();
        shape2.add(new Tile(5, 0, true));
        shape2.add(new Tile(5, 1, true));
        shape2.add(new Tile(5, 2, true));
        shape2.add(new Tile(5, 3, true));
        rotations.add(shape2);

        shape3 = new ArrayList<>();
        shape3.add(new Tile(4, 3, true));
        shape3.add(new Tile(5, 3, true));
        shape3.add(new Tile(6, 3, true));
        shape3.add(new Tile(7, 3, true));
        rotations.add(shape3);
    }
}
