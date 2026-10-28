package de.niktr.rockets.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class RocketItemFactory {

    public static final NamespacedKey ROCKET_TIER_KEY = new NamespacedKey("niktrick_rockets", "rocket_tier");
    private static final MiniMessage MM = MiniMessage.miniMessage();

    public static ItemStack createRocket(int tier, int amount) {
        tier = Math.max(1, Math.min(6, tier));
        amount = Math.max(1, Math.min(64, amount));

        ItemStack item = new ItemStack(Material.FIREWORK_ROCKET, amount);
        FireworkMeta meta = (FireworkMeta) item.getItemMeta();
        if (meta == null) return item;

        meta.setPower(tier);
        meta.displayName(getDisplayName(tier));
        meta.lore(getLore(tier));
        meta.setEnchantmentGlintOverride(tier >= 2);
        meta.getPersistentDataContainer().set(ROCKET_TIER_KEY, PersistentDataType.INTEGER, tier);

        item.setItemMeta(meta);
        return item;
    }

    public static int getRocketTier(ItemStack item) {
        if (item == null || item.getType() != Material.FIREWORK_ROCKET) return 0;
        if (!item.hasItemMeta()) return 0;

        Integer tier = item.getItemMeta().getPersistentDataContainer().get(ROCKET_TIER_KEY, PersistentDataType.INTEGER);
        if (tier != null && tier >= 1 && tier <= 6) {
            return tier;
        }

        if (item.getItemMeta() instanceof FireworkMeta fireworkMeta) {
            int power = fireworkMeta.getPower();
            if (power >= 1 && power <= 6) {
                return power;
            }
        }

        return 0;
    }

    public static Component getDisplayName(int tier) {
        return switch (tier) {
            case 1 -> MM.deserialize("<gradient:#EEEEEE:#B0BEC5><bold>✦ Rakete Stufe 1 ✦</bold></gradient>");
            case 2 -> MM.deserialize("<gradient:#A8E6CF:#1DE9B6><bold>✦ Rakete Stufe 2 ✦</bold></gradient>");
            case 3 -> MM.deserialize("<gradient:#4FC3F7:#0077BE><bold>✦ Rakete Stufe 3 ✦</bold></gradient>");
            case 4 -> MM.deserialize("<gradient:#D946EF:#8B5CF6><bold>✦ Rakete Stufe 4 ✦</bold></gradient>");
            case 5 -> MM.deserialize("<gradient:#F59E0B:#EF4444><bold>✦ Rakete Stufe 5 ✦</bold></gradient>");
            case 6 -> MM.deserialize("<gradient:#FF007F:#FF6600:#FFEE00><bold>✦ HYPERSCHALL RAKETE STUFE 6 ✦</bold></gradient>");
            default -> MM.deserialize("<white>Rakete Stufe " + tier);
        };
    }

    public static List<Component> getLore(int tier) {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());

        switch (tier) {
            case 1 -> {
                lore.add(MM.deserialize("<gray>Stufe: <white>I (Basis)"));
                lore.add(MM.deserialize("<gray>Flugdauer: <white>Standard (~1.5s)"));
                lore.add(MM.deserialize("<gray>Schubkraft: <white>1.0x"));
                lore.add(Component.empty());
                lore.add(MM.deserialize("<dark_gray>Einfache Flugrakete für den Alltag."));
            }
            case 2 -> {
                lore.add(MM.deserialize("<gray>Stufe: <green>II (Verbessert)"));
                lore.add(MM.deserialize("<gray>Flugdauer: <green>Erhöht (~2.5s)"));
                lore.add(MM.deserialize("<gray>Schubkraft: <green>1.15x"));
                lore.add(Component.empty());
                lore.add(MM.deserialize("<dark_gray>Verbesserte Flugrakete für längere Strecken."));
            }
            case 3 -> {
                lore.add(MM.deserialize("<gray>Stufe: <aqua>III (Stark)"));
                lore.add(MM.deserialize("<gray>Flugdauer: <aqua>Lang (~3.5s)"));
                lore.add(MM.deserialize("<gray>Schubkraft: <aqua>1.30x"));
                lore.add(Component.empty());
                lore.add(MM.deserialize("<dark_gray>Starke Flugrakete für weite Reisen."));
            }
            case 4 -> {
                lore.add(MM.deserialize("<gray>Stufe: <light_purple>IV (Turboschub)"));
                lore.add(MM.deserialize("<gray>Flugdauer: <light_purple>Sehr Lang (~5.5s)"));
                lore.add(MM.deserialize("<gray>Schubkraft: <light_purple>1.55x (Turboschub)"));
                lore.add(MM.deserialize("<gray>Effekt: <light_purple>Seelenflammen-Schweif"));
                lore.add(Component.empty());
                lore.add(MM.deserialize("<dark_gray>Spezialrakete mit anhaltendem Turboschub."));
            }
            case 5 -> {
                lore.add(MM.deserialize("<gray>Stufe: <gold>V (Mega-Turbine)"));
                lore.add(MM.deserialize("<gray>Flugdauer: <gold>Extrem Lang (~8.0s)"));
                lore.add(MM.deserialize("<gray>Schubkraft: <gold>1.85x (Mega-Turbine)"));
                lore.add(MM.deserialize("<gray>Effekt: <gold>Drachenatem & Feuerfunken"));
                lore.add(Component.empty());
                lore.add(MM.deserialize("<dark_gray>High-End Triebwerk für rasanten Höhenflug."));
            }
            case 6 -> {
                lore.add(MM.deserialize("<gray>Stufe: <red><bold>VI (MAX)</bold>"));
                lore.add(MM.deserialize("<gray>Flugdauer: <gold><bold>Hyperschall (~11.0s)</bold>"));
                lore.add(MM.deserialize("<gray>Schubkraft: <red><bold>2.30x (Überschall)</bold>"));
                lore.add(MM.deserialize("<gray>Effekt: <aqua>Sonic Boom & End-Funken"));
                lore.add(Component.empty());
                lore.add(MM.deserialize("<red><bold>Achtung: Extrem hohe Geschwindigkeit!"));
                lore.add(MM.deserialize("<dark_gray>Supersonische Flugrakete."));
            }
        }
        return lore;
    }
}
