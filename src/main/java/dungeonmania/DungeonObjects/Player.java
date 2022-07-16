package dungeonmania.DungeonObjects;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Collectables.Bomb;
import dungeonmania.DungeonObjects.Entities.Collectables.InvincibilityPotion;
import dungeonmania.DungeonObjects.Entities.Collectables.InvisibilityPotion;
import dungeonmania.DungeonObjects.Entities.Collectables.Key;
import dungeonmania.DungeonObjects.Entities.Statics.FloorSwitch;
import dungeonmania.DungeonObjects.Entities.Statics.Portal;
import dungeonmania.DungeonObjects.Entities.Statics.Boulder;
import dungeonmania.DungeonObjects.Entities.Statics.Door;
import dungeonmania.DungeonObjects.Entities.Statics.Wall;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;

public class Player extends Entity {

    private int health;
    private final int attackDamage;

    private final int sword_attack;
    private final int shield_defence;
    private final int invincibility_potion_duration;
    private final int invisibility_potion_duration;

    private int allyNum;
    private final int allyAttackBonous;
    private final int allyDefenceBonous;

    private double InvincibilityRemainingTime = 0;
    private double InvisibilityRemainingTime = 0;

    private final Backpack backpack;

    public Player(EntityStruct metaData, JSONObject config) {
        super(metaData);
        this.health = config.getInt("player_health");
        this.attackDamage = config.getInt("player_attack");

        this.sword_attack = config.getInt("sword_attack");
        this.shield_defence = config.getInt("shield_defence");
        this.invincibility_potion_duration = config.getInt("invincibility_potion_duration");
        this.invisibility_potion_duration = config.getInt("invisibility_potion_duration");

        // ally attack & defence not found in config file
        this.allyAttackBonous = config.has("ally_attack") ? config.getInt("ally_attack") : 0;
        this.allyDefenceBonous = config.has("ally_defence") ? config.getInt("ally_defence") : 0;

        this.backpack = new Backpack(config.getInt("bow_durability"), config.getInt("shield_durability"));
    }

    public List<ItemResponse> getPlayerItems() {
        return backpack.getItemResponse();
    }

    public List<String> getBuildables() {
        return backpack.getBuildables();
    }

    private Position getPos() {
        return getMap().getEntityPos(this);
    }

    private boolean isDead() {
        return (this.health <= 0);
    }

    private boolean isInvincible() {
        return (this.InvincibilityRemainingTime > 0);
    }

    private boolean isInvisible() {
        return (this.InvisibilityRemainingTime > 0);
    }

    private boolean holdingSword() {
        return backpack.hasSword();
    }

    private boolean holdingBow() {
        return backpack.hasBow();
    }

    private boolean holdingShield() {
        return backpack.hasShield();
    }

    public void useEquipment(String type) {
        backpack.useEquipment(type);
    }

    private double getAttackDamage() {
        double ad = this.attackDamage;

        if (holdingSword()) { ad += this.sword_attack; }

        if (holdingBow()) { ad *= 2; }

        ad += this.allyNum * this.allyAttackBonous;

        return ad;
    }

    public void attackedBy(double ad) {
        int defence = 0;

        defence += this.allyNum * this.allyDefenceBonous;

        if (holdingShield()) {
            defence = this.shield_defence;
            useEquipment(EntityTypes.SHIELD.toString());
        }

        this.health -= ((ad - defence) / 5);

        if (isDead()) {
            getMap().removeEntity(this);
        }
    }

    public void collect(ICollectable item) throws InvalidActionException {
        if (item instanceof Key && backpack.hasAKey()) {
            throw new InvalidActionException("Already has a key");
        }

        if (item instanceof Bomb) {
            Bomb bomb = (Bomb) item;
            if (!bomb.isCollectible()) {
                throw new InvalidActionException("Cannot collect a dropped bomb");
            }
        }

        backpack.addItem(item);
    }

