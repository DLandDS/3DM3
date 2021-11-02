package id.forgeforce.tridiemtri;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class ItemEvents implements Listener {
    @EventHandler
    public void onFish(PlayerFishEvent event){
        Player player = event.getPlayer();
        Material name1 = player.getInventory().getItemInMainHand().getType();
        Material name2 = player.getInventory().getItemInOffHand().getType();
        if(name1.equals(Material.FISHING_ROD) || name2.equals(Material.FISHING_ROD)){
            if(event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)){
                Location playerLocation = player.getLocation();
                Location hookLocation = event.getHook().getLocation();
                Location change = hookLocation.subtract(playerLocation);
                player.setVelocity(change.toVector().multiply(0.3));
            }
            if(event.getState().equals(PlayerFishEvent.State.IN_GROUND)){
                Location playerLocation = player.getLocation();
                Location hookLocation = event.getHook().getLocation();
                Location change = hookLocation.subtract(playerLocation);
                player.setVelocity(change.toVector().multiply(0.3));
            }
        }
    }
    @EventHandler
    public void fishingThrow(ProjectileLaunchEvent e){
        Projectile hook = e.getEntity();
        if (e.getEntityType().equals(EntityType.FISHING_HOOK)) {
            hook.setVelocity(hook.getVelocity().multiply(3.0));
        }
    }
    @EventHandler
    public void fishingCollision(ProjectileHitEvent event){
        if (event.getEntity() instanceof FishHook && event.getEntity().getShooter() instanceof Player) {
            FishHook fishHook = (FishHook) event.getEntity();
            Player player = (Player) fishHook.getShooter();

            if (event.getHitBlock() != null && !event.getHitBlock().getLocation().getBlock().isEmpty()) {
                Location playerLocation = player.getLocation();
                Location hookLocation = fishHook.getLocation();
                Location change = hookLocation.subtract(playerLocation);
                player.setVelocity(change.toVector().multiply(0.3));
            }
        }
    }
}