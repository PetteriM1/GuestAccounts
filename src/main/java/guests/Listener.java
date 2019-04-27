package guests;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.utils.ClientChainData;

public class Listener implements cn.nukkit.event.Listener {

    private Main plugin;

    public Listener(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onDataReceive(DataPacketReceiveEvent e) {
        if (e.getPacket() instanceof LoginPacket) {
            LoginPacket pk = (LoginPacket) e.getPacket();
            if (!ClientChainData.read(pk).isXboxAuthed()) {
                String uuid = pk.clientUUID.toString();
                String n;
                if (plugin.accountCache.containsKey(uuid)) {
                    n = (String) plugin.accountCache.get(uuid);
                    ((LoginPacket) e.getPacket()).username = n;
                    if (plugin.config.getBoolean("notifyGuests")) {
                        plugin.scheduleXblWarning(e.getPlayer());
                    }
                } else {
                    n = plugin.config.getString("guestString") + plugin.randomNumbers();
                    while (plugin.accountCache.containsValue(n)) {
                        n = plugin.config.getString("guestString") + plugin.randomNumbers();
                    }
                    plugin.accountCache.put(uuid, n);
                    ((LoginPacket) e.getPacket()).username = n;
                    if (plugin.config.getBoolean("notifyGuests")) {
                        plugin.scheduleXblWarning(e.getPlayer());
                    }
                }
            }
        }
    }
}
