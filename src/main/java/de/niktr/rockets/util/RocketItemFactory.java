package de.niktr.rockets.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RocketItemFactory {

    private static NamespacedKey ROCKET_TIER_KEY;

    static {
        try {
            Class.forName("org.bukkit.persistence.PersistentDataContainer");
            ROCKET_TIER_KEY = new NamespacedKey("niktrick_rockets", "rocket_tier");
        } catch (Throwable ignored) {
            ROCKET_TIER_KEY = null;
        }
    }

    public static ItemStack createRocket(int tier, int amount) {
        tier = Math.max(1, Math.min(6, tier));
        amount = Math.max(1, Math.min(64, amount));

        ItemStack item = new ItemStack(XRocketVersion.FIREWORK_MAT, amount);
        ItemMeta rawMeta = item.getItemMeta();
        if (!(rawMeta instanceof FireworkMeta)) return item;
        FireworkMeta meta = (FireworkMeta) rawMeta;

        meta.setPower(tier);
        meta.setDisplayName(getDisplayName(tier));
        meta.setLore(getLore(tier));

        if (tier >= 2) {
            XRocketVersion.setGlint(meta);
        }

        if (ROCKET_TIER_KEY != null) {
            try {
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                pdc.set(ROCKET_TIER_KEY, PersistentDataType.INTEGER, tier);
            } catch (Throwable ignored) {}
        }

        item.setItemMeta(meta);
        return item;
    }

    public static int getRocketTier(ItemStack item) {
        if (item == null || item.getType() != XRocketVersion.FIREWORK_MAT) return 0;
        if (!item.hasItemMeta()) return 0;

        ItemMeta meta = item.getItemMeta();

        if (ROCKET_TIER_KEY != null) {
            try {
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                Integer tier = pdc.get(ROCKET_TIER_KEY, PersistentDataType.INTEGER);
                if (tier != null && tier >= 1 && tier <= 6) {
                    return tier;
                }
            } catch (Throwable ignored) {}
        }

        if (meta.hasLore()) {
            for (String line : meta.getLore()) {
                String clean = ChatColor.stripColor(line).trim();
                if (clean.startsWith("Stufe: I (Basis)")) return 1;
                if (clean.startsWith("Stufe: II (Verbessert)")) return 2;
                if (clean.startsWith("Stufe: III (Stark)")) return 3;
                if (clean.startsWith("Stufe: IV (Turboschub)")) return 4;
                if (clean.startsWith("Stufe: V (Mega-Turbine)")) return 5;
                if (clean.startsWith("Stufe: VI (Hyperschall)")) return 6;
                if (clean.contains("Stufe 1")) return 1;
                if (clean.contains("Stufe 2")) return 2;
                if (clean.contains("Stufe 3")) return 3;
                if (clean.contains("Stufe 4")) return 4;
                if (clean.contains("Stufe 5")) return 5;
                if (clean.contains("Stufe 6")) return 6;
            }
        }

        if (meta instanceof FireworkMeta) {
            int power = ((FireworkMeta) meta).getPower();
            if (power >= 1 && power <= 6) {
                return power;
            }
        }

        return 0;
    }

    public static String getDisplayName(int tier) {
        switch (tier) {
            case 1: return XRocketVersion.colorize("&f&l✦ Rakete Stufe 1 ✦");
            case 2: return XRocketVersion.colorize("&a&l✦ Rakete Stufe 2 ✦");
            case 3: return XRocketVersion.colorize("&b&l✦ Rakete Stufe 3 ✦");
            case 4: return XRocketVersion.colorize("&d&l✦ Rakete Stufe 4 ✦");
            case 5: return XRocketVersion.colorize("&6&l✦ Rakete Stufe 5 ✦");
            case 6: return XRocketVersion.colorize("&c&l✦ HYPERSCHALL RAKETE STUFE 6 ✦");
            default: return XRocketVersion.colorize("&fRakete Stufe " + tier);
        }
    }

    public static List<String> getLore(int tier) {
        List<String> lore = new ArrayList<String>();
        lore.add("");

        switch (tier) {
            case 1:
                lore.add(XRocketVersion.colorize("&7Stufe: &fI (Basis)"));
                lore.add(XRocketVersion.colorize("&7Flugdauer: &fStandard (~1.5s)"));
                lore.add(XRocketVersion.colorize("&7Schubkraft: &f1.0x"));
                lore.add("");
                lore.add(XRocketVersion.colorize("&8Einfache Flugrakete für den Alltag."));
                break;
            case 2:
                lore.add(XRocketVersion.colorize("&7Stufe: &aII (Verbessert)"));
                lore.add(XRocketVersion.colorize("&7Flugdauer: &aErhöht (~2.5s)"));
                lore.add(XRocketVersion.colorize("&7Schubkraft: &a1.15x"));
                lore.add("");
                lore.add(XRocketVersion.colorize("&8Verbesserte Flugrakete für längere Strecken."));
                break;
            case 3:
                lore.add(XRocketVersion.colorize("&7Stufe: &bIII (Stark)"));
                lore.add(XRocketVersion.colorize("&7Flugdauer: &bLang (~3.5s)"));
                lore.add(XRocketVersion.colorize("&7Schubkraft: &b1.30x"));
                lore.add("");
                lore.add(XRocketVersion.colorize("&8Starke Flugrakete für weite Reisen."));
                break;
            case 4:
                lore.add(XRocketVersion.colorize("&7Stufe: &dIV (Turboschub)"));
                lore.add(XRocketVersion.colorize("&7Flugdauer: &dSehr Lang (~5.5s)"));
                lore.add(XRocketVersion.colorize("&7Schubkraft: &d1.55x (Turboschub)"));
                lore.add(XRocketVersion.colorize("&7Effekt: &dSeelenflammen-Schweif"));
                lore.add("");
                lore.add(XRocketVersion.colorize("&8Spezialrakete mit anhaltendem Turboschub."));
                break;
            case 5:
                lore.add(XRocketVersion.colorize("&7Stufe: &6V (Mega-Turbine)"));
                lore.add(XRocketVersion.colorize("&7Flugdauer: &6Extrem Lang (~8.0s)"));
                lore.add(XRocketVersion.colorize("&7Schubkraft: &61.85x (Mega-Turbine)"));
                lore.add(XRocketVersion.colorize("&7Effekt: &6Drachenatem-Schweif"));
                lore.add("");
                lore.add(XRocketVersion.colorize("&8Hochentwickelte Flugturbine für maximale Geschwindigkeit."));
                break;
            case 6:
                lore.add(XRocketVersion.colorize("&7Stufe: &cVI (Hyperschall)"));
                lore.add(XRocketVersion.colorize("&7Flugdauer: &cUltimativ (~11.0s)"));
                lore.add(XRocketVersion.colorize("&7Schubkraft: &c2.30x (Hyperschall!)"));
                lore.add(XRocketVersion.colorize("&7Effekt: &cSonic-Boom & Sternenstaub"));
                lore.add("");
                lore.add(XRocketVersion.colorize("&c&lLEGENDÄR: &4Durchbricht die Schallmauer!"));
                break;
        }

        return lore;
    }
}
