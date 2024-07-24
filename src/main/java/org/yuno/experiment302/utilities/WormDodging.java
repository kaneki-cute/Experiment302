package org.yuno.experiment302.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.yuno.experiment302.attacks.SandWorm;

public class WormDodging implements Listener {

    @EventHandler
    public static void dodgeWorm(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Location loc = p.getLocation();
        ItemStack item = e.getItem();

        if(SandWorm.attackDodge.contains(p)
                && item != null
                && item.getType().toString().contains("SWORD")
                && e.getAction().isRightClick()) {

            p.getWorld().playSound(loc, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            p.sendActionBar(Component.text("Вы смогли ").append(Component.text("парировать").color(NamedTextColor.GOLD)).append(Component.text(" атаку червя!")));
            item.damage(50, p);
            SandWorm.attackDodge.remove(p);
        }
    }
}
