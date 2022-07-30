package dungeonmania.DungeonObjects.Entities.Characters.Friendlies;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

public class MindControlledCharacter extends FriendlyCharacter {

    int remainingDuration;

    // Is that even a word?
    Entity controllee;
    DungeonMap map;

    public MindControlledCharacter(EntityStruct metaData, JSONObject config, Entity controllee, DungeonMap map) {
        super(metaData);
        this.remainingDuration = config.getInt("mind_control_duration");
        this.controllee = controllee;
        this.map = map;
    }

    @Override
    public void move() {
        remainingDuration--;
        super.move();

        if (remainingDuration == 0) {
            Position currPos = map.getEntityPos(this);
            map.removeEntity(this);
            map.placeEntityAt(controllee, currPos);
        }
    }
    
}
