package dungeonmania.MovingStrategies;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.util.Position;

// NOTE: This movestrat only supports following a player
// unlike other movestrats that could support every entity
public class AllyMoveStrat implements IMovingStrategy {

    private Entity mover;
    private Player following = null;

    private DungeonMap map;
    
    public AllyMoveStrat(Entity mover, DungeonMap map) {
        this.mover = mover;
        this.map = map;
    }

    @Override
    public void moveEntity() {
        if (following == null)
            following = map.getAllEntities().stream()
                        .filter(e -> e.getType().equals(EntityTypes.PLAYER.toString()))
                        .map(e -> (Player) e)
                        .findFirst()
                        .get();

        Position followingCurrPos = (Position) following.getState().get("currentPosition");
        Position followingPrevPos = (Position) following.getState().get("previousPosition");

        // Move only if ally does not overlap with player
        if (!followingCurrPos.equals(followingPrevPos))
            map.moveEntityTo(mover, followingPrevPos);
    }
    
}
