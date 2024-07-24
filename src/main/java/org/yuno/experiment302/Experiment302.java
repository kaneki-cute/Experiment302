package org.yuno.experiment302;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuno.experiment302.attacks.Cloud;
import org.yuno.experiment302.attacks.SandWorm;
import org.yuno.experiment302.attacks.HighGround;
import org.yuno.experiment302.events.SuddenDeath;
import org.yuno.experiment302.listeners.BreakingCore;
import org.yuno.experiment302.listeners.MaterialAttack;
import org.yuno.experiment302.utilities.WormDodging;

public final class Experiment302 extends JavaPlugin {

    @Getter
    public static Experiment302 instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        saveDefaultConfig();

        new Cloud();
        new HighGround();
        new SandWorm();
        new SuddenDeath();

        Bukkit.getScheduler().runTaskTimer(this, task -> {
            long overworldTime = Bukkit.getWorld("world").getFullTime();

            if (overworldTime / 24000 >= 17 && !getConfig().getBoolean("events.barrierActive", false)) {
                getConfig().set("events.barrierActive", true);
                saveConfig();

                getLogger().info("Барьер активирован!");
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.sendActionBar(Component.text("Барьер ").append(Component.text("активирован!").color(NamedTextColor.GOLD)));
                    p.getWorld().playSound(p, Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 0.5f, 1f);

                });
            }

        }, 0, 360);

//        Bukkit.getPluginManager().registerEvents(new SculkSlowly(), this);
        Bukkit.getPluginManager().registerEvents(new WormDodging(), this);
        Bukkit.getPluginManager().registerEvents(new BreakingCore(), this);
        Bukkit.getPluginManager().registerEvents(new MaterialAttack(), this);
        getLogger().info("Эксперимент начат! ");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
