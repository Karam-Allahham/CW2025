package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class BrickFactoryTest {

    @Test
    @DisplayName("createBrick should create I brick")
    void createBrickShouldCreateIBrick() {
        Brick brick = BrickFactory.createBrick(BrickFactory.BrickType.I);
        assertNotNull(brick);
        assertInstanceOf(IBrick.class, brick);
    }

    @Test
    @DisplayName("createBrick should create all brick types")
    void createBrickShouldCreateAllTypes() {
        for (BrickFactory.BrickType type : BrickFactory.BrickType.values()) {
            Brick brick = BrickFactory.createBrick(type);
            assertNotNull(brick, "Brick of type " + type + " should not be null");
        }
    }

    @Test
    @DisplayName("createRandomBrick should return a valid brick")
    void createRandomBrickShouldReturnValidBrick() {
        Brick brick = BrickFactory.createRandomBrick();
        assertNotNull(brick);
    }

    @Test
    @DisplayName("createRandomBrick should return bricks with shape matrices")
    void randomBrickShouldHaveShapeMatrix() {
        Brick brick = BrickFactory.createRandomBrick();
        assertNotNull(brick.getShapeMatrix());
        assertFalse(brick.getShapeMatrix().isEmpty());
    }

    @Test
    @DisplayName("getBrickTypeCount should return 7")
    void getBrickTypeCountShouldReturn7() {
        assertEquals(7, BrickFactory.getBrickTypeCount());
    }

    @Test
    @DisplayName("getAllTypes should return all brick types")
    void getAllTypesShouldReturnAllTypes() {
        BrickFactory.BrickType[] types = BrickFactory.getAllTypes();
        assertEquals(7, types.length);
    }
}