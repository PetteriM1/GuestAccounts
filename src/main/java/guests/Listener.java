package guests;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.utils.ClientChainData;

public class Listener implements cn.nukkit.event.Listener {

    private final Main plugin;

    public Listener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void DataPacketReceiveEvent(DataPacketReceiveEvent e) {
        if (e.getPacket() instanceof LoginPacket) {
            LoginPacket pk = (LoginPacket) e.getPacket();
            if (!ClientChainData.read(pk).isXboxAuthed()) {
                String uuid = pk.clientUUID.toString();
                String n;
                if (plugin.accountCache.containsKey(uuid)) {
                    n = (String) plugin.accountCache.get(uuid);
                } else {
                    do {
                        n = plugin.guestString + plugin.randomNumbers();
                    } while (plugin.accountCache.containsValue(n));
                    plugin.accountCache.put(uuid, n);
                }
                ((LoginPacket) e.getPacket()).username = n;
            }
        }
    }

    @EventHandler
    public void PlayerLocallyInitializedEvent(PlayerLocallyInitializedEvent e) {
        if (plugin.notifyGuests && !e.getPlayer().getLoginChainData().isXboxAuthed()) {
            e.getPlayer().sendMessage(plugin.guestMessage);
        }
    }
}
