package org.yuno.experiment302.attacks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yuno.experiment302.Experiment302;

import java.util.ArrayList;
import java.util.Random;

public class HighGround {

    public void HighYAttack() {
        Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task -> {
            Random random = new Random();
            long days = Bukkit.getWorld("world").getFullTime() / 24000 + 1;

            // Предпологаемый шанс - 1 к 70
            if(!Bukkit.getOnlinePlayers().isEmpty() && days >= 16) {
                ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                Player p = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
                if(p.getGameMode().equals(GameMode.SPECTATOR)) return;
                Location loc = p.getLocation();
                loc.setY(loc.getY() + 0.5);

                if(p.getLocation().getY() >= 270) {
                    Bukkit.getLogger().info("Игровой день " + days + ". Выбранный игрок для атаки Хайграунда - " + p.getName());

                    int[] countdown = {0};
                    Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task1 -> {
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc, 30, 2, 0.5, 2, 0.01);
                        loc.getWorld().playSound(loc, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, 1.0f, 1.0f);
                        countdown[0]++;

                        if(countdown[0] >= 6) {
                            task1.cancel();
                            if(p.getLocation().distance(loc) <= 3) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 200, 0));

                                Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task2 -> {
                                    if(p.getLocation().getY() >= 256 && countdown[0] <= 12) {
                                        p.damage(1);
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 60, 1));
                                        p.sendActionBar(Component.text("Вы пересекаете " ).append(Component.text("линию барьера!").color(NamedTextColor.GOLD)));
                                        p.getWorld().spawnParticle(Particle.TRIAL_OMEN, p.getLocation(), 15, 0.5, 0.5, 0.5, 0.01);

                                    } else task2.cancel();
                                }, 0, 10);
                                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.0f);
                            }
                            loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);
                        }

                    }, 0, 10);

                }
            }

        }, 0, 320);
    }
}
