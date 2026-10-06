package com.futuristic.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class FuturisticShop extends JavaPlugin implements Listener {

    private final String GUI_TITLE = ChatColor.LIGHT_PURPLE + "✧ " + ChatColor.AQUA + "" + ChatColor.BOLD + "FUTURISTIC SHOP" + ChatColor.LIGHT_PURPLE + " ✧";

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("shop").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                openFuturisticGUI((Player) sender);
            }
            return true;
        });
    }

    public void openFuturisticGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        // Bintang latar belakang (Border Futuristik)
        ItemStack glassFrame = createItem(Material.CYAN_STAINED_GLASS_PANE, ChatColor.AQUA + "✦ Matrix Grid ✦");
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, glassFrame);
        }

        // Contoh Item Toko
        gui.setItem(11, createItem(Material.DIAMOND, ChatColor.AQUA + "" + ChatColor.BOLD + "Diamond Star", ChatColor.GRAY + "Harga: " + ChatColor.GOLD + "$100"));
        gui.setItem(13, createItem(Material.NETHERITE_INGOT, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Quantum Ingot", ChatColor.GRAY + "Harga: " + ChatColor.GOLD + "$500"));
        gui.setItem(15, createItem(Material.GOLD_INGOT, ChatColor.YELLOW + "" + ChatColor.BOLD + "Starlight Gold", ChatColor.GRAY + "Harga: " + ChatColor.GOLD + "$50"));

        player.openInventory(gui);
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 1.5f);

        // Animasi Bintang & Partikel Futuristik
        new BukkitRunnable() {
            double angle = 0;

            @Override
            public void run() {
                if (!player.getOpenInventory().getTitle().equals(GUI_TITLE)) {
                    cancel();
                    return;
                }

                // Efek lingkaran partikel di sekitar pemain saat membuka GUI
                double x = 0.8 * Math.cos(angle);
                double z = 0.8 * Math.sin(angle);
                player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(x, 1.5, z), 1, 0, 0, 0, 0);
                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation().add(-x, 1.5, -z), 1, 0, 0, 0, 0);

                angle += Math.PI / 8;
            }
        }.runTaskTimer(this, 0L, 2L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(GUI_TITLE)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.CYAN_STAINED_GLASS_PANE) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        // Feedback Suara & Partikel Bintang Saat Klik
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 2.0f);
        player.getWorld().spawnParticle(Particle.CRIT_MAGIC, player.getLocation().add(0, 1, 0), 20, 0.3, 0.3, 0.3, 0.1);

        player.sendMessage(ChatColor.GREEN + "[Shop] " + ChatColor.WHITE + "Kamu memilih " + clickedItem.getItemMeta().getDisplayName());
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(line);
            }
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
        return item;
    }
                         }
          
