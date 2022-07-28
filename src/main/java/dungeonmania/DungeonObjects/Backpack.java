package dungeonmania.DungeonObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.DungeonObjects.Entities.Collectables.Arrow;
import dungeonmania.DungeonObjects.Entities.Collectables.Key;
import dungeonmania.DungeonObjects.Entities.Collectables.SunStone;
import dungeonmania.DungeonObjects.Entities.Collectables.Sword;
import dungeonmania.DungeonObjects.Entities.Collectables.TimeTurner;
import dungeonmania.DungeonObjects.Entities.Collectables.Treasure;
import dungeonmania.DungeonObjects.Entities.Collectables.Wood;
import dungeonmania.DungeonObjects.Entities.Collectables.InvincibilityPotion;
import dungeonmania.DungeonObjects.Entities.Collectables.InvisibilityPotion;
import dungeonmania.DungeonObjects.Entities.Craftables.Bow;
import dungeonmania.DungeonObjects.Entities.Craftables.MidnightArmour;
import dungeonmania.DungeonObjects.Entities.Craftables.Sceptre;
import dungeonmania.DungeonObjects.Entities.Craftables.Shield;
import dungeonmania.DungeonObjects.Entities.LogicEntities.Collectables.Bomb;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;

public final class Backpack {

    private Key key = null;
    private ArrayList<Treasure> treasure = new ArrayList<Treasure>();
    private ArrayList<InvincibilityPotion> invincibilityPotion = new ArrayList<InvincibilityPotion>();
    private ArrayList<InvisibilityPotion> invisibilityPotion = new ArrayList<InvisibilityPotion>();
    private ArrayList<Wood> woods = new ArrayList<Wood>();
    private ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    private ArrayList<Sword> swords = new ArrayList<Sword>();
    private ArrayList<SunStone> sunStones = new ArrayList<SunStone>();
    private ArrayList<MidnightArmour> midnightArmours = new ArrayList<MidnightArmour>();
    private ArrayList<Sceptre> sceptres = new ArrayList<Sceptre>();
    private ArrayList<TimeTurner> timeTurners = new ArrayList<TimeTurner>();

    private int idx = 0;
    private ArrayList<Bow> Bows = new ArrayList<Bow>();
    private ArrayList<Shield> Shields = new ArrayList<Shield>();

    private final int bow_durability;
    private final int shield_durability;

    public Backpack(int bow_durability, int shield_durability) {
        this.bow_durability = bow_durability;
        this.shield_durability = shield_durability;
    }

    private int getIdx() {
        int index = this.idx;
        this.idx++;
        return index;
    }

    public boolean hasSword() {
        return (this.swords.size() >= 1);
    }

    public boolean hasBow() {
        return (this.Bows.size() >= 1);
    }

    public boolean hasShield() {
        return (this.Shields.size() >= 1);
    }

    public boolean hasSceptre() {
        return (this.sceptres.size() >= 1);
    }

    public boolean hasMidnightArmour() {
        return (this.midnightArmours.size() >= 1);
    }

    public boolean hasSunStone() {
        return (this.sunStones.size() >= 1);
    }

    public boolean hasTimeTurner() {
        return (this.timeTurners.size() >= 1);
    }

    public List<ItemResponse> getItemResponse() {
        ArrayList<ItemResponse> items = new ArrayList<ItemResponse>();

        if (key != null) items.add(key.toItemResponse());
        treasure.stream().forEach(e -> items.add(e.toItemResponse()));
        invincibilityPotion.stream().forEach(e -> items.add(e.toItemResponse()));
        invisibilityPotion.stream().forEach(e -> items.add(e.toItemResponse()));
        woods.stream().forEach(e -> items.add(e.toItemResponse()));
        arrows.stream().forEach(e -> items.add(e.toItemResponse()));
        bombs.stream().forEach(e -> items.add(e.toItemResponse()));
        swords.stream().forEach(e -> items.add(e.toItemResponse()));
        Bows.stream().forEach(e -> items.add(e.toItemResponse()));
        Shields.stream().forEach(e -> items.add(e.toItemResponse()));
        sunStones.stream().forEach(e -> items.add(e.toItemResponse()));
        midnightArmours.stream().forEach(e -> items.add(e.toItemResponse()));
        sceptres.stream().forEach(e -> items.add(e.toItemResponse()));
        timeTurners.stream().forEach(e -> items.add(e.toItemResponse()));

        return items;
    }

    public List<String> getBuildables() {
        ArrayList<String> Buildables = new ArrayList<String>();

        if (this.woods.size() >= 1 && this.arrows.size() >= 3) {
            Buildables.add(EntityTypes.BOW.toString());
        }

        if (this.woods.size() >= 2 && (this.treasure.size() >= 1 || this.key != null)) {
            Buildables.add(EntityTypes.SHIELD.toString());
        }

        return Buildables;
    }

