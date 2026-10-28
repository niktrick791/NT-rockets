package de.niktr.rockets.managers;

import de.niktr.rockets.NiktrickRocketsPlugin;
import de.niktr.rockets.util.RocketItemFactory;
import de.niktr.rockets.util.XRocketVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RocketManager {

    private final NiktrickRocketsPlugin plugin;
    private final Map<Integer, Integer> durationTicksMap = new HashMap<Integer, Integer>();
    private final Map<Integer, Double> boostMultiplierMap = new HashMap<Integer, Double>();
    private final Map<Integer, Boolean> sustainedMap = new HashMap<Integer, Boolean>();
    private boolean recipesEnabled = true;
    private String language = "en";

    public RocketManager(NiktrickRocketsPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        language = config.getString("language", "en").toLowerCase(Locale.ROOT);
        if (!"de".equals(language) && !"en".equals(language)) {
            language = "en";
        }

        recipesEnabled = config.getBoolean("rockets.recipes-enabled", true);

        int[] defaultDurations = {25, 45, 70, 110, 160, 220};
        double[] defaultBoosts = {1.0, 1.15, 1.30, 1.55, 1.85, 2.30};
        boolean[] defaultSustained = {false, false, false, true, true, true};

        for (int i = 1; i <= 6; i++) {
            String path = "rockets.tier-" + i + ".";
            durationTicksMap.put(i, config.getInt(path + "duration-ticks", defaultDurations[i - 1]));
            boostMultiplierMap.put(i, config.getDouble(path + "boost-multiplier", defaultBoosts[i - 1]));
            sustainedMap.put(i, config.getBoolean(path + "sustained-propulsion", defaultSustained[i - 1]));
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String lang) {
        String clean = (lang != null && lang.equalsIgnoreCase("de")) ? "de" : "en";
        this.language = clean;
        plugin.getConfig().set("language", clean);
        plugin.saveConfig();
        registerRecipes();
    }

    public boolean isDe() {
        return "de".equalsIgnoreCase(language);
    }

    public void registerRecipes() {
        if (!recipesEnabled) return;

        try {
            for (int tier = 1; tier <= 6; tier++) {
                try {
                    NamespacedKey key = new NamespacedKey(plugin, "rocket_tier_" + tier);
                    Bukkit.removeRecipe(key);
                } catch (Throwable ignored) {}
            }
        } catch (Throwable ignored) {}

        Material paper = Material.PAPER;
        Material gunpowder = XRocketVersion.GUNPOWDER_MAT;
        Material fireworkMat = XRocketVersion.FIREWORK_MAT;
        boolean de = isDe();

        // Tier 1: 1 Paper + 1 Gunpowder -> 3x Tier 1
        ShapelessRecipe t1Recipe = XRocketVersion.createShapelessRecipe(plugin, "rocket_tier_1", RocketItemFactory.createRocket(1, 3, de));
        if (t1Recipe != null) {
            t1Recipe.addIngredient(paper);
            t1Recipe.addIngredient(gunpowder);
            try { Bukkit.addRecipe(t1Recipe); } catch (Throwable ignored) {}
        }

        // Tier 2: 1 Paper + 2 Gunpowder -> 3x Tier 2
        ShapelessRecipe t2Recipe = XRocketVersion.createShapelessRecipe(plugin, "rocket_tier_2", RocketItemFactory.createRocket(2, 3, de));
        if (t2Recipe != null) {
            t2Recipe.addIngredient(paper);
            t2Recipe.addIngredient(gunpowder);
            t2Recipe.addIngredient(gunpowder);
            try { Bukkit.addRecipe(t2Recipe); } catch (Throwable ignored) {}
        }

        // Tier 3: 1 Paper + 3 Gunpowder -> 3x Tier 3
        ShapelessRecipe t3Recipe = XRocketVersion.createShapelessRecipe(plugin, "rocket_tier_3", RocketItemFactory.createRocket(3, 3, de));
        if (t3Recipe != null) {
            t3Recipe.addIngredient(paper);
            t3Recipe.addIngredient(gunpowder);
            t3Recipe.addIngredient(gunpowder);
            t3Recipe.addIngredient(gunpowder);
            try { Bukkit.addRecipe(t3Recipe); } catch (Throwable ignored) {}
        }

        // Tier 4: 1x Firework + 1 Soul Sand + 1 Blaze Powder -> 1x Tier 4
        ShapelessRecipe t4Recipe = XRocketVersion.createShapelessRecipe(plugin, "rocket_tier_4", RocketItemFactory.createRocket(4, 1, de));
        if (t4Recipe != null) {
            Material soulSand = XRocketVersion.safeMaterial("SOUL_SAND", "SOUL_SAND", Material.SAND);
            Material blazePowder = XRocketVersion.safeMaterial("BLAZE_POWDER", "BLAZE_POWDER", Material.REDSTONE);
            t4Recipe.addIngredient(fireworkMat);
            t4Recipe.addIngredient(soulSand);
            t4Recipe.addIngredient(blazePowder);
            try { Bukkit.addRecipe(t4Recipe); } catch (Throwable ignored) {}
        }

        // Tier 5: 1x Firework + 1 Dragon's Breath (or Blaze Rod) + 1 Gold Ingot -> 1x Tier 5
        ShapelessRecipe t5Recipe = XRocketVersion.createShapelessRecipe(plugin, "rocket_tier_5", RocketItemFactory.createRocket(5, 1, de));
        if (t5Recipe != null) {
            Material dragonsBreath = XRocketVersion.safeMaterial("DRAGON_BREATH", null, Material.BLAZE_ROD);
            Material goldIngot = XRocketVersion.safeMaterial("GOLD_INGOT", "GOLD_INGOT", Material.GOLD_INGOT);
            t5Recipe.addIngredient(fireworkMat);
            t5Recipe.addIngredient(dragonsBreath);
            t5Recipe.addIngredient(goldIngot);
            try { Bukkit.addRecipe(t5Recipe); } catch (Throwable ignored) {}
        }

        // Tier 6: 1x Firework + 1 Echo Shard (or Eye of Ender) + 1 Netherite Scrap (or Diamond) -> 1x Tier 6
        ShapelessRecipe t6Recipe = XRocketVersion.createShapelessRecipe(plugin, "rocket_tier_6", RocketItemFactory.createRocket(6, 1, de));
        if (t6Recipe != null) {
            Material echoShard = XRocketVersion.safeMaterial("ECHO_SHARD", "EYE_OF_ENDER", Material.DIAMOND);
            Material scrap = XRocketVersion.safeMaterial("NETHERITE_SCRAP", null, Material.EMERALD);
            t6Recipe.addIngredient(fireworkMat);
            t6Recipe.addIngredient(echoShard);
            t6Recipe.addIngredient(scrap);
            try { Bukkit.addRecipe(t6Recipe); } catch (Throwable ignored) {}
        }
    }

    public void applyRocketBoost(final Player player, final Firework firework, final int tier) {
        Integer durationObj = durationTicksMap.get(tier);
        final int durationTicks = durationObj != null ? durationObj.intValue() : (25 * tier);
        Double boostObj = boostMultiplierMap.get(tier);
        double boostMult = boostObj != null ? boostObj.doubleValue() : 1.0;

        Vector dir = player.getLocation().getDirection().normalize();

        boolean isGliding = XRocketVersion.isGliding(player);
        if (isGliding) {
            player.setVelocity(player.getVelocity().add(dir.multiply(0.4 * boostMult)));
        } else {
            // Jetpack launch fallback on 1.8 or when player is not gliding
            Vector jetpackVel = dir.multiply(0.6 * boostMult);
            jetpackVel.setY(Math.max(0.6 * boostMult, jetpackVel.getY() + 0.4));
            player.setVelocity(jetpackVel);
        }

        if (firework != null) {
            try {
                java.lang.reflect.Method m = firework.getClass().getMethod("setTicksToDetonate", int.class);
                m.invoke(firework, durationTicks);
            } catch (Throwable ignored) {}
            try {
                FireworkMeta meta = firework.getFireworkMeta();
                meta.setPower(tier);
                firework.setFireworkMeta(meta);
            } catch (Throwable ignored) {}
        }

        Location loc = player.getLocation();
        if (tier == 6) {
            XRocketVersion.spawnParticle(loc, "SONIC_BOOM", "EXPLOSION_HUGE", 1, 0, 0, 0, 0.05);
            XRocketVersion.playSound(player, "ENTITY_WARDEN_SONIC_BOOM", "EXPLODE", 0.75f, 1.6f);
            XRocketVersion.playSound(player, "ENTITY_FIREWORK_ROCKET_LAUNCH", "FIREWORK_LAUNCH", 1.0f, 0.7f);
        } else if (tier == 5) {
            XRocketVersion.spawnParticle(loc, "DRAGON_BREATH", "SPELL_MOB", 15, 0.2, 0.2, 0.2, 0.05);
            XRocketVersion.playSound(player, "ENTITY_FIREWORK_ROCKET_LARGE_BLAST", "FIREWORK_LARGE_BLAST", 0.9f, 1.2f);
        } else if (tier == 4) {
            XRocketVersion.spawnParticle(loc, "SOUL_FIRE_FLAME", "FLAME", 10, 0.2, 0.2, 0.2, 0.03);
            XRocketVersion.playSound(player, "ENTITY_FIREWORK_ROCKET_LAUNCH", "FIREWORK_LAUNCH", 1.0f, 1.3f);
        } else {
            XRocketVersion.playSound(player, "ENTITY_FIREWORK_ROCKET_LAUNCH", "FIREWORK_LAUNCH", 0.9f, 1.0f);
        }

        Boolean sustainedObj = sustainedMap.get(tier);
        boolean sustained = sustainedObj != null && sustainedObj.booleanValue();

        if (sustained) {
            new BukkitRunnable() {
                int count = 0;

                @Override
                public void run() {
                    count += 2;
                    if (!player.isOnline() || player.isDead() || count >= durationTicks) {
                        cancel();
                        return;
                    }

                    if (firework != null && (!firework.isValid() || firework.isDead())) {
                        cancel();
                        return;
                    }

                    Vector lookDir = player.getLocation().getDirection().normalize();
                    double accel = 0.03;
                    switch (tier) {
                        case 4: accel = 0.06; break;
                        case 5: accel = 0.11; break;
                        case 6: accel = 0.20; break;
                    }

                    Vector newVel = player.getVelocity().add(lookDir.clone().multiply(accel));
                    if (newVel.length() > 3.2) {
                        newVel = newVel.normalize().multiply(3.2);
                    }
                    player.setVelocity(newVel);

                    Location tail = player.getLocation().subtract(lookDir.clone().multiply(0.75));

                    if (tier == 6) {
                        XRocketVersion.spawnParticle(tail, "END_ROD", "FIREWORKS_SPARK", 6, 0.15, 0.15, 0.15, 0.04);
                        XRocketVersion.spawnParticle(tail, "FIREWORK", "FIREWORKS_SPARK", 3, 0.1, 0.1, 0.1, 0.02);
                        if (count % 16 == 0) {
                            XRocketVersion.playSound(player, "ENTITY_FIREWORK_ROCKET_TWINKLE", "FIREWORK_TWINKLE", 0.5f, 1.4f);
                        }
                    } else if (tier == 5) {
                        XRocketVersion.spawnParticle(tail, "DRAGON_BREATH", "SPELL_MOB", 5, 0.12, 0.12, 0.12, 0.02);
                        XRocketVersion.spawnParticle(tail, "FLAME", "FLAME", 3, 0.1, 0.1, 0.1, 0.02);
                    } else if (tier == 4) {
                        XRocketVersion.spawnParticle(tail, "SOUL_FIRE_FLAME", "FLAME", 4, 0.1, 0.1, 0.1, 0.02);
                    } else {
                        XRocketVersion.spawnParticle(tail, "FIREWORK", "FIREWORKS_SPARK", 3, 0.1, 0.1, 0.1, 0.01);
                    }
                }
            }.runTaskTimer(plugin, 2L, 2L);
        }
    }

    public int getDurationTicks(int tier) {
        Integer d = durationTicksMap.get(tier);
        return d != null ? d.intValue() : (25 * tier);
    }

    public double getBoostMultiplier(int tier) {
        Double b = boostMultiplierMap.get(tier);
        return b != null ? b.doubleValue() : 1.0;
    }
}
