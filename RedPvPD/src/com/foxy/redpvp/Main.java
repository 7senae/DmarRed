package com.foxy.redpvp;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.foxy.redpvp.storage.Players;
import com.foxy.redpvp.storage.RBItems;
import com.foxy.redpvp.storage.Settings;

import net.minecraft.server.v1_8_R3.Block;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Main extends JavaPlugin implements Listener {
	public static Main plugin;
  public void onEnable() {
    getServer().getPluginManager().getPlugin(C("&aRedPVP has been enabled!"));
    Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)this);
    Bukkit.getServer().getPluginManager().registerEvents(new Sidebar(), (Plugin)this);
    Bukkit.getServer().getPluginManager().registerEvents(new Events(), (Plugin)this);
    getCommand("stats").setExecutor(new Commands());
    getCommand("redpvp").setExecutor(new Commands());
    getCommand("rp").setExecutor(new Commands());
    getCommand("redstonepvp").setExecutor(new Commands());
    getCommand("build").setExecutor(new Commands());
    getCommand("trash").setExecutor(new Commands());
    loadConfigManager();
    plugin = this;
  }
  

  public void onDisable() {
    getServer().getPluginManager().getPlugin(C("&4RedPVP has been disabled!"));
  }
  
  public void loadConfigManager() {
    Players.setup();
    Players.get().options().copyDefaults(true);
    Players.save();
    RBItems.setup();
    RBItems.get().options().copyDefaults(true);
    RBItems.save();
    Settings.setup();
    Settings.get().options().copyDefaults(true);
    Settings.save();

  }
  
  public static String C(String G) {
    return ChatColor.translateAlternateColorCodes('&', G);
  }
  
}