    private void updatePotions() {
        // update potion state
        if (isInvisible()) {
            this.InvincibilityRemainingTime -= 1;
        }

        if (isInvisible()) {
            this.InvisibilityRemainingTime -= 1;
        }
    }

    private void make(String type) throws InvalidActionException, IllegalArgumentException {
        backpack.make(type);
    }

    private boolean playerCloseActiveSwitch(Position pos) {
        int x = pos.getX();
        int y = pos.getY();

        Position up = new Position(x - 1, y);
        Position down = new Position(x + 1, y);
        Position left = new Position(x, y - 1);
        Position right = new Position(x, y + 1);

        List<Entity> entityList = new ArrayList<Entity>();
        entityList.addAll(getMap().getEntitiesAt(up));
        entityList.addAll(getMap().getEntitiesAt(down));
        entityList.addAll(getMap().getEntitiesAt(left));
        entityList.addAll(getMap().getEntitiesAt(right));

        boolean activeSwitch = entityList
                                .stream()
                                .filter(e -> e.getType().equals(EntityTypes.FLOOR_SWITCH.toString()))
                                .map(e -> (FloorSwitch) e)
                                .anyMatch(e -> e.isActive());

        return activeSwitch;
    }

    // do not use this for bribe mercenaries
    private void useItem(String itemUsedId) throws InvalidActionException, IllegalArgumentException {
        IEquipment item = backpack.useItem(itemUsedId);

        if (item instanceof InvincibilityPotion) {
            this.InvincibilityRemainingTime += this.invincibility_potion_duration;
        } else if (item instanceof InvisibilityPotion) {
            this.InvisibilityRemainingTime += this.invisibility_potion_duration;
        } else if (item instanceof Bomb) {
            Bomb bomb = (Bomb) item;
            Position pos = getPos();
            if (playerCloseActiveSwitch(pos)) {
                bomb.activate(pos);
            } else {
                getMap().placeEntityAt(bomb, pos);
            }
        }
    }

