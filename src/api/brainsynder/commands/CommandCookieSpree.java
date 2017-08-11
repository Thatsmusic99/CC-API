package api.brainsynder.commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import api.brainsynder.Core;
import api.brainsynder.Utils.Cooldown;
import api.brainsynder.commands.api.Command;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.wrappers.MaterialWrapper;

public class CommandCookieSpree extends CommandCore {
	
	private void removeCookies(Item item) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(item != null) {
					item.remove();
				}
			}
		}.runTaskLater(Core.get(), 100);
	}
	
    private void spawnCookies(Player p) {
        Random num = new Random();
        for (int x1 = 0; x1 <= 16; x1++) {
            double x = -0.5F + (float) (Math.random() * 0.9D);
            double y = 0.5D;
            double z = -0.5F + (float) (Math.random() * 0.9D);
            ItemMaker itemStack = new ItemMaker(MaterialWrapper.COOKIE);
            itemStack.setName("" + num.nextInt(100));
            Item item = p.getWorld().dropItem(p.getLocation(), itemStack.create());
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setVelocity(new Vector(x, y, z));
            removeCookies(item);
        }
    }
	
	@Command(name = "cookiespree")
	public void run(Player p) {
        if (Core.get().isBlockCommands()) {
            p.sendMessage(prefix + "Sorry, Chat Commands are disabled until further notice.");
            return;
        }
        if(!Cooldown.hasCooldown(p)) {
        	spawnCookies(p);
        	Cooldown.giveCooldown(p);
            ItemMaker itemStack = new ItemMaker(MaterialWrapper.COOKIE);
            itemStack.setName(ChatColor.translateAlternateColorCodes('&', "&6Chocolate Chip Cookie"));
            itemStack.addLoreLine(ChatColor.translateAlternateColorCodes('&', "&eFrom&7: &3" + p.getName()));
        	for(Player online : Bukkit.getOnlinePlayers()) {
        		if(online.getInventory().firstEmpty() == -1) continue;
        		online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eCatsCraft &6>> " + p.getName() + " &7tried to open the cookie jar but it exploded &6Freeee Cookiessss &d:D"));
        		online.getInventory().addItem(itemStack.create());
        	}
        }
	}
}