package keithcod.es.generators;


import com.google.common.util.concurrent.AtomicDouble;
import com.sun.org.apache.xpath.internal.operations.Bool;
import keithcod.es.gui.GUIListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;

public class Generators extends JavaPlugin {

    public static Generators INSTANCE;

    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;

    @Override
    public void onEnable() {
        INSTANCE = this;

        if(!getConfig().isConfigurationSection("generators"))
            getConfig().createSection("generators");
        if(!getConfig().isConfigurationSection("locations"))
            getConfig().createSection("locations");

        saveConfig();

        if(!setupEconomy()){
            log.severe(String.format("[%s] - No Vault dependency found! Economy related features will be disabled. ;)", getDescription().getName()));
        }
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);

        AtomicDouble t = new AtomicDouble(0);

        // Run every .25s
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
//                System.out.println("Generator tick... " + t.doubleValue() + "s");
                ConfigurationSection generators = getConfig().getConfigurationSection("generators");
                for(String k : generators.getKeys(false)){
                    ConfigurationSection gen = generators.getConfigurationSection(k);
                    double time = gen.getDouble("time");
                    boolean generate = t.doubleValue() % time == 0;
                    if(generate) {
                        int amount = gen.getInt("amount");
                        Material mat = Material.getMaterial(gen.getString("item"));
                        if (gen.getStringList("locations") != null) {
                            for (String pos : gen.getStringList("locations")) {
                                World world = Bukkit.getServer().getWorld(pos.split(",")[0]);
                                float x = Integer.parseInt(pos.split(",")[1])+0.5f;
                                float y = Integer.parseInt(pos.split(",")[2])+1;
                                float z = Integer.parseInt(pos.split(",")[3])+0.5f;

                                world.dropItem(new Location(world, x, y, z), new ItemStack(mat, amount));
                            }
                        }
                    }
                }

                t.addAndGet(0.25);
            }
        }, 0L, 5L);

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("gen")) {

            GUI.ShowGUI((Player) sender);

            return true;
        }
        return false;
    }

    @Override
    public void onDisable(){

    }
}
