package dungeonmania.DungeonObjects.Entities.Characters.Friendlies;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.AllyMoveStrat;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class FriendlyCharacter extends Entity implements IMovable {

    private DungeonMap map = super.getMap();
    private IMovingStrategy moveStrat = new AllyMoveStrat(this, map);

    public FriendlyCharacter(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public EntityResponse toEntityResponse() {
        return new EntityResponse(super.getId(), super.getType(), map.getEntityPos(this), true);
    }

    // @Override
    public void move() {
        Position posToMove = moveStrat.moveEntity();
        map.moveEntityTo(this, posToMove);
    }
    
}
