/*

    Skull Utility
        by kirushkinx

 */

package dev.noah.perplayerkit.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SkullUtils {


    private static final Property[] CUSTOM_SKULLS = new Property[] {
            new Property("textures", // https://mineskin.org/ru/skins/b3201b952f3b4b25a91e62bd94d72def
                    "ewogICJ0aW1lc3RhbXAiIDogMTc1MDAxNjgwNjc4MywKICAicHJvZmlsZUlkIiA6ICJlNzM4MTYzZTYwM2M0MTFkOTg4MzNiYzkyZTI4Y2IyYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJYZW5va3JhdGVzUml0dmEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FiZTc5MGI0NzgxZjI2MjgwY2E4M2YwODJmZjM5NzI1MDkxZjhjMGQ5NWFkOTNkODY3ZDJmYThiM2EzZTdmOSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                    "vfTnKKNWmv35vYaaRv1H/vEqdMesXah5ldd/8PznzEl71adL9oqU3UNCK5WEDXzD+BYb2cLXx1dfRp2fvCTzDZJEbzAQP7yG0kQQTauc3g4krmzWGu+ymOYqGy7uwbRGlHGNGltGqxsdnyKrdyKaXbppU73fsxO4sNTpZbxGsFVKQCqUcOpq6lJPe0XHs4McRXH0Boo8ZwYoeCbeBFelCWAjpn9csKEUYZvD7n6xNgEeylg2Xh2yjsdYVmcItkpd/rLfSai5YuD6OMnlb7QDK4C+68f9K68lSmixkTBOkM6XZHhVzZAtlwFBXswH1kzRVrTgMaBVHuiwB7untatkO8zWGef4oowjFq+KHQOZoTRq6vnDQeYPt1yxo4WMFFWDwCOlsiKhoV/Eln7U3BWqRVhgvW18Y7NY8vfIQugeBcD95SiM8IOl4XyXX3aquPvbhZKiXsNLJ2sT8eT18EaX4+XxZI+Dq06BEqSMOeKovaZh90FUWDLbIKkYPyHLhCND7Hr/POkbq3h4O/7AcqCIClfrT2Lr9mDecPsyw5CwnSwHC7CDo0g63/OiD19NCZNomE2IdnQ9TOD/mvpS5+gqS+BiFoFny/7jK8BVKXjchO9cxV4q0tLC61ukhuEhA8lX48dPnijHrssB8PeD5EFxCQsJeqwgKyA7zQ4DXit7rIw="),
    };

    private static boolean isValidProperty(Property property) {
        if (property == null) return false;
        try {
            Field valueField = Property.class.getDeclaredField("value");
            valueField.setAccessible(true);
            String value = (String) valueField.get(property);
            return value != null && !value.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public static ItemStack createCustomSkull(String displayName, int skullIndex) {
        if (skullIndex < 0 || skullIndex >= CUSTOM_SKULLS.length) {
            skullIndex = 0;
        }

        Property selectedProperty = CUSTOM_SKULLS[skullIndex];

        if (!isValidProperty(selectedProperty)) {
            for (int i = 0; i < CUSTOM_SKULLS.length; i++) {
                if (isValidProperty(CUSTOM_SKULLS[i])) {
                    selectedProperty = CUSTOM_SKULLS[i];
                    break;
                }
            }

            if (!isValidProperty(selectedProperty)) {
                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(displayName);
                    skull.setItemMeta(meta);
                }
                return skull;
            }
        }

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);

            GameProfile profile = new GameProfile(UUID.randomUUID(), "CustomHead");
            profile.getProperties().put("textures", selectedProperty);

            try {
                try {
                    Class<?> resolvableProfileClass = Class.forName("net.minecraft.world.item.component.ResolvableProfile");
                    Object resolvableProfile = resolvableProfileClass.getConstructor(GameProfile.class).newInstance(profile);

                    Field profileField = meta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(meta, resolvableProfile);
                } catch (Exception e1) {
                    try {
                        Field profileField = meta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        profileField.set(meta, profile);
                    } catch (Exception e2) {
                        meta.setOwningPlayer(Bukkit.getOfflinePlayer(profile.getId()));
                        Field profileField = meta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        Object currentProfile = profileField.get(meta);
                        if (currentProfile != null) {
                            Field propertiesField = currentProfile.getClass().getDeclaredField("properties");
                            propertiesField.setAccessible(true);
                            propertiesField.set(currentProfile, profile.getProperties());
                        }
                    }
                }
            } catch (Exception ignored) {}

            skull.setItemMeta(meta);
        }

        return skull;
    }

    public static ItemStack createRandomCustomSkull(String displayName) {
        int randomIndex = ThreadLocalRandom.current().nextInt(CUSTOM_SKULLS.length);
        return createCustomSkull(displayName, randomIndex);
    }
}