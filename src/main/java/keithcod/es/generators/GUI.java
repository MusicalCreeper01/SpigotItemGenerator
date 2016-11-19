package keithcod.es.generators;

import com.google.common.util.concurrent.AtomicDouble;
import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import keithcod.es.gui.GUIItem;
import keithcod.es.gui.GUIWindow;
import keithcod.es.gui.GUIWindowPaged;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GUI {



    public static void ShowGUI(Player player){
        ShowInitialGUI(player);
    }

    private static void ShowInitialGUI(Player player){
        GUIWindow window = new GUIWindow("Select an Action", 1);

        ItemStack createItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)5);
        createItem = setItemName("Create new Gen", createItem);

        ItemStack reuseItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3);
        reuseItem = setItemName("Create existing Gen", reuseItem);

        ItemStack editItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)4);
        editItem = setItemName("Edit Gen", editItem);

        ItemStack deleteItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14);
        deleteItem = setItemName("Delete Gen", deleteItem);

        ItemStack fillerItem = new ItemStack(Material.THIN_GLASS, 1);
        fillerItem = setItemName("", fillerItem);

        GUIItem create = new GUIItem(createItem, event -> {
            window.close();
            window.dispose();
            ShowCreateGui1(player);
        });

        GUIItem reuse = new GUIItem(reuseItem, event -> {
            window.close();
            window.dispose();
            ShowReuseGUI(player);
        });

        GUIItem edit = new GUIItem(editItem, event -> {
            window.close();
            window.dispose();
            ShowEditGUI1(player);
        });

        GUIItem delete = new GUIItem(deleteItem, event -> {
            window.close();
            window.dispose();
            ShowDeleteGUI(player);
        });

        GUIItem filler = new GUIItem(fillerItem, null);

        window.setItem(0, 0, filler);
        window.setItem(1, 0, create);
        window.setItem(1, 0, create);
        window.setItem(2, 0, reuse);
        window.setItem(3, 0, filler);
        window.setItem(4, 0, edit);
        window.setItem(5, 0, filler);
        window.setItem(6, 0, filler);
        window.setItem(7, 0, delete);
        window.setItem(8, 0, filler);

        window.show(player);
    }

    public static void ShowDeleteGUI(Player player){
        GUIWindowPaged window = new GUIWindowPaged("Delete Generator");

        Material[] allItems = Material.values();

        int i = 0;
        ConfigurationSection generators = Generators.INSTANCE.getConfig().getConfigurationSection("generators");
        for(String k : generators.getKeys(false)){
            ConfigurationSection gen = generators.getConfigurationSection(k);

            double time = gen.getDouble("time");
            int amount = gen.getInt("amount");
            Material block = Material.getMaterial(gen.getString("block"));
            Material item = Material.getMaterial(gen.getString("item"));

            ItemStack itemStack = new ItemStack(block, 1);
            itemStack = setItemName(formatMaterialName(item) + " Generator", itemStack);
            itemStack = setItemLore(new ArrayList<String>(Arrays.asList(
                    "Gen: " + k,
                    "Block: " + formatMaterialName(block),
                    "Item: " + formatMaterialName(item),
                    "Time: " + time,
                    "Amount: " + amount
            )), itemStack);
            itemStack = setItemEnchant(new ArrayList<Enchantment>(Arrays.asList(Enchantment.LUCK)), itemStack);

            final ItemStack iStack = itemStack;
            final String uuid = k;

            GUIItem guiItem = new GUIItem(itemStack, event -> {
                Generators.INSTANCE.getConfig().getConfigurationSection("generators").set(uuid, null);
                Generators.INSTANCE.saveConfig();
                window.close();
                window.dispose();
            });

            window.setItem(i, guiItem);

            ++i;
        }

        window.createInventories();
        window.show(player, 0);
    }

    public static void ShowReuseGUI(Player player){
        GUIWindowPaged window = new GUIWindowPaged("Select an item to generate");

        Material[] allItems = Material.values();


        int i = 0;
        ConfigurationSection generators = Generators.INSTANCE.getConfig().getConfigurationSection("generators");
        for(String k : generators.getKeys(false)){
            ConfigurationSection gen = generators.getConfigurationSection(k);

            double time = gen.getDouble("time");
            int amount = gen.getInt("amount");
            Material block = Material.getMaterial(gen.getString("block"));
            Material item = Material.getMaterial(gen.getString("item"));

            ItemStack itemStack = new ItemStack(block, 1);
            itemStack = setItemName(formatMaterialName(item) + " Generator", itemStack);
            itemStack = setItemLore(new ArrayList<String>(Arrays.asList(
                    "Gen: " + k,
                    "Block: " + formatMaterialName(block),
                    "Item: " + formatMaterialName(item),
                    "Time: " + time,
                    "Amount: " + amount
            )), itemStack);
            itemStack = setItemEnchant(new ArrayList<Enchantment>(Arrays.asList(Enchantment.LUCK)), itemStack);

            final ItemStack iStack = itemStack;

            GUIItem guiItem = new GUIItem(itemStack, event -> {
                player.getInventory().addItem(iStack);
                window.close();
                window.dispose();
            });

            window.setItem(i, guiItem);

            ++i;
        }

        window.createInventories();
        window.show(player, 0);
    }

    public static void ShowEditGUI1(Player player) {
        GUIWindowPaged window = new GUIWindowPaged("Configure the Generator");

        Material[] allItems = Material.values();


        int i = 0;
        ConfigurationSection generators = Generators.INSTANCE.getConfig().getConfigurationSection("generators");
        for(String k : generators.getKeys(false)){
            ConfigurationSection gen = generators.getConfigurationSection(k);

            double time = gen.getDouble("time");
            int amount = gen.getInt("amount");
            Material block = Material.getMaterial(gen.getString("block"));
            Material item = Material.getMaterial(gen.getString("item"));

            ItemStack itemStack = new ItemStack(block, 1);
            itemStack = setItemName(formatMaterialName(item) + " Generator", itemStack);
            itemStack = setItemLore(new ArrayList<String>(Arrays.asList(
                    "Gen: " + k,
                    "Block: " + formatMaterialName(block),
                    "Item: " + formatMaterialName(item),
                    "Time: " + time,
                    "Amount: " + amount
            )), itemStack);
            itemStack = setItemEnchant(new ArrayList<Enchantment>(Arrays.asList(Enchantment.LUCK)), itemStack);

            final ItemStack iStack = itemStack;
            final String uuid = k;
            GUIItem guiItem = new GUIItem(itemStack, event -> {
                ShowEditGUI2(player, uuid);
                window.dispose();
            });

            window.setItem(i, guiItem);

            ++i;
        }

        window.createInventories();
        window.show(player, 0);
    }

    public static void ShowEditGUI2(Player player, String genUUID) {
        GUIWindow window = new GUIWindow("Configure the generator", 3);

        ItemStack subtract = new ItemStack(Material.REDSTONE);
        subtract = setItemName("-1", subtract);

        ItemStack subtract25 = new ItemStack(Material.REDSTONE);
        subtract25 = setItemName("-0.25", subtract25);
        ItemStack subtract50 = new ItemStack(Material.REDSTONE);
        subtract50 = setItemName("-0.5", subtract50);

        ItemStack add = new ItemStack(Material.REDSTONE);
        add = setItemName("+1", add);

        ItemStack add25 = new ItemStack(Material.REDSTONE);
        add25 = setItemName("+0.25", add25);
        ItemStack add50 = new ItemStack(Material.REDSTONE);
        add50 = setItemName("+0.50", add50);

        ConfigurationSection config = Generators.INSTANCE.getConfig().getConfigurationSection("generators").getConfigurationSection(genUUID);

        AtomicInteger itemCount = new AtomicInteger(config.getInt("amount"));
        AtomicDouble timeCount = new AtomicDouble(config.getDouble("time"));

        ItemStack timeItem = new ItemStack(Material.WATCH, timeCount.intValue());
        timeItem = setItemName("Spawn Time: " + timeCount.floatValue() + "s", timeItem);
        window.setItem(2, 1, new GUIItem(timeItem, null));
        ItemStack itemItem = new ItemStack(Material.EXP_BOTTLE, itemCount.intValue());
        itemItem = setItemName("Spawn Items: " + itemCount.floatValue(), itemItem);
        window.setItem(6, 1, new GUIItem(itemItem, null));

        GUIItem subtract1 = new GUIItem(subtract, event -> {
            timeCount.addAndGet(-1);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });

        GUIItem subtract125 = new GUIItem(subtract25, event -> {
            timeCount.addAndGet(-0.25d);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });
        GUIItem subtract150 = new GUIItem(subtract50, event -> {
            timeCount.addAndGet(-0.50d);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });

        GUIItem subtract2 = new GUIItem(subtract, event -> {
            itemCount.decrementAndGet();
            ItemStack i = new ItemStack(Material.EXP_BOTTLE, itemCount.intValue());
            i = setItemName("Spawn Items: " + itemCount.floatValue(), i);
            player.getOpenInventory().setItem(15, i);
            player.updateInventory();
        });

        window.setItem(2, 2, subtract1);
        window.setItem(6, 2, subtract2);

        window.setItem(1, 2, subtract125);
        window.setItem(3, 2, subtract150);

        GUIItem add1 = new GUIItem(add, event -> {
            timeCount.addAndGet(1);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });
        GUIItem add2 = new GUIItem(add, event -> {
            itemCount.incrementAndGet();
            ItemStack i = new ItemStack(Material.EXP_BOTTLE, itemCount.intValue());
            i = setItemName("Spawn Items: " + itemCount.floatValue(), i);
            player.getOpenInventory().setItem(15, i);
            player.updateInventory();
        });

        GUIItem add125 = new GUIItem(add25, event -> {
            timeCount.addAndGet(0.25d);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });
        GUIItem add150 = new GUIItem(add50, event -> {
            timeCount.addAndGet(0.50d);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });

        window.setItem(2, 0, add1);
        window.setItem(6, 0, add2);

        window.setItem(1, 0, add125);
        window.setItem(3, 0, add150);

        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        confirm = setItemName("Confirm", confirm);

        GUIItem confirmItem = new GUIItem(confirm, event -> {
            config.set("time", timeCount.doubleValue());
            config.set("amount", itemCount.intValue());

            Generators.INSTANCE.saveConfig();

            window.close();
            window.dispose();

            ShowInitialGUI(player);
        });

        window.setItem(4, 1, confirmItem);

        window.show(player);
    }

    public static void ShowCreateGui1(Player player){
        GUIWindowPaged window = new GUIWindowPaged("Select a Generator Block");

        Material[] allItems = Material.values();

        List<Material> ignore = new ArrayList<Material>(Arrays.asList(
            Material.AIR,
            Material.BARRIER,
            Material.BURNING_FURNACE,
            Material.STATIONARY_WATER,
            Material.WOOD_DOUBLE_STEP,
            Material.REDSTONE_LAMP_ON,
            Material.GLOWING_REDSTONE_ORE,
            Material.DOUBLE_STEP,
            Material.DOUBLE_STONE_SLAB2,
            Material.PURPUR_DOUBLE_SLAB
        )); // Materials that don't render as an itemstack

        int i = 0;
        for ( Material val : allItems) {
            // Filter out items and blocks that can't be rendered as an itemstack
            if(!val.isBlock() || !val.isSolid() || !val.isOccluding() || ignore.contains(val))
                continue;

            final Material mat = val;

            ItemStack item  = new ItemStack(val, 1);
            if(item != null && item.getAmount() != 0){

                GUIItem guiItem = new GUIItem(item, event -> {
                    ShowCreateGui2(player, mat);
                    window.dispose();
                });

                window.setItem(i, guiItem);

                ++i;
            }
        }

        window.createInventories();
        window.show(player, 0);
    }

    public static void ShowCreateGui2(Player player, Material block) {
        GUIWindowPaged window = new GUIWindowPaged("Select an item to generate");

        Material[] allItems = Material.values();


        int i = 0;
        for ( Material val : allItems) {
            // Filter out items and blocks that can't be rendered as an itemstack
            if(val.isBlock())
                continue;

            final Material mat = val;

            ItemStack item  = new ItemStack(val, 1);
            if(item != null && item.getAmount() != 0){

                GUIItem guiItem = new GUIItem(item, event -> {
                    ShowCreateGui3(player, block, mat);
                    window.dispose();
                });

                window.setItem(i, guiItem);

                ++i;
            }
        }

        window.createInventories();
        window.show(player, 0);
    }

    public static void ShowCreateGui3(Player player, Material block, Material item) {
        GUIWindow window = new GUIWindow("Configure the generator", 3);

        ItemStack subtract = new ItemStack(Material.REDSTONE);
        subtract = setItemName("-1", subtract);

        ItemStack subtract25 = new ItemStack(Material.REDSTONE);
        subtract25 = setItemName("-0.25", subtract25);
        ItemStack subtract50 = new ItemStack(Material.REDSTONE);
        subtract50 = setItemName("-0.5", subtract50);

        ItemStack add = new ItemStack(Material.REDSTONE);
        add = setItemName("+1", add);

        ItemStack add25 = new ItemStack(Material.REDSTONE);
        add25 = setItemName("+0.25", add25);
        ItemStack add50 = new ItemStack(Material.REDSTONE);
        add50 = setItemName("+0.50", add50);

        AtomicInteger itemCount = new AtomicInteger(1);
        AtomicDouble timeCount = new AtomicDouble(5);

        ItemStack timeItem = new ItemStack(Material.WATCH, timeCount.intValue());
        timeItem = setItemName("Spawn Time: " + timeCount.floatValue() + "s", timeItem);
        window.setItem(2, 1, new GUIItem(timeItem, null));
        ItemStack itemItem = new ItemStack(Material.EXP_BOTTLE, itemCount.intValue());
        itemItem = setItemName("Spawn Items: " + itemCount.floatValue(), itemItem);
        window.setItem(6, 1, new GUIItem(itemItem, null));

        GUIItem subtract1 = new GUIItem(subtract, event -> {
            timeCount.addAndGet(-1);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });

        GUIItem subtract125 = new GUIItem(subtract25, event -> {
            timeCount.addAndGet(-0.25d);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });
        GUIItem subtract150 = new GUIItem(subtract50, event -> {
            timeCount.addAndGet(-0.50d);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });

        GUIItem subtract2 = new GUIItem(subtract, event -> {
            itemCount.decrementAndGet();
            ItemStack i = new ItemStack(Material.EXP_BOTTLE, itemCount.intValue());
            i = setItemName("Spawn Items: " + itemCount.floatValue(), i);
            player.getOpenInventory().setItem(15, i);
            player.updateInventory();
        });

        window.setItem(2, 2, subtract1);
        window.setItem(6, 2, subtract2);

        window.setItem(1, 2, subtract125);
        window.setItem(3, 2, subtract150);

        GUIItem add1 = new GUIItem(add, event -> {
            timeCount.addAndGet(1);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });
        GUIItem add2 = new GUIItem(add, event -> {
            itemCount.incrementAndGet();
            ItemStack i = new ItemStack(Material.EXP_BOTTLE, itemCount.intValue());
            i = setItemName("Spawn Items: " + itemCount.floatValue(), i);
            player.getOpenInventory().setItem(15, i);
            player.updateInventory();
        });

        GUIItem add125 = new GUIItem(add25, event -> {
            timeCount.addAndGet(0.25d);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });
        GUIItem add150 = new GUIItem(add50, event -> {
            timeCount.addAndGet(0.50d);
            ItemStack i = new ItemStack(Material.WATCH, timeCount.intValue());
            i = setItemName("Spawn Time: " + timeCount.floatValue() + "s", i);
            player.getOpenInventory().setItem(11, i);
            player.updateInventory();
        });

        window.setItem(2, 0, add1);
        window.setItem(6, 0, add2);

        window.setItem(1, 0, add125);
        window.setItem(3, 0, add150);

        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        confirm = setItemName("Confirm", confirm);

        GUIItem confirmItem = new GUIItem(confirm, event -> {
            window.close();
            window.dispose();
            FinishCreate(player, block, item, timeCount.floatValue(), itemCount.intValue());
        });

        window.setItem(4, 1, confirmItem);


        window.show(player);
    }

    public static void FinishCreate(Player player, Material block, Material item, float time, int amount){
        UUID uuid = UUID.randomUUID();
        ConfigurationSection config = Generators.INSTANCE.getConfig().getConfigurationSection("generators");
        config = config.createSection(uuid.toString());

        config.set("block", block.toString());
        config.set("item", item.toString());
        config.set("time", time);
        config.set("amount", amount);

        Generators.INSTANCE.saveConfig();

        ItemStack itemStack = new ItemStack(block, 64);
        itemStack = setItemName(formatMaterialName(item) + " Generator", itemStack);
        itemStack = setItemLore(new ArrayList<String>(Arrays.asList(
                "Gen: " + uuid.toString(),
                "Block: " + formatMaterialName(block),
                "Item: " + formatMaterialName(item),
                "Time: " + time,
                "Amount: " + amount
        )), itemStack);
        itemStack = setItemEnchant(new ArrayList<Enchantment>(Arrays.asList(Enchantment.LUCK)), itemStack);

        player.getInventory().addItem(itemStack);
    }

    private static ItemStack setItemName(String name, ItemStack stack){
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.setDisplayName(name);

        stack.setItemMeta(itemMeta);
        return stack;
    }

    private static ItemStack setItemLore(List<String> lore, ItemStack stack){
        ItemMeta itemMeta = stack.getItemMeta();

        itemMeta.setLore(lore);

        stack.setItemMeta(itemMeta);
        return stack;
    }

    private static ItemStack setItemEnchant(List<Enchantment> enchants, ItemStack stack){
        ItemMeta itemMeta = stack.getItemMeta();

        for(Enchantment enchant : enchants){
            itemMeta.addEnchant(enchant, 1, true);
        }

        stack.setItemMeta(itemMeta);
        return stack;
    }

    private static String formatMaterialName(Material mat){
        return capitalize(mat.toString().toLowerCase().replace("_", " "));
    }

    public static String capitalize(String text){
        String c = (text != null)? text.trim() : "";
        String[] words = c.split(" ");
        String result = "";
        for(String w : words){
            result += (w.length() > 1? w.substring(0, 1).toUpperCase(Locale.US) + w.substring(1, w.length()).toLowerCase(Locale.US) : w) + " ";
        }
        return result.trim();
    }
}
