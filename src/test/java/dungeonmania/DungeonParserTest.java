package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DungeonParserTest {
    @Test
    @DisplayName("Test wrong dungeon name")
    public void testDungeonNotExist() {

        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("joe bidome", "c_spiderTest_basicMovement"));
    }

    @Test
    @DisplayName("Test wrong config name name")
    public void testConfigNotExist() {

        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("d_battleTest_basicMercenary", "obama prism"));
    }

    @Test
    @DisplayName("Test able to load")
    public void testAbleToLoad() {

        DungeonManiaController dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> dmc.newGame("d_CombatTests/d_battleTest_basicMercenary", "c_spiderTest_basicMovement"));
    }
}
