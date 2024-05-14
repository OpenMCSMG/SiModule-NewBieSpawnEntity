package cn.cyanbukkit.entityspawn2.listener

import cn.cyanbukkit.entityspawn2.SpawnEntity2Data.streamer
import cn.cyanbukkit.entityspawn2.cyanlib.launcher.CyanPluginLauncher
import cn.cyanbukkit.entityspawn2.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerListener : Listener {

    @EventHandler
    fun on(e: PlayerJoinEvent) {
        if (e.player.name == cyanPlugin.config.getString("Player")) {
            streamer  = e.player
        }
    }

}