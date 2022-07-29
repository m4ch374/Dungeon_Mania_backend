package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AssassinBehaviourTest {
    private static final String DIR_PATH = "d_AssassinTests/";

    @Test
    @DisplayName("Test Assassin move towards player basic")
    public void testMovement_basic() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame(DIR_PATH + "d_assassinTest_basicMovement", "c_msic_zeroDamage");

        Position mercPos = TestUtils.getEntityById(res, "assassin").getPosition();
        assertEquals(new Position(1, 5), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "assassin").getPosition();
        assertEquals(new Position(1, 4), mercPos);

        res = dmc.tick(Direction.UP);

        mercPos = TestUtils.getEntityById(res, "assassin").getPosition();
        assertEquals(new Position(1, 3), mercPos);
    }
}
