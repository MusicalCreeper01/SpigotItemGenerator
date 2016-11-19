package keithcod.es.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GUIItem {
    private Consumer<InventoryClickEvent> clickEvent;
    private ItemStack item;

    public GUIItem(ItemStack item, Consumer<InventoryClickEvent> toRun) {
        this.clickEvent = toRun;
        this.item = item;
    }

    ItemStack getBukkitItem() {
        return this.item;
    }

    void click(InventoryClickEvent e) {
        this.clickEvent.accept(e);
    }
}
