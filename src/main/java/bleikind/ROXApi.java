package bleikind;

import bleikind.commands.RoxCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class ROXApi extends JavaPlugin {

    private static ROXApi instance;

    private final int version = 1;

    private final FileConfig fileConfig = new FileConfig();

    private ROXNetwork network;

    private Thread autoNetworkUpdatingThread;

    private AutoNetworkUpdating autoNetworkUpdating;

    private final ArrayList<Player> notify = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("ROX Api enabled. Version: " + version);

        defaultNetwork();
        autoNetworkUpdating();
        loadCommands();
    }

    @Override
    public void onDisable() {
        autoNetworkUpdatingThread.interrupt();
    }

    public static ROXApi getInstance() {
        return instance;
    }

    public int getVersion() {
        return version;
    }

    private void defaultNetwork() {
        if (!fileConfig.getFileConfiguration().getBoolean("system.defaultNetwork")) return;
        ROXNetwork network = new ROXNetwork(fileConfig.getFileConfiguration().getString("defaultNetwork.hostname"), fileConfig.getFileConfiguration().getInt("defaultNetwork.port"),
                UUID.fromString(fileConfig.getFileConfiguration().getString("defaultNetwork.uuid")), fileConfig.getFileConfiguration().getString("defaultNetwork.password"));
        network.connect();
        this.network = network;
    }

    private void autoUpdate() {
        if (!fileConfig.getFileConfiguration().getBoolean("system.autoUpdate")) return;
    }

    private void loadCommands() {
        if (!fileConfig.getFileConfiguration().getBoolean("system.useCommands")) return;
        this.getCommand("rox").setExecutor(new RoxCommand());
    }

    private void autoNetworkUpdating() {
        if (!fileConfig.getFileConfiguration().getBoolean("system.autoNetworkUpdating")) return;
        autoNetworkUpdating = new AutoNetworkUpdating();
        (autoNetworkUpdatingThread = new Thread(autoNetworkUpdating)).start();
    }

    public FileConfig getFileConfig() {
        return fileConfig;
    }

    public ROXNetwork getNetwork() {
        return network;
    }

    public AutoNetworkUpdating getAutoNetworkUpdating() {
        return autoNetworkUpdating;
    }

    public Thread getAutoNetworkUpdatingThread() {
        return autoNetworkUpdatingThread;
    }

    public ArrayList<Player> getNotify() {
        return notify;
    }

    public void setNetwork(ROXNetwork network) {
        this.network = network;
    }

    public void setAutoNetworkUpdatingThread(Thread autoNetworkUpdatingThread) {
        this.autoNetworkUpdatingThread = autoNetworkUpdatingThread;
    }

    public void setAutoNetworkUpdating(AutoNetworkUpdating autoNetworkUpdating) {
        this.autoNetworkUpdating = autoNetworkUpdating;
    }
}
