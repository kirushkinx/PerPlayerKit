/*
 * Copyright 2022-2025 Noah Ross
 *
 * This file is part of PerPlayerKit.
 *
 * PerPlayerKit is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * PerPlayerKit is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with PerPlayerKit. If not, see <https://www.gnu.org/licenses/>.
 */
package dev.noah.perplayerkit.gui;

import dev.noah.perplayerkit.ItemFilter;
import dev.noah.perplayerkit.KitManager;
import dev.noah.perplayerkit.KitRoomDataManager;
import dev.noah.perplayerkit.PublicKit;
import dev.noah.perplayerkit.util.BroadcastManager;
import dev.noah.perplayerkit.util.IDUtil;
import dev.noah.perplayerkit.util.PlayerUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.slot.ClickOptions;
import org.ipvp.canvas.slot.Slot;
import org.ipvp.canvas.type.ChestMenu;
import dev.noah.perplayerkit.util.SkullUtils;
import java.awt.Color;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import static dev.noah.perplayerkit.gui.ItemUtil.addHideFlags;
import static dev.noah.perplayerkit.gui.ItemUtil.createItem;

public class GUI {
    private final Plugin plugin;
    private final boolean filterItemsOnImport;
    private static Map<Player, Boolean> kitDeletionFlag = new WeakHashMap<>();

    public GUI(Plugin plugin) {
        this.plugin = plugin;
        this.filterItemsOnImport = plugin.getConfig().getBoolean("anti-exploit.import-filter", false);
    }

    public static boolean removeKitDeletionFlag(Player player) {
        return kitDeletionFlag.remove(player) != null;
    }

    public static void addLoadPublicKit(Slot slot, String id) {
        slot.setClickHandler((player, info) -> KitManager.get().loadPublicKit(player, id));
    }

    public static Menu createPublicKitMenu() {
        return ChestMenu.builder(3).title(ChatColor.BLUE + "Premade Kits").build();
    }

    public void OpenKitMenu(Player p, int slot) {
        Menu menu = createKitMenu(slot);

        if (KitManager.get().getItemStackArrayById(p.getUniqueId().toString() + slot) != null) {
            ItemStack[] kit = KitManager.get().getItemStackArrayById(p.getUniqueId().toString() + slot);
            for (int i = 0; i < 41; i++) {
                menu.getSlot(i).setItem(kit[i]);
            }
        }
        for (int i = 0; i < 41; i++) {
            allowModification(menu.getSlot(i));
        }
        for (int i = 41; i < 54; i++) {
            menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
        }
        menu.getSlot(45).setItem(createItem(Material.CHAINMAIL_BOOTS, 1, "&7Boots"));
        menu.getSlot(46).setItem(createItem(Material.CHAINMAIL_LEGGINGS, 1, "&7Leggings"));
        menu.getSlot(47).setItem(createItem(Material.CHAINMAIL_CHESTPLATE, 1, "&7Chestplate"));
        menu.getSlot(48).setItem(createItem(Material.CHAINMAIL_HELMET, 1, "&7Helmet"));
        menu.getSlot(49).setItem(createItem(Material.SHIELD, 1, "&7Offhand"));

        menu.getSlot(51).setItem(createItem(Material.CHEST, 1, "&a&lImport from inventory"));
        menu.getSlot(52).setItem(createItem(Material.BARRIER, 1, "&c&lClear", "&7● Shift-click to clear"));
        menu.getSlot(53).setItem(createItem(Material.MANGROVE_DOOR, 1, "&c&lBack"));
        addMainButton(menu.getSlot(53));
        addClear(menu.getSlot(52));
        addImport(menu.getSlot(51));
        menu.setCursorDropHandler(Menu.ALLOW_CURSOR_DROPPING);

        menu.open(p);
    }

