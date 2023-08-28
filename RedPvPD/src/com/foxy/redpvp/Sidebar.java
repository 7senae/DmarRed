package com.foxy.redpvp;




import com.foxy.redpvp.storage.Players;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class Sidebar implements Listener {
  private Plugin plugin = (Plugin)Main.getPlugin(Main.class);
  LuckPerms luckPerms = LuckPermsProvider.get();


  public static void SB(Player p) {
	    ScoreboardManager manger = Bukkit.getServer().getScoreboardManager();
	    Scoreboard SB = manger.getNewScoreboard();

	    if (!Players.get().isInt("RedstonePvP.Statistics." + p.getName() + ".kills"))
	      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".kills", Integer.valueOf(0)); 
	    if (!Players.get().isInt("RedstonePvP.Statistics." + p.getName() + ".deaths"))
	      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".deaths", Integer.valueOf(0)); 
	    if (!Players.get().isInt("RedstonePvP.Statistics." + p.getName() + ".killstreak"))
	      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".killstreak", Integer.valueOf(0)); 
	    if (!Players.get().isInt("RedstonePvP.Statistics." + p.getName() + ".points"))
	      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".points", Integer.valueOf(0)); 
	    Players.save();
	    int kills = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".kills");
	    int deaths = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".deaths");
	    int killstreak = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".killstreak");
	    int points = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".points");

	    Objective o = SB.registerNewObjective("foxy", "dummy");
	    o.setDisplaySlot(DisplaySlot.SIDEBAR);
	    o.setDisplayName(Main.C("&4   Redstone&7&lPvP  &e"));
	    o.getScore(Main.C("&8●&m----------&7")).setScore(8);
	    o.getScore(Main.C("&8● &cName &8: &7"+p.getName())).setScore(7);
	    o.getScore(Main.C("&8● &cKills &8 : &7"+kills )).setScore(6);
	    o.getScore(Main.C("&8● &cDeaths &8 : &7"+deaths )).setScore(5);
	    o.getScore(Main.C("&8● &cKillstreak &8: &7"+killstreak )).setScore(4);
	    o.getScore(Main.C("&8● &cPoints  &8 : &7"+points )).setScore(3);
	    o.getScore(Main.C("&8●&m----------")).setScore(2);
	    o.getScore(Main.C("&cDmar&4MC.com")).setScore(1);


	    p.setScoreboard(SB);
	  }
  
}
