package keithcod.es.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GUIWindow {

    public static HashMap<String, GUIWindow> windows = new HashMap<>();

    public String name = "GUI";
    private Inventory inv;
    public Map<Integer, GUIItem> items;

    public GUIWindow(String title, int rows){
        this.name = title;
        this.items = new HashMap<>(rows * 9);
        this.inv = Bukkit.createInventory(null, rows * 9, name);
        windows.put(this.name, this);
    }

    public void setItem(int x, GUIItem item){
        items.put(x, item);
        inv.setItem(x, item.getBukkitItem());
    }

    public void setItem(int x, int y, GUIItem item){
        int pos = x + y*9;
        items.put(pos, item);
        inv.setItem(pos, item.getBukkitItem());
    }

    public Inventory getInventory(){
        return inv;
    }

    HumanEntity entity = null;

    public void show(HumanEntity h) {
        /*if(h.getOpenInventory() != null)
            h.closeInventory();*/
        Inventory inv = Bukkit.createInventory(h, getInventory().getSize(), getInventory().getTitle());
        inv.setContents(getInventory().getContents());
        h.openInventory(inv);
        entity = h;
    }

    public void close(){
        if(entity != null)
            entity.closeInventory();
    }

    public void dispose(){
        windows.remove(this.name);
    }
}
