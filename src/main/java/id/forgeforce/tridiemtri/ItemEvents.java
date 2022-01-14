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
            boolean mainhand=false,sneaking=false,panah=false,teleport=false;
            ItemStack sebelah;
            if(player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
                mainhand = true;
            }
            if(player.isSneaking()){
                sneaking = true;
            }
            if(mainhand) {
                sebelah = player.getInventory().getItemInOffHand();
            }
            else{
                sebelah = player.getInventory().getItemInMainHand();
            }
            if(sebelah.getType().equals(Material.ENDER_PEARL)){
                teleport = true;
            }
            if(sebelah.getType().equals(Material.ARROW)){
                panah = true;
            }
            Location playerLocation = player.getLocation();
            if(event.getState().equals(PlayerFishEvent.State.IN_GROUND)){
                Location hookLocation = event.getHook().getLocation();
                Location change = hookLocation.subtract(playerLocation);
                player.setVelocity(change.toVector().multiply(0.3));
            }
            if(event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY) && event.getCaught()!=null){
                Location hookLocation = event.getCaught().getLocation();
                if(teleport){
                    event.getCaught().teleport(playerLocation);
                    player.teleport(hookLocation);
                    if(sebelah.getAmount()>1){
                        sebelah.setAmount(sebelah.getAmount()-1);
                    }
                    else {
                        if (mainhand)
                            player.getInventory().setItemInOffHand(null);
                        else
                            player.getInventory().setItemInMainHand(null);
                    }
                }
                if(panah){
                    Location change = playerLocation.subtract(hookLocation);
                    player.getWorld().spawnArrow(playerLocation,change.getDirection(),10,1);
                    if(sebelah.getAmount()>1){
                        sebelah.setAmount(sebelah.getAmount()-1);
                    }
                    else{
                        if(mainhand)
                            player.getInventory().setItemInOffHand(null);
                        else
                            player.getInventory().setItemInMainHand(null);
                    }
                    event.setCancelled(true);
                }
                if(!panah && !teleport){
                    if(sneaking){
                        Location change = playerLocation.subtract(hookLocation);
                        event.getCaught().setVelocity(change.toVector().multiply(0.3));
                    }
                    else{
                        Location change = hookLocation.subtract(playerLocation);
                        player.setVelocity(change.toVector().multiply(0.3));
                    }
                }
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
                        int damage = damagable.getDamage()+(10/(1+damagable.getEnchantLevel(Enchantment.DURABILITY)));
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
                            int damage = damagable.getDamage()+(10/(1+damagable.getEnchantLevel(Enchantment.DURABILITY)));
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
                ItemStack backup,sebelah;
                boolean petir=false,sneaking=false, teleport=false;
                if(player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
                    mainhand = true;
                }
                if(player.isSneaking()){
                    sneaking = true;
                }
                if(mainhand) {
                    backup = player.getInventory().getItemInMainHand();
                    sebelah = player.getInventory().getItemInOffHand();
                }
                else{
                    backup = player.getInventory().getItemInOffHand();
                    sebelah = player.getInventory().getItemInMainHand();
                }
                if(!sneaking) {
                    if(!mainhand)
                        player.getInventory().setItemInOffHand(null);
                    else
                        player.getInventory().setItemInMainHand(null);
                }
                if(sebelah.getEnchantments().containsKey(Enchantment.CHANNELING)){
                    petir = true;
                }
                if(sebelah.getType().equals(Material.ENDER_PEARL)){
                    teleport = true;
                }
                if (player.getVehicle()!=null) {
                    player.getVehicle().removePassenger(player);
                }
                boolean finalMainhand = mainhand;

                ItemMeta meta = backup.getItemMeta();
                Damageable damagable = (Damageable) meta;
                if(damagable!=null) {
                    int damage = damagable.getDamage()+(10/(1+damagable.getEnchantLevel(Enchantment.DURABILITY)));
                    if(damage>=backup.getType().getMaxDurability()){
                        backup.setType(Material.STICK);
                    }
                    else{
                        damagable.setDamage(damage);
                        backup.setItemMeta(damagable);
                    }
                }
                if(event.getHitEntity() != null && !player.isSneaking() && !petir){
                    //kasih damage please kalau entity
                    event.getHitEntity().addPassenger(player);
                }
                if(petir){
                    event.getEntity().getWorld().strikeLightning(fishHook.getLocation());
                }
                if(teleport){
                    if(event.getHitEntity() == null){
                        if(sebelah.getAmount()>1){
                            sebelah.setAmount(sebelah.getAmount()-1);
                        }
                        else{
                            if(mainhand)
                                player.getInventory().setItemInOffHand(null);
                            else
                                player.getInventory().setItemInMainHand(null);
                        }
                        player.teleport(hookLocation);
                    }
                }
                if(!sneaking && !teleport){
                    Location change = hookLocation.subtract(playerLocation);
                    player.setVelocity(change.toVector().multiply(0.3));
                }
                if(sneaking)
                    return;
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