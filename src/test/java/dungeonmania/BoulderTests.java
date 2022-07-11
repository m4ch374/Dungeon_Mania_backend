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

public class BoulderTests {
    private static final String DIR_NAME = "d_BoulderTests/";
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
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_boulderTest_moveEast", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
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
    public void testBoulderMoveAll() {
        DungeonManiaController dmc = new DungeonManiaController();
        // Player set to POS(1, 1), Boulder set to POS(2,1), Exit set to POS(1,3)
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_boulderTest_moveAll", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // Player in the middle of 5x5 grid

        // Boulder east of player
        EntityResponse boulderE = getEntities(dungeonRes, "boulder").get(0);
        EntityResponse expectedBoulder = new EntityResponse(boulderE.getId(), boulderE.getType(), new Position(4, 3), true);
        assertEquals(expectedBoulder, boulderE);
        // Boulder west of player
        EntityResponse boulderW = getEntities(dungeonRes, "boulder").get(1);
        expectedBoulder = new EntityResponse(boulderW.getId(), boulderW.getType(), new Position(2, 3), true);
        assertEquals(expectedBoulder, boulderW);
        // Boulder north of player
        EntityResponse boulderN = getEntities(dungeonRes, "boulder").get(2);
        expectedBoulder = new EntityResponse(boulderN.getId(), boulderN.getType(), new Position(3, 2), true);
        assertEquals(expectedBoulder, boulderN);
        // Boulder south of player
        EntityResponse boulderS = getEntities(dungeonRes, "boulder").get(2);
        expectedBoulder = new EntityResponse(boulderS.getId(), boulderS.getType(), new Position(3, 4), true);
        assertEquals(expectedBoulder, boulderS);

        // Move player east (Player should be at (4,3), and boulderE at (5,3))
        dungeonRes = dmc.tick(Direction.RIGHT);
        expectedBoulder = new EntityResponse(boulderE.getId(), boulderE.getType(), new Position(5, 3), true);
        assertEquals(expectedBoulder, boulderE);
        
        // Move player west TWICE (Player should be at (2,3), and boulderW at (1,3))
        dungeonRes = dmc.tick(Direction.LEFT);
        dungeonRes = dmc.tick(Direction.LEFT);
        expectedBoulder = new EntityResponse(boulderW.getId(), boulderW.getType(), new Position(1, 3), true);
        assertEquals(expectedBoulder, boulderW);
        
        // Move player east then north (Player should be at (3,2), and boulderN at (3,1))
        dungeonRes = dmc.tick(Direction.RIGHT);
        dungeonRes = dmc.tick(Direction.UP);
        expectedBoulder = new EntityResponse(boulderN.getId(), boulderN.getType(), new Position(3, 1), true);
        assertEquals(expectedBoulder, boulderN);
        
        // Move player south TWICE (Player should be at (3,4), and boulderN at (3,5))
        dungeonRes = dmc.tick(Direction.DOWN);
        dungeonRes = dmc.tick(Direction.DOWN);
        expectedBoulder = new EntityResponse(boulderS.getId(), boulderS.getType(), new Position(3, 1), true);
        assertEquals(expectedBoulder, boulderS);
    }


    @Test
    @DisplayName("Boulder 3: Test boulder blocked by wall")
    public void testBoulderBlockedByWall() {
        // Player pushes boulder onto a wall, but player and boulder dont move
        DungeonManiaController dmc = new DungeonManiaController();
        // Player set to POS(1, 1), Boulder set to POS(2,1), Wall set to POS(3,1)
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_boulderTest_moveOntoWall", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // EntityResponse player = getPlayer(dungeonRes).get();
        // EntityResponse wall = getEntities(dungeonRes, "boulder").get(0);
        EntityResponse boulder = getEntities(dungeonRes, "boulder").get(0);
        // Confirm boulder's existence
        EntityResponse expectedBoulder = new EntityResponse(boulder.getId(), boulder.getType(), new Position(2, 1), true);
        assertEquals(expectedBoulder, boulder);
        // Move player east
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Check the boulder has NOT
        assertEquals(expectedBoulder, boulder);
    }


