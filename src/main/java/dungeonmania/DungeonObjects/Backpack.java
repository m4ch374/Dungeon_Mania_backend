package dungeonmania.DungeonObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;

public final class Backpack {

    private Key key = null;
    private ArrayList<Treasure> treasure = new ArrayList<Treasure>();
    private ArrayList<InvincibilityPotion> InvincibilityPotion = new ArrayList<InvincibilityPotion>();
    private ArrayList<InvisibilityPotion> InvisibilityPotion = new ArrayList<InvisibilityPotion>();
    private ArrayList<Wood> Woods = new ArrayList<Wood>();
    private ArrayList<Arrow> Arrows = new ArrayList<Arrow>();
    private ArrayList<Bomb> Bombs = new ArrayList<Bomb>();
    private ArrayList<Sword> Swords = new ArrayList<Sword>();

    private int bow_idx = 0;
    private int shield_idx = 0;
    private ArrayList<Bow> Bows = new ArrayList<Bow>();
    private ArrayList<Shield> Shields = new ArrayList<Shield>();

    private final int bow_durability;
    private final int shield_durability;

    public Backpack(int bow_durability, int shield_durability) {
        this.bow_durability = bow_durability;
        this.shield_durability = shield_durability;
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

    public List<ItemResponse> getItemResponse() {
        ArrayList<ItemResponse> items = new ArrayList<ItemResponse>();

        if (key != null) items.add(key.toItemResponse());
        treasure.stream().forEach(e -> items.add(e.toItemResponse()));
        InvincibilityPotion.stream().forEach(e -> items.add(e.toItemResponse()));
        InvisibilityPotion.stream().forEach(e -> items.add(e.toItemResponse()));
        Woods.stream().forEach(e -> items.add(e.toItemResponse()));
        Arrows.stream().forEach(e -> items.add(e.toItemResponse()));
        Bombs.stream().forEach(e -> items.add(e.toItemResponse()));
        Swords.stream().forEach(e -> items.add(e.toItemResponse()));
        Bows.stream().forEach(e -> items.add(e.toItemResponse()));
        Shields.stream().forEach(e -> items.add(e.toItemResponse()));

        return items;
    }

    public List<String> getBuildables() {
        ArrayList<String> Buildables = new ArrayList<String>();

        if (this.Woods.size() >= 1 && this.Arrows.size() >= 3) {
            Buildables.add(EntityTypes.BOW.toString());
        }

        if (this.Woods.size() >= 2 && (this.treasure.size() >= 1 || this.key != null)) {
            Buildables.add(EntityTypes.SHIELD.toString());
        }

        return Buildables;
    }

    public void make(String type) throws InvalidActionException, IllegalArgumentException {
        if (type.equals(EntityTypes.BOW.toString())) {
            if (this.Woods.size() >= 1 && this.Arrows.size() >= 3) {
                useWoods(1);
                useArrows(3);
                String id = "bow_" + this.bow_idx;
                this.Bows.add(new Bow(id, bow_durability));
                this.bow_idx += 1;
            } else {
                throw new InvalidActionException("ERROR: Not Enough Material For " + type);
            }
        } else if (type.equals(EntityTypes.SHIELD.toString())) {
            if (this.Woods.size() >= 2 && this.treasure.size() >= 1) {
                useWoods(2);
                useTreasure(1);
                String id = "shield_" + this.shield_idx;
                this.Shields.add(new Shield(id, shield_durability));
                this.shield_idx += 1;
            } else if (this.Woods.size() >= 2 && this.key != null) {
                useWoods(2);
                useKey();
                String id = "shield_" + this.shield_idx;
                this.Shields.add(new Shield(id, shield_durability));
                this.shield_idx += 1;
            } else {
                throw new InvalidActionException("ERROR: Not Enough Material For " + type);
            }
        } else {
            throw new IllegalArgumentException("ERROR: Can not make this type of item: " + type);
        }
    }

    public void addItem(ICollectable item) throws InvalidActionException {
        if (item instanceof Treasure) {
            this.treasure.add((Treasure) item);
        } else if (item instanceof Key) {
            this.key = (Key) item;
        } else if (item instanceof InvincibilityPotion) {
            this.InvincibilityPotion.add((InvincibilityPotion) item);
        } else if (item instanceof InvisibilityPotion) {
            this.InvisibilityPotion.add((InvisibilityPotion) item);
        } else if (item instanceof Wood) {
            this.Woods.add((Wood) item);
        } else if (item instanceof Arrow) {
            this.Arrows.add((Arrow) item);
        } else if (item instanceof Bomb) {
            this.Bombs.add((Bomb) item);
        } else if (item instanceof Sword) {
            this.Swords.add((Sword) item);
        } else {
            throw new InvalidActionException("ERROR: Can not collect item ");
        }
    }

    private IEquipment getItemById(String itemUsedId) throws InvalidActionException {
        ArrayList<IEquipment> item = new ArrayList<IEquipment>();

        item.addAll(InvincibilityPotion.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));
        item.addAll(InvisibilityPotion.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));
        item.addAll(Bombs.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));
        item.addAll(Swords.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));
        item.addAll(Bows.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));
        item.addAll(Shields.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));

        if (item.size() == 0 || item.size() > 1) {
            throw new InvalidActionException("ERROR: Can not match item id");
        }

        return item.get(0);
    }

    public void useTreasures(int quantity) throws InvalidActionException {
        if (this.treasure.size() < quantity) {
            throw new InvalidActionException("ERROR: Do not have enough treasure");
        } else {
            useTreasure(quantity);
        }
    }

    public void useEquipment(String type) {
        if (type.equals(EntityTypes.BOW.toString())) {
            useBow(this.Bows.get(0));
        } else if (type.equals(EntityTypes.SWORD.toString())) {
            useSword(this.Swords.get(0));
        } else if (type.equals(EntityTypes.SHIELD.toString())) {
            useShield(this.Shields.get(0));
        }
    }

    public IEquipment useItem(String itemUsedId) throws InvalidActionException, IllegalArgumentException {

        IEquipment item = getItemById(itemUsedId);

        if (item instanceof InvincibilityPotion) {
            useInvincibility((InvincibilityPotion) item);
        } else if (item instanceof InvisibilityPotion) {
            useInvisibility((InvisibilityPotion) item);
        } else if (item instanceof Bomb) {
            useBomb((Bomb) item);
        } else {
            throw new IllegalArgumentException("ERROR: Can not direct use this item");
        }

        return item;
    }

    public boolean hasAKey() {
        return (this.key != null);
    }

    public boolean hasTheKey(int key) {
        return (this.key.getKey() == key);
    }

    public void useKey() {
        this.key = null;
    }

    private void useTreasure(int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.treasure.remove(0);
        }
    }

    private void useWoods(int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.Woods.remove(0);
        }
    }

    private void useArrows (int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.Arrows.remove(0);
        }
    }

    private void useInvincibility(InvincibilityPotion potion) {
        this.InvincibilityPotion.remove(potion);
    }

    private void useInvisibility(InvisibilityPotion potion) {
        this.InvisibilityPotion.remove(potion);
    }

    private void useBomb(Bomb bomb) {
        bomb.drop();
        this.Bombs.remove(bomb);
    }

    private void useSword(Sword sword) {

        sword.reduceDurability(1);

        if (sword.getDurability() <= 0) {
            this.Swords.remove(sword);
        }
    }

    private void useBow(Bow bow) {

        bow.reduceDurability(1);

        if (bow.getDurability() <= 0) {
            this.Bows.remove(bow);
        }
    }

    private void useShield(Shield shield) {

        shield.reduceDurability(1);

        if (shield.getDurability() <= 0) {
            this.Shields.remove(shield);
        }
    }

    /**
     * precondition: has a shield
     * @return
     */
    public Shield getShiled() {
        return this.Shields.get(0);
    }

    /**
     * precondition: has a shield
     * @return
     */
    public Bow getBow() {
        return this.Bows.get(0);
    }

    /**
     * precondition: has a shield
     * @return
     */
    public Sword getSword() {
        return this.Swords.get(0);
    }

}
