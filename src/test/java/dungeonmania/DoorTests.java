package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DoorTests {
    private static final String DIR_NAME = "d_DoorTests/";
    // Add helper fncs here!

    @Test
    @DisplayName("Door 1: Test a player collects key and moves thru door")
    public void testDoorGetsUnlockedBasic() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame("d_DoorsKeysTest_useKeyWalkThroughOpenDoor", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get door info
        EntityResponse doorInfo = TestUtils.getEntityById(dungeonRes, "door");
        // assert its existence
        assertEquals(doorInfo.getPosition(), new Position(3, 1));

        // Player moves east collects key
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Player moves east thru door via the key
        dungeonRes = dmc.tick(Direction.RIGHT);
        // assert player overlaps with door
        player = getPlayer(dungeonRes).get();
        doorInfo = TestUtils.getEntityById(dungeonRes, "door");
        assertEquals(player.getPosition(), doorInfo.getPosition());
    }

    @Test
    @DisplayName("Door 2: Test a player blocked by unopened Door")
    public void testDoorBlocksPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_doorTest_blocksPlayer", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get door info
        EntityResponse doorInfo = TestUtils.getEntityById(dungeonRes, "door");
        // assert its existence
        assertEquals(doorInfo.getPosition(), new Position(2, 1));

        // Player tries to move east into door, but is blocked
        dungeonRes = dmc.tick(Direction.RIGHT);
        // assert player did not move
        player = getPlayer(dungeonRes).get();
        doorInfo = TestUtils.getEntityById(dungeonRes, "door");
        assertNotEquals(player.getPosition(), doorInfo.getPosition());
    }

    @Test
    @DisplayName("Door 3: Test a player has wrong Key and is blocked by unopened Door")
    public void testDoorBlocksPlayerWithWrongKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_doorTest_blocksPlayerWrongKey", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get door1 (paired with Key2) info
        EntityResponse doorInfo = TestUtils.getEntityById(dungeonRes, "door");
        // assert its existence
        assertEquals(doorInfo.getPosition(), new Position(3, 1));

        // Player moves east and collects Key1, for Door2
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Player tries to move east into Door1 with Key1 and without Key2, but is blocked
        dungeonRes = dmc.tick(Direction.RIGHT);
        // assert player did not move
        player = getPlayer(dungeonRes).get();
        doorInfo = TestUtils.getEntityById(dungeonRes, "door");
        assertNotEquals(player.getPosition(), doorInfo.getPosition());
    }

    @Test
    @DisplayName("Door 4: Test Merc is blocked by unopened Door")
    public void testDoorBlocksMerc() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_doorTest_blocksMerc", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // Get door info
        EntityResponse doorInfo = TestUtils.getEntityById(dungeonRes, "door");
        // assert its existence
        assertEquals(doorInfo.getPosition(), new Position(3, 1));

        // Player moves west and Merc follows, but is blocked by unopened door west of Merc
        dungeonRes = dmc.tick(Direction.LEFT);
        // assert Merc did not overlap Door
        EntityResponse mercenary = TestUtils.getEntityById(dungeonRes, "mercenary");
        doorInfo = TestUtils.getEntityById(dungeonRes, "door");
        assertNotEquals(mercenary.getPosition(), doorInfo.getPosition());
    }

}
