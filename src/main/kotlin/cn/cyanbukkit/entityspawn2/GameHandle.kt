package cn.cyanbukkit.entityspawn2

import cn.cyanbukkit.entityspawn2.entity.SpawnMain
import cn.cyanbukkit.entityspawn2.utils.Mode

class GameHandle {

    @Mode("1")
    fun start(team: String, name: String) {
        SpawnMain.call(team, name)
    }

}