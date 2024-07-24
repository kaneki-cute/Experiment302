package org.yuno.experiment302.events;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.yuno.experiment302.Experiment302;

import java.util.Random;

public class SuddenDeath {

    @Getter private static final Location laboratory = new Location(
            Bukkit.getWorld("world"),
            Experiment302.getInstance().getConfig().getInt("laboratory.cordsX"),
            Experiment302.getInstance().getConfig().getInt("laboratory.cordsY"),
            Experiment302.getInstance().getConfig().getInt("laboratory.cordsZ"));

    @Getter private static final Location core = new Location(
            Bukkit.getWorld("world"),
            Experiment302.getInstance().getConfig().getInt("core.cordsX"),
            Experiment302.getInstance().getConfig().getInt("core.cordsY"),
            Experiment302.getInstance().getConfig().getInt("core.cordsZ"));

    public static void deathDay() { // idk как назвать это
            if(!Experiment302.getInstance().getConfig().getBoolean("events.suddenDeath"))
                Bukkit.getLogger().info("Кто-то взорвал ядро!");

            long overworldsDay = laboratory.getWorld().getFullTime() / 24000;

            Experiment302.getInstance().getConfig().set("events.suddenDeathStart", overworldsDay);
            Experiment302.getInstance().getConfig().set("events.suddenDeathEnd", overworldsDay + 1);

            Experiment302.getInstance().saveConfig();

            Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task2 -> {
                laboratory.getWorld().spawnParticle(Particle.END_ROD, laboratory, 20000, 150, 90, 150, 0.03);

                if(!Experiment302.getInstance().getConfig().getBoolean("events.suddenDeath", false)) {
                    task2.cancel();
                }
            }, 0, 20);

        Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task2 -> {
            laboratory.getWorld().spawnParticle(Particle.END_ROD, core, 100, 2, 3, 2, 0.03);
            laboratory.getWorld().playSound(core, Sound.BLOCK_BEACON_DEACTIVATE, 0.3f, 0.5f);

            if(!Experiment302.getInstance().getConfig().getBoolean("events.suddenDeath", false)) {
                task2.cancel();
            }
        }, 0, 80);

            Bukkit.getScheduler().runTaskTimer(Experiment302.getInstance(), task2 -> {
                Bukkit.getOnlinePlayers().forEach(p -> {

                    Location playerLocation = p.getLocation();
                    Random random = new Random();

                    if (playerLocation.distance(laboratory) <= 100) {
                        p.setFoodLevel(p.getFoodLevel() - 1);
                        p.setSaturation(p.getSaturation() - 1);
                        p.damage(1);

                        p.sendActionBar(
                                Component.text("Вы находитесь ")
                                        .append(Component.text("в эпицентре взрыва").color(NamedTextColor.GOLD)).append(Component.text(" реактора!")));
                    }

                    for (int x = 0; x < 3; x++) {
                        for (int z = 0; z < 3; z++) {
                            Block block = p.getLocation().getBlock().getRelative(x, 0, z);
                            p.sendBlockDamage(block.getLocation(), random.nextFloat(0, 1));
                        }
                    }

                    if (!Experiment302.getInstance().getConfig().getBoolean("events.suddenDeath", false)
                    || laboratory.getWorld().getFullTime() / 24000 >= Experiment302.getInstance().getConfig().getLong("events.suddenDeathEnd")) {
                        task2.cancel();
                        Experiment302.getInstance().getConfig().set("events.suddenDeath", false);
                        Experiment302.getInstance().saveConfig();
                    }
                });
                }, 0, 15);

                Experiment302.getInstance().getConfig().set("events.suddenDeath", true);
                Experiment302.getInstance().saveConfig();
    }
}
