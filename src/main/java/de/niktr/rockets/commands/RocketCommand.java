package de.niktr.rockets.commands;

import de.niktr.rockets.NiktrickRocketsPlugin;
import de.niktr.rockets.util.RocketItemFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RocketCommand implements CommandExecutor, TabCompleter {

    private final NiktrickRocketsPlugin plugin;
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final String PREFIX = "<dark_gray>[<gradient:#FF007F:#FF6600><bold>Rockets</bold></gradient><dark_gray>] </dark_gray>";

    public RocketCommand(NiktrickRocketsPlugin plugin) {
        this.plugin = plugin;
    }

    private void msg(CommandSender sender, String text) {
        sender.sendMessage(MM.deserialize(PREFIX + text));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            msg(sender, "<yellow>--- niktrick-rockets Hilfe ---</yellow>");
            msg(sender, "<gray>/rocket info <white>- Zeigt Infos zu den Raketen-Stufen");
            if (sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "<gray>/rocket get <1-6> [anzahl] <white>- Gibt dir Raketen");
                msg(sender, "<gray>/rocket give <spieler> <1-6> [anzahl] <white>- Gibt einem Spieler Raketen");
                msg(sender, "<gray>/rocket reload <white>- Lädt die config.yml neu");
            }
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("info")) {
            msg(sender, "<gradient:#4FC3F7:#0077BE><bold>=== Raketen-Stufen Übersicht ===</bold></gradient>");
            msg(sender, "<white>Stufe 1:</white> <gray>Basis (~1.5s Flug, 1.0x Schub)");
            msg(sender, "<green>Stufe 2:</green> <gray>Verbessert (~2.5s Flug, 1.15x Schub)");
            msg(sender, "<aqua>Stufe 3:</aqua> <gray>Stark (~3.5s Flug, 1.30x Schub, Flammen)");
            msg(sender, "<light_purple>Stufe 4:</light_purple> <gray>Turboschub (~5.5s Flug, 1.55x Schub, Seelenfeuer)");
            msg(sender, "<gold>Stufe 5:</gold> <gray>Mega-Turbine (~8.0s Flug, 1.85x Schub, Drachenatem)");
            msg(sender, "<red><bold>Stufe 6:</bold></red> <aqua>Hyperschall (~11.0s Flug, 2.30x Schub, Sonic Boom!)</aqua>");
            return true;
        }

        if (sub.equals("reload")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "<red>Keine Berechtigung!");
                return true;
            }
            plugin.getRocketManager().loadConfig();
            plugin.getRocketManager().registerRecipes();
            msg(sender, "<green>Konfiguration und Rezepte erfolgreich neu geladen!");
            return true;
        }

        if (sub.equals("get")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "<red>Keine Berechtigung!");
                return true;
            }
            if (!(sender instanceof Player player)) {
                msg(sender, "<red>Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
                return true;
            }
            if (args.length < 2) {
                msg(sender, "<red>Verwendung: /rocket get <1-6> [anzahl]");
                return true;
            }

            int tier = parseInt(args[1], 1);
            int amount = args.length >= 3 ? parseInt(args[2], 1) : 1;

            ItemStack rocket = RocketItemFactory.createRocket(tier, amount);
            player.getInventory().addItem(rocket);
            msg(player, "<green>Du hast <white>" + amount + "x </white>Stufe <gold>" + tier + "</gold> Raketen erhalten!");
            return true;
        }

        if (sub.equals("give")) {
            if (!sender.hasPermission("niktrick.rockets.admin")) {
                msg(sender, "<red>Keine Berechtigung!");
                return true;
            }
            if (args.length < 3) {
                msg(sender, "<red>Verwendung: /rocket give <spieler> <1-6> [anzahl]");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null || !target.isOnline()) {
                msg(sender, "<red>Spieler nicht gefunden!");
                return true;
            }

            int tier = parseInt(args[2], 1);
            int amount = args.length >= 4 ? parseInt(args[3], 1) : 1;

            ItemStack rocket = RocketItemFactory.createRocket(tier, amount);
            target.getInventory().addItem(rocket);
            msg(sender, "<green>Du hast <white>" + target.getName() + " " + amount + "x </white>Stufe <gold>" + tier + "</gold> Raketen gegeben!");
            msg(target, "<green>Du hast <white>" + amount + "x </white>Stufe <gold>" + tier + "</gold> Raketen erhalten!");
            return true;
        }

        msg(sender, "<red>Unbekannter Befehl. Nutze /rocket help");
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
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subs = new ArrayList<>(List.of("info"));
            if (sender.hasPermission("niktrick.rockets.admin")) {
                subs.add("get");
                subs.add("give");
                subs.add("reload");
            }
            for (String s : subs) {
                if (s.startsWith(args[0].toLowerCase())) completions.add(s);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
            for (int i = 1; i <= 6; i++) completions.add(String.valueOf(i));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(p.getName());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            for (int i = 1; i <= 6; i++) completions.add(String.valueOf(i));
        }
        return completions;
    }
}
