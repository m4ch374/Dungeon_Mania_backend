package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercenaryMovementTest {
    private static final String DIR_NAME = "d_MercTests/";

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
        
        res = dmc.tick(Direction.UP);

        Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(4, 1), mercPos);

        res = dmc.tick(Direction.UP);

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
    @DisplayName("Test zigzag movement for merc where it prioritize moving up on the first step")
    public void testZigzagMovement_UpPriority() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_moveUpZigzag", "c_msic_zeroDamage");
        
        res = dmc.tick(Direction.UP);

        Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(3, 3), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 3), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 2), mercPos);
    }

    @Test
    @DisplayName("Test zigzag movement for merc where it prioritize moving right on the first step")
    public void testZigzagMovement_RightPriority() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_moveRightZigzag", "c_msic_zeroDamage");
        
        res = dmc.tick(Direction.UP);

        Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 4), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(1, 3), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
        assertEquals(new Position(2, 3), mercPos);
    }

    @Test
    @DisplayName("Test merc stays in the same position if player and merc overlaps")
    public void testOverlaps_staysInSamePos() {
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

        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            
            Position playerPos = TestUtils.getEntityById(res, "player").getPosition();
            mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
            assertTrue(playerPos.equals(mercPos));
        }
    }

    @Test
    @DisplayName("Test merc blocks by wall")
    public void testMercBlocksByWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mercTest_blockedByWall", "c_msic_zeroDamage");

        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.UP);
            
            Position mercPos = TestUtils.getEntityById(res, "mercenary").getPosition();
            System.out.println(mercPos);
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
            System.out.println(mercPos);
            assertEquals(new Position(1, 5), mercPos);
        }
    }
}
