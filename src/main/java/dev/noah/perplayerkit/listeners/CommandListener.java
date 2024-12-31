package dev.noah.perplayerkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {


    @EventHandler
    public void onEvent(PlayerCommandPreprocessEvent e) {

        if (e.getMessage().length() > 1) {
            /*
            This is used to prevent bypassing external plugins that restrict commands.
             */
            if (e.getMessage().contains("/ ")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("Unknown Command. Type \"/help\" for help.");

            }
        }
    }


}
