package dungeonmania.DungeonObjects;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Collectables.Bomb;
import dungeonmania.DungeonObjects.Entities.Collectables.InvincibilityPotion;
import dungeonmania.DungeonObjects.Entities.Collectables.InvisibilityPotion;
import dungeonmania.DungeonObjects.Entities.Collectables.Key;
import dungeonmania.DungeonObjects.Entities.Statics.FloorSwitch;
import dungeonmania.DungeonObjects.Entities.Statics.Boulder;
import dungeonmania.DungeonObjects.Entities.Statics.Wall;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;

public class Player extends Entity {

    private int health;
    private int attackDamage;

    private int sword_attack;
    private int shield_defence;
    private int invincibility_potion_duration;
    private int invisibility_potion_duration;

    private int allyNum;
    private int allyAttackBonous;
    private int allyDefenceBonous;

    private double InvincibilityRemainingTime = 0;
    private double InvisibilityRemainingTime = 0;

    private Backpack backpack;

    // TODO dont know how this work yet
    private IMovingStrategy moveStrat;

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

    private double getAttackDamage() {
        double ad = this.attackDamage;

        if (holdingSword()) {
            ad += this.sword_attack;
            backpack.useEquipment(EntityTypes.SWORD.toString());
        }

        if (holdingBow()) {
            ad *= 2;
            backpack.useEquipment(EntityTypes.BOW.toString());
        }

        ad += this.allyNum * this.allyAttackBonous;

        return ad;
    }

    public void attackedBy(double ad) {
        int defence = 0;

        defence += this.allyNum * this.allyDefenceBonous;

        if (holdingShield()) {
            defence = this.shield_defence;
            backpack.useEquipment(EntityTypes.SHIELD.toString());
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

    public void make(String type) throws InvalidActionException {
        backpack.make(type);
    }

    private boolean playerCloseActiveSwitch(int x, int y) {
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
    public void useItem(String itemUsedId) throws InvalidActionException {
        IEquipment item = backpack.useItem(itemUsedId);

        if (item instanceof InvincibilityPotion) {
            this.InvincibilityRemainingTime += this.invincibility_potion_duration;
        } else if (item instanceof InvisibilityPotion) {
            this.InvisibilityRemainingTime += this.invisibility_potion_duration;
        } else if (item instanceof Bomb) {
            Bomb bomb = (Bomb) item;
            int x = getPos().getX();
            int y = getPos().getY();
            if (playerCloseActiveSwitch(x, y)) {
                bomb.activate(new Position(x, y));
            } else {
                getMap().placeEntityAt(bomb, new Position(x, y));
            }
        }
    }

    private boolean ableToMove(Position destination) {
        ArrayList<Entity> inCell = new ArrayList<Entity>();
        inCell.addAll(getMap().getEntitiesAt(destination));

        boolean move = true;

        for (Entity entity : inCell) {
            if (entity instanceof ICollectable) {
                if (entity instanceof Bomb) {
                    Bomb bomb = (Bomb) entity;
                    if (!bomb.isCollectible()) {
                        move = false;
                        break;
                    } else {
                        bomb.collectedBy(this);
                    }
                } else {
                    ICollectable collection = (ICollectable) entity;
                    collection.collectedBy(this);
                }
            }

            if (entity instanceof IStaticInteractable) {
                if (entity instanceof Boulder) {
                    Boulder boulder = (Boulder) entity;
                    Position boulderPos1 = this.getMap().getEntityPos(boulder);
                    boulder.interactedBy(this);
                    // Need to let Player know whether boulder has moved or not
                    // IFF positons have changed, boulder's moved, then Player can move!
                    Position boulderPos2 = this.getMap().getEntityPos(boulder);
                    if (!boulderPos1.equals(boulderPos2)) {
                        move = false;
                        break;
                    }

                }
                // TODO add more here
                move = false;
                break;
            }
        }

        return move;
    }

    public void move(Direction direction) throws InvalidActionException {
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

        Position destination = new Position(x, y);

        if (ableToMove(destination)) {
            getMap().moveEntityTo(this, destination);
        } else {
            throw new InvalidActionException("ERROR: Can not move " + direction);
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

    /* TODO for each iterable entirety (door, portal...) there will be a unique method, deal with the effects on the player */

    // This is a template that can be changed by whoever is responsible for static entity interactions
    public void openDoor(int key) throws InvalidActionException {
        if (!backpack.hasAKey() || backpack.hasKey(key)) {
            throw new InvalidActionException("Can not open the door");
        } else {
            backpack.useItem(EntityTypes.KEY.toString());
            // go in the door
        }
    }

    // This is a template that can be changed by whoever is responsible for static entity interactions
    public void goInPortal(int x, int y) throws InvalidActionException {
        if (getMap().getEntitiesAt(new Position(x, y)) instanceof Wall) {
            throw new InvalidActionException("Can not go in th portal");
        } else {
            // to somewhere
        }
    }
}
