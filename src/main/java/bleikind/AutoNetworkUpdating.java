package bleikind;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.net.URL;

public class AutoNetworkUpdating extends Thread {

    private BukkitTask bukkitTask;

    private URL webURL;

    public AutoNetworkUpdating() {
        try {
            webURL = new URL("http://" + ROXApi.getInstance().getFileConfig().getFileConfiguration().getString("defaultNetwork.hostname") + ":" +
                    ROXApi.getInstance().getFileConfig().getFileConfiguration().getInt("autoNetworkUpdating.htmlPort") + "/get?gsUUID=" + ROXApi.getInstance().getFileConfig().getFileConfiguration().getString("defaultNetwork.uuid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(ROXApi.getInstance(), this::update, 20 * ROXApi.getInstance().getFileConfig().getFileConfiguration().getInt("autoNetworkUpdating.interval"), 20 * ROXApi.getInstance().getFileConfig().getFileConfiguration().getInt("autoNetworkUpdating.interval"));
    }

    public void reload() {
        bukkitTask.cancel();
        run();
    }

    public void update() {
        ROXApi.getInstance().getNetwork().send("playerSize", Bukkit.getOnlinePlayers().size());
        ROXApi.getInstance().getNetwork().send("maxPlayers", Bukkit.getMaxPlayers());
        ROXApi.getInstance().getNetwork().send("allowEnd", Bukkit.getAllowEnd());
        ROXApi.getInstance().getNetwork().send("allowFlight", Bukkit.getAllowFlight());
        ROXApi.getInstance().getNetwork().send("allowNether", Bukkit.getAllowNether());
        ROXApi.getInstance().getNetwork().send("bannedPlayers", Bukkit.getBannedPlayers().size());
        ROXApi.getInstance().getNetwork().send("bukkitVersion", Bukkit.getBukkitVersion());
        ROXApi.getInstance().getNetwork().send("connectionThrottle", Bukkit.getConnectionThrottle());
        ROXApi.getInstance().getNetwork().send("idleTimeout", Bukkit.getIdleTimeout());
        ROXApi.getInstance().getNetwork().send("motd", Bukkit.getMotd());
        ROXApi.getInstance().getNetwork().send("name", Bukkit.getName());
        ROXApi.getInstance().getNetwork().send("port", Bukkit.getPort());
        ROXApi.getInstance().getNetwork().send("whitelistedPlayers", Bukkit.getWhitelistedPlayers());
        ROXApi.getInstance().getNetwork().send("hasWhitelist", Bukkit.hasWhitelist());
        ROXApi.getInstance().getNetwork().send("defaultGameMode", Bukkit.getDefaultGameMode().name());

        if (ROXApi.getInstance().getFileConfig().getFileConfiguration().getBoolean("autoNetworkUpdating.updateNotification"))
            ROXApi.getInstance().getLogger().info("Updated Statistics.");
        if (ROXApi.getInstance().getFileConfig().getFileConfiguration().getBoolean("autoNetworkUpdating.writeToConsoleFromWeb"))
            ROXApi.getInstance().getLogger().info(this::getContentWeb);
        if (ROXApi.getInstance().getFileConfig().getFileConfiguration().getBoolean("autoNetworkUpdating.writeToFileFromWeb")) {
            try {
                PrintWriter writer = new PrintWriter(new PrintStream(new FileOutputStream(ROXApi.getInstance().getFileConfig().getWebFile())), true);
                writer.println(getContentWeb());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ROXApi.getInstance().getNotify().parallelStream().forEach(player -> player.sendMessage("Updated Web."));
    }

    private String getContentWeb() {
        try {
            InputStream inputStream = webURL.openStream();
            int ptr;
            StringBuilder builder = new StringBuilder();
            while ((ptr = inputStream.read()) != -1) {
                builder.append((char) ptr);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    @Override
    public void interrupt() {
        bukkitTask.cancel();
        super.interrupt();
    }

    public URL getWebURL() {
        return webURL;
    }

    public void setWebURL(URL webURL) {
        this.webURL = webURL;
    }

    @Override
    public ClassLoader getContextClassLoader() {
        return super.getContextClassLoader();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public State getState() {
        return super.getState();
    }

    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return super.getUncaughtExceptionHandler();
    }

    @Override
    public void setContextClassLoader(ClassLoader cl) {
        super.setContextClassLoader(cl);
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
    }

    public void setBukkitTask(BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }
}
