package com.foxy.redpvp;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.foxy.redpvp.storage.RBItems;
import com.foxy.redpvp.storage.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RandomBox {
  private int start = 0;
  
  private Long maxTime;
  
  private ArrayList<ItemStack> items = new ArrayList<>();
  
  public RandomBox(final Player p) {
    Settings.save();
    final int min = Settings.get().getInt("RedstonePvP.Settings.Random_Box.Minimum");
    final int max = Settings.get().getInt("RedstonePvP.Settings.Random_Box.Maximum");
    int invsize = Settings.get().getInt("RedstonePvP.Settings.Random_Box.Inventory-Size");
    String invname = Settings.get().getString("RedstonePvP.Settings.Random_Box.Inventory-Name");
    final Inventory inv = Bukkit.createInventory(null, invsize, Main.C(invname));
    if (RBItems.get().isList("RedstonePvP.RandomBox.Items")) {
      List<String> ifc = RBItems.get().getStringList("RedstonePvP.RandomBox.Items");
      for (String itemstring : ifc) {
        ItemStack ix = SaveCommand.unValidatedItems(itemstring);
        if (ix != null)
          this.items.add(ix); 
      } 
    } 
    if (this.items.size() > 0) {
      this.maxTime = Long.valueOf(System.currentTimeMillis() + 3000L);
      inv.setItem(min, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4));
      inv.setItem(max, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4));
      inv.setItem(min - 5, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4));
      inv.setItem(min - 4, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4));
      inv.setItem(min - 6, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4));
      inv.setItem(max + 5, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4));
      inv.setItem(max + 4, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4));
      inv.setItem(max + 6, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)4));
      this.start = (new Random()).nextInt(this.items.size());
      p.openInventory(inv);
      (new BukkitRunnable() {
          public void run() {
            if (RandomBox.this.maxTime.longValue() <= System.currentTimeMillis()) {
              int middle = (max - min) / 2 + min;
              ItemStack item = inv.getItem(middle);
              p.getInventory().addItem(new ItemStack[] { item });
              cancel();
              return;
            } 
            for (int i = min + 1; i < max; i++) {
              RandomBox.this.start = RandomBox.this.start + 1;
              if (RandomBox.this.start >= RandomBox.this.items.size())
                RandomBox.this.start = 0; 
              inv.setItem(i, RandomBox.this.items.get(RandomBox.this.start));
            } 
          }
        }).runTaskTimer((Plugin)Main.getPlugin(Main.class), 0L, 6L);
    } 
  }
  
  public ItemStack cI(Material mat, int amount) {
    return new ItemStack(mat, amount);
  }
}
