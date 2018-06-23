package bleikind;

import org.bukkit.plugin.java.JavaPlugin;

public class ROXApi extends JavaPlugin {

    private static ROXApi instance;

    @Override
    public void onEnable(){
        instance = this;
        getLogger().info("ROX Api enabled.");
    }

    public static ROXApi getInstance() {
        return instance;
    }


}
