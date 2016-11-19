package keithcod.es.gui;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        GUIWindow window = GUIWindow.windows.get(e.getInventory().getTitle());
        if(window != null) {
            if(window.items.containsKey(e.getSlot())) {
                GUIItem item = window.items.get(e.getSlot());
                if (item != null) {
                    item.click(e);
                }
            }
            e.setResult(Event.Result.DENY);
            e.setCancelled(true);
        }else{
            GUIWindowPaged windowPaged = GUIWindowPaged.windows.get(e.getInventory().getTitle());
            if(windowPaged != null){
                GUIItem item = windowPaged.getItem(e.getSlot());
                if (item != null) {
                    item.click(e);
                }

                e.setResult(Event.Result.DENY);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent e) {
        if(GUIWindow.windows.containsKey(e.getInventory().getTitle())){
            e.setResult(Event.Result.DENY);
            e.setCancelled(true);
        }
    }
}
