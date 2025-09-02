/*

    Style Manager
        by kirushkinx (https://github.com/kirushkinx)

 */

package dev.noah.perplayerkit.util;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class StyleManager {
    private static StyleManager instance;
    private final Plugin plugin;

    private Material glassMaterial;
    private String titleColor;

    public StyleManager(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
        loadConfig();
    }

    public static StyleManager get() {
        return instance;
    }

    public void loadConfig() {
        try {
            this.glassMaterial = Material.valueOf(plugin.getConfig().getString("interface.glass-material", "BLUE_STAINED_GLASS_PANE"));
        } catch (IllegalArgumentException e) {
            this.glassMaterial = Material.BLUE_STAINED_GLASS_PANE;
        }

        String colorTag = plugin.getConfig().getString("interface.primary-color", "&9");
        this.titleColor = ColorUtil.translateColors(colorTag);
    }

    public Material getGlassMaterial() {
        return glassMaterial;
    }

    public String getPrimaryColor() {
        return titleColor;
    }
}