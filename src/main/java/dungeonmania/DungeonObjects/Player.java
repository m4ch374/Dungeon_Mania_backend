package dungeonmania.DungeonObjects;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.DungeonFactory.EntityStruct;

import java.util.List;

import org.json.JSONObject;

import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.DungeonObjects.Entities.Collectables.Bomb;
import dungeonmania.DungeonObjects.Entities.Collectables.InvincibilityPotion;
import dungeonmania.DungeonObjects.Entities.Collectables.InvisibilityPotion;
import dungeonmania.DungeonObjects.Entities.Collectables.Key;
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

    public Position getPos() {
        return getMap().getEntityPos(this);
    }

    public boolean isDead() {
        return (this.health <= 0);
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
            if (userCloseActiveSwitch(x, y)) {
                getMap().destroyInRange(new Position(x, y), bomb.getBombRadius());
            } else {
                getMap().placeEntityAt(bomb, new Position(x, y));
            }
        }
    }

    private boolean ableToMove(Position destination) {
        List<Entity> inCell = getMap().getEntitiesAt(destination);

        boolean move = true;

        for (Entity entity : inCell) {
            if (entity instanceof IStaticInteractable) {
                // TODO complete static entity interation here
                move = false;
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
        int a = 0;
        int b = 0;
        int x = getPos().getX();
        int y = getPos().getY();

        switch (direction) {
            case UP:
                a = -1;
                break;
            case DOWN:
                a = 1;
                break;
            case LEFT:
                b = -1;
                break;
            case RIGHT:
                b = 1;
                break;
        }

        Position destination = new Position(x + a, y + b);

        if (ableToMove(destination)) {
            getMap().moveEntityTo(this, destination);
        } else {
            throw new InvalidActionException("ERROR: Can not move " + direction);
        }
    }

    // TODO for the man in charge of battle
    public void initiateBattle() {}

    /* TODO for each iterable entirety (wall, spider...) there will be a unique method, deal with the effects on the player */

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
