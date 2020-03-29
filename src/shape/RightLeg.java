package shape;

import gfx.Assets;
import logic.Tile;

import java.util.ArrayList;

public class RightLeg extends Shape {
    public RightLeg() {
        isMoving = true;
        rotations = new ArrayList<>();
        tileImage = Assets.five;

        shape = new ArrayList<>();
        shape.add(new Tile(5, 1, true));
        shape.add(new Tile(5, 2, true));
        shape.add(new Tile(5, 3, true));
        shape.add(new Tile(6, 3, true));
        rotations.add(shape);

        shape1 = new ArrayList<>();
        shape1.add(new Tile(3, 3, true));
        shape1.add(new Tile(4, 3, true));
        shape1.add(new Tile(5, 3, true));
        shape1.add(new Tile(5, 2, true));
        rotations.add(shape1);

        shape2 = new ArrayList<>();
        shape2.add(new Tile(4, 3, true));
        shape2.add(new Tile(5, 3, true));
        shape2.add(new Tile(5, 4, true));
        shape2.add(new Tile(5, 5, true));
        rotations.add(shape2);

        shape3 = new ArrayList<>();
        shape3.add(new Tile(5, 3, true));
        shape3.add(new Tile(6, 3, true));
        shape3.add(new Tile(7, 3, true));
        shape3.add(new Tile(5, 4, true));
        rotations.add(shape3);
    }
}
