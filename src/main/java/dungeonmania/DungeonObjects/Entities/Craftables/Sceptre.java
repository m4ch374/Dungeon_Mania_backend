package dungeonmania.DungeonObjects.Entities.Craftables;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.response.models.ItemResponse;

public class Sceptre implements IEquipment {
    private final String id;

    public Sceptre(String id) {
        this.id = id;
    }

    @Override
    public ItemResponse toItemResponse() {
        return new ItemResponse(this.id, EntityTypes.SCEPTRE.toString());
    }

    @Override
    public String getId() {
        return this.id;
    }
}
