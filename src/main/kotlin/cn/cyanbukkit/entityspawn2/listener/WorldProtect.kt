package cn.cyanbukkit.entityspawn2.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object WorldProtect : Listener {

    @EventHandler
    /**
     * 防爆保护
     */
    fun onExplode(event: org.bukkit.event.entity.EntityExplodeEvent) {
        event.isCancelled = true
    }

}