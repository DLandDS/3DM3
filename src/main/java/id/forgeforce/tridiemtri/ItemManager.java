package id.forgeforce.tridiemtri;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {
    public static ItemStack Tridiem3;
    public static void init(){
        createTridiem3();
    }
    private static void createTridiem3(){
        ItemStack item = new ItemStack(Material.FISHING_ROD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("3D Maneuver MK3");
        item.setItemMeta(meta);
        Tridiem3 = item;
    }
}
