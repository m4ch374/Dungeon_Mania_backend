package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class CombatTests {

    private static DungeonResponse genericMercenarySequence(DungeonManiaController controller, String configFile) {
        /*
         *  exit   wall  wall  wall
         * player  [  ]  merc  wall
         *  wall   wall  wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicMercenary", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");
        
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        return controller.tick(Direction.RIGHT);
    }

    private static DungeonResponse itemCombatSequence(DungeonManiaController controller, String configFile) {
        /*
         *  exit   wall  wall  wall
         * player  [  ]  merc  wall
         *  sword  wall  wall  wall
         *  bow   shield wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_withItems", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");
        
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        return controller.tick(Direction.RIGHT);
    }

    private static DungeonResponse mercAllyCombatSequence(DungeonManiaController controller, String configFile) {
        /*
         *  exit   wall  wall  wall
         * player  merc  merc  wall
         *  trea   wall  wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_withItems", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");
        
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        return controller.tick(Direction.RIGHT);
    }

    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10));
            assertEquals(round.getDeltaEnemyHealth(), -(playerAttack / 5));
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    private void assertModdedBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies,
                String configFilePath, List<String> modifiers, boolean hasBow) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (String mod : modifiers) {
            switch (mod) {
                case "sword":
                    playerAttack += 2;
                    break;
                case "shield":
                    enemyAttack -= 1;
                    break;
                default:
                    continue;
            }
        }

        if (hasBow) {
            playerAttack = playerAttack * 2;
        }

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -(enemyAttack / 10));
            assertEquals(round.getDeltaEnemyHealth(), -(playerAttack / 5));
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    @Test
    @DisplayName("Test basic battle calculations - mercenary - player loses")
    public void testHealthBelowZeroMercenary() {
       DungeonManiaController controller = new DungeonManiaController();
       DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_basicMercenaryPlayerDies");
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("mercenary", battle, false, "c_battleTests_basicMercenaryPlayerDies");
    }


    @Test
    @DisplayName("Test basic battle calculations - mercenary - player wins")
    public void testRoundCalculationsMercenary() {
       DungeonManiaController controller = new DungeonManiaController();
       DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_basicMercenaryMercenaryDies");
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("mercenary", battle, true, "c_battleTests_basicMercenaryMercenaryDies");
    }

    
    @Test
    @DisplayName("Test Combat w/ 1 modifier (Sword/Bow/Shield/Merc)")
    public void testCombatWith1Mod() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_basicMercenaryMercenaryDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("mercenary", battle, true, "c_battleTests_basicMercenaryMercenaryDies");
    }

    @Test
    @DisplayName("Test Combat w/ multiple modifiers (Sword/Bow/Shield/Merc)")
    public void testCombatWithMultipleMod() {
        
    }

}
