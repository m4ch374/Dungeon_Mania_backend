package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DungeonGenerationTest {
    @Test
    @DisplayName("Test generation throws exception")
    public void testThrowsException() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.generateDungeon(1, 1, 5, 5, "joe bidome"));
    }

    @Test
    @DisplayName("Test dungeon generation")
    public void testDungeonGeneration() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> dmc.generateDungeon(1, 1, 5, 5, "c_msic_zeroDamage"));

        DungeonResponse res = dmc.tick(Direction.UP);
        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "player").getPosition());

        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 1), TestUtils.getEntityById(res, "player").getPosition());
    }
}
