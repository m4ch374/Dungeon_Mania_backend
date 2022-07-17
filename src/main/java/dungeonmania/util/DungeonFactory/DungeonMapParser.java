package dungeonmania.util.DungeonFactory;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.DungeonObjects.EntityTypes;
import dungeonmania.DungeonObjects.Player;
import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Characters.*;
import dungeonmania.DungeonObjects.Entities.Collectables.*;
import dungeonmania.DungeonObjects.Entities.Statics.*;
import dungeonmania.util.Position;
import dungeonmania.util.Tracker;

public class DungeonMapParser {
    public static DungeonMap buildDungeonMap(JSONArray entities, JSONObject config, Tracker tracker) {
        ParserUtil util = ParserUtil.getUtil(entities);
        DungeonMap map = new DungeonMap(util.getTopLeft(), util.getBottomRight());
        Map<String, Integer> idMap = new HashMap<String, Integer>();

        for (int i = 0; i < entities.length(); i++) {
            JSONObject entityJson = entities.getJSONObject(i);
            Entity entity = buildEntity(entityJson, idMap, map, config, tracker);
            
            Position entityPos = new Position(entityJson.getInt("x"), entityJson.getInt("y"));
            map.placeEntityAt(entity, entityPos);
        }

        return map;
    }

    private static Entity buildEntity(JSONObject entityJson, Map<String, Integer> idMap, DungeonMap map, JSONObject config, Tracker tracker) {
        String entityType = entityJson.getString("type");
        String entityId = getEntityId(idMap, entityType);
        EntityStruct metaData = new EntityStruct(entityId, entityType, map);

        // Switch case, better than if else at least
        switch(EntityTypes.lookupEnum(entityType)) {
            case PLAYER:
                return new Player(metaData, config, tracker);
            case WALL:
                return new Wall(metaData);
            case EXIT:
                return new Exit(metaData, tracker);
            case BOULDER:
                return new Boulder(metaData);
            case FLOOR_SWITCH:
                return new FloorSwitch(metaData);
            case DOOR:
                int pairedKey = entityJson.getInt("key");
                return new Door(metaData, pairedKey);
            case PORTAL:
                String portalColour = entityJson.getString("colour");
                return new Portal(metaData, portalColour);
            case ZOMBIE_TOAST_SPAWNER:
                return new ZombieToastSpawner(metaData, config, tracker);
            case SPIDER:
                return new Spider(metaData, config, tracker);
            case ZOMBIE_TOAST:
                return new ZombieToast(metaData, config, tracker);
            case MERCENARY:
                return new Mercenary(metaData, config, tracker);
            case TREASURE:
                return new Treasure(metaData);
            case KEY:
                int keyId = entityJson.getInt("key");
                return new Key(metaData, keyId);
            case INVINCIBILITY_POTION:
                return new InvincibilityPotion(metaData, config);
            case INVISIBILITY_POTION:
                return new InvisibilityPotion(metaData, config);
            case WOOD:
                return new Wood(metaData);
            case ARROWS:
                return new Arrow(metaData);
            case BOMB:
                return new Bomb(metaData, config);
            case SWORD:
                return new Sword(metaData, config);
            default:
                return null;
        }
    }

    private static String getEntityId(Map<String, Integer> idMap, String type) {
        idMap.putIfAbsent(type, 0);
        int currId = idMap.get(type);
        String entityId = currId == 0 ? type : type + String.valueOf(currId);
        idMap.put(type, ++currId);
        return entityId;
    }
}

class ParserUtil {
    private final Position topLeft;
    private final Position bottomRight;

    private ParserUtil(int leftMost, int rightMost, int topMost, int bottomMost) {
        topLeft = new Position(leftMost, topMost);
        bottomRight = new Position(rightMost, bottomMost);
    }

    public final Position getTopLeft() {
        return topLeft;
    }

    public final Position getBottomRight() {
        return bottomRight;
    }

    public static ParserUtil getUtil(JSONArray entityArray) {
        if (entityArray.length() == 0)
            return new ParserUtil(0, 0, 0, 0);

        // Set the first entity as all of the attributes
        JSONObject firstEntity = entityArray.getJSONObject(0);
        int maxLeft = firstEntity.getInt("x");
        int maxRight = firstEntity.getInt("x");
        int maxTop = firstEntity.getInt("y");
        int maxBottom = firstEntity.getInt("y");
        
        for (int i = 1; i < entityArray.length(); i++) {
            JSONObject currEntity = entityArray.getJSONObject(i);

            int currX = currEntity.getInt("x");
            int currY = currEntity.getInt("y");

            if (currX < maxLeft)
                maxLeft = currX;
            
            if (currX > maxRight)
                maxRight = currX;

            if (currY < maxTop)
                maxTop = currY;

            if (currY > maxBottom)
                maxBottom = currY;
        }

        return new ParserUtil(maxLeft, maxRight, maxTop, maxBottom);
    }
}
