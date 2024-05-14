package cn.cyanbukkit.entityspawn2.entity

import cn.cyanbukkit.entityspawn2.SpawnEntity2Data
import cn.cyanbukkit.entityspawn2.SpawnEntity2Data.isOnline
import cn.cyanbukkit.entityspawn2.SpawnEntity2Data.streamer
import cn.cyanbukkit.entityspawn2.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

// 1.红蓝方是互打的避免红方自己打自己人所以要识别准
// 2.路径录制系统
object SpawnMain {


    private fun start(entity: MyEntity, user : String) {
        // 对所有除Player都有攻击性
        val serverEntity = when (entity.spawnMode) {
            SpawnMode.FIXED -> {
                entity.location.world!!.spawnEntity(entity.location, EntityType.valueOf(entity.name))
            }
            SpawnMode.REGION -> {
                SpawnEntity2Data.room.randomLocation().world!!.spawnEntity(entity.location, EntityType.valueOf(entity.name))
            }
            SpawnMode.FOLLOW -> { // 跟随
                if (isOnline()) {
                    val newLoc = streamer.location.clone().add(entity.offset!!.x, entity.offset.y, entity.offset.z)
                    newLoc.world!!.spawnEntity(entity.location, EntityType.valueOf(entity.name))
                } else {
                    entity.location.world!!.spawnEntity(entity.location, EntityType.valueOf(entity.name))
                }
            }
        }
        var name = cyanPlugin.config.getString("Display")!!
        name = name.replace("%team%", entity.team)
        // 找到name里面的  %name_2%  中的2他也有可能是别的数字
        val regex = Regex("%name_(\\d+)%")
        val result = regex.find(name)
        if (result != null) {
            val value = result.groupValues[1]
            name = name.replace("%name_$value%", user.substring(0, value.toInt()))
        }
        serverEntity.customName = name
        serverEntity.isCustomNameVisible = true
        if (entity.walkTarget.isNotEmpty()) {
            AIMoveAndAttack(serverEntity as LivingEntity, entity.walkTarget).walk()
        }
    }


    private fun find(team: String, name: String): MyEntity? {
        return SpawnEntity2Data.entities.find { it.team == team && it.name == name }
    }

    fun call(team: String, name: String) {
        val entity = find(team, name)
        if (entity != null) {
            start(entity, team)
        } else {
            Bukkit.broadcastMessage("§c实体不存在")
        }
    }

}