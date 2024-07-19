package de.senf.elytra;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Elytra extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location spawnLocation = player.getWorld().getSpawnLocation();
        double distance = player.getLocation().distance(spawnLocation);

        if (distance < 60) {
            if (!hasElytra(player) && !player.getInventory().contains(Material.ELYTRA)) {
                equipElytra(player);
            }
        } else {
            if (hasElytra(player) && isOnGround(player)) {
                removeElytra(player);
            }
        }
    }

    private boolean hasElytra(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate != null && chestplate.getType() == Material.ELYTRA) {
            ItemMeta meta = chestplate.getItemMeta();
            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(player.getName() + "´s- Flügel")) {
                return true;
            }
        }
        return false;
    }

    private void equipElytra(Player player) {
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta meta = elytra.getItemMeta();
        meta.setDisplayName(player.getName() + "´s- Flügel");
        List<String> lore = new ArrayList<>();
        lore.add("Einweg-Elytra");
        meta.setLore(lore);
        elytra.setItemMeta(meta);
        player.getInventory().setChestplate(elytra);
    }

    private void removeElytra(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate != null && chestplate.getType() == Material.ELYTRA) {
            ItemMeta meta = chestplate.getItemMeta();
            if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals(player.getName() + "´s- Flügel")) {
                player.getInventory().setChestplate(null);
            }
        }
    }

    private boolean isOnGround(Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() - 0.5); // Adjust the offset to check slightly below the player's feet
        return !location.getBlock().getType().equals(Material.AIR);
    }
}