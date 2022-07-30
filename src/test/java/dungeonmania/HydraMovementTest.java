package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

// Same movement as zombie toast
// Hence tests are similar to zombie toast tests
public class HydraMovementTest {
    private static final String DIR_NAME = "d_HydraTests/";

    @Test
    @DisplayName("Hydra moves")
    public void testHydraMoves() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_hydraTest_basicMovement", "c_msic_zeroDamage");
        EntityResponse zombie = TestUtils.getEntityById(resp, "hydra");
        
        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "hydra");

        assertFalse(zombie.getPosition().equals(movedZombie.getPosition()));
    }

    @Test
    @DisplayName("Hydra blocks by wall")
    public void testWallBlocksHydra() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_hydraTest_wallBlocksMovement", "c_msic_zeroDamage");
        EntityResponse zombie = TestUtils.getEntityById(resp, "hydra");

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "hydra");
        
        assertTrue(zombie.getPosition().equals(movedZombie.getPosition()));
    }

    @Test
    @DisplayName("Hydra blocks by locked door")
    public void testLockedDoorBlocksHydra() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_hydraTest_doorBlocksMovement", "c_msic_zeroDamage");
        EntityResponse zombie = TestUtils.getEntityById(resp, "hydra");

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "hydra");
        
        assertTrue(zombie.getPosition().equals(movedZombie.getPosition()));
    }

    @Test
    @DisplayName("Hydra overlaps with portal")
    public void testOverlapsWithPortal() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_hydraTest_overlapsPortal", "c_msic_zeroDamage");
        EntityResponse zombie = TestUtils.getEntityById(resp, "hydra");

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "hydra");
        List<Position> portalPos = TestUtils.getEntities(resp, "portal").stream()
                                    .map(e -> e.getPosition())
                                    .collect(Collectors.toList());
        
        assertFalse(zombie.getPosition().equals(movedZombie.getPosition()));
        assertTrue(portalPos.contains(movedZombie.getPosition()));
    }

    @Test
    @DisplayName("Hydra does not push boulder")
    public void testBoulderRemains() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_hydraTest_doesNotPushBoulder", "c_msic_zeroDamage");
        EntityResponse zombie = TestUtils.getEntityById(resp, "hydra");
        List<Position> originalboulderPos = new ArrayList<Position>();
        originalboulderPos.add(TestUtils.getEntityById(resp, "boulder").getPosition());
        originalboulderPos.add(TestUtils.getEntityById(resp, "boulder1").getPosition());
        originalboulderPos.add(TestUtils.getEntityById(resp, "boulder2").getPosition());
        originalboulderPos.add(TestUtils.getEntityById(resp, "boulder3").getPosition());

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "hydra");
        List<Position> newBoulderPos = new ArrayList<Position>();
        newBoulderPos.add(TestUtils.getEntityById(resp, "boulder").getPosition());
        newBoulderPos.add(TestUtils.getEntityById(resp, "boulder1").getPosition());
        newBoulderPos.add(TestUtils.getEntityById(resp, "boulder2").getPosition());
        newBoulderPos.add(TestUtils.getEntityById(resp, "boulder3").getPosition());
        
        assertFalse(zombie.getPosition().equals(movedZombie.getPosition()));
        assertTrue(newBoulderPos.contains(movedZombie.getPosition()));
        for (int i = 0; i < newBoulderPos.size(); i++) {
            assertTrue(originalboulderPos.get(i).equals(newBoulderPos.get(i)));
        }
    }

    @Test
    @DisplayName("Hydra does not collect")
    public void testCollectablesRemains() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_hydraTest_overlapsPortal", "c_msic_zeroDamage");
        EntityResponse zombie = TestUtils.getEntityById(resp, "hydra");
        int tresureNums = TestUtils.getEntities(resp, "treasure").size();

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "hydra");
        int newTresureNums = TestUtils.getEntities(resp, "treasure").size();
        
        assertFalse(zombie.getPosition().equals(movedZombie.getPosition()));
        assertTrue(tresureNums == newTresureNums);
    }

    @Test
    @DisplayName("Test hydra flees from player leftwards")
    public void testHydraFlee_LEFT() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_hydraTest_hydraFleesLeft", "c_msic_longPotionDuration_noDamage");

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "player").getPosition());
        assertTrue(TestUtils.getEntities(res, "invincibility_potion").size() == 0);

        assertDoesNotThrow(() -> dmc.tick("invincibility_potion"));

        res = dmc.tick(Direction.RIGHT);
        Position originalZombiePos = TestUtils.getEntityById(res, "hydra").getPosition();

        for (int i = 1; i < 98; i++) {
            res = dmc.tick(Direction.RIGHT);
            assertTrue(originalZombiePos.translateBy(new Position(-i, 0)).equals(TestUtils.getEntityById(res, "hydra").getPosition()) 
                || originalZombiePos.translateBy(new Position(-i - 1, 0)).equals(TestUtils.getEntityById(res, "hydra").getPosition()));
        }
    }
}
