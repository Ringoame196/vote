package com.github.ringoame196.vote;

import com.github.ringoame196.vote.Commands.vote;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Vote extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();
        getCommand("vote").setExecutor(new vote());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();
    }
}
