package de.senf.elytra;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Elytra extends JavaPlugin implements Listener {


    private HashMap<UUID, Boolean> flyingPlayers = new HashMap<>();
    private String elytraName;
    private int elytraRadius;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        getServer().getPluginManager().registerEvents(this, this);
        System.out.println(Color.GREEN + "Plugin Loaded!");
    }

    private void loadConfigValues() {
        FileConfiguration config = getConfig();
        elytraName = config.getString("elytra-name", "Default Elytra");
        elytraRadius = config.getInt("elytra-radius", 60);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location spawnLocation = player.getWorld().getSpawnLocation();
        double distance = player.getLocation().distance(spawnLocation);

        if (distance < elytraRadius) {
            if (!hasElytra(player) && !player.getInventory().contains(Material.ELYTRA)) {
                equipElytra(player);
                flyingPlayers.put(player.getUniqueId(), true);
            }
        } else {
            if (hasElytra(player)) {
                if (isOnGround(player)) {
                    if (flyingPlayers.getOrDefault(player.getUniqueId(), false)) {
                        removeElytra(player);
                        flyingPlayers.remove(player.getUniqueId());
                    }
                } else {
                    flyingPlayers.put(player.getUniqueId(), true);
                }
            }
        }
    }

    private boolean hasElytra(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate != null && chestplate.getType() == Material.ELYTRA) {
            ItemMeta meta = chestplate.getItemMeta();
            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(elytraName)) {
                return true;
            }
        }
        return false;
    }

    private void equipElytra(Player player) {
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta meta = elytra.getItemMeta();
        meta.setDisplayName(elytraName);
        List<String> lore = new ArrayList<>();
        lore.add("");
        meta.setLore(lore);
        elytra.setItemMeta(meta);
        player.getInventory().setChestplate(elytra);
    }

    private void removeElytra(Player player) {
        player.getInventory().setChestplate(null);
    }

    private boolean isOnGround(Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() - 0.5);
        return !location.getBlock().getType().equals(Material.AIR);
    }
}