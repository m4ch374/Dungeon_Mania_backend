package dungeonmania.DungeonObjects;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

import java.util.List;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.DungeonMap.DungeonMap;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Collectables.Bomb;
import dungeonmania.DungeonObjects.Entities.Collectables.Key;
import dungeonmania.DungeonObjects.Entities.Statics.Wall;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IMovingStrategy;
import dungeonmania.Interfaces.IStaticInteractable;
import dungeonmania.exceptions.InvalidActionException;

public class Player extends Entity {

    private int x;
    private int y;

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

    // TODO don know how this work yet
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

    public boolean isDead() {
        return (this.health <= 0);
    }

    private void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isInvincible() {
        return (this.InvincibilityRemainingTime > 0);
    }

    public boolean isInvisible() {
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

    public double getAttackDamage() {
        double ad = this.attackDamage;

        if (holdingSword()) ad += this.sword_attack;

        if (holdingBow()) ad *= 2;

        ad += this.allyNum * this.allyAttackBonous;

        return ad;
    }

    public void attackedBy(double ad) {
        int defence = 0;

        defence += this.allyNum * this.allyDefenceBonous;

        if (holdingShield()) defence = this.shield_defence;

        this.health -= ((ad - defence) / 5);
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

    public void useItem(String type) throws InvalidActionException {
        if (type.equals(EntityTypes.INVINCIBILITY_POTION.toString())) {
            backpack.useInvincibility();
            this.InvincibilityRemainingTime += this.invincibility_potion_duration;
        } else if (type.equals(EntityTypes.INVISIBILITY_POTION.toString())) {
            backpack.useInvisibility();
            this.InvisibilityRemainingTime += this.invisibility_potion_duration;
        } else if (type.equals(EntityTypes.BOMB.toString())) {
            Bomb bomb = backpack.useBomb();
            getMap().placeEntityAt(bomb, new Position(x, y));
        } else if (type.equals(EntityTypes.SWORD.toString())) {
            backpack.useSword();
        } else if (type.equals(EntityTypes.BOW.toString())) {
            backpack.useBow();
        } else if (type.equals(EntityTypes.SHIELD.toString())) {
            backpack.useShield();
        } else {
            throw new InvalidActionException("Can not use this item");
        }
    }

    private boolean ableToMove(Position destination) {
        List<Entity> inCell = getMap().getEntitiesAt(destination);

        boolean move = true;

        for (Entity entity : inCell) {
            if (entity instanceof IStaticInteractable) {
                // TODO complete static entity interation here
                break;
            }

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
        }

        return move;
    }

    public void move(Direction direction) throws InvalidActionException {
        // TODO get destination
        // if (ableToMove(direction)) {
        //     switch (direction) {
        //         case LEFT:
                    
        //             break;
        //         case RIGHT:
                    
        //             break;
        //         case UP:
                    
        //             break;
        //         case DOWN:
                    
        //             break;
        //     }
        // } else {
        //     throw new InvalidActionException("ERROR: Can not move " + direction);
        // }
    }

    // TODO for the man in charge of battle
    public void initiateBattle() {}

    // This is a template that can be changed by whoever is responsible for static entity interactions
    public void openDoor(int key) throws InvalidActionException {
        if (!backpack.hasAKey() || backpack.hasKey(key)) {
            throw new InvalidActionException("Can not open the door");
        } else {
            backpack.useKey();
            // go in the door
        }
    }

    // TODO This is a template that can be changed by whoever is responsible for static entity interactions
    public void goInPortal(int x, int y) throws InvalidActionException {
        if (getMap().getEntitiesAt(new Position(x, y)) instanceof Wall) {
            throw new InvalidActionException("Can not go in th portal");
        } else {
            // to somewhere
        }
    }
}
