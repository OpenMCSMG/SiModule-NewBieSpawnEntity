package cn.cyanbukkit.entityspawn2

import cn.cyanbukkit.entityspawn2.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.entityspawn2.entity.MyEntity
import cn.cyanbukkit.entityspawn2.entity.Offset
import cn.cyanbukkit.entityspawn2.entity.RoomRegion
import cn.cyanbukkit.entityspawn2.entity.SpawnMode
import cn.cyanbukkit.entityspawn2.listener.EntityListener
import cn.cyanbukkit.entityspawn2.listener.WorldProtect
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

object SpawnEntity2Data {
    lateinit var room: RoomRegion
    private var format = ""
    lateinit var streamer: Player
    lateinit var spawn: Location
    val entities = mutableListOf<MyEntity>()
    fun isOnline() = if (::streamer.isInitialized) streamer.isOnline else false

    fun onload() {
        // 加载配置到内存
        val p1 = cyanPlugin.config.getString("Room.Pos1")!!.split(",")
        val p2 = cyanPlugin.config.getString("Room.Pos2")!!.split(",")
        spawn = Location(
            Bukkit.getWorld(cyanPlugin.config.getString("Spawn")!!.split(",")[0]),
            cyanPlugin.config.getString("Spawn")!!.split(",")[1].toDouble(),
            cyanPlugin.config.getString("Spawn")!!.split(",")[2].toDouble(),
            cyanPlugin.config.getString("Spawn")!!.split(",")[3].toDouble(),
            cyanPlugin.config.getString("Spawn")!!.split(",")[4].toFloat(),
            cyanPlugin.config.getString("Spawn")!!.split(",")[5].toFloat()
        )
        room = RoomRegion(
            Location(
                Bukkit.getWorld(p1[0]),
                p1[1].toDouble(),
                p1[2].toDouble(),
                p1[3].toDouble()
            ),
            Location(
                Bukkit.getWorld(p2[0]),
                p2[1].toDouble(),
                p2[2].toDouble(),
                p2[3].toDouble()
            )
        )
        format = cyanPlugin.config.getString("Display")!!
        cyanPlugin.server.pluginManager.registerEvents(WorldProtect, cyanPlugin)
        // load mobs
        val mobs = cyanPlugin.mobConfig.getKeys(false)
        mobs.forEach { team ->
            val mob = cyanPlugin.mobConfig.getConfigurationSection(team)!!
            mob.getKeys(false).forEach { name ->
                val section = mob.getConfigurationSection(name)!!
                val entity = MyEntity(
                    name,
                    section.getInt("ModelData"),
                    Location(
                        Bukkit.getWorld(section.getString("Location")!!.split(",")[0]),
                        section.getString("Location")!!.split(",")[1].toDouble(),
                        section.getString("Location")!!.split(",")[2].toDouble(),
                        section.getString("Location")!!.split(",")[3].toDouble(),
                        section.getString("Location")!!.split(",")[4].toFloat(),
                        section.getString("Location")!!.split(",")[5].toFloat()
                    ),
                    section.getInt("Health"),
                    section.getString("Type")!!.let { EntityType.valueOf(it) },
                    section.getStringList("WalkTarget").map {
                        Location(
                            Bukkit.getWorld(it.split(",")[0]),
                            it.split(",")[1].toDouble(),
                            it.split(",")[2].toDouble(),
                            it.split(",")[3].toDouble(),
                            it.split(",")[4].toFloat(),
                            it.split(",")[5].toFloat()
                        )
                    }.toMutableList(),
                    SpawnMode.valueOf(section.getString("SpawnMode")!!.uppercase()),
                    if (section.contains("follow")) {
                        Offset(
                            section.getString("SpawnMode")!!.split(",")[1].toDouble(),
                            section.getString("SpawnMode")!!.split(",")[2].toDouble(),
                            section.getString("SpawnMode")!!.split(",")[3].toDouble()
                        )
                    } else {
                        null
                    },
                    team
                )
                entities.add(entity)
            }
        }

    }


    fun onUnload() {

    }

}