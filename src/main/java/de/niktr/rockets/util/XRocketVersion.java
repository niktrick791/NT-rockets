package de.niktr.rockets.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class XRocketVersion {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    public static final Material FIREWORK_MAT;
    public static final Material GUNPOWDER_MAT;

    static {
        Material f = Material.matchMaterial("FIREWORK_ROCKET");
        if (f == null) f = Material.matchMaterial("FIREWORK");
        FIREWORK_MAT = f != null ? f : Material.PAPER;

        Material g = Material.matchMaterial("GUNPOWDER");
        if (g == null) g = Material.matchMaterial("SULPHUR");
        GUNPOWDER_MAT = g != null ? g : Material.GLOWSTONE_DUST;
    }

    private XRocketVersion() {}

    public static Material safeMaterial(String modernName, String legacyName, Material fallback) {
        Material m = Material.matchMaterial(modernName);
        if (m != null) return m;
        if (legacyName != null) {
            m = Material.matchMaterial(legacyName);
            if (m != null) return m;
        }
        return fallback;
    }

    public static String colorize(String text) {
        if (text == null || text.isEmpty()) return "";
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("&x");
            for (char c : hex.toCharArray()) {
                replacement.append('&').append(c);
            }
            matcher.appendReplacement(sb, replacement.toString());
        }
        matcher.appendTail(sb);
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }

    public static ItemStack getItemInHand(Player player) {
        if (player == null) return null;
        try {
            return player.getInventory().getItemInMainHand();
        } catch (NoSuchMethodError e) {
            try {
                Method m = player.getClass().getMethod("getItemInHand");
                return (ItemStack) m.invoke(player);
            } catch (Throwable ex) {
                return null;
            }
        }
    }

    public static void setItemInHand(Player player, ItemStack item) {
        if (player == null) return;
        try {
            player.getInventory().setItemInMainHand(item);
        } catch (NoSuchMethodError e) {
            try {
                Method m = player.getClass().getMethod("setItemInHand", ItemStack.class);
                m.invoke(player, item);
            } catch (Throwable ignored) {}
        }
    }

    public static boolean isGliding(Player player) {
        if (player == null) return false;
        try {
            Method m = player.getClass().getMethod("isGliding");
            return (Boolean) m.invoke(player);
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static void playSound(Player player, String primarySound, String fallbackSound, float volume, float pitch) {
        if (player == null) return;
        try {
            Sound s = Sound.valueOf(primarySound);
            player.playSound(player.getLocation(), s, volume, pitch);
            return;
        } catch (Throwable ignored) {}

        if (fallbackSound != null) {
            try {
                Sound s = Sound.valueOf(fallbackSound);
                player.playSound(player.getLocation(), s, volume, pitch);
            } catch (Throwable ignored) {}
        }
    }

    public static void spawnParticle(Location loc, String modernParticle, String legacyParticle, int count, double ox, double oy, double oz, double speed) {
        if (loc == null || loc.getWorld() == null) return;
        try {
            Particle p = Particle.valueOf(modernParticle);
            loc.getWorld().spawnParticle(p, loc, count, ox, oy, oz, speed);
            return;
        } catch (Throwable ignored) {}

        if (legacyParticle != null) {
            try {
                Particle p = Particle.valueOf(legacyParticle);
                loc.getWorld().spawnParticle(p, loc, count, ox, oy, oz, speed);
            } catch (Throwable ignored) {}
        }
    }

    public static ShapelessRecipe createShapelessRecipe(Plugin plugin, String keyName, ItemStack result) {
        try {
            Class<?> keyClass = Class.forName("org.bukkit.NamespacedKey");
            Constructor<ShapelessRecipe> ctor = ShapelessRecipe.class.getConstructor(keyClass, ItemStack.class);
            NamespacedKey key = new NamespacedKey(plugin, keyName);
            return ctor.newInstance(key, result);
        } catch (Throwable e) {
            try {
                Constructor<ShapelessRecipe> ctor = ShapelessRecipe.class.getConstructor(ItemStack.class);
                return ctor.newInstance(result);
            } catch (Throwable ex) {
                return null;
            }
        }
    }

    public static void setGlint(ItemMeta meta) {
        if (meta == null) return;
        try {
            Method m = meta.getClass().getMethod("setEnchantmentGlintOverride", boolean.class);
            m.invoke(meta, true);
            return;
        } catch (Throwable ignored) {}

        try {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } catch (Throwable ignored) {}
    }
}
