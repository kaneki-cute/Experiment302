package org.yuno.experiment302.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yuno.experiment302.Experiment302;

import java.util.ArrayList;


public class MaterialAttack implements Listener {

    ArrayList<Player> affectedPlayers = new ArrayList<>();

    @EventHandler
    public void onMaterialAttack(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location loc = e.getTo();
        World world = loc.getWorld();

        if(loc.getY() >= 256 && world.getName().equals("world") && (world.getFullTime() / 24000) < 16) {

            if(!affectedPlayers.contains(p)) p.showElderGuardian();

            p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 120, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 120, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 120, 0));

            if(!affectedPlayers.contains(p)) {
                affectedPlayers.add(p);

                Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task -> {
                    if (p.getLocation().getY() >= 256 && world.getName().equals("world") && (world.getFullTime() / 24000) < 16) {

                        int damage = p.getArrowsInBody() / 2 + 2;
                        p.damage(damage);

                    } else {
                        task.cancel();
                        affectedPlayers.remove(p);
                    }
                }, 0, 5);
            }
        }
    }
}
