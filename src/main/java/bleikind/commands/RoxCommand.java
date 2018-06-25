package bleikind.commands;

import bleikind.ROXApi;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoxCommand implements CommandExecutor {

    private final String PREFIX = ChatColor.RED + "[ROX] " + ChatColor.YELLOW;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        // rox (update, config, notify, web, interval, put, network, reload) <arg> <arg>

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("rox.update") || player.isOp()) {
                switch (args.length) {
                    case 1:
                        if (args[0].equalsIgnoreCase("update")) {
                            ROXApi.getInstance().getAutoNetworkUpdating().update();
                            player.sendMessage(PREFIX + "Updated.");
                        } else if (args[0].equalsIgnoreCase("notify")) {
                            if (ROXApi.getInstance().getNotify().contains(player)) {
                                ROXApi.getInstance().getNotify().remove(player);
                                player.sendMessage(PREFIX + "You get anymore notifications for updates.");
                            } else {
                                ROXApi.getInstance().getNotify().add(player);
                                player.sendMessage(PREFIX + "You get now notifications for updates.");
                            }
                        } else if (args[0].equalsIgnoreCase("web")) {
                            player.sendMessage(ROXApi.getInstance().getAutoNetworkUpdating().getWebURL().toString());
                        } else if (args[0].equalsIgnoreCase("interval")) {
                            player.sendMessage(PREFIX + "Interval: " + ROXApi.getInstance().getFileConfig().getFileConfiguration().getInt("autoNetworkUpdating.interval"));
                        } else if (args[0].equalsIgnoreCase("reload")) {
                            ROXApi.getInstance().getFileConfig().save();
                            ROXApi.getInstance().getAutoNetworkUpdating().reload();
                            player.sendMessage(PREFIX + "Reloaded ROX API.");
                        } else if (args[0].equalsIgnoreCase("config")) {
                            player.sendMessage(PREFIX + "/rox config (update)");
                        } else if (args[0].equalsIgnoreCase("network")) {
                            player.sendMessage(PREFIX + "/rox network (reload)");
                        } else {
                            player.sendMessage(PREFIX + "/rox (update, notify, interval)");
                        }
                        break;
                    case 2:
                        if (args[0].equalsIgnoreCase("interval")) {
                            try {
                                ROXApi.getInstance().getFileConfig().getFileConfiguration().set("autoNetworkUpdating.interval", Integer.parseInt(args[1]));
                                player.sendMessage(PREFIX + "Interval set to " + args[1] + " seconds. You must update the config: /rox config update");
                            } catch (NumberFormatException e) {
                                player.sendMessage(PREFIX + "The 2. Argument must be a number.");
                            }
                        } else if (args[0].equalsIgnoreCase("config")) {
                            switch (args[1]) {
                                case "update":
                                    ROXApi.getInstance().getFileConfig().save();
                                    player.sendMessage(PREFIX + "Updated config file.");
                                    break;
                                default:
                                    player.sendMessage(PREFIX + "/rox config (update)");
                                    break;
                            }
                        } else if (args[0].equalsIgnoreCase("network")) {
                            switch (args[1]) {
                                case "reload":
                                    ROXApi.getInstance().getAutoNetworkUpdating().reload();
                                    player.sendMessage(PREFIX + "Reloaded network updater.");
                                    break;
                                default:
                                    player.sendMessage(PREFIX + "/rox network (reload)");
                                    break;
                            }
                        } else {
                            player.sendMessage(PREFIX + "/rox (interval, config) <arg>. The interval is in seconds.");
                        }
                        break;
                    case 3:
                        if (args[0].equalsIgnoreCase("put")) {
                            ROXApi.getInstance().getNetwork().send(args[1], args[2]);
                            player.sendMessage(PREFIX + "Added information: [" + args[1] + "=" + args[2] + "].");
                        } else {
                            player.sendMessage(PREFIX + "/rox put <key> <value>");
                        }
                        break;

                    default:
                        player.sendMessage(PREFIX + "/rox (update, config, notify, web, interval, put, network, reload) <arg> <arg>");
                        break;
                }
            } else {
                player.sendMessage(PREFIX + "You don't have any permissions to do that.");
            }
        }

        return true;
    }

    public String getPrefix() {
        return PREFIX;
    }
}
