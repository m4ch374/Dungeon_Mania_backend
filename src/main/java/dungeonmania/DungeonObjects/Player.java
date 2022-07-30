package dungeonmania.DungeonObjects;

import dungeonmania.DungeonObjects.Entities.Collectables.InvincibilityPotion;
import dungeonmania.DungeonObjects.Entities.Collectables.InvisibilityPotion;
import dungeonmania.DungeonObjects.Entities.Entity;
import dungeonmania.Interactions.Combat;
import dungeonmania.Interfaces.ICollectable;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.Interfaces.IEquipment;
import dungeonmania.MovingStrategies.PlayerMovement;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.DungeonFactory.EntityStruct;
import dungeonmania.util.Position;
import dungeonmania.util.Tracker.GoalTypes;
import dungeonmania.util.Tracker.Tracker;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;



public class Player extends Entity {

    private double health;
    private final int attackDamage;

    private final int sword_attack;
    private final int shield_defence;
    private final int invincibility_potion_duration;
    private final int invisibility_potion_duration;

    private int allyNum;
    private final int allyAttackBonous;
    private final int allyDefenceBonous;

    private final int midnight_armour_attack;
    private final int midnight_armour_defence;

    private int potionRemainingTime;
    private final ArrayList<IEquipment> potionList;

    private final Backpack backpack;

    private final PlayerMovement movement;

    Tracker tracker;

    public Player(EntityStruct metaData, JSONObject config, Tracker tracker) {
        super(metaData);
        this.health = config.getInt("player_health");
        this.attackDamage = config.getInt("player_attack");

        this.sword_attack = config.getInt("sword_attack");
        this.shield_defence = config.getInt("shield_defence");
        this.invincibility_potion_duration = config.getInt("invincibility_potion_duration") - 1;
        this.invisibility_potion_duration = config.getInt("invisibility_potion_duration") - 1;

        // ally attack & defence not found in config file
        this.allyAttackBonous = config.has("ally_attack") ? config.getInt("ally_attack") : 0;
        this.allyDefenceBonous = config.has("ally_defence") ? config.getInt("ally_defence") : 0;

        this.midnight_armour_attack = config.has("midnight_armour_attack") ? config.getInt("midnight_armour_attack") : 0;
        this.midnight_armour_defence = config.has("midnight_armour_defence") ? config.getInt("midnight_armour_defence") : 0;

        this.backpack = new Backpack(config.getInt("bow_durability"), config.getInt("shield_durability"));
        this.movement = new PlayerMovement(this, getMap());
        this.potionList = new ArrayList<IEquipment>();

        this.potionRemainingTime = 0;
        this.allyNum = 0;

        this.tracker = tracker;
    }

    public Direction getDirection() {
        return movement.getDirection();
    }

    public List<ItemResponse> getPlayerItems() {
        return backpack.getItemResponse();
    }

    public List<String> getBuildables() {
        return backpack.getBuildables();
    }

    public Position getCurrentPosition() {
        return movement.getCurrentPosition();
    }

    public Position getPreviousPosition() {
        return movement.getPreviousPosition();
    }

    public boolean isDead() {
        return (this.health <= 0);
    }

    public boolean isInvincible() {
        return (this.potionList.size() > 0 && (this.potionList.get(0) instanceof InvincibilityPotion));
    }

    public boolean isInvisible() {
        return (this.potionList.size() > 0 && (this.potionList.get(0) instanceof InvisibilityPotion));
    }

    public boolean holdingSword() {
        return backpack.hasSword();
    }

    public boolean holdingBow() {
        return backpack.hasBow();
    }

    public boolean holdingShield() {
        return backpack.hasShield();
    }

    public boolean holdingSceptre() {
        return backpack.hasSceptre();
    }

    public boolean holdingMidnightArmour() {
        return backpack.hasMidnightArmour();
    }

    public boolean holdingTimeTurner() {
        return backpack.hasTimeTurner();
    }

    public void collect(ICollectable item) throws InvalidActionException {
        backpack.addItem(item);
    }

    public void notifyTrackerCollectedTreasure() {
        tracker.notifyTracker(GoalTypes.TREASURE);
    }

    private void make(String type) throws InvalidActionException, IllegalArgumentException {
        backpack.make(type, getMap().hasZombie());
    }

    // do not use this for bribe mercenaries
    private void useItem(String itemUsedId) throws InvalidActionException, IllegalArgumentException {
        IEquipment item = backpack.useItem(itemUsedId);

        if (item instanceof InvincibilityPotion) {
            potionList.add(item);
            if (this.potionRemainingTime <= 0) {
                this.potionRemainingTime += this.invincibility_potion_duration;
            }
        } else if (item instanceof InvisibilityPotion) {
            potionList.add(item);
            if (this.potionRemainingTime <= 0) {
                this.potionRemainingTime += this.invisibility_potion_duration;
            }
        }
    }

