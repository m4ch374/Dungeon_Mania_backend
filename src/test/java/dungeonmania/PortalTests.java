package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PortalTests {
    private static final String DIR_NAME = "d_PortalTests/";
    // Add helper fncs here!

    @Test
    @DisplayName("Portal 1: Test One Portal pair teleports Player to east of the other")
    // SYNOPSIS:
    // Player at (1,1), moves east into red Portal at (2,1), 
    // then gets teleported to (2,3), which is east of pair red Portal at (1,3)
    public void testPortalTeleportsPlayerBasic() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_portalTest_onePair", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get both portal info 
        EntityResponse portal = TestUtils.getEntityById(dungeonRes, "portal");
        EntityResponse portal1 = TestUtils.getEntityById(dungeonRes, "portal1");
        // assert their existence
        EntityResponse expectedPortal = new EntityResponse(portal.getId(), portal.getType(), new Position(2, 1), true);
        assertEquals(expectedPortal.getPosition(), portal.getPosition());
        EntityResponse expectedPortal1 = new EntityResponse(portal1.getId(), portal1.getType(), new Position(1, 3), true);        
        assertEquals(expectedPortal1.getPosition(), portal1.getPosition());

        // Player moves East into Portal, thus teleports east of Portal1 (2,3)
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Assert Portals are still in same location
        expectedPortal = TestUtils.getEntityById(dungeonRes, "portal");
        expectedPortal1 = TestUtils.getEntityById(dungeonRes, "portal1");
        assertEquals(expectedPortal.getPosition(), portal.getPosition());
        assertEquals(expectedPortal1.getPosition(), portal1.getPosition());
        // Now assert the player has teleported
        player = getPlayer(dungeonRes).get();
        assertEquals(new Position(2, 3), player.getPosition());
        
    }


    @Test
    @DisplayName("Portal 2: Test Two Portal pairs teleports Player in all directions of the each other")
    // SYNOPSIS:
    // Player at (1,1), moves east into red Portal at (2,1), 
    // then gets teleported to (2,3), which is east of pair red Portal at (1,3)
    // Player then moves down into blue Portal at (2,4)
    // then gets teleported to (3,3), which is south of pair blue portal at (2,3)
    public void testPortalTeleportsPlayerEastThenSouth() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_portalTest_twoPair", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get both portal info
        // // NOTE: skip testing for "portal" and "portal1", as that was done in Test 1
        EntityResponse portal2 = TestUtils.getEntityById(dungeonRes, "portal2");
        EntityResponse portal3 = TestUtils.getEntityById(dungeonRes, "portal3");
        // assert their existence
        EntityResponse expectedPortal2 = new EntityResponse(portal2.getId(), portal2.getType(), new Position(2, 4), true);
        assertEquals(expectedPortal2.getPosition(), portal2.getPosition());
        EntityResponse expectedPortal3 = new EntityResponse(portal3.getId(), portal3.getType(), new Position(3, 2), true);        
        assertEquals(expectedPortal3.getPosition(), portal3.getPosition());
        
        // Player moves East into Portal, thus teleports east of Portal1 (2,3)
        // ASSUMING Test 1 passes, we skip checks for this movement
        dungeonRes = dmc.tick(Direction.RIGHT);
        player = getPlayer(dungeonRes).get();
        assertEquals(new Position(2, 3), player.getPosition());
        // Player moves South into Portal2 (2,4), thus teleports south of Portal2 (3,2), so placed at (3,3)
        dungeonRes = dmc.tick(Direction.DOWN);
        // Assert Portals 2 & 3 are still in same location
        expectedPortal2 = TestUtils.getEntityById(dungeonRes, "portal2");
        expectedPortal3 = TestUtils.getEntityById(dungeonRes, "portal3");
        assertEquals(expectedPortal2.getPosition(), portal2.getPosition());
        assertEquals(expectedPortal3.getPosition(), portal3.getPosition());
        // Now assert the player has teleported
        player = getPlayer(dungeonRes).get();
        assertEquals(new Position(3, 3), player.getPosition());

    }


    @Test
    @DisplayName("Portal 3: Test Player does not teleport due to Wall east of Portal")
    // SYNOPSIS:
    // Player at (3,2) attempts to move east into Portal at (4,2), 
    // Then its Pair, Portal1 at (4,4) determines Player needs to be teleported east of it,
    // BUT Portal1 detects Wall at (5,4) and does not teleport player, so player simply overlaps the player
    public void testPortalNotTeleportsPlayerCauseWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_portalTest_twoPairBoulderWall", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get both portal info
        EntityResponse portal = TestUtils.getEntityById(dungeonRes, "portal");
        EntityResponse portal1 = TestUtils.getEntityById(dungeonRes, "portal1");
        // assert this test's portals of interest, of their existence (not portal2 and 3)
        assertEquals(new Position(4, 2), portal.getPosition());
        assertEquals(new Position(4, 4), portal1.getPosition());

        // Player attempts to moves East into Portal (4,2), but doesnt teleport to east of Portal1 (5,2), as there is a Wall there
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Assert player now overlaps portal
        player = getPlayer(dungeonRes).get();
        assertEquals(portal.getPosition(), player.getPosition());
    }
    

    @Test
    @DisplayName("Portal 4: Test Player teleports onto Boulder west of Portal")
    // SYNOPSIS:
    // Player at (3,2) moves west into Portal2 at (2,2), 
    // Then its Pair, Portal3 at (3,4) determines Player needs to be teleported west of it,
    // Then Portal3 doesnt detect Wall or another Portal at (2,4) and teleports player, who then interacts with the boulder
    public void testPortalTeleportsPlayerOntoBoulder() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_portalTest_twoPairBoulderWall", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get both portal info
        EntityResponse portal2 = TestUtils.getEntityById(dungeonRes, "portal2");
        EntityResponse portal3 = TestUtils.getEntityById(dungeonRes, "portal3");
        // assert this test's portals of interest, of their existence (not portal and portal1)
        assertEquals(new Position(2, 2), portal2.getPosition());
        assertEquals(new Position(3, 4), portal3.getPosition());

        // Player attempts to moves West into Portal2 (2,2), and teleports to west of Portal3 (3,4), into a boulder and the pushes the boulder
        // assert the boulder is there
        assertEquals(new Position(2, 4), TestUtils.getEntityById(dungeonRes, "boulder").getPosition());
        dungeonRes = dmc.tick(Direction.LEFT);
        
        // Assert boulder has been moved
        assertEquals(new Position(1, 4), TestUtils.getEntityById(dungeonRes, "boulder").getPosition());
        
        // Assert player has teleported
        player = getPlayer(dungeonRes).get();
        assertEquals(new Position(2, 4), player.getPosition());
    }


    @Test
    @DisplayName("Portal 5: Test Player teleports twice in one movement")
    // SYNOPSIS:
    // Player (1,1) moves east into Portal (2,1), which teleports him to its pair Portal1 (1,3),
    // which spits him out east of it (2,3), BUT there is another Portal2 (2,3) there, which then
    // teleports him to its pair Portal3 (3,2), which spits him out east of it (4,2).
    public void testPortalMultipleInstantTeleportaion() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_portalTest_twoPairMultipleTelep", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get all 4 portal info
        EntityResponse portal = TestUtils.getEntityById(dungeonRes, "portal");
        EntityResponse portal1 = TestUtils.getEntityById(dungeonRes, "portal1");
        EntityResponse portal2 = TestUtils.getEntityById(dungeonRes, "portal2");
        EntityResponse portal3 = TestUtils.getEntityById(dungeonRes, "portal3");
        // assert positions of all 4 portals
        assertEquals(new Position(2, 1), portal.getPosition());
        assertEquals(new Position(1, 3), portal1.getPosition());
        assertEquals(new Position(2, 3), portal2.getPosition());
        assertEquals(new Position(3, 2), portal3.getPosition());

        // Player moves east into Portal, ends up east of Portal3(3,2) into (4,2)
        dungeonRes = dmc.tick(Direction.RIGHT);
        
        // Assert player has teleported
        player = getPlayer(dungeonRes).get();
        assertEquals(new Position(4, 2), player.getPosition());
    }

    
    
    @Test
    @DisplayName("Portal 6: Test Merc does teleport")
    // SYNOPSIS:
    // Player (2,1) moves west away from Portal(3,1) and Merc(4,1) follows Player's direction, west, into Portal
    // and gets teleported to west (1,3) of Portal1(2,3). They're surrounded by walls at (1,2),(2,2),(3,2),(4,2),(5,2),(5,1)
    public void testPortalTeleportsMerc() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_portalTest_mercTeleported", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // Get all portal info
        EntityResponse portal = TestUtils.getEntityById(dungeonRes, "portal");
        EntityResponse portal1 = TestUtils.getEntityById(dungeonRes, "portal1");
        // assert positions of all 4 portals
        assertEquals(new Position(3, 1), portal.getPosition());
        assertEquals(new Position(2, 3), portal1.getPosition());
        // get Merc info
        EntityResponse mercenary = TestUtils.getEntityById(dungeonRes, "mercenary");


        // Player moves West, Merc follows onto Portal(3,1), and gets teleported to east of Portal1(2,3) 
        dungeonRes = dmc.tick(Direction.LEFT);
        // Assert Merc is east (1,3) of Portal1(2,3)
        mercenary = TestUtils.getEntityById(dungeonRes, "mercenary");
        assertEquals(new Position(1, 3), mercenary.getPosition());
    }
    
    @Test
    @DisplayName("Portal 7: Test Merc travels through portal end up overlapping boulder")
    public void testPortalTeleportsMerc_overlapsBoulder() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_portalTest_mercTeleported_overlapsBoulder", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // Get all portal info
        EntityResponse portal = TestUtils.getEntityById(dungeonRes, "portal");
        EntityResponse portal1 = TestUtils.getEntityById(dungeonRes, "portal1");
        // assert positions of all 4 portals
        assertEquals(new Position(3, 1), portal.getPosition());
        assertEquals(new Position(2, 3), portal1.getPosition());
        // get Merc info
        EntityResponse mercenary = TestUtils.getEntityById(dungeonRes, "mercenary");


        // Player moves West, Merc follows onto Portal(3,1), and gets teleported to east of Portal1(2,3) 
        dungeonRes = dmc.tick(Direction.LEFT);
        // Assert Merc is east (1,3) of Portal1(2,3)
        mercenary = TestUtils.getEntityById(dungeonRes, "mercenary");
        assertEquals(new Position(1, 3), mercenary.getPosition());
    }

    @Test
    @DisplayName("Portal 8: Test player travels through portal and pushed boulder")
    public void testPlayerTeleported_pushBoulder() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_portalTest_playerPushBoulder", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // Get all portal info
        EntityResponse portal = TestUtils.getEntityById(res, "portal");
        EntityResponse portal1 = TestUtils.getEntityById(res, "portal1");
        // assert positions of all 4 portals
        assertEquals(new Position(3, 1), portal.getPosition());
        assertEquals(new Position(2, 3), portal1.getPosition());
        assertEquals(new Position(3, 3), TestUtils.getEntityById(res, "boulder").getPosition());

        res = dmc.tick(Direction.RIGHT);

        assertEquals(new Position(3, 3), TestUtils.getEntityById(res, "player").getPosition());
        assertEquals(new Position(4, 3), TestUtils.getEntityById(res, "boulder").getPosition());
    }

    @Test
    @DisplayName("Portal 9: Test player cannot travel through portal due to boulder being stuck")
    public void testPlayerCannotTeleport_boulderStuck() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_portalTest_cannotTravel_boulderStuck", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // Get all portal info
        EntityResponse portal = TestUtils.getEntityById(res, "portal");
        EntityResponse portal1 = TestUtils.getEntityById(res, "portal1");
        // assert positions of all 4 portals
        assertEquals(new Position(3, 1), portal.getPosition());
        assertEquals(new Position(2, 3), portal1.getPosition());
        assertEquals(new Position(3, 3), TestUtils.getEntityById(res, "boulder").getPosition());

        res = dmc.tick(Direction.RIGHT);

        assertEquals(new Position(3, 1), TestUtils.getEntityById(res, "player").getPosition());
        assertEquals(new Position(3, 3), TestUtils.getEntityById(res, "boulder").getPosition());
    }
}