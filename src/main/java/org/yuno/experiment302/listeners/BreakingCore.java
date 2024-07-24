package org.yuno.experiment302.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yuno.experiment302.Experiment302;
import org.yuno.experiment302.events.SuddenDeath;

import java.util.Random;

public class BreakingCore implements Listener {

    public int timer = 90;
    public int shake = 0;

    @EventHandler
    public void onBreakCore(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Location blockLocation = e.getBlock().getLocation();

        if(blockLocation.distance(SuddenDeath.getCore()) <= 5 && !Experiment302.getInstance().getConfig().getBoolean("core.broken")) {
            Experiment302.getInstance().getConfig().set("core.broken", true);
            Experiment302.getInstance().saveConfig();

            Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task -> {
                SuddenDeath.getLaboratory().getWorld().spawnParticle(Particle.END_ROD, SuddenDeath.getCore(), 100, 2, 3, 2, 0.03);
                SuddenDeath.getLaboratory().getWorld().playSound(SuddenDeath.getCore(), Sound.BLOCK_BEACON_DEACTIVATE, 0.3f, 0.5f);
                p.sendActionBar(Component.text(timer).color(NamedTextColor.GOLD));

                if(timer <= 0) {
                    task.cancel();
                    SuddenDeath.getLaboratory().getWorld().createExplosion(SuddenDeath.getCore(), 140f, true, true);
                    SuddenDeath.deathDay(); // Начало Дня Бури

                    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                        blockLocation.getWorld().playSound(onlinePlayer, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 0.5f);

                        Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task2 -> {
                            Random random = new Random();
                            Location playerLocation = onlinePlayer.getLocation();

                            if(shake <= 80) {
                                task2.cancel();
                                onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 240, 0));
                            }

                            float yaw = playerLocation.getYaw() + (random.nextFloat(4) - 4);
                            float pitch = playerLocation.getPitch() + (random.nextFloat(4) - 4);

                            playerLocation.setYaw(yaw);
                            playerLocation.setPitch(pitch);

                            onlinePlayer.teleport(playerLocation);
                            shake++;
                        }, 0, 1);
                    });
                }
                timer--;
            }, 0, 20);

        }
    }
}
