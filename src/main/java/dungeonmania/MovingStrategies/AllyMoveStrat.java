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
    private Player following;
    private Position followingPrevPos;

    private DungeonMap map;
    
    public AllyMoveStrat(Entity mover, DungeonMap map) {
        this.mover = mover;
        this.map = map;

        following = map.getAllEntities().stream()
                    .filter(e -> e.getType().equals(EntityTypes.PLAYER.toString()))
                    .map(e -> (Player) e)
                    .findFirst()
                    .get();

        followingPrevPos = map.getEntityPos(following);
    }

    @Override
    public Position moveEntity() {
        Position followingCurrPos = map.getEntityPos(following);
        
        // Move only if ally does not overlap with player
        if (!followingCurrPos.equals(followingPrevPos)) {
            Position posToReturn = followingPrevPos;
            followingPrevPos = followingCurrPos;
            return posToReturn;
        }
        return map.getEntityPos(mover);
    }
    
}
