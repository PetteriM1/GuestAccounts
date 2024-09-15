package guests;

import cn.nukkit.plugin.PluginBase;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends PluginBase {

    HashMap accountCache;
    private Stream stream;
    String guestString;
    private int randomNumbersLength;
    boolean notifyGuests;
    String guestMessage;

    @Override
    public void onEnable() {
        initConfig();
        initAccountCache();
        getServer().getPluginManager().registerEvents(new Listener(this), this);
    }

    private void initConfig() {
        saveDefaultConfig();
        guestString = getConfig().getString("guestString", "Guest_");
        randomNumbersLength = getConfig().getInt("randomNumbersLength", 6);
        notifyGuests = getConfig().getBoolean("notifyGuests");
        guestMessage = getConfig().getString("guestMessage");
    }

    private void initAccountCache() {
        stream = new Stream(this);
        stream.init();
        getServer().getScheduler().scheduleRepeatingTask(this, () -> stream.save(), getConfig().getInt("cacheSaveInterval"));
    }

    @Override
    public void onDisable() {
        stream.save();
    }

    private static final String NUMBERS = "0123456789";

    String randomNumbers() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < randomNumbersLength; ++i) {
            sb.append(NUMBERS.charAt(ThreadLocalRandom.current().nextInt(NUMBERS.length())));
        }
        return sb.toString();
    }
}
