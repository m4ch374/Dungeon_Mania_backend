package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTileTest {
    private static final String DIR_NAME = "d_SwampTests/";

    @Test
    @DisplayName("Test zombies trapped by swamp tiles")
    public void testZombieTrapped() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_swampTest_trapsZombie", "c_msic_zeroDamage");
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "zombie_toast").getPosition());

        res = dmc.tick(Direction.LEFT);
        Position stuckPos = TestUtils.getEntityById(res, "zombie_toast").getPosition();
        assertFalse(Objects.equals(new Position(4, 1), stuckPos));

        for (int i = 0; i < 20; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(stuckPos, TestUtils.getEntityById(res, "zombie_toast").getPosition());
        }

        res = dmc.tick(Direction.LEFT);
        assertFalse(stuckPos.equals(TestUtils.getEntityById(res, "zombie_toast").getPosition()));
    }

    @Test
    @DisplayName("Test hydra trapped by swamp tiles")
    public void testHydraTrapped() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_swampTest_trapsHydra", "c_msic_zeroDamage");
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "hydra").getPosition());

        res = dmc.tick(Direction.LEFT);
        Position stuckPos = TestUtils.getEntityById(res, "hydra").getPosition();
        assertFalse(Objects.equals(new Position(4, 1), stuckPos));

        for (int i = 0; i < 20; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(stuckPos, TestUtils.getEntityById(res, "hydra").getPosition());
        }

        res = dmc.tick(Direction.LEFT);
        assertFalse(stuckPos.equals(TestUtils.getEntityById(res, "hydra").getPosition()));
    }

    @Test
    @DisplayName("Test spider trapped by swamp tiles")
    public void testSpiderTrapped() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_swampTest_trapsSpider", "c_msic_zeroDamage");
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "spider").getPosition());

        res = dmc.tick(Direction.LEFT);
        Position stuckPos = new Position(4, 0);
        assertEquals(stuckPos, TestUtils.getEntityById(res, "spider").getPosition());

        for (int i = 0; i < 20; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(stuckPos, TestUtils.getEntityById(res, "spider").getPosition());
        }

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(5, 0), TestUtils.getEntityById(res, "spider").getPosition());
    }

    @Test
    @DisplayName("Test merc trapped by swamp tiles")
    public void testMercTrapped() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_swampTest_trapsMerc", "c_msic_zeroDamage");
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.LEFT);
        Position stuckPos = new Position(3, 1);
        assertEquals(stuckPos, TestUtils.getEntityById(res, "mercenary").getPosition());

        for (int i = 0; i < 20; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(stuckPos, TestUtils.getEntityById(res, "mercenary").getPosition());
        }

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test assassin trapped by swamp tiles")
    public void testAssassinTrapped() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_swampTest_trapsAssassin", "c_msic_zeroDamage");
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "assassin").getPosition());

        res = dmc.tick(Direction.LEFT);
        Position stuckPos = new Position(3, 1);
        assertEquals(stuckPos, TestUtils.getEntityById(res, "assassin").getPosition());

        for (int i = 0; i < 20; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(stuckPos, TestUtils.getEntityById(res, "assassin").getPosition());
        }

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getEntityById(res, "assassin").getPosition());
    }
}
