package bleikind;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileConfig {

    private final File file = new File("plugins/ROX/", "config.yml");

    private File webFile;

    private final FileConfiguration fileConfiguration;

    public FileConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            fileConfiguration.options().header("Interval is in seconds.");
            fileConfiguration.addDefault("system.autoUpdate", true);
            fileConfiguration.addDefault("system.autoNetworkUpdating", true);
            fileConfiguration.addDefault("system.useCommands", true);
            fileConfiguration.addDefault("system.defaultNetwork", true);
            fileConfiguration.addDefault("defaultNetwork.uuid", "f1341d9c-754b-4c96-a85a-9c08a86de10b");
            fileConfiguration.addDefault("defaultNetwork.password", "1234");
            fileConfiguration.addDefault("defaultNetwork.hostname", "localhost");
            fileConfiguration.addDefault("defaultNetwork.port", 8982);
            fileConfiguration.addDefault("autoNetworkUpdating.interval", 180);
            fileConfiguration.addDefault("autoNetworkUpdating.htmlPort", 8081);
            fileConfiguration.addDefault("autoNetworkUpdating.updateNotification", true);
            fileConfiguration.addDefault("autoNetworkUpdating.writeToConsoleFromWeb", true);
            fileConfiguration.addDefault("autoNetworkUpdating.writeToFileFromWeb", true);
            fileConfiguration.addDefault("autoNetworkUpdating.fileName", "web.json");
            fileConfiguration.options().copyDefaults(true);
            try {
                fileConfiguration.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        webFile = new File("plugins/ROX/", fileConfiguration.getString("autoNetworkUpdating.fileName"));
        if(!webFile.exists()){
            try {
                if(webFile.createNewFile()) ROXApi.getInstance().getLogger().info("Created web file.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void save(){
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public File getWebFile() {
        return webFile;
    }

    public void setWebFile(File webFile) {
        this.webFile = webFile;
    }
}
