package cn.cyanbukkit.entityspawn2.entity

import cn.cyanbukkit.entityspawn2.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.lang.reflect.Method
import kotlin.math.asin
import kotlin.math.atan2

class AIMoveAndAttack(
    val liveEntity: LivingEntity,
    val targetList: List<Location>,
    val speed: Double = 1.0
) {
    private var currentTargetIndex = 0
    private var bukkitRunnable: BukkitRunnable? = null
    var isAttacking = false

    fun walk() {
        bukkitRunnable = object : BukkitRunnable() {
            override fun run() {
                if (liveEntity.isDead) {
                    this.cancel()
                    return
                }
                if (isAttacking) {
                    return
                }
                if (currentTargetIndex >= targetList.size) {
                    this.cancel()
                    return
                }
                val targetLocation = targetList[currentTargetIndex]
                val spawnLocation = liveEntity.location
                val direction = targetLocation.toVector().subtract(spawnLocation.toVector()).normalize()
                val distance = spawnLocation.distance(targetLocation)
                val steps = (distance * 10).toInt()
                if (steps == 0) {
                    currentTargetIndex++
                    return
                }
                liveEntity.velocity = direction.multiply(speed)
                val newLocation = spawnLocation.clone().add(direction.clone().multiply(1.0 / 10.0))
                val yaw = Math.toDegrees(atan2(direction.z, direction.x)).toFloat() - 90
                val pitch = Math.toDegrees(asin(direction.y)).toFloat()
                newLocation.yaw = yaw
                newLocation.pitch = pitch
                liveEntity.teleport(newLocation)
            }
        }
        bukkitRunnable?.runTaskTimer(cyanPlugin, 0L, 1L)
        attack()
    }

    var attackEntity: LivingEntity? = null

    private fun attack() {
        cleanAttack()
        object : BukkitRunnable() {
            override fun run() {
                // 搜寻范围内的实体主动攻击
                if (attackEntity == null) { // 没有攻击目标
                    val entities = liveEntity.getNearbyEntities(10.0, 10.0, 10.0)
                    for (entity in entities) {
                        if (entity is LivingEntity) {
                            if (entity is Player) continue
                            val mate = if (entity.hasMetadata("team")) {
                                entity.getMetadata("team")[0]
                            } else {
                                null
                            }
                            if (mate == null) continue
                            if (mate.value().toString() == entity.getMetadata("team")[0].value().toString()) continue
                            attackEntity(attackEntity!!)
                            return
                        }
                    }
                } else { // 有攻击目标
                    if (attackEntity!!.isDead) {
                        cleanAttack()
                        return
                    }
                }

            }
        }.runTaskTimer(cyanPlugin, 0L, 1L)
    }

    fun stopWalk() {
        bukkitRunnable?.cancel()
        bukkitRunnable = null
    }

    // 控制攻击状态
    fun attackEntity(target: LivingEntity) {
        liveEntity.setAI(true)
        attackEntity = target
        isAttacking = true
        val typeClass = liveEntity.type.entityClass
        // 根据type 找到对应的Class通过反射 然后直接 直接反射调用public abstract void setTarget(     @Nullable org. bukkit. entity. LivingEntity livingEntity )
        val method: Method = typeClass!!.getMethod("setTarget", LivingEntity::class.java)
        method.invoke(liveEntity, target)
    }

    fun cleanAttack() {
        liveEntity.setAI(true)
        attackEntity = null
        isAttacking = false
        val typeClass = liveEntity.type.entityClass
        // 根据type 找到对应的Class通过反射 然后直接 直接反射调用public abstract void setTarget(     @Nullable org. bukkit. entity. LivingEntity livingEntity )
        val method: Method = typeClass!!.getMethod("setTarget", LivingEntity::class.java)
        method.invoke(liveEntity, null)
    }

}