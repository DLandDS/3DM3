package id.forgeforce.tridiemtri;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ItemEvents implements Listener {
    public List<Player> pengguna = new ArrayList<>();

    @EventHandler
    public void onFish(PlayerFishEvent event){
        Player player = event.getPlayer();
        Material name1 = player.getInventory().getItemInMainHand().getType();
        Material name2 = player.getInventory().getItemInOffHand().getType();
        if(name1.equals(Material.FISHING_ROD) || name2.equals(Material.FISHING_ROD)){
            if(event.getState().equals(PlayerFishEvent.State.IN_GROUND)){
                Location playerLocation = player.getLocation();
                Location hookLocation = event.getHook().getLocation();
                Location change = hookLocation.subtract(playerLocation);
                player.setVelocity(change.toVector().multiply(0.3));
            }
        }
    }


    @EventHandler
    public void fishingThrow(ProjectileLaunchEvent event){
        if (event.getEntity() instanceof FishHook) {
            FishHook fishHook = (FishHook) event.getEntity();
            fishHook.setVelocity(fishHook.getVelocity().multiply(3.0));
            if(fishHook.getShooter() instanceof Player){
                Player player = (Player) fishHook.getShooter();
                if(player.getVehicle() instanceof Trident){
                    boolean mainhand = false;
                    ItemStack backup;
                    if(player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
                        mainhand = true;
                    }
                    if(mainhand) {
                        backup = player.getInventory().getItemInMainHand();
                        player.getInventory().setItemInMainHand(null);
                    }
                    else{
                        backup = player.getInventory().getItemInOffHand();
                        player.getInventory().setItemInOffHand(null);
                    }
                    ItemMeta meta = backup.getItemMeta();
                    Damageable damagable = (Damageable) meta;
                    if(damagable!=null) {
                        int damage = damagable.getDamage()+1;
                        if(damage>=backup.getType().getMaxDurability()){
                            backup.setType(Material.STICK);
                        }
                        else{
                            damagable.setDamage(damage);
                            backup.setItemMeta(damagable);
                        }
                    }
                    if(mainhand) {
                        player.getInventory().setItemInMainHand(backup);
                    }
                    else{
                        player.getInventory().setItemInOffHand(backup);
                    }
                    player.getVehicle().setVelocity(fishHook.getVelocity());
                    event.setCancelled(true);
                }
                else if(player.getInventory().getChestplate() != null){
                    if(player.getInventory().getChestplate().getType().equals(Material.ELYTRA)) {
                        fishHook.addPassenger(player);
                        ItemMeta meta = player.getInventory().getChestplate().getItemMeta();
                        Damageable damagable = (Damageable) meta;
                        if(damagable!=null) {
                            int damage = damagable.getDamage()+1;
                            damagable.setDamage(damage);
                            player.getInventory().getChestplate().setItemMeta(damagable);
                        }
                    }
                }
            }
        }
        if (event.getEntity() instanceof Trident) {
            Trident trident = (Trident) event.getEntity();
            if(trident.getShooter() instanceof Player) {
                Player player = (Player) trident.getShooter();
                if(player.getInventory().getItemInOffHand().getType().equals(Material.FISHING_ROD)) {
                    trident.addPassenger(player);
                }
            }
        }
    }

    @EventHandler
    public void fishingCollision(ProjectileHitEvent event){
        if (event.getEntity() instanceof FishHook && event.getEntity().getShooter() instanceof Player) {
            FishHook fishHook = (FishHook) event.getEntity();
            Player player = (Player) fishHook.getShooter();
            boolean mainhand = false;

            if ((event.getHitBlock() != null && !event.getHitBlock().getLocation().getBlock().isEmpty())||
                event.getHitEntity() != null) {
                if(event.getHitEntity() != null){
                    //kasih damage please kalau entity
                    if (event.getEntity().equals(player)){
                        return;
                    }
                }
                if(!pengguna.contains(player))
                    pengguna.add(player);
                Location playerLocation = player.getLocation();
                Location hookLocation = fishHook.getLocation();
                ItemStack backup;
                boolean petir=false;
                if(player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
                    mainhand = true;
                }
                if(mainhand) {
                    backup = player.getInventory().getItemInMainHand();
                    if(player.getInventory().getItemInOffHand().getEnchantments().containsKey(Enchantment.CHANNELING)){
                        petir = true;
                    }
                    player.getInventory().setItemInMainHand(null);
                }
                else{
                    backup = player.getInventory().getItemInOffHand();
                    if(player.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.CHANNELING)){
                        petir = true;
                    }
                    player.getInventory().setItemInOffHand(null);
                }
                if (player.getVehicle()!=null) {
                    player.getVehicle().removePassenger(player);
                }
                if(player.isSneaking()){
                    Location change = playerLocation.subtract(hookLocation);
                    event.getEntity().setVelocity(change.toVector().multiply(0.3));
                }
                else {
                    Location change = hookLocation.subtract(playerLocation);
                    player.setVelocity(change.toVector().multiply(0.3));
                }
                boolean finalMainhand = mainhand;

                ItemMeta meta = backup.getItemMeta();
                Damageable damagable = (Damageable) meta;
                if(damagable!=null) {
                    int damage = damagable.getDamage()+1;
                    if(damage>=backup.getType().getMaxDurability()){
                        backup.setType(Material.STICK);
                    }
                    else{
                        damagable.setDamage(damage);
                        backup.setItemMeta(damagable);
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(finalMainhand) {
                            player.getInventory().setItemInMainHand(backup);
                        }
                        else{
                            player.getInventory().setItemInOffHand(backup);
                        }
                    }
                }.runTaskLater(Tridiemtri.getPlugin(),2);
                if(event.getHitEntity() != null && !player.isSneaking() && !petir){
                    //kasih damage please kalau entity
                    event.getHitEntity().addPassenger(player);
                }
                if(petir){
                    event.getEntity().getWorld().strikeLightning(fishHook.getLocation());
                }
            }
        }
    }
    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if(e.getEntity() instanceof Player){
                Player player = (Player) e.getEntity();
                if(pengguna.contains(player)) {
                    pengguna.remove(player);
                    e.setCancelled(true);
                }
            }
        }
    }
}