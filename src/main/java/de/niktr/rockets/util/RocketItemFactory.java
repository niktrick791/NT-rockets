package de.niktr.rockets.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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
        return createRocket(tier, amount, false);
    }

    public static ItemStack createRocket(int tier, int amount, boolean isDe) {
        tier = Math.max(1, Math.min(6, tier));
        amount = Math.max(1, Math.min(64, amount));

        ItemStack item = new ItemStack(XRocketVersion.FIREWORK_MAT, amount);
        ItemMeta rawMeta = item.getItemMeta();
        if (!(rawMeta instanceof FireworkMeta)) return item;
        FireworkMeta meta = (FireworkMeta) rawMeta;

        meta.setPower(tier);
        meta.setDisplayName(getDisplayName(tier, isDe));
        meta.setLore(getLore(tier, isDe));

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
                if (clean.startsWith("Stufe: I (Basis)") || clean.startsWith("Tier: I (Basic)")) return 1;
                if (clean.startsWith("Stufe: II (Verbessert)") || clean.startsWith("Tier: II (Enhanced)")) return 2;
                if (clean.startsWith("Stufe: III (Stark)") || clean.startsWith("Tier: III (Powerful)")) return 3;
                if (clean.startsWith("Stufe: IV (Turboschub)") || clean.startsWith("Tier: IV (Turbo Boost)")) return 4;
                if (clean.startsWith("Stufe: V (Mega-Turbine)") || clean.startsWith("Tier: V (Mega Turbine)")) return 5;
                if (clean.startsWith("Stufe: VI (Hyperschall)") || clean.startsWith("Tier: VI (Hypersonic)")) return 6;
                if (clean.contains("Stufe 1") || clean.contains("Tier 1")) return 1;
                if (clean.contains("Stufe 2") || clean.contains("Tier 2")) return 2;
                if (clean.contains("Stufe 3") || clean.contains("Tier 3")) return 3;
                if (clean.contains("Stufe 4") || clean.contains("Tier 4")) return 4;
                if (clean.contains("Stufe 5") || clean.contains("Tier 5")) return 5;
                if (clean.contains("Stufe 6") || clean.contains("Tier 6")) return 6;
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

    public static String getDisplayName(int tier, boolean isDe) {
        if (isDe) {
            switch (tier) {
                case 1: return XRocketVersion.colorize("&f&l✦ Rakete Stufe 1 ✦");
                case 2: return XRocketVersion.colorize("&a&l✦ Rakete Stufe 2 ✦");
                case 3: return XRocketVersion.colorize("&b&l✦ Rakete Stufe 3 ✦");
                case 4: return XRocketVersion.colorize("&d&l✦ Rakete Stufe 4 ✦");
                case 5: return XRocketVersion.colorize("&6&l✦ Rakete Stufe 5 ✦");
                case 6: return XRocketVersion.colorize("&c&l✦ HYPERSCHALL RAKETE STUFE 6 ✦");
                default: return XRocketVersion.colorize("&fRakete Stufe " + tier);
            }
        } else {
            switch (tier) {
                case 1: return XRocketVersion.colorize("&f&l✦ Rocket Tier 1 ✦");
                case 2: return XRocketVersion.colorize("&a&l✦ Rocket Tier 2 ✦");
                case 3: return XRocketVersion.colorize("&b&l✦ Rocket Tier 3 ✦");
                case 4: return XRocketVersion.colorize("&d&l✦ Rocket Tier 4 ✦");
                case 5: return XRocketVersion.colorize("&6&l✦ Rocket Tier 5 ✦");
                case 6: return XRocketVersion.colorize("&c&l✦ HYPERSONIC ROCKET TIER 6 ✦");
                default: return XRocketVersion.colorize("&fRocket Tier " + tier);
            }
        }
    }

    public static List<String> getLore(int tier, boolean isDe) {
        List<String> lore = new ArrayList<String>();
        lore.add("");

        if (isDe) {
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
                    lore.add(XRocketVersion.colorize("&7Effekt: &6Drachenatem & Feuerschweif"));
                    lore.add("");
                    lore.add(XRocketVersion.colorize("&8High-End Rakete für maximale Reichweite."));
                    break;
                case 6:
                    lore.add(XRocketVersion.colorize("&7Stufe: &cVI (Hyperschall)"));
                    lore.add(XRocketVersion.colorize("&7Flugdauer: &cMaximum (~11.0s)"));
                    lore.add(XRocketVersion.colorize("&7Schubkraft: &c2.30x (Hyperschall-Speed)"));
                    lore.add(XRocketVersion.colorize("&7Effekt: &bSonic Boom & Endstab-Schweif"));
                    lore.add("");
                    lore.add(XRocketVersion.colorize("&8Das Nonplusultra der Flugraketen-Technologie."));
                    break;
            }
        } else {
            switch (tier) {
                case 1:
                    lore.add(XRocketVersion.colorize("&7Tier: &fI (Basic)"));
                    lore.add(XRocketVersion.colorize("&7Flight Duration: &fStandard (~1.5s)"));
                    lore.add(XRocketVersion.colorize("&7Boost Power: &f1.0x"));
                    lore.add("");
                    lore.add(XRocketVersion.colorize("&8Simple flight rocket for everyday travel."));
                    break;
                case 2:
                    lore.add(XRocketVersion.colorize("&7Tier: &aII (Enhanced)"));
                    lore.add(XRocketVersion.colorize("&7Flight Duration: &aExtended (~2.5s)"));
                    lore.add(XRocketVersion.colorize("&7Boost Power: &a1.15x"));
                    lore.add("");
                    lore.add(XRocketVersion.colorize("&8Enhanced rocket for longer journeys."));
                    break;
                case 3:
                    lore.add(XRocketVersion.colorize("&7Tier: &bIII (Powerful)"));
                    lore.add(XRocketVersion.colorize("&7Flight Duration: &bLong (~3.5s)"));
                    lore.add(XRocketVersion.colorize("&7Boost Power: &b1.30x"));
                    lore.add("");
                    lore.add(XRocketVersion.colorize("&8Powerful rocket for vast distances."));
                    break;
                case 4:
                    lore.add(XRocketVersion.colorize("&7Tier: &dIV (Turbo Boost)"));
                    lore.add(XRocketVersion.colorize("&7Flight Duration: &dVery Long (~5.5s)"));
                    lore.add(XRocketVersion.colorize("&7Boost Power: &d1.55x (Turbo Boost)"));
                    lore.add(XRocketVersion.colorize("&7Effect: &dSoul Flame Trail"));
                    lore.add("");
                    lore.add(XRocketVersion.colorize("&8Special rocket with sustained turbo propulsion."));
                    break;
                case 5:
                    lore.add(XRocketVersion.colorize("&7Tier: &6V (Mega Turbine)"));
                    lore.add(XRocketVersion.colorize("&7Flight Duration: &6Extremely Long (~8.0s)"));
                    lore.add(XRocketVersion.colorize("&7Boost Power: &61.85x (Mega Turbine)"));
                    lore.add(XRocketVersion.colorize("&7Effect: &6Dragon Breath & Flame Trail"));
                    lore.add("");
                    lore.add(XRocketVersion.colorize("&8High-end rocket for supreme velocity."));
                    break;
                case 6:
                    lore.add(XRocketVersion.colorize("&7Tier: &cVI (Hypersonic)"));
                    lore.add(XRocketVersion.colorize("&7Flight Duration: &cMaximum (~11.0s)"));
                    lore.add(XRocketVersion.colorize("&7Boost Power: &c2.30x (Hypersonic Velocity)"));
                    lore.add(XRocketVersion.colorize("&7Effect: &bSonic Boom & End Rod Sparks"));
                    lore.add("");
                    lore.add(XRocketVersion.colorize("&8The ultimate rocket engineered for supreme distance."));
                    break;
            }
        }

        return lore;
    }
}