    private void updatePotions() {
        // update potion state
        if (this.potionRemainingTime > 0) {
            this.potionRemainingTime -= 1;
            if (this.potionRemainingTime <= 0) {
                this.potionList.remove(0);
            }
        }

        if (this.potionRemainingTime <= 0 && this.potionList.size() >= 1) {
            IEquipment potion = this.potionList.get(0);

            if (potion instanceof InvincibilityPotion) {
                this.potionRemainingTime += this.invincibility_potion_duration;
            } else {
                this.potionRemainingTime += this.invisibility_potion_duration;
            }
        }
    }

    public void tick(String action, Direction direction, String item) throws InvalidActionException, IllegalArgumentException {
        if (action.equals(EntityTypes.PLAYERUSE.toString())) {
            movement.playerStays();
            useItem(item);
        } else if (action.equals(EntityTypes.PLAYERMAKE.toString())) {
            make(item);
        } else if (action.equals(EntityTypes.PLAYERMOVE.toString())) {
            movement.setDirection(direction);

            Position position = getCurrentPosition().translateBy(direction);
            movement.move(position);

            updatePotions();

            movement.setDirection(null);

            getMap().activeBombIfActive();
        } else {
            throw new IllegalArgumentException("ERROR: Undefined player behavior");
        }
    }

    public void openDoor(int key) throws InvalidActionException {
        if (!backpack.hasSunStone()) {
            if (backpack.hasAKey() && backpack.hasTheKey(key)) {
                backpack.useKey();
            } else {
                throw new InvalidActionException("ERROR: Can not open the door");
            }
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

    public void notifyAllyReduce() {
        this.allyNum -= 1;
        tracker.unnotifyTracker(GoalTypes.ENEMIES);
    }

    public void notifyAllyIncrease() {
        this.allyNum += 1;
        tracker.notifyTracker(GoalTypes.ENEMIES);
    }

    public void mindControll() throws InvalidActionException {
        if (holdingSceptre()) {
            this.allyNum += 1;
        } else {
            throw new InvalidActionException("ERROR: Player do not have a sceptre");
        }
    }

    public void tryBribe(int quantity) throws InvalidActionException  {
        backpack.useTreasures(quantity);
    }

    // items that can/will be used in the battle at the moment
    public List<ItemResponse> getEquipmentUsedInRound () {
        ArrayList<ItemResponse> items = new ArrayList<ItemResponse>();
        ArrayList<IEquipment> battleEquipment = new ArrayList<IEquipment>();

        if (holdingSword()) battleEquipment.add(backpack.getSword());
        if (holdingBow()) battleEquipment.add(backpack.getBow());
        if (holdingShield()) battleEquipment.add(backpack.getShiled());

        // potion
        if (isInvincible() || isInvisible()) {
            battleEquipment.add(this.potionList.get(0));
        }

        battleEquipment
            .stream()
            .forEach(e -> items.add(e.toItemResponse()));

        return items;
    }

    public void useEquipment(String itemId) throws InvalidActionException {
        backpack.useEquipment(itemId);
    }

    public double getAttackDamage() {
        double ad = this.attackDamage;

        if (holdingSword()) { ad += this.sword_attack; }

        if (holdingBow()) { ad *= 2; }

        if (holdingMidnightArmour()) { ad += this.midnight_armour_attack; }

        ad += this.allyNum * this.allyAttackBonous;
        return ad;
    }

    public double getHealth(){
        return this.health;
    }

    public double attackedBy(double ad) {
        int defence = 0;
        defence += this.allyNum * this.allyDefenceBonous;

        if (holdingMidnightArmour()) { defence += this.midnight_armour_defence; }

        if (holdingShield()) {
            defence = this.shield_defence;
        }

        double playerHealthDelta = ((ad - defence) / 10);

        this.health -= playerHealthDelta;

        if (isDead()) {
            getMap().removeEntity(this);
        }
        return -playerHealthDelta;
    }

    public List<BattleResponse> initiateBattle() {
        if (this.isInvisible()){
            return null;
        }
        List<BattleResponse> battles = new ArrayList<BattleResponse>();
        for (Entity enemy : getMap().getEntitiesOverlapped(this)){
            if (enemy instanceof IEnemy){
                Combat battle = new Combat(this, (IEnemy)enemy);
                battle.resolveCombat();
                battles.add(battle.returnBattleResponse());
            }
        }
        if (battles.size() == 0){
            return null;
        }
        return battles;
    }
}
