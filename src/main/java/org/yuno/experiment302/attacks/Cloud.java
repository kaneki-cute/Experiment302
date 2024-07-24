package org.yuno.experiment302.attacks;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yuno.experiment302.Experiment302;

import java.util.ArrayList;
import java.util.Random;

public class Cloud {

    public  void cloudAttacking() {
            Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task -> {
                Random random = new Random();
                long days = Bukkit.getWorld("world").getFullTime() / 24000 + 1;


                // Шанс появления днем - 1 к 350
                // Шанс появления ночью - 1 к 150
                if (random.nextInt(350) == 35 && !Bukkit.getOnlinePlayers().isEmpty() && days >= 6 && Bukkit.getWorld("world").isDayTime()
                        || random.nextInt(150) == 15 && !Bukkit.getOnlinePlayers().isEmpty() && days >= 1 && !Bukkit.getWorld("world").isDayTime()) {
                    ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                    Player p = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
                    if(p.getGameMode().equals(GameMode.SPECTATOR)) return;
                    Bukkit.getLogger().info("Игровой день " + days + ". Выбранный игрок для атаки облаком - " + p.getName());

                    Block cloudLocation = rollCloudLocation(p.getLocation());
                    final int[] attackTimer = {0};
                    final int[] attackCount = {0};
                    Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task1 -> {
                        attackTimer[0]++;

                        if (!Bukkit.getWorld("world").isDayTime()) {
                            cloudLocation.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, cloudLocation.getLocation(), 45, 1, 1, 1, 0.007);
                            cloudLocation.getWorld().playSound(cloudLocation.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1f, 1f);
                        } else {
                            cloudLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, cloudLocation.getLocation(), 45, 1, 1, 1, 0.007);
                            cloudLocation.getWorld().playSound(cloudLocation.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
                        }

                        if (p.getLocation().distance(cloudLocation.getLocation()) <= 5 || attackCount[0] >= 1) {
                            Location playerParticles = p.getLocation();
                            playerParticles.setY(playerParticles.getY() + 2);

                            p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 60, 1));
                            p.setWalkSpeed(0.05f);

                            p.getWorld().spawnParticle(Particle.TRIAL_OMEN, p.getLocation(), 7, 0.3, 0.3, 0.3, 0.01);
                            p.playSound(p, Sound.ENTITY_BREEZE_SHOOT, 1f, 0.3f);
                            p.damage(2);
                            attackCount[0]++;
                        } else p.setWalkSpeed(0.2f);

                        if (attackTimer[0] >= 12 && attackCount[0] <= 5 || attackCount[0] >= 5) {
                            task1.cancel();
                            p.setWalkSpeed(0.2f);
                        }

                    }, 0, 20);
                }

            }, 0, 120);
    }

    public static Block rollCloudLocation(Location loc) {
        Random random = new Random();
        Block cloudLocation = null;
        boolean success = false;
        while(!success) {
            cloudLocation = loc.getBlock().getRelative(random.nextInt(-7, 15), random.nextInt(-3, 3), random.nextInt(-7, 15));
            if (cloudLocation.getType().equals(Material.AIR) && cloudLocation.getLocation().distance(loc) >= 5) {
                success = true;
                break;
            }
        }
        return cloudLocation;
    }
}
