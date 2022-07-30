package dungeonmania.Interactions;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.Interfaces.IEnemy;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import java.util.ArrayList;
import java.util.List;



public class Combat {

    private Player player;
    private IEnemy enemy;
    private double playerInitialHealth;
    private double playerAttackDamage;
    private double playerHealth;
    private double enemyInitialHealth;
    private double enemyAttackDamage;
    private double enemyHealth;
    private List<ItemResponse> items;
    private List<RoundResponse> rounds = new ArrayList<RoundResponse>();

    public Combat(Player player, IEnemy enemy){
        this.player = player;
        this.enemy = enemy;
        this.items = player.getEquipmentUsedInRound();
        
        this.playerAttackDamage = player.getAttackDamage();
        this.playerInitialHealth = player.getHealth();
        this.playerHealth = playerInitialHealth;
        
        this.enemyAttackDamage = enemy.getAttackDamage();
        this.enemyInitialHealth = enemy.getHealth();
        this.enemyHealth = enemyInitialHealth;

    }

    /*Calculates player and npc health per round and adds record to this.rounds*/
    public void resolveCombat(){
        for (ItemResponse item : this.items){
            try {
                player.useEquipment(item.getId());
            } catch (Exception e) {
                continue;
            }
        }
        
        while (playerHealth > 0 && enemyHealth > 0) {
            /*If a potion is used then combat is ended prematurely*/
            if (player.isInvincible()){
                enemy.death();
                rounds.add(new RoundResponse(0, enemyHealth, this.items));
                return;
            }
            
            double playerHealthDelta = player.attackedBy(enemyAttackDamage);
            double enemyHealthDelta = (playerAttackDamage / 5);
            enemyHealth -= enemyHealthDelta;
            playerHealth = player.getHealth();

            rounds.add(new RoundResponse( -playerHealthDelta,  -enemyHealthDelta, items));
        }
        if (enemyHealth <= 0) {
            enemy.death();
        }
        return;
    }

    /*Returns BattleResponse*/
    public BattleResponse returnBattleResponse(){
        BattleResponse response = new BattleResponse(enemy.getClasString(),
                rounds , playerInitialHealth, enemyInitialHealth);
        return response;
    }

}
