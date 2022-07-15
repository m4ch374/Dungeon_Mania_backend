package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class SpawningTests {
    private static final String D_DIR_NAME = "d_SpawnTests/";
    private static final String C_DIR_NAME = "c_spawnTests/";

    @Test
    @DisplayName("Test spider spawns every tick")
    public void testSpiderSpawn_everyTick() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_spiderSpawn", C_DIR_NAME + "c_spawnTests_spiderSpawnEachTick");

        assertTrue(TestUtils.getEntities(res, "spider").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "spider").size() == i + 1);
        }
    }

    @Test
    @DisplayName("Test spider spawns every 2 ticks")
    public void testSpiderSpawn_every2Ticks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_spiderSpawn", C_DIR_NAME + "c_spawnTests_spiderSpawnEvery2Ticks");

        assertTrue(TestUtils.getEntities(res, "spider").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "spider").size() == (int)((i + 1) / 2));
        }
    }

    @Test
    @DisplayName("Test spider spawns every 7 ticks")
    public void testSpiderSpawn_every7Ticks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_spiderSpawn", C_DIR_NAME + "c_spawnTests_spiderSpawnEvery7Ticks");

        assertTrue(TestUtils.getEntities(res, "spider").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "spider").size() == (int)((i + 1) / 7));
        }
    }

    @Test
    @DisplayName("Test spider does not spawn")
    public void testSpiderSpawn_doesNotSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_spiderSpawn", "c_spiderTest_basicMovement");

        assertTrue(TestUtils.getEntities(res, "spider").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "spider").size() == 0);
        }
    }
}
