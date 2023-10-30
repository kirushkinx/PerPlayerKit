package net.vanillapractice.perplayerkit;

import net.vanillapractice.perplayerkit.sql.SQLGetter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.UUID;

public class KitManager {
    private static final PerPlayerKit plugin = PerPlayerKit.getPlugin(PerPlayerKit.class);


    public static boolean savekit(UUID uuid, int slot) {
        if (Bukkit.getPlayer(uuid) != null) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                return savekit(uuid, slot, player.getInventory().getContents());

            }
        }
        return false;
    }


    public static boolean savekit(UUID uuid, int slot,ItemStack[] kit) {

        if (Bukkit.getPlayer(uuid) != null) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                /*if (PerPlayerKit.data.containsKey(uuid.toString() + slot)) {
                    PerPlayerKit.data.remove(uuid.toString() + slot);
                    player.sendMessage("Kit Cleared (manager)");
                }

                 */

                boolean notEmpty=false;
                for(ItemStack i: kit){
                    if(i!=null) {
                        if(!notEmpty) {
                            notEmpty = true;
                        }

                    }
                }

                if(notEmpty) {

                    if(kit[36]!=null) {
                        if (!kit[36].getType().toString().contains("BOOTS")) {
                            kit[36] = null;
                        }
                    }
                    if(kit[37]!=null) {
                        if (!kit[37].getType().toString().contains("LEGGINGS")) {
                            kit[37] = null;
                        }
                    }
                    if(kit[38]!=null) {
                        if (!(kit[38].getType().toString().contains("CHESTPLATE")||kit[38].getType().toString().contains("ELYTRA"))) {
                            kit[38] = null;
                        }
                    }
                    if(kit[39]!=null) {
                        if (!kit[39].getType().toString().contains("HELMET")) {
                            kit[39] = null;
                        }
                    }



                    PerPlayerKit.data.put(uuid.toString() + slot, kit);
                    player.sendMessage(ChatColor.GREEN+"Kit "+slot+" saved!");

                    return true;
                }else{
                        player.sendMessage(ChatColor.RED+ "You cant save an empty kit!");
                    }


                }

            }
            return false;

    }

    public static boolean savekit(UUID uuid, int slot,ItemStack[] kit,boolean silent) {
if(silent) {
    if (Bukkit.getPlayer(uuid) != null) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
                /*if (PerPlayerKit.data.containsKey(uuid.toString() + slot)) {
                    PerPlayerKit.data.remove(uuid.toString() + slot);
                    player.sendMessage("Kit Cleared (manager)");
                }

                 */

            boolean notEmpty = false;
            for (ItemStack i : kit) {
                if (i != null) {
                    if (!notEmpty) {
                        notEmpty = true;
                    }

                }
            }

            if (notEmpty) {

                if (kit[36] != null) {
                    if (!kit[36].getType().toString().contains("BOOTS")) {
                        kit[36] = null;
                    }
                }
                if (kit[37] != null) {
                    if (!kit[37].getType().toString().contains("LEGGINGS")) {
                        kit[37] = null;
                    }
                }
                if (kit[38] != null) {
                    if (!(kit[38].getType().toString().contains("CHESTPLATE") || kit[38].getType().toString().contains("ELYTRA"))) {
                        kit[38] = null;
                    }
                }
                if (kit[39] != null) {
                    if (!kit[39].getType().toString().contains("HELMET")) {
                        kit[39] = null;
                    }
                }


                PerPlayerKit.data.put(uuid.toString() + slot, Filter.filterItemStack(kit));
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You cant save an empty kit!");
            }


        }

    }
    return false;
}else{
    return savekit(uuid,slot,kit);
}

    }








    public static boolean loadkit(UUID uuid, int slot){

        if(Bukkit.getPlayer(uuid)!=null) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {

                if(PerPlayerKit.data.get(uuid.toString()+slot)!=null){
                player.getInventory().setContents(PerPlayerKit.data.get(uuid.toString()+slot));
                Broadcast.bcKit(player);
                player.sendMessage(ChatColor.GREEN+"Kit "+slot+ " loaded!");
                PerPlayerKit.lastKit.put(uuid,slot);
                    return true;
                }
                else{
                    player.sendMessage(ChatColor.RED+"Kit "+slot+" does not exist!");
                }
            }
        }
        return false;
    }

    public static boolean respawnKitLoad(UUID uuid, int slot){

        if(Bukkit.getPlayer(uuid)!=null) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {

                if(PerPlayerKit.data.get(uuid.toString()+slot)!=null){
                    player.getInventory().setContents(PerPlayerKit.data.get(uuid.toString()+slot));
                    player.sendMessage(ChatColor.GREEN+"Last kit loaded!");
                    PerPlayerKit.lastKit.put(uuid,slot);
                    return true;
                }
                else{
                    player.sendMessage(ChatColor.RED+"Last used kit does not exist");
                }
            }
        }
        return false;
    }


    public static boolean hasKit(UUID uuid,int slot){
        return PerPlayerKit.data.get(uuid.toString() + slot) != null;

    }

    public static ItemStack[] getKit (UUID uuid, int slot){
        if(hasKit(uuid,slot)) {
            return PerPlayerKit.data.get(uuid.toString()+slot);
        }
        else{
            return null;
        }

    }


    public static void loadFromSQL(UUID uuid){
        for(int i = 1;i<10;i++){
            String data = PerPlayerKit.sqldata.getMySQLKit(uuid.toString()+i);
            if(!data.equalsIgnoreCase("error")){
                try {
                    PerPlayerKit.data.put(uuid.toString()+i,Filter.filterItemStack(Serializer.itemStackArrayFromBase64(data)));
                } catch (IOException ignored) {
                }
            }
        }
        String data = PerPlayerKit.sqldata.getMySQLKit(uuid.toString()+"enderchest");
        if(!data.equalsIgnoreCase("error")){
            try {
                PerPlayerKit.data.put(uuid.toString()+"enderchest",Filter.filterItemStack(Serializer.itemStackArrayFromBase64(data)));
            } catch (IOException ignored) {
            }
        }
    }

    public static void saveToSQL(UUID uuid){
        for(int i = 1;i<10  ;i++){
            if(PerPlayerKit.data.get(uuid.toString()+i)!=null){
                PerPlayerKit.sqldata.saveMySQLKit(uuid.toString()+i,Serializer
                        .itemStackArrayToBase64(PerPlayerKit.data.get(uuid.toString()+i)));
                PerPlayerKit.data.remove(uuid.toString()+i);
            }
        }
        if(PerPlayerKit.data.get(uuid.toString()+"enderchest")!=null){
            PerPlayerKit.sqldata.saveMySQLKit(uuid.toString()+"enderchest",Serializer
                    .itemStackArrayToBase64(PerPlayerKit.data.get(uuid.toString()+"enderchest")));
            PerPlayerKit.data.remove(uuid.toString()+"enderchest");
        }
    }



    public static boolean deleteKitAll(UUID uuid,int slot){
        if(hasKit(uuid,slot)){
            String kitid = uuid.toString() + slot;
            PerPlayerKit.data.remove(kitid);
            new BukkitRunnable() {

                @Override
                public void run() {
                    PerPlayerKit.sqldata.deleteKitSQL(kitid);
                }
            }.runTaskAsynchronously(plugin);
            return true;
        }
        return false;
    }


}
