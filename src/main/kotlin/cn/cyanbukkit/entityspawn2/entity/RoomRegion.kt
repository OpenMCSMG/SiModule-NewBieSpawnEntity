package cn.cyanbukkit.entityspawn2.entity

import org.bukkit.Location

data class RoomRegion(
    val pos1 : Location,
    val pos2 : Location,
) {
    fun randomLocation() : Location {
        val x = pos1.x + Math.random() * (pos2.x - pos1.x)
        val y = pos1.y + Math.random() * (pos2.y - pos1.y)
        val z = pos1.z + Math.random() * (pos2.z - pos1.z)
        return Location(pos1.world, x, y, z)
    }
}
