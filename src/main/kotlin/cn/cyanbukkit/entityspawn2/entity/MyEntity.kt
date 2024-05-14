package cn.cyanbukkit.entityspawn2.entity

import org.bukkit.Location
import org.bukkit.entity.EntityType

data class MyEntity(
    val name: String,
    val data : Int,
    val location  : Location,
    val health : Int,
    val type : EntityType,
    val walkTarget : MutableList<Location>,
    val spawnMode : SpawnMode,
    val offset : Offset?,
    val team : String
)


val color = mutableMapOf(
    "蓝" to "§1",
    "红" to "§c",
)

