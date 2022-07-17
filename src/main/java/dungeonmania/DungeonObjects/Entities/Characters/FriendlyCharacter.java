package dungeonmania.DungeonObjects.Entities.Characters;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.MovingStrategies.AllyMoveStrat;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.response.models.RoundResponse;

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

    // @Override
    public RoundResponse battleWith(Entity opponent) {
        // Porbably would need this in milestone 3 so imma keep it
        return null;
    }

    // @Override
    public void death() {
        // Porbably would need this in milestone 3 so imma keep it
    }

    // @Override
    public String getClasString() {
        // Porbably would need this in milestone 3 so imma keep it
        return "deez";
    }
    
    // @Override
    public double getAttackDamage() {
        // Porbably would need this in milestone 3 so imma keep it
        return 0.0;
    }
    
    // @Override
    public double getHealth() {
        // Porbably would need this in milestone 3 so imma keep it
        return 0.0;
    }
    
}
