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
package dev.noah.perplayerkit;

import org.bukkit.Material;

public class PublicKit {
    public String id;
    public String name;
    public Material icon;
    public int slot;

    public PublicKit(String id, String name, Material icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.slot = -1;
    }

    public PublicKit(String id, String name, Material icon, int slot) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.slot = slot;
    }
}