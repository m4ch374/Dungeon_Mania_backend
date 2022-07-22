package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
