package id.forgeforce.tridiemtri;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;
        if(player.hasPermission("tridiemtri.admin")){
            if(cmd.getName().equalsIgnoreCase("tridiemtri")){
                player.getInventory().addItem(ItemManager.Tridiem3);
            }
        }
        else{
            sender.sendMessage("YEEE SOK OP LU");
        }
        return true;
    }
}
