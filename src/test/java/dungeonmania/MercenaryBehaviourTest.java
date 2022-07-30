package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercenaryBehaviourTest {
    private static final String DIR_NAME = "d_MercTests/";
    private static final String C_DIR_NAME = "c_bribeTests/";

    @Test
    @DisplayName("Test basic movement for the merc moving up")
    public void testBasicMovement_UP() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_moveUpSimple", "c_msic_zeroDamage");
        
        Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 5), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 4), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 3), mercPos);
    }

    @Test
    @DisplayName("Test basic movement for merc going left")
    public void testBasicMovement_LEFT() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_moveLeftSimple", "c_msic_zeroDamage");
        
        res = dmc.tick(Direction.LEFT);

        Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(4, 1), mercPos);

        res = dmc.tick(Direction.LEFT);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(3, 1), mercPos);
    }

    @Test
    @DisplayName("Test basic movement for merc going right")
    public void testBasicMovement_RIGHT() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_moveRightSimple", "c_msic_zeroDamage");
        
        res = dmc.tick(Direction.RIGHT);

        Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 1), mercPos);

        res = dmc.tick(Direction.RIGHT);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(3, 1), mercPos);
    }

    @Test
    @DisplayName("Test basic movement for merc going down")
    public void testBasicMovement_DOWN() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_moveDownSimple", "c_msic_zeroDamage");
        
        res = dmc.tick(Direction.DOWN);

        Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 2), mercPos);

        res = dmc.tick(Direction.DOWN);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 3), mercPos);
    }

    @Test
    @DisplayName("Test diagonal movement for merc where the player is to the upper left of merc")
    public void testDiagonalMovement_UpperLeft() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_moveUpperLeft", "c_msic_zeroDamage");
        
        res = dmc.tick(Direction.UP);

        Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 4), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 3), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 2), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 1), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 1), mercPos);
    }

    @Test
    @DisplayName("Test diagonal movement where the player is to the upper right of the merc")
    public void testDiagonalMovement_UpperRight() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_moveUpperRight", "c_msic_zeroDamage");
        
        res = dmc.tick(Direction.UP);

        Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 4), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 4), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(3, 4), mercPos);
        
        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(4, 4), mercPos);
        
        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(4, 3), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(4, 2), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(4, 2), mercPos);
    }

    @Test
    @DisplayName("Test merc blocks by wall")
    public void testMercBlocksByWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_blockedByWall", "c_msic_zeroDamage");

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            
            Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
            assertEquals(new Position(1, 5), mercPos);
        }
    }

    @Test
    @DisplayName("Test merc blocks by door")
    public void testMercBlocksByDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_blockByDoor", "c_msic_zeroDamage");

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            
            Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
            assertEquals(new Position(1, 5), mercPos);
        }
    }

    @Test
    @DisplayName("Test not in radius")
    public void testMercBribe_notInRadius() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(DIR_NAME + "d_mercTest_bribeTest", C_DIR_NAME + "c_bribeTests_radius1");

        assertThrows(InvalidActionException.class, () -> dmc.interact("mercenary"));

        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        assertThrows(InvalidActionException.class, () -> dmc.interact("mercenary"));
    }

    @Test
    @DisplayName("Test not enough treasure")
    public void testMercBribe_notEnoughTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(DIR_NAME + "d_mercTest_bribeTest", C_DIR_NAME + "c_bribeTests_radius1");

        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);

        assertThrows(InvalidActionException.class, () -> dmc.interact("mercenary"));
    }

    @Test
    @DisplayName("Test player able to bribe within radius of 1")
    public void testMercBribe_withinRadius1() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_bribeTest", C_DIR_NAME + "c_bribeTests_radius1");

        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        assertEquals(0, TestUtils.getEntities(res, "treasure").size());

        assertDoesNotThrow(() -> dmc.interact("mercenary"));
    }

    @Test
    @DisplayName("Test player able to bribe within radius of 2 diagonally")
    public void testMercBribe_withinRadius2_diagonally() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_bribeTest", C_DIR_NAME + "c_bribeTests_radius2");

        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, TestUtils.getEntities(res, "treasure").size());

        assertDoesNotThrow(() -> dmc.interact("mercenary"));
    }

    @Test
    @DisplayName("Test player able to bribe within radius of 2 cardinally")
    public void testMercBribe_withinRadius2_cardinally() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_bribeTest", C_DIR_NAME + "c_bribeTests_radius2");

        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, TestUtils.getEntities(res, "treasure").size());

        assertDoesNotThrow(() -> dmc.interact("mercenary"));
    }

    @Test
    @DisplayName("Test bribed merc's movement")
    public void testMercBribe_allyMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_bribeTest", C_DIR_NAME + "c_bribeTests_radius2");

        res = dmc.tick(Direction.RIGHT);

        assertEquals(0, TestUtils.getEntities(res, "treasure").size());

        assertDoesNotThrow(() -> dmc.interact("mercenary"));

        // remains in the same position
        assertEquals(new Position(2, 3), TestUtils.getEntityById(res, "mercenary").getPosition());

        // Teleports in the next tick
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        // Occupies the previous position from then on
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "player").getPosition());
        assertEquals(new Position(2, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        // make sure hostile merc does not exist in the map
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());

        dmc.tick(Direction.DOWN);

        // Ally does not overlap with player
        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.DOWN);

            Position playerPos = TestUtils.getEntityById(res, "player").getPosition();
            Position allyPos = TestUtils.getEntityById(res, "mercenary").getPosition();

            assertEquals(new Position(1, 2), playerPos);
            assertEquals(new Position(1, 1), allyPos);
        }
    }

    @Test
    @DisplayName("Test merc flees from player upwards")
    public void testMercFlee_UP() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_fleeUpSimple", "c_msic_longPotionDuration_noDamage");

        res = dmc.tick(Direction.DOWN);
        Position originalMercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 4), TestUtils.getEntityById(res, "player").getPosition());
        assertEquals(new Position(1, 0), originalMercPos);
        assertTrue(TestUtils.getEntities(res, "invincibility_potion").size() == 0);

        assertDoesNotThrow(() -> dmc.tick("invincibility_potion"));

        for (int i = 1; i <= 98; i++) {
            res = dmc.tick(Direction.DOWN);
            assertEquals(originalMercPos.translateBy(new Position(0, -i - 1)), TestUtils.getEntityById(res, "mercenary").getPosition());
        }

        originalMercPos = TestUtils.getEntityById(res, "mercenary").getPosition();

        // goes back to normal move strat
        for (int i = 0; i < 99; i++) {
            res = dmc.tick(Direction.DOWN);
            assertEquals(originalMercPos.translateBy(new Position(0, i + 1)), TestUtils.getEntityById(res, "mercenary").getPosition());
        }
    }

    @Test
    @DisplayName("Test merc flees from player downwards")
    public void testMercFlee_DOWN() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_fleeDownSimple", "c_msic_longPotionDuration_noDamage");

        res = dmc.tick(Direction.UP);
        Position originalMercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "player").getPosition());
        assertEquals(new Position(1, 5), originalMercPos);
        assertTrue(TestUtils.getEntities(res, "invincibility_potion").size() == 0);

        assertDoesNotThrow(() -> dmc.tick("invincibility_potion"));

        for (int i = 1; i <= 98; i++) {
            res = dmc.tick(Direction.UP);
            assertEquals(originalMercPos.translateBy(new Position(0, i + 1)), TestUtils.getEntityById(res, "mercenary").getPosition());
        }

        originalMercPos = TestUtils.getEntityById(res, "mercenary").getPosition();

        // goes back to normal move strat
        for (int i = 0; i < 99; i++) {
            res = dmc.tick(Direction.UP);
            assertEquals(originalMercPos.translateBy(new Position(0, -i - 1)), TestUtils.getEntityById(res, "mercenary").getPosition());
        }
    }

    @Test
    @DisplayName("Test merc flees from player rightwards")
    public void testMercFlee_RIGHT() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_fleeRightSimple", "c_msic_longPotionDuration_noDamage");

        res = dmc.tick(Direction.LEFT);
        Position originalMercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "player").getPosition());
        assertEquals(new Position(5, 1), originalMercPos);
        assertTrue(TestUtils.getEntities(res, "invincibility_potion").size() == 0);

        assertDoesNotThrow(() -> dmc.tick("invincibility_potion"));

        for (int i = 1; i <= 98; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(originalMercPos.translateBy(new Position(i + 1,0)), TestUtils.getEntityById(res, "mercenary").getPosition());
        }

        originalMercPos = TestUtils.getEntityById(res, "mercenary").getPosition();

        // goes back to normal move strat
        for (int i = 0; i < 98; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(originalMercPos.translateBy(new Position(-i - 1, 0)), TestUtils.getEntityById(res, "mercenary").getPosition());
        }
    }

    @Test
    @DisplayName("Test merc flees from player leftwards")
    public void testMercFlee_LEFT() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_fleeLeftSimple", "c_msic_longPotionDuration_noDamage");

        res = dmc.tick(Direction.RIGHT);
        Position originalMercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "player").getPosition());
        assertEquals(new Position(0, 1), originalMercPos);
        assertTrue(TestUtils.getEntities(res, "invincibility_potion").size() == 0);

        assertDoesNotThrow(() -> dmc.tick("invincibility_potion"));

        for (int i = 1; i <= 98; i++) {
            res = dmc.tick(Direction.RIGHT);
            assertEquals(originalMercPos.translateBy(new Position(-i - 1,0)), TestUtils.getEntityById(res, "mercenary").getPosition());
        }

        originalMercPos = TestUtils.getEntityById(res, "mercenary").getPosition();

        // goes back to normal move strat
        for (int i = 0; i < 99; i++) {
            res = dmc.tick(Direction.RIGHT);
            assertEquals(originalMercPos.translateBy(new Position(i + 1, 0)), TestUtils.getEntityById(res, "mercenary").getPosition());
        }
    }

    @Test
    @DisplayName("Test merc move around 1 block of wall towards the top")
    public void testMercMoveAround_1Wall_UP() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_aroundWallsSimple_Up", "c_msic_zeroDamage");

        assertEquals(new Position(2, 4), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(3, 4), TestUtils.getEntityById(res, "mercenary").getPosition());

        for (int i = 0; i < 4; i++) {
            res = dmc.tick(Direction.UP);
        }

        assertEquals(new Position(3, 1), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc move around 1 block of wall towards the bottom")
    public void testMercMoveAround_1Wall_DOWN() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_aroundWallsSimple_Down", "c_msic_zeroDamage");

        assertEquals(new Position(2, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        for (int i = 0; i < 4; i++) {
            res = dmc.tick(Direction.UP);
        }
        
        assertEquals(new Position(1, 5), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc move around 1 block of wall towards the left")
    public void testMercMoveAround_1Wall_LEFT() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_aroundWallsSimple_Left", "c_msic_zeroDamage");

        assertEquals(new Position(4, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        for (int i = 0; i < 4; i++) {
            res = dmc.tick(Direction.LEFT);
        }

        assertEquals(new Position(2, 2), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc move around 1 block of wall towards the right")
    public void testMercMoveAround_1Wall_RIGHT() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_aroundWallsSimple_Right", "c_msic_zeroDamage");

        assertEquals(new Position(0, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(0, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(new Position(3, 1), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc move around walls towards the shorter path")
    public void testMercMoveAroundAdvanced_shorterPath() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_shorterPath", "c_msic_zeroDamage");

        assertEquals(new Position(0, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(0, 3), TestUtils.getEntityById(res, "mercenary").getPosition());

        for (int i = 0; i < 6; i++) {
            res = dmc.tick(Direction.RIGHT);
        }

        assertEquals(new Position(2, 2), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc move around walls with only one opening")
    public void testMercMoveAroundAdvanced_oneOpening() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_oneWallEnclosed", "c_msic_zeroDamage");

        assertEquals(new Position(0, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(0, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        for (int i = 0; i < 8; i++) {
            res = dmc.tick(Direction.RIGHT);
        }

        assertEquals(new Position(3, 1), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc move around enclosed wall")
    public void testMercMoveAroundAdvanced_enclosedWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_wallsEnclosed", "c_msic_zeroDamage");

        assertEquals(new Position(0, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(-1, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        for (int i = 0; i < 8; i++) {
            res = dmc.tick(Direction.RIGHT);
        }

        assertEquals(new Position(2, 2), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc use portal for shortest path")
    public void testActivelyUsePortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_activelyUsePortal", "c_msic_zeroDamage");

        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(4, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(4, 3), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(4, 3), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc not using portal for shortest path if the destination is blocked off")
    public void testActivelyUsePortal_destinationBlocked() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_portalDestinationBlocked", "c_msic_zeroDamage");

        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(2, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(2, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(3, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(4, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(4, 3), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.UP);
        assertEquals(new Position(4, 3), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc use portal smartly")
    public void testActivelyUsePortal_smart() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_usePortalSmartly", "c_msic_zeroDamage");

        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(0, 2), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(5, 0), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(6, 0), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(7, 0), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(7, 0), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test merc avoids swamp tile")
    public void testSwamp_avoidSwampTile() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_avoidSwampTile", "c_msic_zeroDamage");

        assertEquals(new Position(5, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(5, 2), TestUtils.getEntityById(res, "mercenary").getPosition());
    }
}
