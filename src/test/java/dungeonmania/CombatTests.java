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

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;





public class CombatTests {

    private static DungeonResponse spiderSequence(DungeonManiaController controller, String configFile) {
        /*
         *  exit  
         * player  spider
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicMercenary", configFile);
        int spiderCount = countEntityOfType(initialResponse, "spider");
        
        // assertEquals(1, countEntityOfType(initialResponse, "player"));
        // assertEquals(1, spiderCount);
        return controller.tick(Direction.RIGHT);
    }

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

    private static DungeonResponse swordCombatSequence(DungeonManiaController controller, String configFile) {
        /*
         *  exit   wall  wall  wall
         * player  [  ]  merc  wall
         *  sword  wall  wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_withSword", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");
        
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        controller.tick(Direction.DOWN);
        return controller.tick(Direction.UP);
    }

    private static DungeonResponse mercAllyCombatSequence(DungeonManiaController controller, String configFile) throws InvalidActionException {
        /*
         *  exit   wall  wall  wall
         * player  merc  merc  wall
         *  trea   wall  wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_ally1Kill1", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");

        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(2, mercenaryCount);
        DungeonResponse res = controller.tick(Direction.DOWN);
        controller.interact("mercenary");
        return controller.tick(Direction.UP);
    }

    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(-(enemyAttack / 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-(playerAttack / 5), round.getDeltaEnemyHealth(), 0.001);
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
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (String mod : modifiers) {
            switch (mod) {
                case "sword":
                    playerAttack += Double.parseDouble(getValueFromConfigFile("sword_attack", configFilePath));
                    break;
                case "shield":
                    enemyAttack -= Double.parseDouble(getValueFromConfigFile("shield_defence", configFilePath));;
                    break;
                default:
                    continue;
            }
        }

        if (hasBow) {
            playerAttack = playerAttack * 2;
        }

        for (RoundResponse round : rounds) {
            assertEquals(-(enemyAttack / 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-(playerAttack / 5), round.getDeltaEnemyHealth(), 0.001);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    private void assertAlliedBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies,
                String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double alliedAtkBonus = Double.parseDouble(getValueFromConfigFile("ally_attack", configFilePath));
        double alliedDfnceBonus = Double.parseDouble(getValueFromConfigFile("ally_defence", configFilePath));

        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath)) + alliedAtkBonus;
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath)) - alliedDfnceBonus;

        for (RoundResponse round : rounds) {
            assertEquals(-(enemyAttack / 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-(playerAttack / 5), round.getDeltaEnemyHealth(), 0.001);
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
    @DisplayName("Test Combat w/ 1 modifier (Sword)")
    public void testCombatWithSword() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = swordCombatSequence(controller, "c_battleTests_basicMercenaryMercenaryDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        List<String> mods = new ArrayList<String>();
        mods.add("sword");
        assertModdedBattleCalculations("mercenary", battle, true, "c_battleTests_basicMercenaryMercenaryDies", mods, false);
    }

    @Test
    @DisplayName("Test Combat w/ Ally Merc")
    public void testCombatWithAllyMerc() throws InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = mercAllyCombatSequence(controller, "c_battleTests_basicMercenaryMercenaryDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertAlliedBattleCalculations("mercenary", battle, true, "c_battleTests_basicMercenaryMercenaryDies");
    }

    @Test
    @DisplayName("Test Combat w/ Spider")
    public void testCombatWithSpider() throws InvalidActionException {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse postBattleResponse = spiderSequence(controller, "c_battleTests_basicMercenaryMercenaryDies");
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        assertBattleCalculations("spider", battle, true, "c_battleTests_basicMercenaryMercenaryDies");
    }
}
