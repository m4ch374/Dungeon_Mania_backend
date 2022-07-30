package dungeonmania;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import dungeonmania.exceptions.InvalidActionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.util.Direction;

public class SwitchRelatedTests {
    private static final String DIR_NAME = "d_SwitchRelatedTests/";
    // ############################
    // ## File contains Tests for Floor Switch, Switch Doors, Light Bulbs and Wires
    // ###########################

    @Test
    @DisplayName("Floor Switch 1: Switch is Active after dropping a bomb")
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


    // @Test
    // @DisplayName("Light Bulb 1: Bulb lights up from cardinally adjacent active Switch")
    // public void testBulbSimple() {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse DungonRes = dmc.newGame("d_SwitchRelated_activeSwitchSimple", "c_playerCollectionTest");
    //     // Push boulder onto switch, that is cardnally adjacent to Bulb
    //     // Check bulb is lit up
    // }

    // @Test
    // @DisplayName("Light Bulb 2: Bulb lights up from active Switch in a Circuit")
    // public void testBulbCircuit() {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse DungonRes = dmc.newGame("d_SwitchRelated_bulbCircuit", "c_playerCollectionTest");
    //     // Push boulder onto switch, that is 5 tiles from Bulb
    //     // connected via Wires
    //     // Check bulb is lit up
    // }


    // @Test
    // @DisplayName("Switch Door 1: Switch Door is closed, then opened via activated switch")
    // public void testSwitchDoorSimpleActiveSwitch() {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse DungonRes = dmc.newGame("d_SwitchRelated_ActiveSwitchSimple", "c_playerCollectionTest");
    //     // Player moves into Switch Door, which blocks him
    //     // Player then pushes boulder onto switch, that is 3 tiles from Switch Door
    //     // connected via Wires
    //     // Player can now pass through Switch Door
    // }

}