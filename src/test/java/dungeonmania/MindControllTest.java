package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MindControllTest {
    private static final String DIR_NAME = "d_MindControllTests/";
    private static final String C_DIR_NAME = "c_mindControllTests/";

    @Test
    @DisplayName("Test player cannot mind control")
    public void testMindControll_noSceptre() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(DIR_NAME + "d_mindControllTest_Merc", C_DIR_NAME + "c_mindControllTests_longControlDuration");

        assertThrows(InvalidActionException.class, () -> dmc.interact("mercenary"));
    }

    @Test
    @DisplayName("Test player able to control merc")
    public void testMindControl_controllsMerc() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mindControllTest_Merc", C_DIR_NAME + "c_mindControllTests_longControlDuration");
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.build("sceptre"));

        assertDoesNotThrow(() -> dmc.interact("mercenary"));

        // merc should teleport
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getEntityById(res, "mercenary").getPosition());
    }

    @Test
    @DisplayName("Test player able to control assassin")
    public void testMindControl_controllAssassin() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mindControllTest_Assassin", C_DIR_NAME + "c_mindControllTests_longControlDuration");
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "assassin").getPosition());

        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.build("sceptre"));

        assertDoesNotThrow(() -> dmc.interact("assassin"));

        // assassin should teleport
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getEntityById(res, "assassin").getPosition());
    }

    @Test
    @DisplayName("Test merc turns hostile after getting controlled")
    public void testMindControl_hostileAfterExpiery_Merc() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mindControllTest_Merc", C_DIR_NAME + "c_mindControllTest_playerOP");
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.build("sceptre"));

        assertDoesNotThrow(() -> dmc.interact("mercenary"));

        // merc should teleport
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getEntityById(res, "mercenary").getPosition());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
    }

    @Test
    @DisplayName("Test assassin turns hostile after getting controlled")
    public void testMindControl_hostileAfterExpiery_Assassin() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_mindControllTest_Assassin", C_DIR_NAME + "c_mindControllTest_playerOP");
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "assassin").getPosition());

        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.build("sceptre"));

        assertDoesNotThrow(() -> dmc.interact("assassin"));

        // merc should teleport
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getEntityById(res, "assassin").getPosition());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "assassin").size());
    }
}
