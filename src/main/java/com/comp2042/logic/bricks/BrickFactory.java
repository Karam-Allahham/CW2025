package com.comp2042.logic.bricks;

/**
 * Factory class for creating Tetris brick instances.
 * Implements the Factory pattern to create different types of bricks based on type specification.
 * Supports creating specific brick types or random bricks.
 */
public class BrickFactory {

    /**
     * Enum representing the different types of Tetris bricks available.
     */
    public enum BrickType {
        /** I-piece: horizontal line of four blocks */
        I,
        /** J-piece: L-shaped piece with block on the left */
        J,
        /** L-piece: L-shaped piece with block on the right */
        L,
        /** O-piece: 2x2 square */
        O,
        /** S-piece: S-shaped piece */
        S,
        /** T-piece: T-shaped piece */
        T,
        /** Z-piece: Z-shaped piece */
        Z
    }

    /**
     * Creates a new brick instance of the specified type.
     * 
     * @param type the type of brick to create
     * @return a new Brick instance of the specified type
     */
    public static Brick createBrick(BrickType type) {
        return switch (type) {
            case I -> new IBrick();
            case J -> new JBrick();
            case L -> new LBrick();
            case O -> new OBrick();
            case S -> new SBrick();
            case T -> new TBrick();
            case Z -> new ZBrick();
        };
    }

    /**
     * Creates a new brick instance of a randomly selected type.
     * 
     * @return a new Brick instance of a random type
     */
    public static Brick createRandomBrick() {
        BrickType[] types = BrickType.values();
        int randomIndex = java.util.concurrent.ThreadLocalRandom.current().nextInt(types.length);
        return createBrick(types[randomIndex]);
    }

    /**
     * Gets an array of all available brick types.
     * 
     * @return an array containing all BrickType values
     */
    public static BrickType[] getAllTypes() {
        return BrickType.values();
    }

    /**
     * Gets the total number of different brick types available.
     * 
     * @return the number of brick types
     */
    public static int getBrickTypeCount() {
        return BrickType.values().length;
    }
}
