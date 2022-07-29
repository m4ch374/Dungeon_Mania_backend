package dungeonmania.DungeonObjects.Entities.Statics;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.util.DungeonFactory.EntityStruct;

// Not implementing IStaticInteractable
//
// Results of technical debt, unlucky, unfortunate....
public class SwampTile extends Entity {

    private int movementFactor;
    Map<Entity, Integer> trapMap = new HashMap<Entity, Integer>();

    public SwampTile(EntityStruct metaData, int movementFactor) {
        super(metaData);
        this.movementFactor = movementFactor;
    }

    public int getMovementFactor() {
        return movementFactor;
    }
    
    public boolean ableToMove(Entity movingEntity) {
        if (!(movingEntity instanceof IEnemy))
            return true;

        trapMap.putIfAbsent(movingEntity, movementFactor);
        
        int entityTrapDuration = trapMap.get(movingEntity);
        if (entityTrapDuration == 0) {
            trapMap.remove(movingEntity);
            return true;
        }

        trapMap.replace(movingEntity, entityTrapDuration - 1);
        return false;
    }
}
