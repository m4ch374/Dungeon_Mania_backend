package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Direction;

public class Boulder extends Entity implements IStaticInteractable {

    public Boulder(EntityStruct metaData) {
        super(metaData);
    }

    @Override
    public void interactedBy(Entity interactor) { 
        // // move() IFF interactor is of type Player i.e.
        // // If interactor is of TYPE BOULDER, then DO NOT move()

        // Check if the next position's cell is movable onto i.e whether theres a wall there, merc etc
        // via getAllEntitiesInCell() in DungeonCell.java


        // move(DIRECTION)
        // // direction of movement can be determined by where interactor came from, viasimple maths
        // // e.g. if (this.PosX == interactorPosX - 1), then move(RIGHT) 
    }

    public void move(Direction direction) {}
    
}
