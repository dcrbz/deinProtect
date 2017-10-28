package bz.dcr.deinprotect.listener.worldedit;

import bz.dcr.deinprotect.DeinProtectPlugin;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldEditEventHandler {

    private DeinProtectPlugin plugin;


    public WorldEditEventHandler(DeinProtectPlugin plugin) {
        this.plugin = plugin;
    }


    @Subscribe
    public void wrapForLogging(EditSessionEvent event) {
        final World world = Bukkit.getWorld(event.getWorld().getName());

        // World not found
        if (world == null) {
            return;
        }

        // WorldEdit logger extent
        event.setExtent(new DeinProtectWorldEditLogger(event.getExtent(), world, plugin));
    }

}
