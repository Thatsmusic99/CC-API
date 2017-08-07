package api.brainsynder.commands;

import api.brainsynder.Core;
import api.brainsynder.Utils.Cooldown;
import api.brainsynder.commands.api.Command;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.sound.SoundMaker;

import java.util.Random;

public class CommandExplode extends CommandCore {

    @Command(name = "explode")
    public void run(Player p) {
        if (Core.get().isBlockCommands()) {
            p.sendMessage(prefix + "Sorry, Chat Commands are disabled until further notice.");
            return;
        }

        if (!Cooldown.hasCooldown(p)) {
            Cooldown.giveCooldown(p);
            Bukkit.getOnlinePlayers()
                    .forEach(player -> player.sendMessage(prefix + p.getName() + " just exploded; Who put that TNT in their inventory?"));
            Random r = new Random();
            sendParticle(p, ParticleMaker.Particle.EXPLOSION_HUGE, 1.0F, 1.0F, 1.0F);
            SoundMaker.ENTITY_GENERIC_EXPLODE.playSound(p.getLocation());
            Location l = p.getLocation();
            for (ItemStack item : p.getInventory().getContents()) {
                if (item != null) {
                    final Item ITEM = l.getWorld().dropItem(l.add(0.0, 2.0, 0.0), item);
                    ITEM.setPickupDelay(Integer.MAX_VALUE);
                    ITEM.setMetadata("takeable", new FixedMetadataValue(Core.get(), "takeable"));
                    ITEM.setVelocity(new org.bukkit.util.Vector(r.nextDouble() - 0.5D, r.nextDouble() / 2.0D, r.nextDouble() - 0.5D));
                    Bukkit.getScheduler().runTaskLater(Core.get(), ITEM::remove, 20 * 6);
                }
            }
        }
    }
}
