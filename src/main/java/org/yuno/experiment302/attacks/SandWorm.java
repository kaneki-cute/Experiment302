package org.yuno.experiment302.attacks;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.yuno.experiment302.Experiment302;

import java.util.ArrayList;
import java.util.Random;

public class SandWorm {

    public static ArrayList<Player> attackDodge = new ArrayList<>();

    public void WormAttack() {
            Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task -> {
                Random random = new Random();
                long days = Bukkit.getWorld("world").getFullTime() / 24000 + 1;

                // Предпологаемый шанс - 1 к 60
                if(random.nextInt(60) == 6 && !Bukkit.getOnlinePlayers().isEmpty()) {
                    ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                    Player p = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
                    if(p.getGameMode().equals(GameMode.SPECTATOR)) return;

                    if(p.getLocation().getBlock().getBiome().equals(Biome.DESERT)) {
                        Bukkit.getLogger().info("Игровой день " + days + ". Выбранный игрок для атаки Червя - " + p.getName());

                        int[] countdown = {0};
                        Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task1 -> {
                                Location loc = p.getLocation();
                                loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 0.8);

                                loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 30, 2, 0, 2, 0.01);
                                loc.getWorld().playSound(loc, Sound.BLOCK_SAND_HIT, 1.0f, 1.0f);
                                countdown[0]++;

                                if (countdown[0] >= 4) {
                                    task1.cancel();
                                    attackDodge.add(p);

                                    Bukkit.getScheduler().runTaskLater(Experiment302.getInstance(), task2 -> {

                                        if (p.getLocation().distance(loc) <= 5 && attackDodge.contains(p)) {
                                            p.damage(6);

                                            p.setVelocity(p.getLocation().toVector().subtract(p.getLocation().toVector()).multiply(0.2).setY(1.5));
                                            loc.getNearbyEntities(3, 3, 3).forEach(entity ->
                                                    entity.setVelocity(entity.getLocation().toVector().subtract(entity.getLocation().toVector()).multiply(0.2).setY(1.5)));

                                            if (p.getInventory().getBoots() != null)
                                                p.getInventory().getBoots().damage(15, p);

                                            if (p.getInventory().getLeggings() != null)
                                                p.getInventory().getLeggings().damage(15, p);

                                            attackDodge.remove(p);
                                        }

                                        loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 30, 2, 0, 2, 0.01);
                                        loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 1.0f);
                                    }, 20);
                                }
                        }, 0, 10);
                    }
                }

            }, 0, 200);
        }
}