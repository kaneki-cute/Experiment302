package org.yuno.experiment302.listeners;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yuno.experiment302.Experiment302;

import java.util.ArrayList;
import java.util.Random;

public class SculkSlowly implements Listener {

    public static ArrayList<Player> beingSculky = new ArrayList<>();

    @EventHandler
    public static void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location blockLocation = p.getLocation(); blockLocation.setY(blockLocation.getY() - 1);
        Block[] block = {blockLocation.getBlock()};

        if(block[0].getType() == Material.SCULK && block[0].getBiome().equals(Biome.DEEP_DARK)) {
            float[] playerSpeed = new float[]{p.getWalkSpeed()};
                    playerSpeed[0] -= 0.005f;
                    p.setWalkSpeed(playerSpeed[0]);

                    if (playerSpeed[0] <= 0.01) {
                        Bukkit.getScheduler().runTaskLater(Experiment302.getInstance(), bukkitTask -> {
                            Location playerLocation = p.getLocation();
                            Random random = new Random();

                            float shakeYaw = random.nextFloat(1, 70);
                            float shakePitch = random.nextFloat(1, 70);

                            playerLocation.setPitch(playerLocation.getPitch() + shakePitch);
                            playerLocation.setYaw(playerLocation.getYaw() + shakeYaw);

                            p.teleport(playerLocation);

                            if (!beingSculky.contains(p)) {
                                beingSculky.add(p);
                                playerLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, playerLocation, 30, 2, 0, 2, 0.01);
                                playerLocation.getWorld().playSound(playerLocation, Sound.BLOCK_SCULK_CATALYST_BLOOM, 1.0f, 1.0f);

                                Bukkit.getScheduler().runTaskLater(Experiment302.getInstance(), () ->
                                        p.setVelocity(p.getLocation().toVector().subtract(p.getLocation().toVector()).multiply(0.2).setY(1.5)), 40);
                                Bukkit.getScheduler().runTaskLater(Experiment302.getInstance(), () -> beingSculky.remove(p), 120);
                            }
                        }, 60);
                        Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), bukkitTask1 -> {

                            if (!p.getLocation().getBlock().getType().equals(Material.SCULK)) {
                                bukkitTask1.cancel();
                            }


                            p.getWorld().spawnParticle(Particle.SCULK_SOUL, p.getLocation(), 15, 0.3, 0.5, 0.3, 0.01);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 120, 0, true, false, false));


                        }, 0, 10);
                    }

        } else {
            p.setWalkSpeed(0.2f);
        }
    }
}