    public void make(String type, boolean hasZombies) throws InvalidActionException, IllegalArgumentException {
        if (type.equals(EntityTypes.BOW.toString())) {
            if (this.woods.size() >= 1 && this.arrows.size() >= 3) {
                useWoods(1);
                useArrows(3);
                String id = "bow_" + getIdx();
                this.Bows.add(new Bow(id, bow_durability));
            } else {
                throw new InvalidActionException("ERROR: Not Enough Material For " + type);
            }
        } else if (type.equals(EntityTypes.SHIELD.toString())) {
            if (this.woods.size() >= 2 && this.treasure.size() >= 1) {
                useWoods(2);
                useTreasure(1);
                String id = "shield_" + getIdx();
                this.Shields.add(new Shield(id, shield_durability));
            } else if (this.woods.size() >= 2 && this.key != null) {
                useWoods(2);
                useKey();
                String id = "shield_" + getIdx();
                this.Shields.add(new Shield(id, shield_durability));
            } else {
                throw new InvalidActionException("ERROR: Not Enough Material For " + type);
            }
        } else if (type.equals(EntityTypes.SCEPTRE.toString())) {
            if ((this.woods.size() >= 1 || this.arrows.size() >= 2)
            && (this.treasure.size() >= 1 || this.key != null)
            && this.sunStones.size() >= 1) {
                if (this.woods.size() >= 1) {
                    useWoods(1);
                } else {
                    useArrows(2);
                }

                if (this.treasure.size() >= 1) {
                    useTreasure(1);
                } else {
                    useKey();
                }

                useSunStone(1);

                this.sceptres.add(new Sceptre("sceptre_" + getIdx()));
            }else {
                throw new InvalidActionException("ERROR: Not Enough Material For " + type);
            }
        } else if (type.equals(EntityTypes.MIDNIGHTARMOUR.toString())) {
            if (!hasZombies && this.swords.size() >= 1 && this.sunStones.size() >= 1) {
                useSunStone(1);
                useSword(1);

                this.midnightArmours.add(new MidnightArmour("midnightArmour_" + getIdx()));
            } else {
                throw new InvalidActionException("ERROR: Not Enough Material For " + type);
            }
        } else {
            throw new IllegalArgumentException("ERROR: Can not make this type of item: " + type);
        }
    }

    public void addItem(ICollectable item) throws InvalidActionException {
        if (item instanceof Key && hasAKey()) {
            throw new InvalidActionException("ERROR: Already has a key");
        }

        if (item instanceof Bomb) {
            Bomb bomb = (Bomb) item;
            if (!bomb.isCollectible()) {
                throw new InvalidActionException("ERROR: Cannot collect a dropped bomb");
            }
        }

        if (item instanceof Treasure) {
            this.treasure.add((Treasure) item);
        } else if (item instanceof Key) {
            this.key = (Key) item;
        } else if (item instanceof InvincibilityPotion) {
            this.invincibilityPotion.add((InvincibilityPotion) item);
        } else if (item instanceof InvisibilityPotion) {
            this.invisibilityPotion.add((InvisibilityPotion) item);
        } else if (item instanceof Wood) {
            this.woods.add((Wood) item);
        } else if (item instanceof Arrow) {
            this.arrows.add((Arrow) item);
        } else if (item instanceof Bomb) {
            this.bombs.add((Bomb) item);
        } else if (item instanceof Sword) {
            this.swords.add((Sword) item);
        } else if (item instanceof TimeTurner) {
            this.timeTurners.add((TimeTurner) item);
        } else if (item instanceof SunStone) {
            this.sunStones.add((SunStone) item);
        } else {
            throw new InvalidActionException("ERROR: Can not collect item");
        }
    }

    private IEquipment getItemById(String itemUsedId) throws InvalidActionException {
        ArrayList<IEquipment> item = new ArrayList<IEquipment>();

        item.addAll(invincibilityPotion.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));
        item.addAll(invisibilityPotion.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));
        item.addAll(bombs.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));
        item.addAll(swords.stream().filter(e -> e.getId().equals(itemUsedId)).collect(Collectors.toList()));
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

    public void useEquipment(String ItemId) throws InvalidActionException {

        IEquipment item = getItemById(ItemId);

        if (item instanceof Bow) {
            useBow((Bow) item);
        } else if (item instanceof Sword) {
            useSword((Sword) item);
        } else if (item instanceof Shield) {
            useShield((Shield) item);
        } else {
            throw new InvalidActionException("ERROR: Can not use this equipment");
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
            this.woods.remove(0);
        }
    }

    private void useArrows (int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.arrows.remove(0);
        }
    }

    private void useSunStone (int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.sunStones.remove(0);
        }
    }

    private void useSword (int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.swords.remove(0);
        }
    }

    private void useSunStone (int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.sunStones.remove(0);
        }
    }

    private void useSword (int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.swords.remove(0);
        }
    }

    private void useSunStone (int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.sunStones.remove(0);
        }
    }

    private void useSword (int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.swords.remove(0);
        }
    }

    private void useInvincibility(InvincibilityPotion potion) {
        this.invincibilityPotion.remove(potion);
    }

    private void useInvisibility(InvisibilityPotion potion) {
        this.invisibilityPotion.remove(potion);
    }

    private void useBomb(Bomb bomb) {
        bomb.drop();
        this.bombs.remove(bomb);
    }

    private void useSword(Sword sword) {

        sword.reduceDurability(1);

        if (sword.getDurability() <= 0) {
            this.swords.remove(sword);
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
        return this.swords.get(0);
    }
    
}
