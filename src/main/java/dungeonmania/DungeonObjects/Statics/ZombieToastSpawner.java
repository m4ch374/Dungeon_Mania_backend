package dungeonmania.DungeonObjects.Statics;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interfaces.ISpawnable;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.response.models.EntityResponse;

public class ZombieToastSpawner extends Entity implements IStaticInteractable, ISpawnable {

    @Override
    public EntityResponse toEntityResponse() { return null; }

    @Override
    public void spawn() {}

    @Override
    public void interactedBy(Entity interactor) {}
    
}
