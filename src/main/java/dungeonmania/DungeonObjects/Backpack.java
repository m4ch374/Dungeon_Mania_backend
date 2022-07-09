package dungeonmania.DungeonObjects;

import java.util.ArrayList;
import java.util.HashMap;

import dungeonmania.DungeonObjects.Entities.Collectables.Arrow;
import dungeonmania.DungeonObjects.Entities.Collectables.Bomb;
import dungeonmania.DungeonObjects.Entities.Collectables.Key;
import dungeonmania.DungeonObjects.Entities.Collectables.Sword;
import dungeonmania.DungeonObjects.Entities.Collectables.Treasure;
import dungeonmania.DungeonObjects.Entities.Collectables.Wood;
import dungeonmania.DungeonObjects.Entities.Collectables.InvincibilityPotion;
import dungeonmania.DungeonObjects.Entities.Collectables.InvisibilityPotion;
import dungeonmania.DungeonObjects.Entities.Craftables.Bow;
import dungeonmania.DungeonObjects.Entities.Craftables.Shield;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.exceptions.InvalidActionException;

public final class Backpack {

    private Key key = null;
    private int treasure = 0;
    private int InvincibilityPotion = 0;
    private int InvisibilityPotion = 0;
    private int Woods = 0;
    private int Arrows = 0;
    private ArrayList<Bomb> Bombs = new ArrayList<Bomb>();
    private ArrayList<Sword> Swords = new ArrayList<Sword>();

    private int bow_num = 0;
    private int shield_num = 0;
    private ArrayList<Bow> Bows = new ArrayList<Bow>();
    private ArrayList<Shield> Shields = new ArrayList<Shield>();

    private final int bow_durability;
    private final int shield_durability;

    public Backpack(int bow_durability, int shield_durability) {
        this.bow_durability = bow_durability;
        this.shield_durability = shield_durability;
    }

    public HashMap<String, Integer> getState() {
        HashMap<String, Integer> state = new HashMap<>();

        state.put(EntityTypes.TREASURE.toString(), this.treasure);
        if (this.key != null) state.put(EntityTypes.KEY.toString(), 1);
        else state.put(EntityTypes.KEY.toString(), 0);
        state.put(EntityTypes.INVINCIBILITY_POTION.toString(), this.InvincibilityPotion);
        state.put(EntityTypes.INVISIBILITY_POTION.toString(), this.InvisibilityPotion);
        state.put(EntityTypes.WOOD.toString(), this.Woods);
        state.put(EntityTypes.ARROWS.toString(), this.Arrows);
        state.put(EntityTypes.BOMB.toString(), this.Bombs.size());

        return state;
    }

    public void make(String type) throws InvalidActionException {
        if (type.equals(EntityTypes.BOW.toString())) {
            if (this.Woods >= 1 && this.Arrows >= 3) {
                useWoods(1);
                useArrows(3);
                String id = "user_bow_" + this.bow_num;
                this.Bows.add(new Bow(id, bow_durability));
                this.bow_num += 1;
            } else {
                throw new InvalidActionException("ERROR: Not Enough Material For " + type);
            }
        } else if (type.equals(EntityTypes.SHIELD.toString())) {
            if (this.Woods >= 2 && this.treasure >= 1) {
                useWoods(2);
                useTreasure(1);
                String id = "user_shield_" + this.shield_num;
                this.Shields.add(new Shield(id, shield_durability));
                this.shield_num += 1;
            } else if (this.Woods >= 2 && this.key != null) {
                useWoods(2);
                useKey();
                String id = "user_shield_" + this.shield_num;
                this.Shields.add(new Shield(id, shield_durability));
                this.shield_num += 1;
            } else {
                throw new InvalidActionException("ERROR: Not Enough Material For " + type);
            }
        } else {
            throw new InvalidActionException("ERROR: Can Not Make Item " + type);
        }
    }

    public void addItem(ICollectable item) throws InvalidActionException {
        if (item instanceof Treasure) {
            Treasure treasure = (Treasure) item;
            this.treasure += treasure.getQuantity();
        } else if (item instanceof Key) {
            this.key = (Key) item;
        } else if (item instanceof InvincibilityPotion) {
            this.InvincibilityPotion += 1;
        } else if (item instanceof InvisibilityPotion) {
            this.InvisibilityPotion += 1;
        } else if (item instanceof Wood) {
            Wood wood = (Wood) item;
            this.InvisibilityPotion += wood.getQuantity();
        } else if (item instanceof Arrow) {
            Arrow arrow = (Arrow) item;
                this.InvisibilityPotion += arrow.getQuantity();
        } else if (item instanceof Bomb) {
            this.Bombs.add((Bomb) item);
        } else if (item instanceof Sword) {
            this.Swords.add((Sword) item);
        } else {
            throw new InvalidActionException("ERROR: Can Not Collect Item ");
        }
    }

    public boolean hasAKey() {
        return (this.key != null);
    }

    public boolean hasKey(int key) {
        return (this.key.getKey() == key);
    }

    public void useKey() {
        this.key = null;
    }

    public void useTreasure(int quantity) {
        this.treasure -= quantity;
    }

    public void useWoods(int quantity) {
        this.Woods -= quantity;
    }

    public void useArrows(int quantity) {
        this.Arrows -= quantity;
    }

    public void useInvincibility() throws InvalidActionException {
        if (this.InvincibilityPotion == 0) {
            throw new InvalidActionException("Do Not Have Potion");
        }

        this.InvincibilityPotion -= 1;
    }

    public void useInvisibility() throws InvalidActionException {
        if (this.InvincibilityPotion == 0) {
            throw new InvalidActionException("Do Not Have Potion");
        }

        this.InvisibilityPotion -= 1;
    }

    public Bomb useBomb() throws InvalidActionException {
        if (this.Bombs.size() <= 0) {
            throw new InvalidActionException("Do Not Have Bomb");
        }

        Bomb bomb = this.Bombs.get(0);
        this.Bombs.remove(bomb);

        bomb.drop();

        return bomb;
    }

    public boolean hasSword() {
        return (this.Swords.size() >= 1);
    }

    public boolean hasBow() {
        return (this.Bows.size() >= 1);
    }

    public boolean hasShield() {
        return (this.Shields.size() >= 1);
    }

    public void useSword() throws InvalidActionException {
        if (this.Shields.size() == 0) {
            throw new InvalidActionException("Do Not Have A Sword");
        }

        Sword sword = this.Swords.get(0);

        sword.reduceDurability(1);

        if (sword.getDurability() <= 0) {
            this.Swords.remove(sword);
        }
    }

    public void useBow() throws InvalidActionException {
        if (this.Shields.size() == 0) {
            throw new InvalidActionException("Do Not Have A Bow");
        }

        Bow bow = this.Bows.get(0);

        bow.reduceDurability(1);

        if (bow.getDurability() <= 0) {
            this.Bows.remove(bow);
        }
    }

    public void useShield() throws InvalidActionException {
        if (this.Shields.size() == 0) {
            throw new InvalidActionException("Do Not Have A Shield");
        }

        Shield shield = this.Shields.get(0);

        shield.reduceDurability(1);

        if (shield.getDurability() <= 0) {
            this.Shields.remove(shield);
        }
    }
}
