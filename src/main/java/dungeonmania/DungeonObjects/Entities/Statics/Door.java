package dungeonmania.DungeonObjects.Entities.Statics;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.LogicEntities.LogicEntity;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class Door extends LogicEntity implements IStaticInteractable {
    private int keyId;
    private boolean isopen = false;

    public Door(EntityStruct metaData, int keyId) {
        super(metaData);
        this.keyId = keyId;
    }

    public Door(EntityStruct metaData, int keyId, String logic) {
        super(metaData, logic);
        this.keyId = keyId;
    }

    public boolean isOpen() {
        return this.isopen;
    }

    public void open() {
        this.isopen = true;
    }

    @Override
    public void interactedBy(Entity interactor) throws InvalidActionException {
        if (interactor instanceof Player) {
            Player player = (Player) interactor;
            player.openDoor(this.keyId);
            open();
        } else {
            throw new InvalidActionException("ERROR: Door cannot interact with this entity");
        }
    }

    public String getLogic() {
        return null;
    }
}
