package cn.cyanbukkit.entityspawn2.entity

import cn.cyanbukkit.entityspawn2.SpawnEntity2Data
import cn.cyanbukkit.entityspawn2.SpawnEntity2Data.isOnline
import cn.cyanbukkit.entityspawn2.SpawnEntity2Data.streamer
import cn.cyanbukkit.entityspawn2.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.apache.commons.lang.StringUtils.substring
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.metadata.FixedMetadataValue

// 1.红蓝方是互打的避免红方自己打自己人所以要识别准
// 2.路径录制系统
object SpawnMain {
    // 缩短根据长度截取字符串如果超过2
    private fun String.sub(size: Int) : String = if (this.length > size) substring(this, 0, size) else this

    private fun start(entity: MyEntity, user : String) {
        // 对所有除Player都有攻击性
        val serverEntity = when (entity.spawnMode) {
            SpawnMode.FIXED -> {
                entity.location.world!!.spawnEntity(entity.location, entity.type)
            }
            SpawnMode.REGION -> {
                SpawnEntity2Data.room.randomLocation().world!!.spawnEntity(entity.walkTarget[0], entity.type)
            }
            SpawnMode.FOLLOW -> { // 跟随
                if (isOnline()) {
                    val newLoc = streamer.location.clone().add(entity.offset!!.x, entity.offset.y, entity.offset.z)
                    newLoc.world!!.spawnEntity(entity.location, entity.type)
                } else {
                    entity.location.world!!.spawnEntity(entity.location, entity.type)
                }
            }
        }
        val serverLiving =  serverEntity as LivingEntity
        serverLiving.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue = entity.health.toDouble()
        serverLiving.health = entity.health.toDouble()
        var name = cyanPlugin.config.getString("Display")!!
        serverLiving.setMetadata("team", FixedMetadataValue(cyanPlugin, entity.team))
        val col = color[entity.team]
        name = name.replace("%team%", col + entity.team)
        // 找到name里面的  %name_2%  中的2他也有可能是别的数字
        val regex = Regex("%name_(\\d+)%")
        val result = regex.find(name)
        if (result != null) {
            val value = result.groupValues[1]
            name = name.replace("%name_$value%", user.sub(value.toInt()))
        }
        serverLiving.customName = name
        serverLiving.isCustomNameVisible = true
        if (entity.walkTarget.isNotEmpty()) {
            AIMoveAndAttack(serverLiving, entity.walkTarget).walk()
        }
    }


    private fun find(team: String, name: String): MyEntity? {
        return SpawnEntity2Data.entities.find { it.team == team && it.name == name }
    }

    fun call(team: String, name: String, user: String) {
        val entity = find(team, name)
        if (entity != null) {
            start(entity, user)
        } else {
            Bukkit.broadcastMessage("§c实体不存在")
        }
    }




}

