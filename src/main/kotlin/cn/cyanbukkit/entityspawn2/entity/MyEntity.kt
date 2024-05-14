package cn.cyanbukkit.entityspawn2.entity

import org.bukkit.Location

data class MyEntity(
    val name: String,
    val data : Int,
    val location  : Location,
    val health : Int,
    val walkTarget : MutableList<Location>,
    val spawnMode : SpawnMode,
    val offset : Offset?,
    val team : String
)


