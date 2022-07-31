package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.DungeonFactory.EntityStruct;

// TODO: stubbed
public class TimeTravellingPortal extends Entity implements IStaticInteractable{

    public TimeTravellingPortal(EntityStruct metaData) {
        super(metaData);
        //TODO Auto-generated constructor stub
    }
    
    
    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException {}
}
