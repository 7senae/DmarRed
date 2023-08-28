package com.foxy.redpvp.storage;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.foxy.redpvp.Main;

public class Players {
  private static File file;
  
  private static FileConfiguration saves;

  
  public static void setup() {
    file = new File("plugins/DmarRedPvP", "Players.yml");
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
