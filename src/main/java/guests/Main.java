package guests;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.util.HashMap;
import java.util.Random;

public class Main extends PluginBase {

    public HashMap accountCache;
    public Config config;
    private Stream stream;

    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(new Listener(this), this);
        initAccountCache();
    }

    public void onDisable() {
        stream.save();
    }

    private void initAccountCache() {
        stream = new Stream(this);
        stream.init();

        getServer().getScheduler().scheduleRepeatingTask(new cn.nukkit.scheduler.Task() {
            @Override
            public void onRun(int i) {
                stream.save();
            }
        }, config.getInt("cacheSaveInterval"), true);
    }

    public String randomNumbers() {
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < config.getInt("randomNumbersLength"); ++i) {
            sb.append(numbers.charAt(new Random().nextInt(numbers.length())));
        }
        return sb.toString();
    }

    public void scheduleXblWarning(Player p) {
        getServer().getScheduler().scheduleDelayedTask(new cn.nukkit.scheduler.Task() {
            @Override
            public void onRun(int i) {
                if (p.isOnline()) {
                    p.sendMessage(config.getString("guestMessage"));
                }
            }
        }, config.getInt("notificationDelay"), true);
    }
}
