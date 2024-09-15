package guests;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Stream {

    private final Main plugin;

    public Stream(Main plugin) {
        this.plugin = plugin;
    }

    public void init() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(plugin.getDataFolder() + "/accountCache.dat"));
            plugin.accountCache = (HashMap) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            plugin.getLogger().warning("No account cache data found");
            plugin.accountCache = new HashMap();
        } catch (IOException | ClassNotFoundException e) {
            plugin.getLogger().error("Unable to get account cache data: " + e.getMessage());
            plugin.accountCache = new HashMap();
        }
    }

    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(plugin.getDataFolder() + "/accountCache.dat")));
            oos.writeObject(plugin.accountCache);
            oos.close();
        } catch (IOException e) {
            plugin.getLogger().error("Unable to save account cache data: " + e.getMessage());
        }
    }
}
