package com.foxy.redpvp.storage;


import java.io.File;
import java.io.IOException;
import com.foxy.redpvp.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {
  private static File file;
  
  private static FileConfiguration saves;
  
  public static void setup() {
    file = new File("plugins/DmarRedPvP", "Settings.yml");
    if (!file.exists())
      ((Main)Main.getPlugin(Main.class)).saveResource("Settings.yml", true); 
    saves = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
  }
  
  public static FileConfiguration get() {
    return saves;
  }
  
  public static void save() {
    try {
      saves.save("Settings.yml");
    } catch (IOException e) {
      System.out.println(Main.C("&cCould'n Save File"));
    } 
  }
  
  public static void reload() {
    saves = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
  }
}
