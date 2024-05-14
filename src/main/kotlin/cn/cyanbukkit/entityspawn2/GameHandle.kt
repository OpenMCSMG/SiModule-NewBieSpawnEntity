package cn.cyanbukkit.entityspawn2

import cn.cyanbukkit.entityspawn2.cyanlib.launcher.CyanPluginLauncher
import cn.cyanbukkit.entityspawn2.entity.SpawnMain
import cn.cyanbukkit.entityspawn2.utils.Mode

class GameHandle {

    @Mode("1")
    fun start(team: String, name: String, user: String, amount: Int = 1) {
        for (i in 0 until amount) {
            CyanPluginLauncher.cyanPlugin.server.scheduler.runTaskLater(CyanPluginLauncher.cyanPlugin, Runnable {
                SpawnMain.call(team, name, user)
            }, 20L * i) // 20 ticks = 1 second
        }
    }

}