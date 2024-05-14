package cn.cyanbukkit.entityspawn2.listener

import cn.cyanbukkit.entityspawn2.SpawnEntity2Data.spawn
import cn.cyanbukkit.entityspawn2.SpawnEntity2Data.streamer
import cn.cyanbukkit.entityspawn2.command.EntitySpawn2Setup.pos1
import cn.cyanbukkit.entityspawn2.command.EntitySpawn2Setup.pos2
import cn.cyanbukkit.entityspawn2.command.EntitySpawn2Setup.sendHelp
import cn.cyanbukkit.entityspawn2.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

object EntityListener : Listener {

    @EventHandler
    fun on(e: PlayerJoinEvent) {
        if (e.player.name == cyanPlugin.config.getString("Player")) {
            streamer  = e.player
        }
        if (cyanPlugin.config.getBoolean("Setup")) {
            e.player.sendHelp()
        }
        e.player.teleport(spawn)
    }


    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (!event.hasItem()) return
        if (!event.hasBlock()) return
        if (!event.item!!.hasItemMeta()) return
        if (!event.item!!.itemMeta!!.hasDisplayName()) return
        if (event.item!!.itemMeta!!.displayName == "§6绑图工具") {
            val block = event.clickedBlock!!
            event.isCancelled = true
            if (event.action.name.contains("LEFT")) {
                pos1 = block
                event.player.sendMessage("§aPOS1 = $block")
            } else {
                pos2 = block
                event.player.sendMessage("§aPOS2 = $block")
            }
        }
    }

}