package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TrackerTests {
    private static final String D_DIR = "d_TrackerTests/";
    private static final String C_DIR = "c_trackerTests/";

    @Test
    @DisplayName("Test parsing works")
    public void testParsingWorks_Simple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTests_treasure", C_DIR + "c_trackerTest_treasureTest");

        assertTrue(res.getGoals().contains(":treasure"));
    }

    @Test
    @DisplayName("Test simple treasure goal")
    public void testTreasure_Simple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTests_treasure", C_DIR + "c_trackerTest_treasureTest");

        assertTrue(res.getGoals().contains(":treasure"));

        res = dmc.tick(Direction.DOWN);
        assertTrue(res.getGoals().contains(":treasure"));

        res = dmc.tick(Direction.DOWN);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test simple enemy goal")
    public void testEnemy_Simple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTests_enemy", C_DIR + "c_trackerTest_enemyTest_playerOP");

        assertTrue(res.getGoals().contains(":enemies"));

        res = dmc.tick(Direction.DOWN);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test simple boulder goal")
    public void testBoulder_Simple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTests_boulderFufilled", C_DIR + "c_trackerTest_enemyTest_playerOP");

        assertTrue(res.getGoals().contains(":boulders"));

        res = dmc.tick(Direction.DOWN);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test unable to finish boulder goal")
    public void testBoulder_Unfufilled() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTests_boulderUnfufilled", C_DIR + "c_trackerTest_enemyTest_playerOP");

        assertTrue(res.getGoals().contains(":boulders"));

        res = dmc.tick(Direction.DOWN);
        assertTrue(res.getGoals().contains(":boulders"));
    }

    @Test
    @DisplayName("Test simple exit goal")
    public void testExit_Simple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTest_exit", C_DIR + "c_trackerTest_enemyTest_playerOP");

        assertTrue(res.getGoals().contains(":exit"));

        res = dmc.tick(Direction.DOWN);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test simple enemy with spawner")
    public void testEnemies_SimpleSpawner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTest_spawner", C_DIR + "c_trackerTest_enemyTest_playerOP_zombieSpawner");

        assertTrue(res.getGoals().contains(":enemies"));

        res = dmc.tick(Direction.UP);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test composite treasure goal - treasure or enemy")
    public void testComposite_treasureOr() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTest_treasureOr", C_DIR + "c_trackerTest_treasureTest");

        assertTrue(res.getGoals().contains(":treasure"));
        assertTrue(res.getGoals().contains(":enemies"));

        res = dmc.tick(Direction.DOWN);
        assertTrue(res.getGoals().contains(":treasure"));
        assertTrue(res.getGoals().contains(":enemies"));

        res = dmc.tick(Direction.DOWN);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test composite treasure goal - 4 disjoint goals")
    public void testComposite_4Disjoints() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTest_4Disjoints", C_DIR + "c_trackerTest_treasureTest");

        assertTrue(res.getGoals().contains(":treasure"));
        assertTrue(res.getGoals().contains(":enemies"));
        assertTrue(res.getGoals().contains(":boulders"));
        assertTrue(res.getGoals().contains(":exit"));

        res = dmc.tick(Direction.DOWN);
        assertTrue(res.getGoals().contains(":treasure"));
        assertTrue(res.getGoals().contains(":enemies"));
        assertTrue(res.getGoals().contains(":boulders"));
        assertTrue(res.getGoals().contains(":exit"));

        res = dmc.tick(Direction.DOWN);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test composite goal - mixed conjunction & disjunction")
    public void testComposite_mixedSimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTest_mixed", C_DIR + "c_trackerTest_enemyTest_playerOP");

        assertTrue(res.getGoals().contains(":treasure"));
        assertTrue(res.getGoals().contains(":enemies"));
        assertTrue(res.getGoals().contains(":boulders"));
        assertTrue(res.getGoals().contains(":exit"));

        res = dmc.tick(Direction.RIGHT);
        assertTrue(res.getGoals().contains(":treasure"));
        assertFalse(res.getGoals().contains(":enemies"));
        assertTrue(res.getGoals().contains(":boulders"));
        assertTrue(res.getGoals().contains(":exit"));

        dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        assertFalse(res.getGoals().contains(":treasure"));
        assertFalse(res.getGoals().contains(":enemies"));
        assertFalse(res.getGoals().contains(":boulders"));
        assertTrue(res.getGoals().contains(":exit"));

        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals("", res.getGoals());
    }

    @Test
    @DisplayName("Test composite goal - exit last")
    public void testComposite_mixed_exitLast() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(D_DIR + "d_trackerTest_mixed", C_DIR + "c_trackerTest_enemyTest_playerOP");

        assertTrue(res.getGoals().contains(":treasure"));
        assertTrue(res.getGoals().contains(":enemies"));
        assertTrue(res.getGoals().contains(":boulders"));
        assertTrue(res.getGoals().contains(":exit"));

        res = dmc.tick(Direction.RIGHT);
        assertTrue(res.getGoals().contains(":treasure"));
        assertFalse(res.getGoals().contains(":enemies"));
        assertTrue(res.getGoals().contains(":boulders"));
        assertTrue(res.getGoals().contains(":exit"));

        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        assertFalse(res.getGoals().contains(":treasure"));
        assertFalse(res.getGoals().contains(":enemies"));
        assertFalse(res.getGoals().contains(":boulders"));
        assertTrue(res.getGoals().contains(":exit"));

        dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals("", res.getGoals());
    }
}
