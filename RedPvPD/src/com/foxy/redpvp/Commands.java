package com.foxy.redpvp;



import java.util.ArrayList;

import com.foxy.redpvp.storage.Players;
import com.foxy.redpvp.storage.RBItems;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

public class Commands implements CommandExecutor {
  public static ArrayList<Player> build = new ArrayList<>();
  LuckPerms luckPerms = LuckPermsProvider.get();

  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("resetstats"))
      if (args.length == 1) {
        if (Players.get().isInt("RedstonePvP.Statistics." + args[0] + ".kills")) {
          Players.get().set("RedstonePvP.Statistics." + args[0] + ".kills", Integer.valueOf(0));
          Players.get().set("RedstonePvP.Statistics." + args[0] + ".deaths", Integer.valueOf(0));
          Players.get().set("RedstonePvP.Statistics." + args[0] + ".killstreak", Integer.valueOf(0));
          Players.get().set("RedstonePvP.Statistics." + args[0] + ".points", Integer.valueOf(0));
          Players.save();
          sender.sendMessage(Main.C("&8? &cDmarMC &8? &7" + args[0] + "&c's stats have been resetted!"));
          if (Bukkit.getPlayer(args[0]) != null) {
            Player target = Bukkit.getPlayer(args[0]);
            target.sendMessage(Main.C("&8» &cDmarMC &8» &4Your stats have been resetted!"));
            return false;
          } 
        } else {
          sender.sendMessage(Main.C("&8» &cDmarMC &8» &cThis player doesn't exist neither online or offline!"));
        } 
      } else {
        sender.sendMessage(Main.C("&8» &cDmarMC &8» &cCorrect Usage: '&4/resetstats <player>&c'"));
      }  
    if (cmd.getName().equalsIgnoreCase("redpvp") || cmd
      .getName().equalsIgnoreCase("rp") || cmd.getName().equalsIgnoreCase("redstonepvp")) {
      if (sender instanceof Player) {
        Player p = (Player)sender;
        if (p.hasPermission("dmar.admin")) {
          if (args.length == 0) {
            p.sendMessage(Main.C("&8» &c/redstonepvp &7<help> &8- shows this list!"));
            p.sendMessage(Main.C("&8» &c/redstonepvp &7<setspawn> &8- set respawn location"));
            p.sendMessage(Main.C("&8» &c/redstonepvp &7<setdropparty> &8- set the drop party block [beacon]"));
            p.sendMessage(Main.C("&8» &c/redstonepvp &7<setrandombox> &8- set randombox [piston]"));
            p.sendMessage(Main.C("&8» &c/redstonepvp &7<save> &8- save your inventory to randombox items [Randombox.yml]"));
            p.sendMessage(Main.C("&8» &c/redstonepvp &7<setrepair> &8- set repairing block [anvil]"));
            p.sendMessage(Main.C("&8» &c/redstonepvp &7<setPlateLocation> &8- set the [teleporting pressure plate]"));
            p.sendMessage(Main.C("&8»&8"));
            p.sendMessage(Main.C(""));
          } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
              p.sendMessage(Main.C("&8» &c/redstonepvp &7<help> &8- shows this list!"));
              p.sendMessage(Main.C("&8» &c/redstonepvp &7<setspawn> &8- set respawn location"));
              p.sendMessage(Main.C("&8» &c/redstonepvp &7<setdropparty> &8- set the drop party block [beacon]"));
              p.sendMessage(Main.C("&8» &c/redstonepvp &7<setrandombox> &8- set randombox [piston]"));
              p.sendMessage(Main.C("&8» &c/redstonepvp &7<save> &8- save your inventory to randombox items [Randombox.yml]"));
              p.sendMessage(Main.C("&8» &c/redstonepvp &7<setrepair> &8- set repairing block [anvil]"));
              p.sendMessage(Main.C("&8» &c/redstonepvp &7<setPlateLocation> &8- set the [teleporting pressure plate]"));
              p.sendMessage(Main.C("&8»&8"));
              p.sendMessage(Main.C(""));
            } else if (args[0].equalsIgnoreCase("setspawn")) {
              Players.get().set("RedstonePvP.Settings.Locations.Spawn.World", p.getWorld().getName());
              Players.get().set("RedstonePvP.Settings.Locations.Spawn.x", Double.valueOf(p.getLocation().getX()));
              Players.get().set("RedstonePvP.Settings.Locations.Spawn.y", Double.valueOf(p.getLocation().getY()));
              Players.get().set("RedstonePvP.Settings.Locations.Spawn.z", Double.valueOf(p.getLocation().getZ()));
              Players.get().set("RedstonePvP.Settings.Locations.Spawn.yaw", Float.valueOf(p.getLocation().getYaw()));
              Players.get().set("RedstonePvP.Settings.Locations.Spawn.pitch", Float.valueOf(p.getLocation().getPitch()));
              p.sendMessage(Main.C("&aSpawn has been set successfully to:"));
              p.sendMessage(Main.C("&aWorld: &b" + p.getLocation().getWorld()));
              p.sendMessage(Main.C("&ax: &b" + p.getLocation().getX()));
              p.sendMessage(Main.C("&ay: &b" + p.getLocation().getY()));
              p.sendMessage(Main.C("&az: &b" + p.getLocation().getZ()));
              p.sendMessage(Main.C("&ayaw: &b" + p.getLocation().getYaw()));
              p.sendMessage(Main.C("&apitch: &b" + p.getLocation().getPitch()));
              Players.save();
            } else if (args[0].equalsIgnoreCase("setplatelocation")) {
              Players.get().set("RedstonePvP.Settings.Locations.PressurePlate.Location", p.getLocation());
              Players.save();
            } else if (args[0].equalsIgnoreCase("save")) {
              PlayerInventory playerInventory = p.getInventory();
              ArrayList<String> items = new ArrayList<>();
              for (int i = 0; i < p.getInventory().getSize(); i++) {
                if (playerInventory.getItem(i) != null)
                  items.add(SaveCommand.validateItem(playerInventory.getItem(i))); 
              } 
              RBItems.get().set("RedstonePvP.RandomBox.Items", items);
              RBItems.save();
              RBItems.reload();
              p.sendMessage(Main.C("&aYou've saved your inventory as RandomBox Items"));
            } else if (args[0].equalsIgnoreCase("setrepair")) {
              p.getWorld().getBlockAt(p.getLocation()).setTypeId(145);
              p.sendMessage(Main.C("&eRepairingAnvil &ehas been spawned!"));
            } else if (args[0].equalsIgnoreCase("setdropparty")) {
              p.getWorld().getBlockAt(p.getLocation()).setTypeId(138);
              p.sendMessage(Main.C("&aDropParty &ehas been spawned!"));
            } else if (args[0].equalsIgnoreCase("setrandombox")) {
              p.getWorld().getBlockAt(p.getLocation()).setTypeId(33);
              p.sendMessage(Main.C("&eRandomBox &ehas been spawned!"));
            } 
          } 
        } else {
          sender.sendMessage(Main.C(""));
        } 
      } else {
        sender.sendMessage(Main.C("&cThis commmand is only for players!"));
      } 
    } else if (cmd.getName().equalsIgnoreCase("trash")) {
      if (sender instanceof Player) {
        Player p = (Player)sender;
        Inventory i = Bukkit.createInventory((InventoryHolder)p, 54, Main.C("&4&lTrash"));
        p.openInventory(i);
      } 
    } else if (cmd.getName().equalsIgnoreCase("build")) {
      if (args.length == 0 && 
        sender instanceof Player) {
        Player p = (Player)sender;
        if (p.hasPermission("build.use")) {
          if (build.contains(p)) {
            build.remove(p);
            p.sendMessage(Main.C("&cYour build mode has been turned off!"));
            return false;
          } 
          build.add(p);
          p.sendMessage(Main.C("&aYour build mode has been turned on!"));
          return false;
        } 
      } 
    } else if (cmd.getName().equalsIgnoreCase("stats")) {
      if (args.length == 0) {
        if (sender instanceof Player) {
          Player p = (Player)sender;
          int kills = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".kills");
          int deaths = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".deaths");
          int killstreak = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".killstreak");
          int points = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".points");
          p.sendMessage(Main.C("&8•&m--------------------------"));
          p.sendMessage(Main.C("&8• &cName &8 :&7"+ p.getName()));
          p.sendMessage(Main.C("&8• &cKills &8 : &7"+kills));
          p.sendMessage(Main.C("&8• &cDeaths &8 : &7"+deaths));
          p.sendMessage(Main.C("&8• &cKillstreak &8: &7"+killstreak));
          p.sendMessage(Main.C("&8• &cPoints &8: &7"+points));
          p.sendMessage(Main.C("&8•&m--------------------------"));
        } 
      } else if (args.length == 1) {
        if (Bukkit.getServer().getPlayer(args[0]) != null) {
          Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
          int kills = Players.get().getInt("RedstonePvP.Statistics." + targetPlayer.getName() + ".kills");
          int deaths = Players.get().getInt("RedstonePvP.Statistics." + targetPlayer.getName() + ".deaths");
          int killstreak = Players.get().getInt("RedstonePvP.Statistics." + targetPlayer.getName() + ".killstreak");
          int points = Players.get().getInt("RedstonePvP.Statistics." + targetPlayer.getName() + ".points");
          String suffix = this.luckPerms.getGroupManager().getGroup(this.luckPerms.getUserManager().getUser(targetPlayer.getName()).getPrimaryGroup()).getCachedData().getMetaData().getSuffix();
          sender.sendMessage(Main.C("&8•&m--------------------------"));
          sender.sendMessage(Main.C("&8• &cName &8 "+ suffix + targetPlayer.getName()));
          sender.sendMessage(Main.C("&8• &cKills &8 : &7"+kills));
          sender.sendMessage(Main.C("&8• &cDeaths &8 : &7"+deaths));
          sender.sendMessage(Main.C("&8• &cKillstreak &8: &7"+killstreak));
          sender.sendMessage(Main.C("&8• &cPoints &8: &7"+points));
          sender.sendMessage(Main.C("&8•&m--------------------------"));
        } else {
          sender.sendMessage(Main.C("&cThis player is not online!"));
        } 
      } 
    } 
    return false;
  }
}
