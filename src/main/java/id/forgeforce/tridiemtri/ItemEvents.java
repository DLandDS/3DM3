package id.forgeforce.tridiemtri;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemEvents implements Listener {
    @EventHandler
    public void onFish(PlayerFishEvent event){
        Player player = event.getPlayer();
        ItemStack name1 = player.getInventory().getItemInMainHand();
        ItemStack name2 = player.getInventory().getItemInOffHand();
        boolean mainhand=false,lanjut = false;
        if(name1.getItemMeta()!=null && name1.getType().equals(Material.FISHING_ROD) && name1.getItemMeta().getDisplayName().equals("§bTridiemtri")) {
            mainhand=true;
            lanjut=true;
        }
        else if(name2.getItemMeta()!=null && name2.getType().equals(Material.FISHING_ROD) && name2.getItemMeta().getDisplayName().equals("§bTridiemtri")) {
            lanjut=true;
        }
        if(lanjut){
            boolean teleport=false;
            ItemStack sebelah;
            if(player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
                mainhand = true;
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
            Location playerLocation = player.getLocation();
            if(event.getState().equals(PlayerFishEvent.State.IN_GROUND)){
                Location hookLocation = event.getHook().getLocation();
                Location change = hookLocation.subtract(playerLocation);
                player.setVelocity(change.toVector().multiply(0.3));
            }
            if(event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY) && event.getCaught()!=null){
                Location hookLocation = event.getCaught().getLocation();
                if(teleport){
                    if(!(event.getCaught() instanceof ArmorStand)) {
                        event.getCaught().teleport(playerLocation);
                    }
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
            }
        }
    }


    @EventHandler
    public void fishingThrow(ProjectileLaunchEvent event){
        if (event.getEntity() instanceof FishHook) {
            FishHook fishHook = (FishHook) event.getEntity();
            if(fishHook.getShooter() instanceof Player){
                Player player = (Player) fishHook.getShooter();
                ItemStack name1 = player.getInventory().getItemInMainHand();
                ItemStack name2 = player.getInventory().getItemInOffHand();
                boolean lanjut = false;
                if(name1.getItemMeta()!=null && name1.getType().equals(Material.FISHING_ROD) && name1.getItemMeta().getDisplayName().equals("§bTridiemtri")) {
                    lanjut=true;
                }
                else if(name2.getItemMeta()!=null && name2.getType().equals(Material.FISHING_ROD) && name2.getItemMeta().getDisplayName().equals("§bTridiemtri")) {
                    lanjut=true;
                }
                if(lanjut && player.getVehicle() instanceof Trident && player.getInventory().getChestplate()!=null){
                    if(player.getInventory().getChestplate().getType().equals(Material.ELYTRA)) {
                        ItemMeta meta = player.getInventory().getChestplate().getItemMeta();
                        Damageable damagable = (Damageable) meta;
                        if(damagable!=null) {
                            int damage = damagable.getDamage()+(10/(1+damagable.getEnchantLevel(Enchantment.DURABILITY)));
                            damagable.setDamage(damage);
                            player.getInventory().getChestplate().setItemMeta(damagable);
                        }
                        player.getVehicle().setVelocity(fishHook.getVelocity());
                        event.setCancelled(true);
                    }
                }
            }
        }
        if (event.getEntity() instanceof Trident) {
            Trident trident = (Trident) event.getEntity();
            if(trident.getShooter() instanceof Player) {
                Player player = (Player) trident.getShooter();
                ItemStack name1 = player.getInventory().getItemInOffHand();
                if(name1.getItemMeta()!=null && name1.getType().equals(Material.FISHING_ROD) && name1.getItemMeta().getDisplayName().equals("§bTridiemtri")) {
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
            boolean mainhand = false,lanjut = false;
            ItemStack name1 = player.getInventory().getItemInMainHand();
            ItemStack name2 = player.getInventory().getItemInOffHand();
            if(name1.getItemMeta()!=null && name1.getType().equals(Material.FISHING_ROD) && name1.getItemMeta().getDisplayName().equals("§bTridiemtri")) {
                mainhand=true;
                lanjut=true;
            }
            else if(name2.getItemMeta()!=null && name2.getType().equals(Material.FISHING_ROD) && name2.getItemMeta().getDisplayName().equals("§bTridiemtri")) {
                lanjut=true;
            }
            if(lanjut)
            if ((event.getHitBlock() != null && !event.getHitBlock().getLocation().getBlock().isEmpty())||
                event.getHitEntity() != null) {
                if(event.getHitEntity() != null){
                    //kasih damage please kalau entity
                    if (event.getEntity().equals(player)){
                        return;
                    }
                }
                Location hookLocation = fishHook.getLocation();
                ItemStack sebelah;
                boolean petir=false, teleport=false;
                if(mainhand) {
                    sebelah = player.getInventory().getItemInOffHand();
                }
                else{
                    sebelah = player.getInventory().getItemInMainHand();
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

                if(event.getHitEntity() != null && !teleport && !petir){
                    //kasih damage please kalau entity
                    if(!player.isSneaking()) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                event.getHitEntity().addPassenger(player);
                            }
                        }.runTaskLater(Tridiemtri.getPlugin(),15);
                    }
                }
                if(petir && sebelah.getItemMeta()!=null){
                    event.getEntity().getWorld().strikeLightning(fishHook.getLocation());
                    ItemMeta sebelahi = sebelah.getItemMeta();
                    Damageable sebelahx = (Damageable) sebelahi;
                    sebelahx.setDamage(sebelahx.getDamage()+(3/(1+sebelah.getEnchantmentLevel(Enchantment.DURABILITY))));
                    sebelah.setItemMeta(sebelahx);
                    if(mainhand) {
                        player.getInventory().setItemInOffHand(sebelah);
                    }
                    else{
                        player.getInventory().setItemInMainHand(sebelah);
                    }
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
                    else{
                        final Location finalHookLocation = event.getHitEntity().getLocation();
                        if(!(event.getHitEntity() instanceof ArmorStand)) {
                            event.getHitEntity().teleport(player.getLocation());
                        }
                        if(!player.isSneaking()) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.teleport(finalHookLocation);
                                }
                            }.runTaskLater(Tridiemtri.getPlugin(),10);
                        }
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
                }
            }
        }
    }
}