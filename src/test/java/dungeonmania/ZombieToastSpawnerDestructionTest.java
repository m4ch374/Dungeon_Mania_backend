package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawnerDestructionTest {
    private static final String DIR_NAME = "d_ZombieSpawnerTests/";

    @Test
    @DisplayName("Test player can overlap with toast spawner")
    public void testOverlapsWithPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame(DIR_NAME + "d_zombieSpawnerTest_generalTest", "c_movementTest_testMovementDown");
        assertTrue(TestUtils.getEntities(res, "player").size() == 1);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 1);
        
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        Position playerPos = TestUtils.getEntityById(res, "player").getPosition();
        Position ztsPos = TestUtils.getEntityById(res, "zombie_toast_spawner").getPosition();

        assertTrue(playerPos.equals(ztsPos));
    }

    @Test
    @DisplayName("Test no such entity")
    public void testException_noSuchEntity() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame(DIR_NAME + "d_zombieSpawnerTest_generalTest", "c_movementTest_testMovementDown");
        assertTrue(TestUtils.getEntities(res, "player").size() == 1);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 1);
        
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("inval33d"));
    }

    @Test
    @DisplayName("Test player not in range")
    public void testException_playerNotInRnage() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame(DIR_NAME + "d_zombieSpawnerTest_generalTest", "c_movementTest_testMovementDown");
        assertTrue(TestUtils.getEntities(res, "player").size() == 1);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 1);
        
        assertThrows(InvalidActionException.class, () -> dmc.interact("zombie_toast_spawner"));

        // move to upper left of the toast spawner
        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.interact("zombie_toast_spawner"));

        // move to upper right of the toast spawner
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.interact("zombie_toast_spawner"));

        // move to lower right of the toast spawner
        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertThrows(InvalidActionException.class, () -> dmc.interact("zombie_toast_spawner"));

        // move to lower left of the toast spawner
        dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertThrows(InvalidActionException.class, () -> dmc.interact("zombie_toast_spawner"));

        // overlaps with spawner
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        assertThrows(InvalidActionException.class, () -> dmc.interact("zombie_toast_spawner"));
    }

    @Test
    @DisplayName("Test player no weapon")
    public void testException_playerNoWeapon() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame(DIR_NAME + "d_zombieSpawnerTest_generalTest", "c_movementTest_testMovementDown");
        assertTrue(TestUtils.getEntities(res, "player").size() == 1);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 1);
        
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertThrows(InvalidActionException.class, () -> dmc.interact("zombie_toast_spawner"));
    }

    @Test
    @DisplayName("Test player destroy with weapon")
    public void testDestruction() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame(DIR_NAME + "d_zombieSpawnerTest_generalTest", "c_movementTest_testMovementDown");
        assertTrue(TestUtils.getEntities(res, "player").size() == 1);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 1);
        
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        assertTrue(TestUtils.getEntities(res, "sword").size() == 0);

        assertDoesNotThrow(() -> dmc.interact("zombie_toast_spawner"));
        
        res = dmc.tick(Direction.UP);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 0);
    }

    @Test
    @DisplayName("Test player destroy with bow")
    public void testDestruction_withBow() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame(DIR_NAME + "d_zombieSpawnerTest_generalTest", "c_movementTest_testMovementDown");
        assertTrue(TestUtils.getEntities(res, "player").size() == 1);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 1);
        
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        assertTrue(TestUtils.getEntities(res, "arrow").size() == 0);
        assertTrue(TestUtils.getEntities(res, "wood").size() == 0);
        assertDoesNotThrow(() -> dmc.build("bow"));

        assertDoesNotThrow(() -> dmc.interact("zombie_toast_spawner"));
        
        res = dmc.tick(Direction.UP);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 0);
    }

    @Test
    @DisplayName("Test player destry with bow and sword")
    public void testDestruction_bowAndSword() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame(DIR_NAME + "d_zombieSpawnerTest_generalTest", "c_movementTest_testMovementDown");
        assertTrue(TestUtils.getEntities(res, "player").size() == 1);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 1);
        
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        assertTrue(TestUtils.getEntities(res, "bow").size() == 0);
        assertTrue(TestUtils.getEntities(res, "arrow").size() == 0);
        assertTrue(TestUtils.getEntities(res, "wood").size() == 0);
        assertDoesNotThrow(() -> dmc.build("bow"));

        assertDoesNotThrow(() -> dmc.interact("zombie_toast_spawner"));
        
        res = dmc.tick(Direction.UP);
        assertTrue(TestUtils.getEntities(res, "zombie_toast_spawner").size() == 0);
    }
}
