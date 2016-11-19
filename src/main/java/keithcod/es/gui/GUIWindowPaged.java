package keithcod.es.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class GUIWindowPaged {

    public static HashMap<String, GUIWindowPaged> windows = new HashMap<>();

    private static final int invSize = 6*9;

    public String name = "GUI";

    public Inventory[] inventories;
    public Map<Integer, HashMap<Integer, GUIItem>> items = new HashMap<>();

    public int displayedPage = -1;

    public GUIWindowPaged(String title){
        this.name = title;

        windows.put(this.name, this);
    }

    public void setItem(int x, GUIItem item){
        int page = (x/(5*9));
        int slot = x-((x/(5*9))*(5*9));
        if(!items.containsKey(page))
            items.put(page, new HashMap<>());
        items.get(page).put(slot, item);
        System.out.println("input x: " + x + " page: " + page + " slot: " + slot);
    }

    public void setItem(int page, int x, GUIItem item){
        if(!items.containsKey(page))
            items.put(page, new HashMap<>());
        items.get(page).put(page, item);

        //inv.setItem(x, item.getBukkitItem());
    }

    public void setItem(int page, int x, int y, GUIItem item){
        if(!items.containsKey(page))
            items.put(page, new HashMap<>());
        int pos = (x + y*9);
        items.get(page).put(pos, item);
    }

    public void createInventories(){
        int pages = items.size();
        if(pages == 0)
            pages = 1;
        System.out.println("pages: " + pages);
        inventories = new Inventory[pages];
        for(int i = 0; i < pages; ++i){



            inventories[i] = Bukkit.createInventory(null, invSize, name);
            if(items.size() > 0) {
                for (int item = 0; item < 5 * 9; ++item) {
                    GUIItem theItem = items.get(i).containsKey(item) ? items.get(i).get(item) : null;
                    if (theItem != null)
                        inventories[i].setItem(item, theItem.getBukkitItem());
                }
            }
            if(i < pages-1) {
                ItemStack nextPage = new ItemStack(Material.ARROW, 1);

                nextPage = setItemName("Next Page", nextPage);
                final int t = i;
                GUIItem nextItem = new GUIItem(nextPage, event -> {
                    show(entity, t + 1);
                });
                setItem(i, 8, 5, nextItem);
                inventories[i].setItem(53, nextItem.getBukkitItem());
            }
            if(i > 0) {
                ItemStack nextPage = new ItemStack(Material.ARROW, 1);

                nextPage = setItemName("Previous Page", nextPage);
                final int t = i;
                GUIItem nextItem = new GUIItem(nextPage, event -> {
                    System.out.println("Showing previous page...");
                    show(entity, t - 1);
                });
                setItem(i, 0, 5, nextItem);
                inventories[i].setItem(45, nextItem.getBukkitItem());
            }

            ItemStack infoStack = new ItemStack(Material.SLIME_BALL, 1);
            infoStack = setItemName("Page " + (i+1) + "/" + pages, infoStack);
            inventories[i].setItem(49, infoStack);

        }
    }
    public Inventory getInventory(int page){
        return inventories[page];
    }

    HumanEntity entity = null;

    public GUIItem getItem(int pos){
        if(displayedPage != -1){
            if(items.size() > 0 && items.size() > displayedPage)
                return items.get(displayedPage).get(pos);
        }
        return null;
    }

    public void show(HumanEntity h, int page) {
        /*if(h.getOpenInventory() != null)
            h.closeInventory();*/

        displayedPage = page;

        Inventory inv = Bukkit.createInventory(h, getInventory(page).getSize(), getInventory(page).getTitle());
        inv.setContents(getInventory(page).getContents());
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

    private static ItemStack setItemName(String name, ItemStack stack){
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.setDisplayName(name);

        stack.setItemMeta(itemMeta);
        return stack;
    }
}
