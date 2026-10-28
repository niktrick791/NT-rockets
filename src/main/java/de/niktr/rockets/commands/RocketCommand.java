package de.niktr.rockets.commands;

import de.niktr.rockets.NiktrickRocketsPlugin;
import de.niktr.rockets.util.RocketItemFactory;
import de.niktr.rockets.util.XRocketVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RocketCommand implements CommandExecutor, TabCompleter {

    private final NiktrickRocketsPlugin plugin;
    private static final String PREFIX = "&8[&6Rockets&8] ";

    public RocketCommand(NiktrickRocketsPlugin plugin) {
        this.plugin = plugin;
    }

    private boolean isDe() {
        return plugin.getRocketManager() != null && plugin.getRocketManager().isDe();
    }

    private void msg(CommandSender sender, String text) {
        sender.sendMessage(XRocketVersion.colorize(PREFIX + text));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);

        if (sub.equals("info")) {
            if (isDe()) {
                msg(sender, "&b&l=== Raketen-Stufen Übersicht ===");
                msg(sender, "&fStufe 1: &7Basis (~1.5s Flug, 1.0x Schub)");
                msg(sender, "&aStufe 2: &7Verbessert (~2.5s Flug, 1.15x Schub)");
                msg(sender, "&bStufe 3: &7Stark (~3.5s Flug, 1.30x Schub, Flammen)");
                msg(sender, "&dStufe 4: &7Turboschub (~5.5s Flug, 1.55x Schub, Seelenfeuer)");
                msg(sender, "&6Stufe 5: &7Mega-Turbine (~8.0s Flug, 1.85x Schub, Drachenatem)");
                msg(sender, "&c&lStufe 6: &bHyperschall (~11.0s Flug, 2.30x Schub, Sonic Boom!)");
            } else {
                msg(sender, "&b&l=== Rocket Tiers Overview ===");
                msg(sender, "&fTier 1: &7Basic (~1.5s flight, 1.0x boost)");
                msg(sender, "&aTier 2: &7Enhanced (~2.5s flight, 1.15x boost)");
                msg(sender, "&bTier 3: &7Powerful (~3.5s flight, 1.30x boost, flame trail)");
                msg(sender, "&dTier 4: &7Turbo Boost (~5.5s flight, 1.55x boost, soul flame trail)");
                msg(sender, "&6Tier 5: &7Mega Turbine (~8.0s flight, 1.85x boost, dragon breath)");
                msg(sender, "&c&lTier 6: &bHypersonic (~11.0s flight, 2.30x boost, Sonic Boom!)");
            }
            return true;
        }

        if (sub.equals("lang") || sub.equals("language")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, isDe() ? "&cKeine Berechtigung!" : "&cYou do not have permission!");
                return true;
            }
            if (args.length >= 2) {
                String target = args[1].toLowerCase(Locale.ROOT);
                if (target.equals("de") || target.equals("en")) {
                    plugin.getRocketManager().setLanguage(target);
                    if ("de".equals(target)) {
                        msg(sender, "&aSprache wurde auf &eDeutsch &ageändert. Rezepte aktualisiert!");
                    } else {
                        msg(sender, "&aLanguage set to &eEnglish&a. Recipes updated!");
                    }
                    return true;
                }
            }
            msg(sender, isDe()
                    ? "&7Aktuelle Sprache: &e" + plugin.getRocketManager().getLanguage() + " &7(Verfügbar: &bde&7, &ben&7)"
                    : "&7Current language: &e" + plugin.getRocketManager().getLanguage() + " &7(Available: &bde&7, &ben&7)");
            return true;
        }

        if (sub.equals("reload")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, isDe() ? "&cKeine Berechtigung!" : "&cYou do not have permission!");
                return true;
            }
            plugin.getRocketManager().loadConfig();
            plugin.getRocketManager().registerRecipes();
            msg(sender, isDe() ? "&aKonfiguration und Rezepte erfolgreich neu geladen!" : "&aConfiguration and recipes reloaded successfully!");
            return true;
        }

        if (sub.equals("get")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, isDe() ? "&cKeine Berechtigung!" : "&cYou do not have permission!");
                return true;
            }
            if (!(sender instanceof Player)) {
                msg(sender, isDe() ? "&cDieser Befehl kann nur von einem Spieler ausgeführt werden." : "&cThis command can only be executed by a player.");
                return true;
            }
            Player player = (Player) sender;
            if (args.length < 2) {
                msg(sender, isDe() ? "&cVerwendung: /rocket get <1-6> [anzahl]" : "&cUsage: /rocket get <1-6> [amount]");
                return true;
            }

            int tier = parseInt(args[1], 1);
            int amount = args.length >= 3 ? parseInt(args[2], 1) : 1;

            ItemStack rocket = RocketItemFactory.createRocket(tier, amount, isDe());
            player.getInventory().addItem(rocket);
            msg(player, isDe() ? "&aDu hast &f" + amount + "x &aStufe &6" + tier + " &aRaketen erhalten!" : "&aReceived &f" + amount + "x &aTier &6" + tier + " &arockets!");
            return true;
        }

        if (sub.equals("give")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, isDe() ? "&cKeine Berechtigung!" : "&cYou do not have permission!");
                return true;
            }
            if (args.length < 3) {
                msg(sender, isDe() ? "&cVerwendung: /rocket give <spieler> <1-6> [anzahl]" : "&cUsage: /rocket give <player> <1-6> [amount]");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null || !target.isOnline()) {
                msg(sender, isDe() ? "&cSpieler nicht gefunden!" : "&cPlayer not found!");
                return true;
            }

            int tier = parseInt(args[2], 1);
            int amount = args.length >= 4 ? parseInt(args[3], 1) : 1;

            ItemStack rocket = RocketItemFactory.createRocket(tier, amount, isDe());
            target.getInventory().addItem(rocket);
            msg(sender, isDe() ? "&aDu hast &f" + target.getName() + " " + amount + "x &aStufe &6" + tier + " &aRaketen gegeben!" : "&aGave &f" + target.getName() + " " + amount + "x &aTier &6" + tier + " &arockets!");
            msg(target, isDe() ? "&aDu hast &f" + amount + "x &aStufe &6" + tier + " &aRaketen erhalten!" : "&aReceived &f" + amount + "x &aTier &6" + tier + " &arockets!");
            return true;
        }

        msg(sender, isDe() ? "&cUnbekannter Befehl. Nutze /rocket help" : "&cUnknown command. Use /rocket help");
        return true;
    }

    private void sendHelp(CommandSender sender) {
        if (isDe()) {
            msg(sender, "&e--- niktrick-rockets Hilfe ---");
            msg(sender, "&7/rocket info &f- Zeigt Infos zu den Raketen-Stufen");
            if (sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "&7/rocket get <1-6> [anzahl] &f- Gibt dir Raketen");
                msg(sender, "&7/rocket give <spieler> <1-6> [anzahl] &f- Gibt einem Spieler Raketen");
                msg(sender, "&7/rocket reload &f- Lädt die config.yml & Rezepte neu");
                msg(sender, "&7/rocket lang <de|en> &f- Ändert die Sprache (Global)");
            }
        } else {
            msg(sender, "&e--- niktrick-rockets Help ---");
            msg(sender, "&7/rocket info &f- Displays rocket tier specifications");
            if (sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "&7/rocket get <1-6> [amount] &f- Gives you rockets");
                msg(sender, "&7/rocket give <player> <1-6> [amount] &f- Gives rockets to a player");
                msg(sender, "&7/rocket reload &f- Reloads config.yml & recipes");
                msg(sender, "&7/rocket lang <de|en> &f- Changes plugin language (Global)");
            }
        }
    }

    private int parseInt(String str, int def) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            List<String> subs = new ArrayList<String>(Arrays.asList("info", "help"));
            if (sender.hasPermission("niktrick.rockets.admin")) {
                subs.add("get");
                subs.add("give");
                subs.add("reload");
                subs.add("lang");
            }
            String prefix = args[0].toLowerCase(Locale.ROOT);
            for (String s : subs) {
                if (s.startsWith(prefix)) completions.add(s);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
            for (int i = 1; i <= 6; i++) completions.add(String.valueOf(i));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            String prefix = args[1].toLowerCase(Locale.ROOT);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase(Locale.ROOT).startsWith(prefix)) {
                    completions.add(p.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            for (int i = 1; i <= 6; i++) completions.add(String.valueOf(i));
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("lang") || args[0].equalsIgnoreCase("language")) && sender.hasPermission("niktrick.rockets.admin")) {
            for (String l : Arrays.asList("de", "en")) {
                if (l.startsWith(args[1].toLowerCase(Locale.ROOT))) {
                    completions.add(l);
                }
            }
        }
        return completions;
    }
}