    private boolean ableToMove(Position destination) {
        List<Entity> inCell = getMap().getEntitiesAt(destination);

        List<ICollectable> collections = inCell
                                        .stream()
                                        .filter(e -> (e instanceof ICollectable))
                                        .map(e -> (ICollectable) e)
                                        .collect(Collectors.toList());

        // check if a fixed bomb exist, exclude interaction
        Boolean hasFixedBomb = collections
                        .stream()
                        .filter(e -> (e instanceof Bomb))
                        .map(e -> (Bomb) e)
                        .anyMatch(e -> !e.isCollectible());

        if (hasFixedBomb) return false;

        List<IStaticInteractable> staticEntity = inCell
                                                .stream()
                                                .filter(e -> (e instanceof IStaticInteractable))
                                                .map(e -> (IStaticInteractable) e)
                                                .collect(Collectors.toList());

        // check if a static entity blocking the player, include door interaction
        for (IStaticInteractable entity : staticEntity) {
            try {
                if (entity instanceof Wall) {
                    return false;
                } else if (entity instanceof Door) {
                    Door door = (Door) entity;
                    if (!door.isOpen()) {
                        door.interactedBy(this);
                    }
                }
            } catch (InvalidActionException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        // if nothing else block player, check if player can push the boulder
        for (IStaticInteractable entity : staticEntity) {
            /* try {
                if (entity instanceof Boulder) {
                    Boulder boulder = (Boulder) entity;
                    Position boulderPos1 = this.getMap().getEntityPos(boulder);
                    boulder.interactedBy(this);
                    // Need to let Player know whether boulder has moved or not
                    // IFF positons have changed, boulder's moved, then Player can move!
                    Position boulderPos2 = this.getMap().getEntityPos(boulder);
                    // TODO change to use exception
                    if (boulderPos1.equals(boulderPos2)) {
                        return false;
                    }
                }
            } catch (InvalidActionException e) {
                System.out.println(e.getMessage());
                return false;
            } */
        }

        return true;
    }

    private void interactWithOverlapCollections(Position destination) throws InvalidActionException {
        List<Entity> inCell = getMap().getEntitiesAt(destination);

        List<ICollectable> collections = inCell
                                        .stream()
                                        .filter(e -> (e instanceof ICollectable))
                                        .map(e -> (ICollectable) e)
                                        .collect(Collectors.toList());

        // interaction with collections
        for (ICollectable collection : collections) {
            try {
                collection.collectedBy(this);
            } catch (InvalidActionException e) {
                System.out.println(e.getMessage());

                // Inactive bombs will block players as a wall
                if (collection instanceof Bomb) {
                    throw new InvalidActionException("ERROR: Unpickable bomb, should never happened");
                }
            }
        }
    }

    private void move(Position destination) throws InvalidActionException {
        // Check if something is blocking the player
        if (ableToMove(destination)) {
            // move the player
            getMap().moveEntityTo(this, destination);

            // deal with interaction of collections
            interactWithOverlapCollections(destination);

            // deal with interaction of overlapped portal
            Portal portal = getOverlapPortal();
            if (portal != null) {
                try {
                    // TODO get new destination
                    Position direction_new = portal.getDestination();
                    move(direction_new);
                } catch (InvalidActionException e) {
                    // nothing here, just let the player overlap with portal without teleport
                    // this structure will allow player go in portal as much as possible
                    // and player will stop at the portal which he cannot goes in
                }
            }
        }
    }

    private Portal getOverlapPortal() {
        Position pos = getMap().getEntityPos(this);
        List<Entity> inCell = getMap().getEntitiesAt(pos);
        List<Portal> portal = inCell
                                .stream()
                                .filter(e -> (e instanceof Portal))
                                .map(e -> (Portal) e)
                                .collect(Collectors.toList());

        if (portal.size() == 0) return null;

        // If multiple portals overlap, always take the first one, it may be random, but it doesn't matter
        return portal.get(0);
    }

    public void tick(String action, Direction direction, String str) throws InvalidActionException, IllegalArgumentException {
        if (action.equals(Constant.PLAYERUSE)) {
            useItem(str);
            updatePotions();
        } else if (action.equals(Constant.PLAYERMAKE)) {
            make(str);
            updatePotions();
        } else if (action.equals(Constant.PLAYERMOVE)) {
            int x = getPos().getX();
            int y = getPos().getY();

            switch (direction) {
                case UP:
                    y -= 1;
                    break;
                case DOWN:
                    y += 1;
                    break;
                case LEFT:
                    x -= 1;
                    break;
                case RIGHT:
                    x += 1;
                    break;
            }

            Position position = new Position(x, y);

            move(position);
            updatePotions();
        } else {
            throw new IllegalArgumentException("ERROR: Undefined player behavior");
        }
    }

    // used for bribe mercenaries
    public void bribe(int quantity) throws InvalidActionException {
        try {
            backpack.useTreasures(quantity);
            this.allyNum += 1;
        } catch (InvalidActionException e) {
            throw e;
        }
    }

    public HashMap<String, Object> getState() {
        HashMap<String, Object> state = new HashMap<String, Object>();

        state.put("health", this.health);               // double
        state.put("invincible", isInvincible());        // boolean
        state.put("invisible", isInvisible());          // boolean
        state.put("dead", isDead());                    // boolean
        state.put("sword", holdingSword());             // boolean
        state.put("bow", holdingBow());                 // boolean
        state.put("shield", holdingShield());           // boolean
        state.put("attackDamage", getAttackDamage());   // double

        return state;
    }

    // TODO for the man in charge of battle
    public void initiateBattle() {}

    public void openDoor(int key) throws InvalidActionException {
        if (!backpack.hasAKey() || backpack.hasKey(key)) {
            throw new InvalidActionException("Can not open the door");
        } else {
            backpack.useKey();
        }
    }
}
