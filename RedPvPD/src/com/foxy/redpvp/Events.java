package com.foxy.redpvp;


import java.util.HashMap;
import java.util.Random;

import com.foxy.redpvp.storage.Players;
import com.foxy.redpvp.storage.Settings;

import net.minecraft.server.v1_8_R3.EntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Events implements Listener {
  private HashMap<String, Long> cooldown = new HashMap<>();
  
  private final int cooldownPressurePlate = 60;
  
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    if (!Players.get().isInt("RedstonePvP.Statistics." + p.getName() + ".kills"))
      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".kills", Integer.valueOf(0)); 
    if (!Players.get().isInt("RedstonePvP.Statistics." + p.getName() + ".deaths"))
      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".deaths", Integer.valueOf(0)); 
    if (!Players.get().isInt("RedstonePvP.Statistics." + p.getName() + ".killstreak"))
      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".killstreak", Integer.valueOf(0)); 
    if (!Players.get().isInt("RedstonePvP.Statistics." + p.getName() + ".points"))
      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".points", Integer.valueOf(0)); 
    Players.save();
    Location loc = new Location(Bukkit.getWorld(Players.get().getString("RedstonePvP.Settings.Locations.Spawn.World")), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.x"), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.y"), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.z"), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.yaw"), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.pitch"));
    p.teleport(loc);
    Sidebar.SB(p);

  }
  
  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    Player p = e.getPlayer();
    Players.get().set("RedstonePvP.Statistics." + p.getName() + ".killstreak", Integer.valueOf(0));
    Players.save();
    Sidebar.SB(p);

  }

  @EventHandler
  public void onBreak(BlockBreakEvent e) {
    Player p = e.getPlayer();
    if (!Commands.build.contains(p))
      e.setCancelled(true); 
  }
  
  @EventHandler
  public void onPlace(BlockPlaceEvent e) {
    Player p = e.getPlayer();
    if (!Commands.build.contains(p))
      e.setCancelled(true); 
  }
  
  @EventHandler
  public void onInvClick(InventoryClickEvent e) {
    Player p = (Player)e.getWhoClicked();
    Inventory inv = e.getClickedInventory();
    if (inv == null)
      return; 
    if (inv.getName().contains("Random"))
      e.setCancelled(true); 
  }
  
  @EventHandler
  public void onHungerChange(FoodLevelChangeEvent e) {
    e.setCancelled(true);
  }
  
  @EventHandler
  public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage(Main.C("&8• &CDmarMC &8» &cUse &4/trash &cto drop your un-necessary Items!"));
        e.getPlayer().sendMessage(Main.C("&8• &CDmarMC &8» &cUse &4/trade to trade with your friends "));

  }
  @EventHandler
  public void fishingThrow(ProjectileLaunchEvent e) {
    Projectile ege = e.getEntity();
    if (e.getEntityType().equals(EntityType.FISHING_HOOK))
      ege.setVelocity(ege.getVelocity().multiply(1.4D)); 
  }

  

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onDamage(EntityDamageByEntityEvent e) {
      if (e.getEntity().getLocation().getZ() < -67.0D) {
          e.setCancelled(true);
          return;
      }
      if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
          e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), new ItemStack(
                  Material.getMaterial(Settings.get().getInt("RedstonePvP.Settings.Main_Item")), 2));
          int chance = Settings.get().getInt("RedstonePvP.Settings.Sub_Item_DropChance");
          int random = (new Random()).nextInt(chance - 1);
          if (random == 1)
              e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), new ItemStack(
                      Material.getMaterial(Settings.get().getInt("RedstonePvP.Settings.Sub_Item")),
                      Settings.get().getInt("RedstonePvP.Settings.Sub_Item_DropAmount")));
      }
  }
  
  @EventHandler
  public void onDeath(EntityDeathEvent e){

      if(e.getDroppedExp()==0){
          Player victim = (Player) e.getEntity();
          e.setDroppedExp(e.getDroppedExp());
      }
  }
  
  @EventHandler
  public void onRespawn(PlayerRespawnEvent e) {
    Player p = e.getPlayer();
    Location loc = new Location(Bukkit.getWorld(Players.get().getString("RedstonePvP.Settings.Locations.Spawn.World")), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.x"), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.y"), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.z"), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.yaw"), Players.get().getInt("RedstonePvP.Settings.Locations.Spawn.pitch"));
    e.setRespawnLocation(loc);
    p.getInventory().setHelmet(MUItem(Material.DIAMOND_HELMET, 1, null, 0));
    p.getInventory().setChestplate(MUItem(Material.DIAMOND_CHESTPLATE, 1, null, 0));
    p.getInventory().setLeggings(MUItem(Material.DIAMOND_LEGGINGS, 1, null, 0));
    p.getInventory().setBoots(MUItem(Material.DIAMOND_BOOTS, 1, null, 0));
    p.getInventory().setItem(0, MUItem(Material.DIAMOND_SWORD, 1, null, 0));
    p.getInventory().setItem(1, MUItem(Material.BOW, 1, null, 0));
    p.getInventory().setItem(2, MUItem(Material.FISHING_ROD, 1, null, 0));
    p.getInventory().setItem(3, MUItem(Material.GOLDEN_APPLE, 64, null, 0));
    p.getInventory().setItem(9, MUItem(Material.ARROW, 64, null, 0));
  }
  
  @EventHandler
  public void onDeath(PlayerDeathEvent e) {
    Player p = e.getEntity();
    if (e.getEntity().getKiller() instanceof Player) {
      Player k = e.getEntity().getKiller();
      int deaths = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".deaths");
      deaths++;
      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".deaths", Integer.valueOf(deaths));
      int kills = Players.get().getInt("RedstonePvP.Statistics." + k.getName() + ".kills");
      kills++;
      Players.get().set("RedstonePvP.Statistics." + k.getName() + ".kills", Integer.valueOf(kills));
      int killstreak = Players.get().getInt("RedstonePvP.Statistics." + k.getName() + ".killstreak");
      killstreak++;
      Players.get().set("RedstonePvP.Statistics." + k.getName() + ".killstreak", Integer.valueOf(killstreak));
      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".killstreak", Integer.valueOf(0));
      int pointsVictim = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".points");
      int pointsKiller = Players.get().getInt("RedstonePvP.Statistics." + k.getName() + ".points");
      pointsVictim -= 2;
      pointsKiller += 3;
      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".points", Integer.valueOf(pointsVictim));
      Players.get().set("RedstonePvP.Statistics." + k.getName() + ".points", Integer.valueOf(pointsKiller));
      Players.save();
      e.setDeathMessage(Main.C("&8» &c%victim% &7has been killed by &a" + k.getName() + "  &8[ &4" + 
            (int)k.getHealth() + " &8]").replace("%victim%", p.getName()));
      if (killstreak % 5 == 0)
        Bukkit.broadcastMessage(Main.C("&8» &6%killer% &7has &6").replace("%killer%", k.getDisplayName()) + killstreak + 
            Main.C(" &7killstreaks now!")); 
      Sidebar.SB(p);
      Sidebar.SB(k);
      
    } else {
      int deaths = Players.get().getInt("RedstonePvP.Statistics." + p.getName() + ".deaths");
      deaths++;
      Players.get().set("RedstonePvP.Statistics." + p.getName() + ".deaths", Integer.valueOf(deaths));
      e.setDeathMessage(ChatColor.GRAY + p.getName() + " died!");
      Players.save();
    } 
  }
  
  @EventHandler
  public void onRightClick(PlayerInteractEvent e) {
    final Block block = e.getClickedBlock();
    final Player p = e.getPlayer();
    if (e.getAction() == Action.LEFT_CLICK_BLOCK && 
      e.getClickedBlock().getType().equals(Material.ANVIL)) {
      e.setCancelled(true);
      int matid = Settings.get().getInt("RedstonePvP.Settings.Main_Item");
      int repairallcost = Settings.get().getInt("RedstonePvP.Settings.Repair.RepairAll_Cost");
      if (p.getInventory().containsAtLeast(new ItemStack(Material.getMaterial(matid)), repairallcost)) {
        ItemStack main_item = new ItemStack(Material.getMaterial(matid), repairallcost);
        p.getInventory().removeItem(new ItemStack[] { main_item });
        ItemStack[] contents = p.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
          ItemStack item = contents[i];
          if (item.getDurability() != 0) {
            item.setDurability((short)0);
            p.sendMessage(Main.C("&a" + item.getType().toString() + " &ehas been repaired!"));
            contents[i] = item;
          } 
        } 
        p.getInventory().setContents(contents);
        p.updateInventory();
      } else {
        p.sendMessage(Main.C("&cSorry, but you don't have enough redstone!"));
      } 
    } 
    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      if (e.getClickedBlock().getType().equals(Material.EMERALD_BLOCK)) {
        p.setHealth(20.0D);
        p.setFoodLevel(20);
        p.setFireTicks(0);
        Location loc = new Location(block.getWorld(), block.getLocation().getX(), block.getLocation().getY() + 1.0D, block.getLocation().getZ());
        p.getWorld().playEffect(loc, Effect.HEART, 1);
      } else if (e.getClickedBlock().getType().equals(Material.ANVIL)) {
        int matid = Settings.get().getInt("RedstonePvP.Settings.Main_Item");
        int repaircost = Settings.get().getInt("RedstonePvP.Settings.Repair.Cost");
        ItemStack main_item = new ItemStack(Material.getMaterial(matid), repaircost);
        e.setCancelled(true);
        if (p.getInventory().containsAtLeast(main_item, repaircost)) {
          if (p.getItemInHand() != null || p.getItemInHand().hasItemMeta()) {
            if (p.getItemInHand().getDurability() != 0) {
              p.getInventory().removeItem(new ItemStack[] { main_item });
              p.getItemInHand().setDurability((short)0);
              p.sendMessage(Main.C("&a" + p.getItemInHand().getType().toString() + " &ehas been repaired!"));
            } else {
              p.sendMessage(Main.C("&cThis item is already repaired!"));
            } 
          } else {
            p.sendMessage(Main.C("&cThis item can't be repaired!"));
            return;
          } 
        } else {
          p.sendMessage(Main.C("&cSorry, but you don't have enough redstone!"));
        } 
      } 
      if (e.getClickedBlock().getType().equals(Material.getMaterial(138))) {
        if (!Settings.get().isInt("RedstonePvP.Settings.Drop_Party.cooldown"))
          Settings.get().set("RedstonePvP.Settings.Drop_Party.cooldown", Integer.valueOf(1800)); 
        if (!Settings.get().isInt("RedstonePvP.Settings.Drop_Party.minimum_players"))
          Settings.get().set("RedstonePvP.Settings.Drop_Party.minimum_players", Integer.valueOf(5)); 
        Settings.save();
        int cooldowndropparty = Settings.get().getInt("RedstonePvP.Settings.Drop_Party.cooldown");
        int droppartyminimum = Settings.get().getInt("RedstonePvP.Settings.Drop_Party.minimum_players");
        e.setCancelled(true);
        int online = Bukkit.getOnlinePlayers().size();
        if (online >= droppartyminimum) {
          if (this.cooldown.containsKey("dropparty")) {
            long timeleft = ((Long)this.cooldown.get("dropparty")).longValue() / 1000L + cooldowndropparty - System.currentTimeMillis() / 1000L;
            int seconds = (int)timeleft % 60;
            int minutes = (int)((timeleft - seconds) / 60L);
            if (timeleft > 0L) {
              p.sendMessage(Main.C("&cYou have to wait &6(" + minutes + " minutes , " + seconds + " seconds) &cbefore activating the DropParty!"));
              return;
            } 
            this.cooldown.remove("dropparty");
          } else {
            this.cooldown.put("dropparty", Long.valueOf(System.currentTimeMillis()));
            Bukkit.broadcastMessage(Main.C("&8? &eDrop Party is starting in &a30 &eseconds"));
            (new BukkitRunnable() {
                int count = 30;
                
                Location loc = new Location(block.getWorld(), block.getLocation().getX(), block
                    .getLocation().getY() + 3.0D, block.getLocation().getZ());
                
                int matid = Settings.get().getInt("RedstonePvP.Settings.Main_Item");
                
                ItemStack item1 = new ItemStack(Material.getMaterial(this.matid), 1);
                
                int amount_of_stacks = Settings.get().getInt("RedstonePvP.Settings.Drop_Party.Amount_of_Stacks");
                
                ItemStack item2 = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
                
                public void run() {
                  if (this.count == 25)
                    Bukkit.broadcastMessage(Main.C("&8» &eDrop Party is starting in &a25 &eseconds")); 
                  if (this.count == 20)
                    Bukkit.broadcastMessage(Main.C("&8» &eDrop Party is starting in &a20 &eseconds")); 
                  if (this.count == 15)
                    Bukkit.broadcastMessage(Main.C("&8» &eDrop Party is starting in &a15 &eseconds")); 
                  if (this.count == 10)
                    Bukkit.broadcastMessage(Main.C("&8» &eDrop Party is starting in &a10 &eseconds")); 
                  if (this.count == 5)
                    Bukkit.broadcastMessage(Main.C("&8» &eDrop Party is starting in &a5 &eseconds")); 
                  if (this.count == 4)
                    Bukkit.broadcastMessage(Main.C("&8» &eDrop Party is starting in &a4 &eseconds")); 
                  if (this.count == 3)
                    Bukkit.broadcastMessage(Main.C("&8» &eDrop Party is starting in &a3 &eseconds")); 
                  if (this.count == 2)
                    Bukkit.broadcastMessage(Main.C("&8» &eDrop Party is starting in &a2 &eseconds")); 
                  if (this.count == 1)
                    Bukkit.broadcastMessage(Main.C("&8» &eDrop Party is starting in &a1 &eseconds")); 
                  if (this.count == 0)
                    Bukkit.broadcastMessage(Main.C("&8» &aDrop Party has been activated!")); 
                  this.count--;
                  if (this.count < 0) {
                    int random = (new Random()).nextInt(9);
                    if (random == 0) {
                      p.getWorld().dropItemNaturally(this.loc, Events.this
                          .MUItem(Material.DIAMOND_SWORD, 1, Enchantment.DAMAGE_ALL, 5));
                    } else if (random == 1) {
                      p.getWorld().dropItemNaturally(this.loc, Events.this
                          .MUItem(Material.DIAMOND_CHESTPLATE, 1, Enchantment.PROTECTION_ENVIRONMENTAL, 4));
                    } else if (random == 2) {
                      p.getWorld().dropItemNaturally(this.loc, Events.this
                          .MUItem(Material.DIAMOND_LEGGINGS, 1, Enchantment.PROTECTION_ENVIRONMENTAL, 4));
                    } else if (random == 3) {
                      p.getWorld().dropItemNaturally(this.loc, Events.this
                          .MUItem(Material.DIAMOND_BOOTS, 1, Enchantment.PROTECTION_ENVIRONMENTAL, 4));
                    } else if (random == 4) {
                      p.getWorld().dropItemNaturally(this.loc, Events.this
                          .MUItem(Material.DIAMOND_HELMET, 1, Enchantment.PROTECTION_ENVIRONMENTAL, 4));
                    } else if (random == 6) {
                      p.getWorld().dropItemNaturally(this.loc, Events.this
                          .MUItem(Material.BOW, 1, Enchantment.ARROW_KNOCKBACK, 3));
                    } else if (random == 5) {
                      p.getWorld().dropItemNaturally(this.loc, this.item2);
                    } 
                    block.getWorld().dropItemNaturally(this.loc, this.item1);
                    if (this.count == -this.amount_of_stacks) {
                      cancel();
                      Bukkit.broadcastMessage(Main.C("&8» &4Drop Party has ended!"));
                    } 
                  } 
                }
              }).runTaskTimer((Plugin)Main.getPlugin(Main.class), 20L, 20L);
          } 
        } else {
          p.sendMessage(Main.C("&cThere must be more than &6" + droppartyminimum + " &cto to activate the DropParty!"));
        } 
      } else if (block.getType().equals(Material.PISTON_BASE)) {
        int matid = Settings.get().getInt("RedstonePvP.Settings.Main_Item");
        int amount = Settings.get().getInt("RedstonePvP.Settings.Random_Box.Cost");
        ItemStack rCost = new ItemStack(Material.getMaterial(matid), amount);
        if (p.getInventory().containsAtLeast(new ItemStack(Material.getMaterial(matid)), amount)) {
          p.getInventory().removeItem(new ItemStack[] { rCost });
          (new BukkitRunnable() {
              public void run() {
                new RandomBox(p);
              }
            }).runTaskLater((Plugin)Main.getPlugin(Main.class), 5L);
        } else {
          p.sendMessage(Main.C("&cSorry, You don't have enough redstone!"));
        } 
      } 
    } 
  }
  
  public ItemStack MUItem(Material mat, int amount, Enchantment ench, int level) {
    ItemStack item = new ItemStack(mat);
    item.setAmount(amount);
    if (level > 0 && ench != null)
      item.addUnsafeEnchantment(ench, level); 
    return item;
  }
  
  @EventHandler
  public void Claymore(PlayerInteractEvent e) {
    if (e.getAction().equals(Action.PHYSICAL) && 
      e.getClickedBlock().getType() == Material.GOLD_PLATE) {
      Player p = e.getPlayer();
      if (this.cooldown.containsKey("pressureplate")) {
        long timeleft = ((Long)this.cooldown.get("pressureplate")).longValue() / 1000L + 60L - System.currentTimeMillis() / 1000L;
        if (timeleft > 0L) {
          p.sendMessage(Main.C("&cYou have to wait &6(" + timeleft + " seconds) &cbefore teleporting again!"));
          return;
        } 
        this.cooldown.remove("pressureplate");
      } else {
        Location loc = (Location)Players.get().get("RedstonePvP.Settings.Locations.PressurePlate.Location");
        p.teleport(loc);
        this.cooldown.put("pressureplate", Long.valueOf(System.currentTimeMillis()));
      } 
    } 
  }
}
  