    @Test
    @DisplayName("Boulder 4: Test two cardinally adjacent boulders cannot be moved")
    public void testBoulderTwoAdjacentNotMoved() {
        // Player pushes boulder which has another boulder to its east, but player and both boulders dont move
        DungeonManiaController dmc = new DungeonManiaController();
        // Player set to POS(1, 1), Boulder2 set to POS(2,3), Boulder3 set to POS(3,3) Switch2 set to POS(4,3)
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_boulderTest_moveOntoFloorSwitch", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // EntityResponse player = getPlayer(dungeonRes).get();
        EntityResponse boulder2 = getEntities(dungeonRes, "boulder").get(1);
        EntityResponse boulder3 = getEntities(dungeonRes, "boulder").get(2);
        // Confirm both boulder's existence
        EntityResponse expectedBoulder = new EntityResponse(boulder2.getId(), boulder2.getType(), new Position(2, 3), true);
        assertEquals(expectedBoulder, boulder2);
        expectedBoulder = new EntityResponse(boulder3.getId(), boulder3.getType(), new Position(3, 3), true);
        assertEquals(expectedBoulder, boulder3);
        // Move player south TWICE, then attempt to move east (into boulder2)
        dungeonRes = dmc.tick(Direction.DOWN);
        dungeonRes = dmc.tick(Direction.DOWN);
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Check the BOTH boulders has NOT moved
        assertEquals(expectedBoulder, boulder3);
        assertEquals(new Position(2, 3), boulder2.getPosition());
    }


    @Test
    @DisplayName("Boulder 5: Test boulder move onto floor switch")
    public void testBoulderMoveOntoFloorSwitch() {
        // When boulder is moved onto floor switch, it would "trigger" it i.e. put it in a "triggered" STATE
        // this would also contribute to Tracker's counting of goals (if a goal is having a boulder on all floor switches)
        // So here, game's basic goal is to have a boulder on all switches
        // NOTE: you dont need to test any of this, only test the boulder is on the same position as the switch
        // (repeat test1, but how one floor switch where boulder will be pushed onto)
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_boulderTest_moveOntoFloorSwitch", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // Get switch info 
        // LEGACY: EntityResponse floorSwitch1 = TestUtils.getEntityById(dungeonRes, "switch");
        EntityResponse floorSwitch1 = getEntities(dungeonRes, "switch").get(0);
        // Get boulder info 
        EntityResponse boulder1 = getEntities(dungeonRes, "boulder").get(0);
        // Confirm boulder's existence
        EntityResponse expectedBoulder = new EntityResponse(boulder1.getId(), boulder1.getType(), new Position(2, 1), true);
        assertEquals(expectedBoulder, boulder1);
        // Move player east
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Check the boulder has moved via comparing entity res to expectedBoulder's position
        expectedBoulder = new EntityResponse(boulder1.getId(), boulder1.getType(), new Position(3, 1), true);
        assertEquals(expectedBoulder, boulder1);
        // Confirm this is also same position as the switch
        assertEquals(floorSwitch1.getPosition(), boulder1.getPosition());
        
        // Now move boulder off the switch
        dungeonRes = dmc.tick(Direction.RIGHT);
        expectedBoulder = new EntityResponse(boulder1.getId(), boulder1.getType(), new Position(4, 1), true);
        assertEquals(expectedBoulder, boulder1);
    }
    

