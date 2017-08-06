package bz.dcr.deinprotect.gui;

import bz.dcr.deinprotect.gui.window.MainMenuWindow;
import bz.dcr.deinprotect.protection.entity.Protection;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.gui.GuiManager;

public class GUIManager {

    public void openProtectionGui(Player player, Protection protection) {
        final MainMenuWindow window = new MainMenuWindow(protection);
        GuiManager.open(player, window);
    }

}
