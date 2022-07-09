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
        DungeonResponse DungonRes = dmc.newGame("d_playertest", "c_playertest");
        EntityResponse Player = getPlayer(DungonRes).get();

        EntityResponse expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(2, 3), false);
        assertEquals(expectedPlayer, Player);

        DungonRes = dmc.tick(Direction.UP);
        Player = getPlayer(DungonRes).get();
        expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(1, 3), false);
        assertEquals(expectedPlayer, Player);

        DungonRes = dmc.tick(Direction.RIGHT);
        Player = getPlayer(DungonRes).get();
        expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(1, 3), false);
        assertEquals(expectedPlayer, Player);

        DungonRes = dmc.tick(Direction.DOWN);
        Player = getPlayer(DungonRes).get();
        expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(2, 3), false);
        assertEquals(expectedPlayer, Player);

        DungonRes = dmc.tick(Direction.LEFT);
        Player = getPlayer(DungonRes).get();
        expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(2, 2), false);
        assertEquals(expectedPlayer, Player);

        DungonRes = dmc.tick(Direction.RIGHT);
        Player = getPlayer(DungonRes).get();
        expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(2, 3), false);
        assertEquals(expectedPlayer, Player);
    }

    @Test
    @DisplayName("Item collection: Pick All Item")
    public void testPickTAW() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playertest", "c_playertest");

        assertEquals(DungonRes.getInventory().size(), 21);

        pickAll(dmc);

        DungonRes = dmc.getDungeonResponseModel();
        // a key
        assertEquals(DungonRes.getInventory().size(), 1);
    }

    @Test
    @DisplayName("Use item: Use potion")
    public void testUsePotion() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playertest", "c_playertest");

        pickAll(dmc);

        try {
            dmc.tick("invincibility_potion_1");
            dmc.tick("invincibility_potion_2");
        } catch (IllegalArgumentException e) {
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            assertEquals(true, false);
        }

        try {
            dmc.tick("invisibility_potion_1");
            dmc.tick("invisibility_potion_2");
        } catch (IllegalArgumentException e) {
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            assertEquals(true, false);
        }
    }

    @Test
    @DisplayName("Crafted: Fail to make bow & shield")
    public void testFailToCrafted() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playertest", "c_playertest");

        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Crafted: Make bow")
    public void testMakeBow() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playertest", "c_playertest");

        pickAll(dmc);

        assertDoesNotThrow(() -> dmc.build("bow"));
    }

    @Test
    @DisplayName("Crafted: Make shield with treasure")
    public void testMakeST() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_playertest", "c_playertest");

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
        dmc.newGame("d_playertest", "c_playertest");

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
        dmc.newGame("d_playertest", "c_playertest");

        pickAll(dmc);

        assertDoesNotThrow(() -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Drop item: Drop bomb")
    public void testDropBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playertest", "c_playertest");

        assertEquals(DungonRes.getInventory().size(), 21);

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);
        assertEquals(DungonRes.getInventory().size(), 19);

        try {
            DungonRes = dmc.tick("bomb_1");
            assertEquals(DungonRes.getInventory().size(), 20);
        } catch (IllegalArgumentException e) {
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            assertEquals(true, false);
        }
    }

    @Test
    @DisplayName("Drop item: Cannot cross/pick up a placed bomb")
    public void testCannotPick() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playertest", "c_playertest");

        assertEquals(DungonRes.getInventory().size(), 21);

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.DOWN);
        assertEquals(DungonRes.getInventory().size(), 19);

        try {
            DungonRes = dmc.tick("bomb_1");
            assertEquals(DungonRes.getInventory().size(), 20);

            dmc.tick(Direction.DOWN);
            EntityResponse Player = getPlayer(DungonRes).get();
            EntityResponse expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(2, 2), false);
            assertEquals(expectedPlayer, Player);
            assertEquals(DungonRes.getInventory().size(), 20);

            dmc.tick(Direction.UP);
            Player = getPlayer(DungonRes).get();
            expectedPlayer = new EntityResponse(Player.getId(), Player.getType(), new Position(2, 2), false);
            assertEquals(expectedPlayer, Player);
            assertEquals(DungonRes.getInventory().size(), 20);
        } catch (IllegalArgumentException e) {
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            assertEquals(true, false);
        }
    }

    @Test
    @DisplayName("Drop item: Drop a bomb will destroy other bombs on the map")
    public void testBombDestroyBomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame("d_playertest", "c_playertest");

        assertEquals(DungonRes.getInventory().size(), 21);

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

        assertEquals(DungonRes.getInventory().size(), 19);

        try {
            DungonRes = dmc.tick("bomb_1");
            assertEquals(DungonRes.getInventory().size(), 8);
        } catch (IllegalArgumentException e) {
            assertEquals(true, false);
        } catch (InvalidActionException e) {
            assertEquals(true, false);
        }
    }
}
