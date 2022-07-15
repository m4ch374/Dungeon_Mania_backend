package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static dungeonmania.TestUtils.getPlayer;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

/**
 * this test use "d_playertest" & "c_playertest"
 */
public class PlayerCollectionTest {
    // take all items on the map and return to original point
    private void pickAll(DungeonManiaController dmc) {
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        // go back
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
    }

    @Test
    @DisplayName("Player: Test basic player movement")
    public void testPlayerMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        EntityResponse expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(3, 2), false);
        assertEquals(expectedPlayer, Player);

        DungonRes = dmc.tick(Direction.UP);
        Player = getPlayer(DungonRes).get();
        expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(3, 1), false);
        assertEquals(expectedPlayer, Player);

        DungonRes = dmc.tick(Direction.DOWN);
        Player = getPlayer(DungonRes).get();
        expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(3, 2), false);
        assertEquals(expectedPlayer, Player);

        DungonRes = dmc.tick(Direction.LEFT);
        Player = getPlayer(DungonRes).get();
        expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(2, 2), false);
        assertEquals(expectedPlayer, Player);

        DungonRes = dmc.tick(Direction.RIGHT);
        Player = getPlayer(DungonRes).get();
        expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(3, 2), false);
        assertEquals(expectedPlayer, Player);
    }

    @Test
    @DisplayName("Item collection: Pick All Item")
    public void testPickTAW() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        assertEquals(0, DungonRes.getInventory().size());
        assertEquals(22, DungonRes.getEntities().size());

        pickAll(dmc);

        DungonRes = dmc.getDungeonResponseModel();

        assertEquals(18, DungonRes.getInventory().size());
        assertEquals(4, DungonRes.getEntities().size());
    }

    @Test
    @DisplayName("Use item: Use potion")
    public void testUsePotion() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");
        DungeonResponse DungonRes;

        pickAll(dmc);

        DungonRes = dmc.getDungeonResponseModel();
        ArrayList<String> potion_id = new ArrayList<String>();
        DungonRes.getInventory()
                .stream()
                .filter(e -> e.getType().equals(EntityTypes.INVINCIBILITY_POTION.toString()))
                .forEach(e -> potion_id.add(e.getId()));

        assertEquals(2, potion_id.size());

        try {
            DungonRes = dmc.tick(potion_id.get(0));
            DungonRes = dmc.tick(potion_id.get(1));
            assertEquals(16, DungonRes.getInventory().size());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        }

        DungonRes = dmc.getDungeonResponseModel();
        potion_id.clear();
        DungonRes.getInventory()
                .stream()
                .filter(e -> e.getType().equals(EntityTypes.INVISIBILITY_POTION.toString()))
                .forEach(e -> potion_id.add(e.getId()));

        assertEquals(2, potion_id.size());

        try {
            DungonRes = dmc.tick(potion_id.get(0));
            DungonRes = dmc.tick(potion_id.get(1));
            assertEquals(14, DungonRes.getInventory().size());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        }
    }

    @Test
    @DisplayName("Crafted: Items that cannot be crafted")
    public void testFailToMake() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        assertThrows(IllegalArgumentException.class, () -> dmc.build("sword"));

        assertThrows(IllegalArgumentException.class, () -> dmc.build("bomb"));
    }

    @Test
    @DisplayName("Crafted: Fail to make bow & shield")
    public void testFailToCrafted() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Crafted: Make bow")
    public void testMakeBow() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        pickAll(dmc);

        assertDoesNotThrow(() -> dmc.build("bow"));
    }

    @Test
    @DisplayName("Crafted: Make shield with treasure")
    public void testMakeST() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);

        assertDoesNotThrow(() -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Crafted: Make shield with key")
    public void testMakeSK() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);

        assertDoesNotThrow(() -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Crafted: Make shield with both treasure and key")
    public void testMakeSTK() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        pickAll(dmc);

        assertDoesNotThrow(() -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Drop item: Drop bomb")
    public void testDropBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        assertEquals(0, DungonRes.getInventory().size());
        assertEquals(22, DungonRes.getEntities().size());

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        assertEquals(2, DungonRes.getInventory().size());
        assertEquals(20, DungonRes.getEntities().size());

        ArrayList<String> bomb_id = new ArrayList<String>();
        DungonRes.getInventory()
                .stream()
                .filter(e -> e.getType().equals(EntityTypes.BOMB.toString()))
                .forEach(e -> bomb_id.add(e.getId()));

        assertEquals(1, bomb_id.size());

        try {
            DungonRes = dmc.tick(bomb_id.get(0));
            assertEquals(1, DungonRes.getInventory().size());
            assertEquals(21, DungonRes.getEntities().size());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        }
    }

    @Test
    @DisplayName("Drop item: Cannot cross/pick up a placed bomb")
    public void testCannotPick() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        assertEquals(0, DungonRes.getInventory().size());
        assertEquals(22, DungonRes.getEntities().size());

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        assertEquals(2, DungonRes.getInventory().size());
        assertEquals(20, DungonRes.getEntities().size());
        EntityResponse Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 1), Player.getPosition());

        ArrayList<String> bomb_id = new ArrayList<String>();
        DungonRes.getInventory()
                .stream()
                .filter(e -> e.getType().equals(EntityTypes.BOMB.toString()))
                .forEach(e -> bomb_id.add(e.getId()));

        assertEquals(1, bomb_id.size());

        try {
            dmc.tick(bomb_id.get(0));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        }

        DungonRes = dmc.tick(Direction.DOWN);
        assertEquals(1, DungonRes.getInventory().size());
        assertEquals(21, DungonRes.getEntities().size());
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 2), Player.getPosition());
        assertEquals(1, DungonRes.getInventory().size());

        DungonRes = dmc.tick(Direction.UP);
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 2), Player.getPosition());
        assertEquals(1, DungonRes.getInventory().size());
    }

    @Test
    @DisplayName("Drop item: Drop a bomb will destroy other bombs on the map")
    public void testBombDestroyBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playerCollectionTest", "c_playerCollectionTest");

        assertEquals(22, DungonRes.getEntities().size());

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        ArrayList<String> bomb_id = new ArrayList<String>();
        DungonRes.getInventory()
                .stream()
                .filter(e -> e.getType().equals(EntityTypes.BOMB.toString()))
                .forEach(e -> bomb_id.add(e.getId()));

        assertEquals(1, bomb_id.size());

        try {
            DungonRes = dmc.tick(bomb_id.get(0));
            // will pass after boulder-switch interaction finished
            assertEquals(8, DungonRes.getEntities().size());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            System.out.println(e.getMessage());
            assertEquals(true, false);
        }
    }
}
