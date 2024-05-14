package cn.cyanbukkit.entityspawn2.cyanlib.launcher;

import cn.cyanbukkit.entityspawn2.SpawnEntity2Data;
import cn.cyanbukkit.entityspawn2.command.EntitySpawn2Setup;
import cn.cyanbukkit.entityspawn2.command.ModeEntry;
import cn.cyanbukkit.entityspawn2.cyanlib.loader.KotlinBootstrap;
import cn.cyanbukkit.entityspawn2.listener.EntityListener;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;

/**
 * 嵌套框架
 */

public class CyanPluginLauncher extends JavaPlugin {

    public static CyanPluginLauncher cyanPlugin;
    public File yaml;
    public YamlConfiguration config;
    public File mobs;
    public YamlConfiguration mobConfig;
    public String mainPlugin = "SiModuleGame";
    public String me = getDescription().getName();

    public CyanPluginLauncher() {
        cyanPlugin = this;
        KotlinBootstrap.init();
    }

    public void registerCommand(Command command) {
        Class<?> pluginManagerClass = cyanPlugin.getServer().getPluginManager().getClass();
        try {
            Field field = pluginManagerClass.getDeclaredField("commandMap");
            field.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) field.get(cyanPlugin.getServer().getPluginManager());
            commandMap.register(cyanPlugin.getName(), command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDefaultConfig() {
        yaml = new File("plugins/" + mainPlugin + "/addon/" + me + "/config.yml");
        mobs = new File("plugins/" + mainPlugin + "/addon/" + me + "/mobs.yml");
        if (!yaml.exists()) {
            try {
                if (yaml.getParentFile().mkdirs()) {
                    InputStream is = getResource("config.yml");
                    if (is != null) {
                        Files.copy(is, yaml.toPath());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!mobs.exists()) {
            try {
                if (mobs.getParentFile().mkdirs()) {
                    InputStream is = getResource("mobs.yml");
                    if (is != null) {
                        Files.copy(is, mobs.toPath());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config = YamlConfiguration.loadConfiguration(yaml);
        mobConfig = YamlConfiguration.loadConfiguration(mobs);
    }

    @Override
    public void saveConfig() {
        try {
            config.save(yaml);
            mobConfig.save(mobs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(yaml);
        mobConfig = YamlConfiguration.loadConfiguration(mobs);
    }


    @Override
    public FileConfiguration getConfig() {
        return config;
    }



    @Override
    protected File getFile() {
        return yaml;
    }




    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommand(ModeEntry.INSTANCE);
        registerCommand(EntitySpawn2Setup.INSTANCE);
        getServer().getPluginManager().registerEvents(EntityListener.INSTANCE, cyanPlugin);
        if (!config.getBoolean("Setup")) {
            SpawnEntity2Data.INSTANCE.onload();
        } else {
            getLogger().info("插件配置未配置请配置");
        }
    }

    @Override
    public void onDisable() {
        SpawnEntity2Data.INSTANCE.onUnload();
    }


}