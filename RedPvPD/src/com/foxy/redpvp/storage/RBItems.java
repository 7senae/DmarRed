package com.foxy.redpvp.storage;


import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.foxy.redpvp.Main;

public class RBItems {
  private static File file;
  
  private static FileConfiguration saves;
  
  public static void setup() {
    file = new File("plugins/DmarRedPvP", "Randombox.yml");
    if (!file.exists())
      try {
        file.createNewFile();
      } catch (IOException iOException) {} 
    saves = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
  }
  
  public static FileConfiguration get() {
    return saves;
  }
  
  public static void save() {
    try {
      saves.save(file);
    } catch (IOException e) {
      System.out.println(Main.C("&cCould'n Save File"));
    } 
  }
  
  public static void reload() {
    saves = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
  }
}
