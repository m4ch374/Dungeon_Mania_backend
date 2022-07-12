package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
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
        // Player set to POS(1, 1), Door set to POS(3,1), Exit set to POS(1,3)
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_portalTest_basic", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get both portal info 
        EntityResponse portal1 = TestUtils.getEntityById(dungeonRes, "portal");
        EntityResponse portal2 = TestUtils.getEntityById(dungeonRes, "portal1");
        
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
        // Player set to POS(1, 1), Door set to POS(3,1), Exit set to POS(1,3)
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_portalTest_twoPair", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get both portal info 
        
    }
}