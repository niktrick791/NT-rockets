package de.niktr.rockets.managers;

import de.niktr.rockets.NiktrickRocketsPlugin;
import de.niktr.rockets.util.RocketItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class RocketManager {

    private final NiktrickRocketsPlugin plugin;
    private final Map<Integer, Integer> durationTicksMap = new HashMap<>();
    private final Map<Integer, Double> boostMultiplierMap = new HashMap<>();
    private final Map<Integer, Boolean> sustainedMap = new HashMap<>();
    private final Map<Integer, Particle> particleMap = new HashMap<>();
    private boolean recipesEnabled = true;

    public RocketManager(NiktrickRocketsPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        recipesEnabled = config.getBoolean("rockets.recipes-enabled", true);

        int[] defaultDurations = {25, 45, 70, 110, 160, 220};
        double[] defaultBoosts = {1.0, 1.15, 1.30, 1.55, 1.85, 2.30};
        boolean[] defaultSustained = {false, false, false, true, true, true};
        Particle[] defaultParticles = {
                Particle.FIREWORK,
                Particle.FIREWORK,
                Particle.FLAME,
                Particle.SOUL_FIRE_FLAME,
                Particle.DRAGON_BREATH,
                Particle.END_ROD
        };

        for (int i = 1; i <= 6; i++) {
            String path = "rockets.tier-" + i + ".";
            durationTicksMap.put(i, config.getInt(path + "duration-ticks", defaultDurations[i - 1]));
            boostMultiplierMap.put(i, config.getDouble(path + "boost-multiplier", defaultBoosts[i - 1]));
            sustainedMap.put(i, config.getBoolean(path + "sustained-propulsion", defaultSustained[i - 1]));

            String particleName = config.getString(path + "trail-particle", defaultParticles[i - 1].name());
            try {
                particleMap.put(i, Particle.valueOf(particleName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                particleMap.put(i, defaultParticles[i - 1]);
            }
        }
    }

    public void registerRecipes() {
        if (!recipesEnabled) return;

        for (int tier = 1; tier <= 6; tier++) {
            NamespacedKey key = new NamespacedKey(plugin, "rocket_tier_" + tier);
            Bukkit.removeRecipe(key);
        }

        // Tier 1: 1 Paper + 1 Gunpowder -> 3x Tier 1
        NamespacedKey t1Key = new NamespacedKey(plugin, "rocket_tier_1");
        ShapelessRecipe t1Recipe = new ShapelessRecipe(t1Key, RocketItemFactory.createRocket(1, 3));
        t1Recipe.addIngredient(Material.PAPER);
        t1Recipe.addIngredient(Material.GUNPOWDER);
        Bukkit.addRecipe(t1Recipe);

        // Tier 2: 1 Paper + 2 Gunpowder -> 3x Tier 2
        NamespacedKey t2Key = new NamespacedKey(plugin, "rocket_tier_2");
        ShapelessRecipe t2Recipe = new ShapelessRecipe(t2Key, RocketItemFactory.createRocket(2, 3));
        t2Recipe.addIngredient(Material.PAPER);
        t2Recipe.addIngredient(2, Material.GUNPOWDER);
        Bukkit.addRecipe(t2Recipe);

        // Tier 3: 1 Paper + 3 Gunpowder -> 3x Tier 3
        NamespacedKey t3Key = new NamespacedKey(plugin, "rocket_tier_3");
        ShapelessRecipe t3Recipe = new ShapelessRecipe(t3Key, RocketItemFactory.createRocket(3, 3));
        t3Recipe.addIngredient(Material.PAPER);
        t3Recipe.addIngredient(3, Material.GUNPOWDER);
        Bukkit.addRecipe(t3Recipe);

        // Tier 4: 1x Tier 3 + 1 Soul Sand + 1 Blaze Powder -> 1x Tier 4
        NamespacedKey t4Key = new NamespacedKey(plugin, "rocket_tier_4");
        ShapelessRecipe t4Recipe = new ShapelessRecipe(t4Key, RocketItemFactory.createRocket(4, 1));
        t4Recipe.addIngredient(Material.FIREWORK_ROCKET);
        t4Recipe.addIngredient(Material.SOUL_SAND);
        t4Recipe.addIngredient(Material.BLAZE_POWDER);
        Bukkit.addRecipe(t4Recipe);

        // Tier 5: 1x Tier 4 + 1 Dragon's Breath + 1 Gold Ingot -> 1x Tier 5
        NamespacedKey t5Key = new NamespacedKey(plugin, "rocket_tier_5");
        ShapelessRecipe t5Recipe = new ShapelessRecipe(t5Key, RocketItemFactory.createRocket(5, 1));
        t5Recipe.addIngredient(Material.FIREWORK_ROCKET);
        t5Recipe.addIngredient(Material.DRAGON_BREATH);
        t5Recipe.addIngredient(Material.GOLD_INGOT);
        Bukkit.addRecipe(t5Recipe);

        // Tier 6: 1x Tier 5 + 1 Echo Shard + 1 Netherite Scrap -> 1x Tier 6
        NamespacedKey t6Key = new NamespacedKey(plugin, "rocket_tier_6");
        ShapelessRecipe t6Recipe = new ShapelessRecipe(t6Key, RocketItemFactory.createRocket(6, 1));
        t6Recipe.addIngredient(Material.FIREWORK_ROCKET);
        t6Recipe.addIngredient(Material.ECHO_SHARD);
        t6Recipe.addIngredient(Material.NETHERITE_SCRAP);
        Bukkit.addRecipe(t6Recipe);
    }

    public void applyRocketBoost(Player player, Firework firework, int tier) {
        int durationTicks = durationTicksMap.getOrDefault(tier, 25 * tier);
        double boostMult = boostMultiplierMap.getOrDefault(tier, 1.0);

        // Boost initial elytra launch speed
        Vector dir = player.getLocation().getDirection().normalize();
        player.setVelocity(player.getVelocity().add(dir.multiply(0.4 * boostMult)));

        firework.setTicksToDetonate(durationTicks);

        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(tier);
        firework.setFireworkMeta(meta);

        Location loc = player.getLocation();
        if (tier == 6) {
            player.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 1);
            player.playSound(loc, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.75f, 1.6f);
            player.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 0.7f);
        } else if (tier == 5) {
            player.getWorld().spawnParticle(Particle.DRAGON_BREATH, loc, 15, 0.2, 0.2, 0.2, 0.05);
            player.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 0.9f, 1.2f);
        } else if (tier == 4) {
            player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 10, 0.2, 0.2, 0.2, 0.03);
            player.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.3f);
        }

        boolean sustained = sustainedMap.getOrDefault(tier, false);
        if (sustained) {
            new BukkitRunnable() {
                int count = 0;

                @Override
                public void run() {
                    count += 2;
                    if (!player.isOnline() || player.isDead() || !player.isGliding() || !firework.isValid() || firework.isDetonated() || count >= durationTicks) {
                        cancel();
                        return;
                    }

                    Vector lookDir = player.getLocation().getDirection().normalize();
                    double accel = switch (tier) {
                        case 4 -> 0.06;
                        case 5 -> 0.11;
                        case 6 -> 0.20;
                        default -> 0.03;
                    };

                    Vector newVel = player.getVelocity().add(lookDir.clone().multiply(accel));
                    if (newVel.length() > 3.2) {
                        newVel = newVel.normalize().multiply(3.2);
                    }
                    player.setVelocity(newVel);

                    Location tail = player.getLocation().subtract(lookDir.clone().multiply(0.75));
                    Particle p = particleMap.getOrDefault(tier, Particle.FIREWORK);

                    if (tier == 6) {
                        player.getWorld().spawnParticle(Particle.END_ROD, tail, 6, 0.15, 0.15, 0.15, 0.04);
                        player.getWorld().spawnParticle(Particle.FIREWORK, tail, 3, 0.1, 0.1, 0.1, 0.02);
                        if (count % 16 == 0) {
                            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 0.5f, 1.4f);
                        }
                    } else if (tier == 5) {
                        player.getWorld().spawnParticle(Particle.DRAGON_BREATH, tail, 5, 0.12, 0.12, 0.12, 0.02);
                        player.getWorld().spawnParticle(Particle.FLAME, tail, 3, 0.1, 0.1, 0.1, 0.02);
                    } else if (tier == 4) {
                        player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, tail, 4, 0.1, 0.1, 0.1, 0.02);
                    } else {
                        player.getWorld().spawnParticle(p, tail, 3, 0.1, 0.1, 0.1, 0.01);
                    }
                }
            }.runTaskTimer(plugin, 2L, 2L);
        }
    }

    public int getDurationTicks(int tier) {
        return durationTicksMap.getOrDefault(tier, 25 * tier);
    }

    public double getBoostMultiplier(int tier) {
        return boostMultiplierMap.getOrDefault(tier, 1.0);
    }
}
