package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getPlayer;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.exceptions.InvalidActionException;

public class LogicSwitchTest {
    private final String PATH = "d_LogicTests/";

    @Test
    @DisplayName("Logic 1: Can not go in SD")
    public void canNotGoIn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "or_and", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(6, 3), Player.getPosition());
    }

    @Test
    @DisplayName("Logic 2: Can go in SD with key")
    public void canGoIn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "or_and", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.UP);
        Player = getPlayer(dmc.getDungeonResponseModel()).get();
        assertEquals(new Position(6, 2), Player.getPosition());
        dmc.tick(Direction.DOWN);
        Player = getPlayer(dmc.getDungeonResponseModel()).get();
        assertEquals(new Position(6, 3), Player.getPosition());
        dmc.tick(Direction.DOWN);
        Player = getPlayer(dmc.getDungeonResponseModel()).get();
        assertEquals(new Position(6, 4), Player.getPosition());
    }

    @Test
    @DisplayName("Logic 3: Can go in SD with SunStone")
    public void canGoInWithSS() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "or_and", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.UP);
        assertEquals(1, dmc.getDungeonResponseModel().getInventory().size());
        dmc.tick(Direction.UP);
        assertEquals(2, dmc.getDungeonResponseModel().getInventory().size());
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        assertEquals(2, dmc.getDungeonResponseModel().getInventory().size());

        Player = getPlayer(dmc.getDungeonResponseModel()).get();
        assertEquals(new Position(6, 4), Player.getPosition());
        assertEquals(2, dmc.getDungeonResponseModel().getInventory().size());
    }

    @Test
    @DisplayName("Logic 4: Can go in SD with active switch")
    public void canGoInWithAS() {
        // Player pushes boulder down onto floor switch, that is connected to switch door via wires and a light bulb
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "or_and", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        // Now push the boulder onto floor switch
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        Player = getPlayer(dmc.getDungeonResponseModel()).get();
        assertEquals(new Position(6, 3), Player.getPosition());

        dmc.tick(Direction.DOWN);

        Player = getPlayer(dmc.getDungeonResponseModel()).get();
        assertEquals(new Position(6, 4), Player.getPosition());
    }

    @Test
    @DisplayName("Logic 5: And bomb")
    public void andBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(PATH + "or_and", "c_logicSwitchTest");

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);

        assertDoesNotThrow(() -> dmc.tick("bomb1"));
        assertEquals(19, dmc.getDungeonResponseModel().getEntities().size());

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);

        assertEquals(15, dmc.getDungeonResponseModel().getEntities().size());
    }

    @Test
    @DisplayName("Logic 6: And door")
    public void andDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "and", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        // Push boulder0 onto floor_switch0
        dmc.tick(Direction.DOWN);

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());

        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        // assert the switch door has not opened, since its an "AND" logic
        assertEquals(new Position(2, 4), Player.getPosition());

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());

        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());

        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        // Push boulder1 onto floor_switch1
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());

        // Now switch_door should be opened, because both floor_switches are active, hence satisfying "AND" condition
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(3, 4), Player.getPosition());
    }

    @Test
    @DisplayName("Logic 7: Xor door")
    public void xorDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "xor", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        // Push boulder1 onto floor_switch1
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 3), Player.getPosition());
        // Successfully move into switch_door, as its condition "XOR" is currently satisfied
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(3, 3), Player.getPosition());

        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        // Push boulder0 onto floor_switch0
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 3), Player.getPosition());
        // Attempt to move into switch_door, but fail cause "XOR" cond is violated, there are more than one card.adj. activated entities (wire0 and wire1)
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 3), Player.getPosition());
    }

    @Test
    @DisplayName("Logic 8: Co_and door")
    public void coAndDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "co_and", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.DOWN);
        // attempts to move into locked switch_door, but fails obv
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);

        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        // Push boulder0 onto floor_switch0
        dmc.tick(Direction.DOWN);

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());
        // attempt to move into switch_door that is still locked since "CO_AND" condition has not passed
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        // Push boulder2 onto floor_switch2
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());
        // Successful in moving into switch_door that is now unlocked since "CO_AND" condition has passed (wire0 and 2 via floor_switch0 and 2)
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(3, 4), Player.getPosition());

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        // Push boulder1 onto floor_switch1
        dmc.tick(Direction.DOWN);
        // Push boulder0 off floor_switch0
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());
        // Successful in moving into switch_door that is still unlocked even though a wire0 has been replaced with wire1 
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(3, 4), Player.getPosition());
    }

    @Test
    @DisplayName("Logic 9: Lit on light bulb")
    public void litOn() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(PATH + "bulb", "c_logicSwitchTest");
        // Push boulder onto floor_switch, activating light bulb via wire
        // the light bulb then converts into type LIGHT_BULB_ON 
        dmc.tick(Direction.RIGHT);

        DungeonResponse dungeonResponse = dmc.getDungeonResponseModel();
        List<EntityResponse> entityResponse = dungeonResponse.getEntities();

        // Confirm the only light bulb is of type ON
        boolean onlyOneLightBulbON = entityResponse.stream().filter(e -> e.getType().equals(EntityTypes.LIGHT_BULB_ON.toString())).count() == 1;
        assertEquals(true, onlyOneLightBulbON);

        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        // Push boulder off floor_switch
        dungeonResponse = dmc.tick(Direction.UP);
        // Now light_bulb converts back to LIGHT_BULB_OFF
        entityResponse = dungeonResponse.getEntities();
        onlyOneLightBulbON = entityResponse.stream().filter(e -> e.getType().equals(EntityTypes.LIGHT_BULB_ON.toString())).count() == 1;
        assertEquals(false, onlyOneLightBulbON);
    }

    ////////////

    @Test
    @DisplayName("Floor Switch 1 (NON-LOGIC): Switch is Active after dropping a bomb")
    public void testFloorSwitchActiveAfterBombDrop() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        assertEquals(22, DungonRes.getEntities().size());

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);


        DungonRes = dmc.getDungeonResponseModel();
        assertEquals(20, DungonRes.getEntities().size());

        ArrayList<String> bomb_id = new ArrayList<String>();
        DungonRes.getInventory()
                .stream()
                .filter(e -> e.getType().equals(EntityTypes.BOMB.toString()))
                .forEach(e -> bomb_id.add(e.getId()));

        assertEquals(1, bomb_id.size());

        try {
            DungonRes = dmc.tick(bomb_id.get(0));
            assertEquals(21, DungonRes.getEntities().size());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        }

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        DungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(9, DungonRes.getEntities().size());
    }


    @Test
    @DisplayName("Logic 10: Bulb lights up via active Switch two wires away thus activates switch_door")
    public void testBulbSpecEg() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dungeonRes = dmc.newGame(PATH + "d_switchBulbSpecEg", "c_logicSwitchTest");
        EntityResponse player = getPlayer(dungeonRes).get();
        // confirm position of logical entities
        EntityResponse wireInfo = TestUtils.getEntityById(dungeonRes, "wire");
        EntityResponse wire1Info = TestUtils.getEntityById(dungeonRes, "wire1");
        EntityResponse bulbInfo = TestUtils.getEntityById(dungeonRes, "light_bulb_off");
        EntityResponse switchDoorInfo = TestUtils.getEntityById(dungeonRes, "switch_door");

        assertEquals(wireInfo.getPosition(), new Position(4, 1));
        assertEquals(wire1Info.getPosition(), new Position(5, 1));
        assertEquals(bulbInfo.getPosition(), new Position(6, 1));
        assertEquals(switchDoorInfo.getPosition(), new Position(7, 1));
        // Push boulder onto switch, that is cardinally adjacent (C.A) to a wire, which is C.A to another wire, which lights up Bulb, which then opens the switch_door
        dmc.tick(Direction.RIGHT);
        // Check bulb is lit up by trying to move through the switch door, which should be opened (don't need a key if electrically opened)
        dmc.tick(Direction.DOWN);
        for (int i = 0; i < 5; i++) {dmc.tick(Direction.RIGHT);}
        dungeonRes = dmc.tick(Direction.UP);
        // Player should be overlapping the switch door now, hence door is opened, hence light bulb must also be ON
        player = getPlayer(dungeonRes).get();
        assertEquals(player.getPosition(), switchDoorInfo.getPosition());
    }
    
}
