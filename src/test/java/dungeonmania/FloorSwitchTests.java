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

public class FloorSwitchTests {
    private static final String DIR_NAME = "d_FloorSwitchTests/";
    // Add helper fncs here!

    @Test
    @DisplayName("FloorSwitch 1: Test a boulder moving onto switch activates it")
    public void testPortalTeleportsPlayerBasic() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_FloorSwitch_simpleIsActive", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get switch info
        // assert its existence
        
    }
}