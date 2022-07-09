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

public class StaticInteractableTests {
    // Add helper fncs here!
    /*
     * Fnc that returns where the boulder to move to ('U', 'D', 'L', 'R'), based in player movement
     */



    @Test
    @DisplayName("Boulder 1: Test boulder move East")
    // basic movement = up, down, left, right (depending on player interaction)
    public void testBoulderMoveEast() {
        DungeonManiaController dmc = new DungeonManiaController();
        // Player set to POS(1, 1), Boulder set to POS(2,1), Exit set to POS(1,3)
        DungeonResponse dungeonRes = dmc.newGame("d_boulderTest_testBoulderMoveEast", "c_boulderTest_testBoulderMoveEast");
        
        // Get player info, then move it east into boulder (via dmc's tick() fnc)
        EntityResponse player = getPlayer(dungeonRes).get();
        // Get boulder info 
        EntityResponse boulder = getEntities(dungeonRes, "boulder").get(0);
        // Confirm boulder's existence
        EntityResponse expectedBoulder = new EntityResponse(boulder.getId(), boulder.getType(), new Position(2, 1), true);
        assertEquals(expectedBoulder, boulder);
        // Move player east
        // NOTE: dont need to confirm player also moves...that's playerTest's job...keep it blackboxed brav
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Check the boulder has moved via comparing entity res to expectedBoulder's position
        expectedBoulder = new EntityResponse(boulder.getId(), boulder.getType(), new Position(3, 1), true);
        assertEquals(expectedBoulder, boulder);
    }


    @Test
    @DisplayName("Boulder 2: Test boulder movement in all directions")
    // tests all four movement directions, with different boulders too
    public void testBoulderMovementAll() {
    }


    @Test
    @DisplayName("Boulder 3: Test boulder move onto floor switch")
    // tests all four movement directions, with different boulders too
    public void testBoulderMoveOntoFloorSwitch() {
    }
    

    @Test
    @DisplayName("Boulder 4: Test boulder move onto collectable")
    // tests all four movement directions, with different boulders too
    public void testBoulderMoveOntoCollectable() {
    }
    
}
