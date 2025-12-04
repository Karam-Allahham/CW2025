package com.comp2042.logic.bricks;

public class BrickFactory {

    public enum BrickType {
        I, J, L, O, S, T, Z
    }

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

    public static Brick createRandomBrick() {
        BrickType[] types = BrickType.values();
        int randomIndex = java.util.concurrent.ThreadLocalRandom.current().nextInt(types.length);
        return createBrick(types[randomIndex]);
    }

    public static BrickType[] getAllTypes() {
        return BrickType.values();
    }

    public static int getBrickTypeCount() {
        return BrickType.values().length;
    }
}
