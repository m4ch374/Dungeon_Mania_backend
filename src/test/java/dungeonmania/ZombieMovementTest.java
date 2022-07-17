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

public class ZombieMovementTest {
    private static final String DIR_NAME = "d_ZombieTests/";

    @Test
    @DisplayName("Zombie moves")
    public void testZombieMoves() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_zombieTest_basicMovement", "c_movementTest_testMovementDown");
        EntityResponse zombie = TestUtils.getEntityById(resp, "zombie_toast");
        
        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "zombie_toast");

        assertFalse(zombie.getPosition().equals(movedZombie.getPosition()));
    }

    @Test
    @DisplayName("Zombie blocks by wall")
    public void testWallBlocksZombies() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_zombieTest_wallBlocksMovement", "c_movementTest_testMovementDown");
        EntityResponse zombie = TestUtils.getEntityById(resp, "zombie_toast");

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "zombie_toast");
        
        assertTrue(zombie.getPosition().equals(movedZombie.getPosition()));
    }

    @Test
    @DisplayName("Zombie blocks by locked door")
    public void testLockedDoorBlocksZombies() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_zombieTest_doorBlocksMovement", "c_movementTest_testMovementDown");
        EntityResponse zombie = TestUtils.getEntityById(resp, "zombie_toast");

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "zombie_toast");
        
        assertTrue(zombie.getPosition().equals(movedZombie.getPosition()));
    }

    @Test
    @DisplayName("Zombie overlaps with portal")
    public void testOverlapsWithPortal() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_zombieTest_overlapsPortal", "c_movementTest_testMovementDown");
        EntityResponse zombie = TestUtils.getEntityById(resp, "zombie_toast");

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "zombie_toast");
        List<Position> portalPos = TestUtils.getEntities(resp, "portal").stream()
                                    .map(e -> e.getPosition())
                                    .collect(Collectors.toList());
        
        assertFalse(zombie.getPosition().equals(movedZombie.getPosition()));
        assertTrue(portalPos.contains(movedZombie.getPosition()));
    }

    @Test
    @DisplayName("Zombie does not push boulder")
    public void testBoulderRemains() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_zombieTest_doesNotPushBoulder", "c_movementTest_testMovementDown");
        EntityResponse zombie = TestUtils.getEntityById(resp, "zombie_toast");
        List<Position> originalboulderPos = new ArrayList<Position>();
        originalboulderPos.add(TestUtils.getEntityById(resp, "boulder").getPosition());
        originalboulderPos.add(TestUtils.getEntityById(resp, "boulder1").getPosition());
        originalboulderPos.add(TestUtils.getEntityById(resp, "boulder2").getPosition());
        originalboulderPos.add(TestUtils.getEntityById(resp, "boulder3").getPosition());

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "zombie_toast");
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
    @DisplayName("Zombie does not collect")
    public void testCollectablesRemains() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Just need a config which nothing spawns
        DungeonResponse resp = dmc.newGame(DIR_NAME + "d_zombieTest_overlapsPortal", "c_movementTest_testMovementDown");
        EntityResponse zombie = TestUtils.getEntityById(resp, "zombie_toast");
        int tresureNums = TestUtils.getEntities(resp, "treasure").size();

        resp = dmc.tick(Direction.RIGHT);
        EntityResponse movedZombie = TestUtils.getEntityById(resp, "zombie_toast");
        int newTresureNums = TestUtils.getEntities(resp, "treasure").size();
        
        assertFalse(zombie.getPosition().equals(movedZombie.getPosition()));
        assertTrue(tresureNums == newTresureNums);
    }

    @Test
    @DisplayName("Test zombie flees from player leftwards")
    public void testZombieFlee_LEFT() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_zombieTest_zombieFleesLeft", "c_msic_longPotionDuration_noDamage");

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getEntityById(res, "player").getPosition());
        assertTrue(TestUtils.getEntities(res, "invincibility_potion").size() == 0);

        assertDoesNotThrow(() -> dmc.tick("invincibility_potion"));

        res = dmc.tick(Direction.RIGHT);
        Position originalZombiePos = TestUtils.getEntityById(res, "zombie_toast").getPosition();

        for (int i = 1; i < 99; i++) {
            res = dmc.tick(Direction.RIGHT);
            assertTrue(originalZombiePos.translateBy(new Position(-i, 0)).equals(TestUtils.getEntityById(res, "zombie_toast").getPosition()) 
                || originalZombiePos.translateBy(new Position(-i - 1, 0)).equals(TestUtils.getEntityById(res, "zombie_toast").getPosition()));
        }
    }
}
