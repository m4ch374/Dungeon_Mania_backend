package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SpiderMovementTest {
    private static final String DIR_NAME = "d_SpiderTests/";

    @Test
    @DisplayName("Test spider circulate spawn")
    public void basicMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), TestUtils.getEntities(res, "spider").get(0).getPosition());
            
            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test spider moves through all statics except Boulder")
    public void testMovesThroughAllExcptBoulder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_spiderTest_movesThroughAllStatics", "c_spiderTest_basicMovement");
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();
        
        int numEntities = res.getEntities().size();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), TestUtils.getEntities(res, "spider").get(0).getPosition());
            
            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }

            assertEquals(numEntities, res.getEntities().size());
        }
    }

    @Test
    @DisplayName("Test spider would change direction on hitting boulder")
    public void testReverseOnBoulder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_spiderTest_reverseOnBoulder", "c_spiderTest_basicMovement");
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        Position boulderPos = TestUtils.getEntityById(res, "boulder").getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x-1, y-1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 35; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), TestUtils.getEntities(res, "spider").get(0).getPosition());
            assertEquals(boulderPos, TestUtils.getEntityById(res, "boulder").getPosition());
            
            nextPositionElement++;
            if (nextPositionElement == movementTrajectory.size()){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Another test for spider would change direction on hitting boulder")
    public void testReverseOnBoulder_anotherTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_spiderTest_reverseOnBoudler_extra", "c_spiderTest_basicMovement");
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        Position boulderPos = TestUtils.getEntityById(res, "boulder").getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x-1, y-1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x, y+1));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x+1  , y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x, y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 35; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), TestUtils.getEntities(res, "spider").get(0).getPosition());
            assertEquals(boulderPos, TestUtils.getEntityById(res, "boulder").getPosition());
            
            nextPositionElement++;
            if (nextPositionElement == movementTrajectory.size()){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test spider would not be affected if the initial phase of the movement have a boulder")
    public void testIgnoreBoulderOnFirstStep() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_spiderTest_ignoreBoulderAtFirst", "c_spiderTest_basicMovement");
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        Position boulderPos = TestUtils.getEntityById(res, "boulder").getPosition();

        int x = pos.getX();
        int y = pos.getY();

        res = dmc.tick(Direction.UP);
        assertEquals(TestUtils.getEntityById(res, "spider").getPosition(), new Position(x, y - 1));
        assertEquals(TestUtils.getEntityById(res, "boulder").getPosition(), boulderPos);

        // trajectory after initial phase
        List<Position> movementTrajectory = new ArrayList<Position>();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x+1, y));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 35; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), TestUtils.getEntities(res, "spider").get(0).getPosition());
            assertEquals(boulderPos, TestUtils.getEntityById(res, "boulder").getPosition());
            
            nextPositionElement++;
            if (nextPositionElement == movementTrajectory.size()){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test spawn on boulder will have no effect on trajectory")
    public void testSpawnOnBoulderNoEffect() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(DIR_NAME + "d_spiderTest_spawnOnBoulderNoEffect", "c_spiderTest_basicMovement");
        Position pos = TestUtils.getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), TestUtils.getEntities(res, "spider").get(0).getPosition());
            
            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }
}
