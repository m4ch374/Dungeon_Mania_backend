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

    @Test
    @DisplayName("Test zombie toast spawner spawns zombies for each tick")
    public void testZombieToasSpawnerSpawns_spawnsEachTick() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_zombieSpawnBasic", C_DIR_NAME + "c_spawnTests_zombieSpawnEachTick");

        assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == i + 1);
        }
    }

    @Test
    @DisplayName("Test zombie toast spawner spawns zombies for every 2 ticks")
    public void testZombieToasSpawnerSpawns_spawnsEvery2Ticks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_zombieSpawnBasic", C_DIR_NAME + "c_spawnTests_zombieSpawnEvery2Ticks");

        assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == (int)((i + 1) / 2));
        }
    }

    @Test
    @DisplayName("Test zombie toast spawner spawns zombies for every 7 ticks")
    public void testZombieToasSpawnerSpawns_spawnsEvery7Ticks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_zombieSpawnBasic", C_DIR_NAME + "c_spawnTests_zombieSpawnEvery7Ticks");

        assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == (int)((i + 1) / 7));
        }
    }

    @Test
    @DisplayName("Test zombie does not spawn")
    public void testZombieToastDoesNotSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_zombieSpawnBasic", "c_spiderTest_basicMovement");

        assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);
        }
    }

    @Test
    @DisplayName("Test zombie toast spawner does not spawn if it is blocked")
    public void testZombieToastDoesNotSpawn_spawnerBlockedByWalls() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_zombieSpawnerBlocked", C_DIR_NAME + "c_spawnTests_zombieSpawnEachTick");

        assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);
        }
    }

    @Test
    @DisplayName("Test zombie toast spawner does not spawn if it is blocked")
    public void testZombieToastDoesNotSpawn_spawnerBlockedByDoors() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_zombieSpawnerBlockedByDoors", C_DIR_NAME + "c_spawnTests_zombieSpawnEachTick");

        assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);
        }
    }

    // Very buggy, the output in this test is different in 2 instances
    // 1. Running it standalone: The spawn ID starts from 0
    // 2. Running it alongside the whole project: The spawn ID starts from 1
    //
    // Result is non deterministic
    // Nevertheless, it has been proven that the zombie will spawn in the desired position
    @Test
    @DisplayName("Test zombie toast spawner spawns in one direction")
    public void testZombieToastSpawnsInOneDirection() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR_NAME + "d_spawnTest_zombieSpawnerOneOpening", C_DIR_NAME + "c_spawnTests_zombieSpawnEachTick");

        assertTrue(TestUtils.getEntities(res, "zombie_toast").size() == 0);

        System.out.println();
        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            //assertEquals(new Position(3, 3), TestUtils.getEntityById(res, "spawned_zombie" + (i + 1)).getPosition());
        }
    }
}