    @Test
    @DisplayName("Boulder 6: Test boulder cannot move onto a collectable")
    public void testBoulderMoveOntoCollectable() {
        // Playerset to POS(1,1), Treasure set to POS(3,1), Boulder set to POS(2,1)
        // So after Tick(RIGHT), player should not move, and boulder and treasure should stay in place
        // Thus player cant move boulder since a non-switch entity is cardinally adjacent ot boulder. 
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_boulderTest_moveOntoCollectable", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // EntityResponse treasure = getEntities(dungeonRes, "treasure").get(0);
        EntityResponse boulder = getEntities(dungeonRes, "boulder").get(0);
        // Confirm boulder's existence
        EntityResponse expectedBoulder = new EntityResponse(boulder.getId(), boulder.getType(), new Position(3, 1), true);
        assertEquals(expectedBoulder, boulder);
        // Move player east
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Check the boulder has NOT moved
        assertEquals(expectedBoulder, boulder);
        
    }

    @Test
    @DisplayName("Boulder 7: Test boulder cannot move onto a static")
    public void testBoulderMoveOntoStatic() {
        // Playerset to POS(1,1), XXXXXX set to POS(3,1), Boulder set to POS(2,1)
        // So after Tick(RIGHT), player should not move, and boulder and XXXXXX should stay in place
        // Thus player cant move boulder since a non-switch entity is cardinally adjacent ot boulder. 
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_boulderTest_moveOntoCollectable", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // EntityResponse treasure = getEntities(dungeonRes, "treasure").get(0);
        EntityResponse boulder = getEntities(dungeonRes, "boulder").get(0);
        // Confirm boulder's existence
        EntityResponse expectedBoulder = new EntityResponse(boulder.getId(), boulder.getType(), new Position(3, 1), true);
        assertEquals(expectedBoulder, boulder);
        // Move player east
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Check the boulder has NOT moved
        assertEquals(expectedBoulder, boulder);
        
    }
    
    
    @Test
    @DisplayName("Boulder 8: Test boulder cannot move onto an entity")
    public void testBoulderMoveOntoEntity() {
        // Similar to Test 8
        // Player set to POS(2,1), Merc set to POS(4,1), Boulder set to POS(3,1), and Walls at (1,2), (2,2), (3,2), (4,2), (5,2) and (5,1)
        // Thus the walls and boulder block locks Merc in place (since merc cant move boulder) 
        // So after Tick(RIGHT), player should not move, and boulder and Merc should stay in place
        // Thus player cant move boulder since a non-switch entity is cardinally adjacent ot boulder. 
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_boulderTest_moveByMerc", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // EntityResponse merc = getEntities(dungeonRes, "mercenary").get(0);
        EntityResponse boulder = getEntities(dungeonRes, "boulder").get(0);
        // Confirm boulder's existence
        EntityResponse expectedBoulder = new EntityResponse(boulder.getId(), boulder.getType(), new Position(3, 1), true);
        assertEquals(expectedBoulder, boulder);
        // Move player east
        dungeonRes = dmc.tick(Direction.RIGHT);
        // Check the boulder has NOT moved
        assertEquals(expectedBoulder, boulder);
        
    }
    
    
    @Test
    @DisplayName("Boulder 9: Test boulder cannot be moved by non-Player entity")
    public void testBoulderMoveByNonPlayer() {
        // SYNOPSIS:
        // Playerset to POS(2,1), Merc set to POS(4,1), Boulder set to POS(3,1), and Walls at (1,2), (2,2), (3,2), (4,2), (5,2) and (5,1)
        // Thus the walls block Merc from moving around the boulder
        // So after Tick(LEFT), player will be at (1,1), and boulder and Merc should stay in place
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(DIR_NAME + "d_boulderTest_moveByMerc", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // EntityResponse merc = getEntities(dungeonRes, "mercenary").get(0);
        EntityResponse boulder = getEntities(dungeonRes, "boulder").get(0);
        // Confirm boulder's existence
        EntityResponse expectedBoulder = new EntityResponse(boulder.getId(), boulder.getType(), new Position(3, 1), true);
        assertEquals(expectedBoulder, boulder);
        // Move player west
        dungeonRes = dmc.tick(Direction.LEFT);
        // Check the boulder has NOT moved
        assertEquals(expectedBoulder, boulder);
    }
    
}
