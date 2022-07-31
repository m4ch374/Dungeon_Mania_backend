package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dungeonmania.TestUtils.getPlayer;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TimePortalTest {
    private static final String DIR_NAME = "d_TimePortalTest/";

    @Test
    @DisplayName("Time Portal 1: Very Simple, only two other entities")
    public void testTimePortalVerySimple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonResInitial = dmc.newGame(DIR_NAME + "d_TimePortalTest_VerySimple", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        EntityResponse playerInit = getPlayer(dungeonResInitial).get();
        // Assert existence of the time travelling portal and boulder
        EntityResponse boulderInit = TestUtils.getEntityById(dungeonResInitial, "boulder");
        EntityResponse timePortal = TestUtils.getEntityById(dungeonResInitial, "time_travelling_portal");
        
        assertEquals(new Position(3, 2), timePortal.getPosition());
        assertEquals(new Position(2, 1), boulderInit.getPosition());

        // Push boulder to the right on the current tick
        DungeonResponse dungeonRes = dmc.tick(Direction.RIGHT);
        // Then enter the portal
        dmc.tick(Direction.DOWN);
        dungeonRes = dmc.tick(Direction.RIGHT);

        // Get current tick's entities info
        EntityResponse player = getPlayer(dungeonRes).get();
        EntityResponse boulder = TestUtils.getEntityById(dungeonResInitial, "boulder");
        // assert currBoulder is the same as initBoulder
        assertNotEquals(boulder.getPosition(), boulderInit.getPosition());

        // assertFalse(zombie.getPosition().equals(movedZombie.getPosition()));
    }
}
