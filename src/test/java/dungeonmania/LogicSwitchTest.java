package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getPlayer;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicSwitchTest {
    private final String PATH = "d_LogicTests/";

    @Test
    @DisplayName("Logic: Can not go in SD")
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
    @DisplayName("Logic: Can go in SD with key")
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
    @DisplayName("Logic: Can go in SD with SunStone")
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
    @DisplayName("Logic: Can go in SD with active switch")
    public void canGoInWithAS() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "or_and", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
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
    @DisplayName("Logic: And bomb")
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
    @DisplayName("Logic: And door")
    public void andDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "and", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
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
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());

        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(3, 4), Player.getPosition());
    }

    @Test
    @DisplayName("Logic: Xor door")
    public void xorDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "xor", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 3), Player.getPosition());

        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(3, 3), Player.getPosition());

        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 3), Player.getPosition());

        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 3), Player.getPosition());
    }

    @Test
    @DisplayName("Logic: Co_and door")
    public void coAndDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse DungonRes = dmc.newGame(PATH + "co_and", "c_logicSwitchTest");
        EntityResponse Player = getPlayer(DungonRes).get();

        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);

        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
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
        assertEquals(new Position(3, 4), Player.getPosition());

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(2, 4), Player.getPosition());

        dmc.tick(Direction.RIGHT);

        DungonRes = dmc.getDungeonResponseModel();
        Player = getPlayer(DungonRes).get();
        assertEquals(new Position(3, 4), Player.getPosition());
    }

    @Test
    @DisplayName("Logic: Lit on light bulb")
    public void litOn() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame(PATH + "bulb", "c_logicSwitchTest");

        dmc.tick(Direction.RIGHT);

        DungeonResponse dungeonResponse = dmc.getDungeonResponseModel();
        List<EntityResponse> entityResponse = dungeonResponse.getEntities();

        int num_on = 0;

        for (EntityResponse e : entityResponse) {
            if (e.getType().equals(EntityTypes.LIGHT_BULB_OFF.toString())) {
                assertEquals(false, true);
            }
            if (e.getType().equals(EntityTypes.LIGHT_BULB_ON.toString())) {
                num_on++;
            }
        }

        assertEquals(1, num_on);
    }
}
