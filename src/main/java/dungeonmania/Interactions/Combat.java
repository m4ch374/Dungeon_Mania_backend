package dungeonmania.Interactions;

import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dungeonmania.DungeonObjects.Player;
import dungeonmania.Interfaces.IMovable;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;


public class Combat {

    private Player player;
    private IMovable enemy;
    private HashMap<String, Object> playerState;
    private double playerInitialHealth;
    private double playerAttackDamage;
    private double playerHealth;
    private double enemyInitialHealth;
    private double enemyAttackDamage;
    private double enemyHealth;
    private List<ItemResponse> items;
    private List<RoundResponse> rounds = new ArrayList<RoundResponse>();

    public Combat(Player player, IMovable enemy){
        this.player = player;
        this.enemy = enemy;
        this.playerState = player.getState();
        this.items = (List<ItemResponse>) playerState.get("ItemResponse");

        this.playerAttackDamage = (Double) playerState.get("attackDamage");
        this.playerInitialHealth = (Double) playerState.get("health");
        this.playerHealth = playerInitialHealth;
        
        this.enemyAttackDamage = enemy.getAttackDamage();
        this.enemyInitialHealth = enemy.getHealth();
        this.enemyHealth = enemyInitialHealth;

    }

    /*Calculates player and npc health per round and adds record to this.rounds*/
    public void resolveCombat(){
        while (playerHealth > 0 && enemyHealth > 0) {
            /*If a potion is used then combat is ended prematurely*/
            if ((boolean) playerState.get("invincible") || 
                (boolean) playerState.get("invisible")){
                return;
            }
            player.attackedBy(enemyAttackDamage);
            enemyHealth -= playerAttackDamage;
            playerHealth = (Double) playerState.get("health");

            rounds.add(new RoundResponse(playerHealth, enemyHealth, items));
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