    public void OpenPublicKitEditor(Player p, String kitId) {
        Menu menu = createPublicKitMenu(kitId);

        if (KitManager.get().getItemStackArrayById(IDUtil.getPublicKitId(kitId)) != null) {
            ItemStack[] kit = KitManager.get().getItemStackArrayById(IDUtil.getPublicKitId(kitId));
            for (int i = 0; i < 41; i++) {
                menu.getSlot(i).setItem(kit[i]);
            }
        }
        for (int i = 0; i < 41; i++) {
            allowModification(menu.getSlot(i));
        }
        for (int i = 41; i < 54; i++) {
            menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
        }
        menu.getSlot(45).setItem(createItem(Material.CHAINMAIL_BOOTS, 1, "&7Boots"));
        menu.getSlot(46).setItem(createItem(Material.CHAINMAIL_LEGGINGS, 1, "&7Leggings"));
        menu.getSlot(47).setItem(createItem(Material.CHAINMAIL_CHESTPLATE, 1, "&7Chestplate"));
        menu.getSlot(48).setItem(createItem(Material.CHAINMAIL_HELMET, 1, "&7Helmet"));
        menu.getSlot(49).setItem(createItem(Material.SHIELD, 1, "&7Offhand"));

        menu.getSlot(51).setItem(createItem(Material.CHEST, 1, "&a&lImport from inventory"));
        menu.getSlot(52).setItem(createItem(Material.BARRIER, 1, "&c&lClear", "&7● Shift-click to clear"));
        menu.getSlot(53).setItem(createItem(Material.MANGROVE_DOOR, 1, "&c&lBack"));
        addMainButton(menu.getSlot(53));
        addClear(menu.getSlot(52));
        addImport(menu.getSlot(51));
        menu.setCursorDropHandler(Menu.ALLOW_CURSOR_DROPPING);

        menu.open(p);
    }

    public void OpenECKitKenu(Player p, int slot) {
        Menu menu = createECMenu(slot);

        for (int i = 27; i < 36; i++) {
            menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
        }
        if (KitManager.get().getItemStackArrayById(p.getUniqueId() + "ec" + slot) != null) {
            ItemStack[] kit = KitManager.get().getItemStackArrayById(p.getUniqueId() + "ec" + slot);
            for (int i = 0; i < 27; i++) {
                menu.getSlot(i).setItem(kit[i]);
            }
        }
        for (int i = 0; i < 27; i++) {
            allowModification(menu.getSlot(i));
        }
        menu.getSlot(30).setItem(createItem(Material.ENDER_CHEST, 1, "&a&lImport from enderchest"));
        menu.getSlot(31).setItem(createItem(Material.BARRIER, 1, "&c&lClear", "&7● Shift-click to clear"));
        menu.getSlot(32).setItem(createItem(Material.MANGROVE_DOOR, 1, "&c&lBack"));
        addMainButton(menu.getSlot(32));
        addClear(menu.getSlot(31), 0, 27);
        addImportEC(menu.getSlot(30));
        menu.setCursorDropHandler(Menu.ALLOW_CURSOR_DROPPING);
        menu.open(p);
    }

    public void InspectKit(Player p, UUID target, int slot) {
        Menu menu = createInspectMenu(slot, target);

        KitManager.get().loadPlayerDataFromDB(target);

        if (KitManager.get().hasKit(target, slot)) {
            ItemStack[] kit = KitManager.get().getPlayerKit(target, slot);
            for (int i = 0; i < 41; i++) {
                menu.getSlot(i).setItem(kit[i]);
            }
        }

        for (int i = 41; i < 54; i++) {
            menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
        }
        menu.getSlot(45).setItem(createItem(Material.CHAINMAIL_BOOTS, 1, "&7Boots"));
        menu.getSlot(46).setItem(createItem(Material.CHAINMAIL_LEGGINGS, 1, "&7Leggings"));
        menu.getSlot(47).setItem(createItem(Material.CHAINMAIL_CHESTPLATE, 1, "&7Chestplate"));
        menu.getSlot(48).setItem(createItem(Material.CHAINMAIL_HELMET, 1, "&7Helmet"));
        menu.getSlot(49).setItem(createItem(Material.SHIELD, 1, "&7Offhand"));

        menu.getSlot(53).setItem(createItem(Material.MANGROVE_DOOR, 1, "&c&lBack"));
        menu.getSlot(53).setClickHandler((player, info) -> info.getClickedMenu().close());

        if (p.hasPermission("perplayerkit.admin")) {
            for (int i = 0; i < 41; i++) {
                allowModification(menu.getSlot(i));
            }
            menu.getSlot(52).setItem(createItem(Material.BARRIER, 1, "&c&lClear", "&7● Shift-click to delete kit"));
            addClearKit(menu.getSlot(52), target, slot);
        }

        menu.setCursorDropHandler(Menu.ALLOW_CURSOR_DROPPING);
        menu.open(p);
    }

