package net.vanillapractice.perplayerkit.listeners;

import net.md_5.bungee.api.ChatColor;
import net.vanillapractice.perplayerkit.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class KitMenuCloseListener implements Listener {

    @EventHandler
    public void onMenuClose(InventoryCloseEvent e){
        Inventory inv = e.getInventory();
        if(inv.getSize()==54){
            if(inv.getLocation()==null){
                InventoryView view = e.getView();
                if(view.getTitle().contains(ChatColor.DARK_PURPLE+"Kit: ")){
                   Player p = (Player) e.getPlayer();
                    UUID uuid = p.getUniqueId();
                    int slot = Integer.parseInt(view.getTitle().replace(ChatColor.DARK_PURPLE+"Kit: ",""));
                    ItemStack[] kit = new ItemStack[41];
                    ItemStack[] chestitems = e.getInventory().getContents().clone();

                    for(int i = 0;i<41;i++){
                        if(chestitems[i]!=null) {
                            kit[i] = chestitems[i].clone();
                        }else{
                            kit[i] = null;
                        }

                    }
                    KitManager.savekit(uuid,slot,kit);



                }
            }
        }
    }

}
