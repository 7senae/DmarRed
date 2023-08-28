package com.foxy.redpvp;


import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SaveCommand {
  Main main = (Main)Main.getPlugin(Main.class);
  
  public static String validateItem(ItemStack item) {
    String _6ez = "";
    _6ez = _6ez + String.valueOf(item.getTypeId()) + ";";
    if (item.hasItemMeta() && item.getItemMeta().hasEnchants())
      for (Map.Entry<Enchantment, Integer> en : (Iterable<Map.Entry<Enchantment, Integer>>)item.getEnchantments().entrySet())
        _6ez = _6ez + "e@" + String.valueOf(((Enchantment)en.getKey()).getId()) + "@" + String.valueOf(en.getValue());  
    if (item.getAmount() > 1)
      _6ez = _6ez + "a@" + String.valueOf(item.getAmount()) + ";"; 
    return _6ez;
  }
  
  public static ItemStack unValidatedItems(String str) {
    ItemStack item = null;
    String[] zxc = str.split(";");
    try {
      int z = Integer.parseInt(zxc[0]);
      item = new ItemStack(Material.getMaterial(z));
    } catch (NumberFormatException numberFormatException) {}
    if (item != null && zxc.length > 1)
      for (int j = 1; j < zxc.length; j++) {
        String[] cx = zxc[j].split("@");
        if (cx[0].equalsIgnoreCase("e") && cx.length > 2) {
          Enchantment ench = null;
          int level = 1;
          try {
            ench = Enchantment.getById(Integer.parseInt(cx[1]));
            level = Integer.parseInt(cx[2]);
          } catch (NumberFormatException numberFormatException) {}
          if (ench != null)
            item.addUnsafeEnchantment(ench, level); 
        } else if (cx[0].equalsIgnoreCase("a")) {
          int _5 = 1;
          try {
            _5 = Integer.parseInt(cx[1]);
          } catch (NumberFormatException numberFormatException) {}
          if (_5 > 1)
            item.setAmount(_5); 
        } 
      }  
    return item;
  }
}