    public void InspectEc(Player p, UUID target, int slot) {
        Menu menu = createInspectEcMenu(slot, target);

        KitManager.get().loadPlayerDataFromDB(target);

        if (KitManager.get().hasEC(target, slot)) {
            ItemStack[] kit = KitManager.get().getPlayerEC(target, slot);
            for (int i = 0; i < 27; i++) {
                menu.getSlot(i).setItem(kit[i]);
            }
        }

        for (int i = 27; i < 36; i++) {
            menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
        }
        menu.getSlot(31).setItem(createItem(Material.MANGROVE_DOOR, 1, "&c&lBack"));
        menu.getSlot(31).setClickHandler((player, info) -> info.getClickedMenu().close());

        if (p.hasPermission("perplayerkit.admin")) {
            for (int i = 0; i < 27; i++) {
                allowModification(menu.getSlot(i));
            }
            menu.getSlot(32).setItem(createItem(Material.BARRIER, 1, "&c&lClear", "&7● Shift-click to delete enderchest"));
            addClearEnderchest(menu.getSlot(32), target, slot);
        }

        menu.setCursorDropHandler(Menu.ALLOW_CURSOR_DROPPING);
        menu.open(p);
    }

    public void OpenMainMenu(Player p) {
        Menu menu = createMainMenu();
        for (int i = 0; i < 45; i++) {
            menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
        }
        for (int i = 9; i < 18; i++) {
            if (KitManager.get().getItemStackArrayById(p.getUniqueId().toString() + (i - 8)) != null) {
                menu.getSlot(i).setItem(createItem(Material.LIME_SHULKER_BOX, 1, "&aKit " + (i - 8), "&7● Left click to load kit", "&7● Right click to edit kit"));
            } else {
                menu.getSlot(i).setItem(createItem(Material.RED_SHULKER_BOX, 1, "&cKit " + (i - 8) + " &c(empty)", "&7● Click to create kit"));
            }
            addEditLoad(menu.getSlot(i), i - 8);
        }
        for (int i = 18; i < 27; i++) {
            if (KitManager.get().getItemStackArrayById(p.getUniqueId() + "ec" + (i - 17)) != null) {
                menu.getSlot(i).setItem(createItem(Material.ENDER_CHEST, 1, "&aEnderchest " + (i - 17), "&7● Left click to load enderchest", "&7● Right click to edit enderchest"));
                addEditLoadEC(menu.getSlot(i), i - 17);
            } else {
                menu.getSlot(i).setItem(createItem(Material.ENDER_CHEST, 1, "&cEnderchest " + (i - 17) + " &c(empty)", "&7● Click to create enderchest"));
                addEditEC(menu.getSlot(i), i - 17);
            }
        }

        for (int i = 37; i < 44; i++) {
            menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
        }

        menu.getSlot(29).setItem(createItem(Material.NETHER_STAR, 1, "&a&lKit Room"));
        menu.getSlot(30).setItem(createItem(Material.BOOKSHELF, 1, "&e&lPremade Kits"));
        menu.getSlot(31).setItem(createItem(Material.REDSTONE_BLOCK, 1, "&c&lClear Inventory", "&7● Shift click"));
        menu.getSlot(32).setItem(createItem(Material.COMPASS, 1, "&a&lShare Kits", "&7● /sharekit <slot>"));
        menu.getSlot(33).setItem(createItem(Material.EXPERIENCE_BOTTLE, 1, "&a&lRepair Items"));
        menu.getSlot(44).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, "&f\uD83D\uDC08"));
        addMeowButton(menu.getSlot(44));
        addRepairButton(menu.getSlot(33));
        addKitRoom(menu.getSlot(29));
        addPublicKitMenu(menu.getSlot(30));
        addClearButton(menu.getSlot(31));

        menu.setCursorDropHandler(Menu.ALLOW_CURSOR_DROPPING);
        menu.open(p);
    }

    public void OpenKitRoom(Player p) {
        OpenKitRoom(p, 0);
    }

    public void OpenKitRoom(Player p, int page) {
        Menu menu = createKitRoom();
        for (int i = 0; i < 45; i++) {
            allowModification(menu.getSlot(i));
        }
        for (int i = 45; i < 54; i++) {
            menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
        }
        if (KitRoomDataManager.get().getKitRoomPage(page) != null) {
            for (int i = 0; i < 45; i++) {
                menu.getSlot(i).setItem(KitRoomDataManager.get().getKitRoomPage(page)[i]);
            }
        }

        menu.getSlot(45).setItem(createItem(Material.BEACON, 1, "&3&lRefill"));
        addKitRoom(menu.getSlot(45), page);

        if (!p.hasPermission("perplayerkit.editkitroom")) {
            menu.getSlot(53).setItem(createItem(Material.MANGROVE_DOOR, 1, "&c&lBack"));
            addMainButton(menu.getSlot(53));
        } else {
            menu.getSlot(53).setItem(createItem(Material.BARRIER, page + 1, "&c&lEdit menu", "&7Shift right click to save"));
        }
        addKitRoom(menu.getSlot(47), 0);
        addKitRoom(menu.getSlot(48), 1);
        addKitRoom(menu.getSlot(49), 2);
        addKitRoom(menu.getSlot(50), 3);
        addKitRoom(menu.getSlot(51), 4);

        for (int i = 1; i < 6; i++) {
            menu.getSlot(46 + i).setItem(addHideFlags(createItem(Material.valueOf(plugin.getConfig().getString("kitroom.items." + i + ".material")), "&r" + plugin.getConfig().getString("kitroom.items." + i + ".name"))));
        }

        menu.getSlot(page + 47).setItem(ItemUtil.addEnchantLook(menu.getSlot(page + 47).getItem(p)));

        menu.setCursorDropHandler(Menu.ALLOW_CURSOR_DROPPING);
        menu.open(p);
    }

    public Menu ViewPublicKitMenu(Player p, String id) {
        ItemStack[] kit = KitManager.get().getPublicKit(id);

        if (kit == null) {
            p.sendMessage(ChatColor.RED + "Kit not found");
            if (p.hasPermission("perplayerkit.admin")) {
                p.sendMessage(ChatColor.RED + "To assign a kit to this publickit use /savepublickit <id>");
            }
            return null;
        }
        Menu menu = ChestMenu.builder(6).title(ChatColor.BLUE + "Viewing Public Kit " + id).build();

        for (int i = 0; i < 54; i++) {
            menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
        }

        for (int i = 9; i < 36; i++) {
            menu.getSlot(i).setItem(kit[i]);
        }
        for (int i = 0; i < 9; i++) {
            menu.getSlot(i + 36).setItem(kit[i]);
        }
        for (int i = 36; i < 41; i++) {
            menu.getSlot(i + 9).setItem(kit[i]);
        }

        menu.getSlot(52).setItem(createItem(Material.AXOLOTL_BUCKET, 1, "&a&lLoad kit"));
        menu.getSlot(53).setItem(createItem(Material.MANGROVE_DOOR, 1, "&c&lBack"));
        addPublicKitMenu(menu.getSlot(53));
        addLoadPublicKit(menu.getSlot(52), id);

        menu.open(p);

        return menu;
    }

    public void OpenPublicKitMenu(Player player) {
        Menu menu = createPublicKitMenu();

        List<PublicKit> publicKitList = KitManager.get().getPublicKitList();

        if (publicKitList == null || publicKitList.isEmpty()) {
            for (int i = 0; i < 27; i++) {
                menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
            }
            ItemStack placeholderHead = SkullUtils.createCustomSkull(
                    ChatColor.of(new Color(196, 158, 243)) + "Nothing here yet...", 0);
            menu.getSlot(13).setItem(placeholderHead);
            addMeowButton(menu.getSlot(13));
        } else {
            for (int i = 0; i < 27; i++) {
                menu.getSlot(i).setItem(createItem(Material.BLUE_STAINED_GLASS_PANE, 1, " "));
            }

            for (PublicKit publicKit : publicKitList) {
                int targetSlot = (publicKit.slot >= 0 && publicKit.slot <= 26) ? publicKit.slot : 13;

                if (KitManager.get().hasPublicKit(publicKit.id)) {
                    if (player.hasPermission("perplayerkit.admin")) {
                        menu.getSlot(targetSlot).setItem(createItem(publicKit.icon, 1,
                                ChatColor.RESET + publicKit.name, "&7● [ADMIN] Shift-click to edit"));
                    } else {
                        menu.getSlot(targetSlot).setItem(createItem(publicKit.icon, 1,
                                ChatColor.RESET + publicKit.name));
                    }
                    addPublicKitButton(menu.getSlot(targetSlot), publicKit.id);
                } else {
                    if (player.hasPermission("perplayerkit.admin")) {
                        menu.getSlot(targetSlot).setItem(createItem(publicKit.icon, 1,
                                ChatColor.RESET + publicKit.name + " &c&l[UNASSIGNED]",
                                "&7● Admins have not yet setup this kit yet",
                                "&7● [ADMIN] Shift click to edit"));
                        addAdminPublicKitButton(menu.getSlot(targetSlot), publicKit.id);
                    } else {
                        menu.getSlot(targetSlot).setItem(createItem(publicKit.icon, 1,
                                ChatColor.RESET + publicKit.name + " &c&l[UNASSIGNED]",
                                "&7● Admins have not yet setup this kit yet"));
                    }
                }
            }
        }

        menu.open(player);
    }

    public void addClear(Slot slot) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType().isShiftClick()) {
                Menu m = info.getClickedMenu();
                for (int i = 0; i < 41; i++) {
                    m.getSlot(i).setItem((ItemStack) null);
                }
            }
        });
    }

    public void addClear(Slot slot, int start, int end) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType().isShiftClick()) {
                Menu m = info.getClickedMenu();
                for (int i = start; i < end; i++) {
                    m.getSlot(i).setItem((ItemStack) null);
                }
            }
        });
    }

    public void addClearKit(Slot slot, UUID target, int slotNumber) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType() == ClickType.SHIFT_LEFT || info.getClickType() == ClickType.SHIFT_RIGHT) {
                boolean deleted = KitManager.get().deleteKit(target, slotNumber);
                if (deleted) {
                    kitDeletionFlag.put(player, true);
                    player.sendMessage(ChatColor.GREEN + "Kit " + slotNumber + " deleted for player!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                    info.getClickedMenu().close();
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to delete kit!");
                }
            }
        });
    }

    public void addClearEnderchest(Slot slot, UUID target, int slotNumber) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType() == ClickType.SHIFT_LEFT || info.getClickType() == ClickType.SHIFT_RIGHT) {
                boolean deleted = KitManager.get().deleteEnderchest(target, slotNumber);
                if (deleted) {
                    kitDeletionFlag.put(player, true);
                    player.sendMessage(ChatColor.GREEN + "Enderchest " + slotNumber + " deleted for player!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                    info.getClickedMenu().close();
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to delete enderchest!");
                }
            }
        });
    }

    public void addPublicKitButton(Slot slot, String id) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType() == ClickType.LEFT) {
                KitManager.get().loadPublicKit(player, id);
            } else if (info.getClickType() == ClickType.RIGHT) {
                Menu m = ViewPublicKitMenu(player, id);
                if (m != null) {
                    m.open(player);
                }
            }
        });
    }

    public void addAdminPublicKitButton(Slot slot, String id) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType().isShiftClick()) {
                OpenPublicKitEditor(player, id);
                return;
            }
            if (info.getClickType() == ClickType.LEFT) {
                KitManager.get().loadPublicKit(player, id);
            } else if (info.getClickType() == ClickType.RIGHT) {
                Menu m = ViewPublicKitMenu(player, id);
                if (m != null) {
                    m.open(player);
                }
            }
        });
    }

    public void addMainButton(Slot slot) {
        slot.setClickHandler((player, info) -> OpenMainMenu(player));
    }

    public void addKitRoom(Slot slot) {
        slot.setClickHandler((player, info) -> {
            OpenKitRoom(player);
            BroadcastManager.get().broadcastPlayerOpenedKitRoom(player);
        });
    }

    public void addKitRoom(Slot slot, int page) {
        slot.setClickHandler((player, info) -> OpenKitRoom(player, page));
    }

    public void addPublicKitMenu(Slot slot) {
        slot.setClickHandler((player, info) -> OpenPublicKitMenu(player));
    }

    public void addKitRoomSaveButton(Slot slot, int page) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType().isRightClick() && info.getClickType().isShiftClick()) {
                ItemStack[] data = new ItemStack[45];
                for (int i = 0; i < 41; i++) {
                    data[i] = player.getInventory().getContents()[i];
                }
                KitRoomDataManager.get().setKitRoom(page, data);
                player.sendMessage("saved menu");
            }
        });
    }

    public void addRepairButton(Slot slot) {
        slot.setClickHandler((player, info) -> {
            BroadcastManager.get().broadcastPlayerRepaired(player);
            PlayerUtil.repairAll(player);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        });
    }

    public void addMeowButton(Slot slot) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType() == ClickType.RIGHT) {
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_STRAY_AMBIENT, 1f, 1f);
            } else if (info.getClickType() == ClickType.LEFT) {
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1f, 1f);
            } else if (info.getClickType() == ClickType.SHIFT_LEFT || info.getClickType() == ClickType.SHIFT_RIGHT) {
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_DEATH, 1f, 1f);
            }
        });
    }

    public void addClearButton(Slot slot) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType().isShiftClick()) {
                player.getInventory().clear();
                player.sendMessage(ChatColor.GREEN + "Inventory cleared");
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            }
        });
    }

    public void addImport(Slot slot) {
        slot.setClickHandler((player, info) -> {
            Menu m = info.getClickedMenu();
            ItemStack[] inv;
            if (filterItemsOnImport) {
                inv = ItemFilter.get().filterItemStack(player.getInventory().getContents());
            } else {
                inv = player.getInventory().getContents();
            }
            for (int i = 0; i < 41; i++) {
                m.getSlot(i).setItem(inv[i]);
            }
        });
    }

    public void addImportEC(Slot slot) {
        slot.setClickHandler((player, info) -> {
            Menu m = info.getClickedMenu();
            ItemStack[] inv;
            if (filterItemsOnImport) {
                inv = ItemFilter.get().filterItemStack(player.getEnderChest().getContents());
            } else {
                inv = player.getEnderChest().getContents();
            }
            for (int i = 0; i < 27; i++) {
                m.getSlot(i).setItem(inv[i]);
            }
        });
    }

    public void addEdit(Slot slot, int i) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType().isLeftClick() || info.getClickType().isRightClick()) {
                OpenKitMenu(player, i);
            }
        });
    }

    public void addEditEC(Slot slot, int i) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType().isLeftClick() || info.getClickType().isRightClick()) {
                LazyOpenECKitMenu(player, i);
            }
        });
    }

    public void addLoad(Slot slot, int i) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType() == ClickType.LEFT || info.getClickType() == ClickType.SHIFT_LEFT) {
                KitManager.get().loadKit(player, i);
                info.getClickedMenu().close();
            }
        });
    }

    public void addEditLoad(Slot slot, int i) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType() == ClickType.LEFT || info.getClickType() == ClickType.SHIFT_LEFT) {
                KitManager.get().loadKit(player, i);
                info.getClickedMenu().close();
            } else if (info.getClickType() == ClickType.RIGHT || info.getClickType() == ClickType.SHIFT_RIGHT) {
                OpenKitMenu(player, i);
            }
        });
    }

    public void addEditLoadEC(Slot slot, int i) {
        slot.setClickHandler((player, info) -> {
            if (info.getClickType() == ClickType.LEFT || info.getClickType() == ClickType.SHIFT_LEFT) {
                KitManager.get().loadEnderchest(player, i);
                info.getClickedMenu().close();
            } else if (info.getClickType() == ClickType.RIGHT || info.getClickType() == ClickType.SHIFT_RIGHT) {
                LazyOpenECKitMenu(player, i);
            }
        });
    }

    public Menu createKitMenu(int slot) {
        return ChestMenu.builder(6).title(ChatColor.BLUE + "Kit " + slot).build();
    }

    public Menu createPublicKitMenu(String id) {
        return ChestMenu.builder(6).title(ChatColor.BLUE + "Public Kit " + id).build();
    }

    public Menu createECMenu(int slot) {
        return ChestMenu.builder(4).title(ChatColor.BLUE + "Enderchest " + slot).build();
    }

    public Menu createInspectMenu(int slot, UUID target) {
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
        String playerName = targetPlayer.getName() != null ? targetPlayer.getName() : "Unknown Player";
        return ChestMenu.builder(6).title(ChatColor.BLUE + "Inspecting " + playerName + "'s kit " + slot).build();
    }

    public Menu createInspectEcMenu(int slot, UUID target) {
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
        String playerName = targetPlayer.getName() != null ? targetPlayer.getName() : "Unknown Player";
        return ChestMenu.builder(4).title(ChatColor.BLUE + "Inspecting " + playerName + "'s enderchest " + slot).build();
    }

    public Menu createMainMenu() {
        return ChestMenu.builder(5).title(ChatColor.BLUE + "Kit Menu").build();
    }

    public Menu createKitRoom() {
        return ChestMenu.builder(6).title(ChatColor.BLUE + "Кit Room").build();
    }

    public void allowModification(Slot slot) {
        ClickOptions options = ClickOptions.ALLOW_ALL;
        slot.setClickOptions(options);
    }

    public void LazyOpenECKitMenu(Player p, int slot) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> OpenECKitKenu(p, slot), 1L);
    }
}