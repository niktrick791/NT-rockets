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

    private void msg(CommandSender sender, String text) {
        sender.sendMessage(XRocketVersion.colorize(PREFIX + text));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            msg(sender, "&e--- niktrick-rockets Hilfe ---");
            msg(sender, "&7/rocket info &f- Zeigt Infos zu den Raketen-Stufen");
            if (sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "&7/rocket get <1-6> [anzahl] &f- Gibt dir Raketen");
                msg(sender, "&7/rocket give <spieler> <1-6> [anzahl] &f- Gibt einem Spieler Raketen");
                msg(sender, "&7/rocket reload &f- Lädt die config.yml neu");
            }
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);

        if (sub.equals("info")) {
            msg(sender, "&b&l=== Raketen-Stufen Übersicht ===");
            msg(sender, "&fStufe 1: &7Basis (~1.5s Flug, 1.0x Schub)");
            msg(sender, "&aStufe 2: &7Verbessert (~2.5s Flug, 1.15x Schub)");
            msg(sender, "&bStufe 3: &7Stark (~3.5s Flug, 1.30x Schub, Flammen)");
            msg(sender, "&dStufe 4: &7Turboschub (~5.5s Flug, 1.55x Schub, Seelenfeuer)");
            msg(sender, "&6Stufe 5: &7Mega-Turbine (~8.0s Flug, 1.85x Schub, Drachenatem)");
            msg(sender, "&c&lStufe 6: &bHyperschall (~11.0s Flug, 2.30x Schub, Sonic Boom!)");
            return true;
        }

        if (sub.equals("reload")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "&cKeine Berechtigung!");
                return true;
            }
            plugin.getRocketManager().loadConfig();
            plugin.getRocketManager().registerRecipes();
            msg(sender, "&aKonfiguration und Rezepte erfolgreich neu geladen!");
            return true;
        }

        if (sub.equals("get")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "&cKeine Berechtigung!");
                return true;
            }
            if (!(sender instanceof Player)) {
                msg(sender, "&cDieser Befehl kann nur von einem Spieler ausgeführt werden.");
                return true;
            }
            Player player = (Player) sender;
            if (args.length < 2) {
                msg(sender, "&cVerwendung: /rocket get <1-6> [anzahl]");
                return true;
            }

            int tier = parseInt(args[1], 1);
            int amount = args.length >= 3 ? parseInt(args[2], 1) : 1;

            ItemStack rocket = RocketItemFactory.createRocket(tier, amount);
            player.getInventory().addItem(rocket);
            msg(player, "&aDu hast &f" + amount + "x &aStufe &6" + tier + " &aRaketen erhalten!");
            return true;
        }

        if (sub.equals("give")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "&cKeine Berechtigung!");
                return true;
            }
            if (args.length < 3) {
                msg(sender, "&cVerwendung: /rocket give <spieler> <1-6> [anzahl]");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null || !target.isOnline()) {
                msg(sender, "&cSpieler nicht gefunden!");
                return true;
            }

            int tier = parseInt(args[2], 1);
            int amount = args.length >= 4 ? parseInt(args[3], 1) : 1;

            ItemStack rocket = RocketItemFactory.createRocket(tier, amount);
            target.getInventory().addItem(rocket);
            msg(sender, "&aDu hast &f" + target.getName() + " " + amount + "x &aStufe &6" + tier + " &aRaketen gegeben!");
            msg(target, "&aDu hast &f" + amount + "x &aStufe &6" + tier + " &aRaketen erhalten!");
            return true;
        }

        msg(sender, "&cUnbekannter Befehl. Nutze /rocket help");
        return true;
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
            List<String> subs = new ArrayList<String>(Arrays.asList("info"));
            if (sender.hasPermission("niktrick.rockets.admin")) {
                subs.add("get");
                subs.add("give");
                subs.add("reload");
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
        }
        return completions;
    }
}
