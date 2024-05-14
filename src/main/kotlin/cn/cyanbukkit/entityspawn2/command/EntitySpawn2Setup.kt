package cn.cyanbukkit.entityspawn2.command

import cn.cyanbukkit.entityspawn2.cyanlib.launcher.CyanPluginLauncher.cyanPlugin
import cn.cyanbukkit.entityspawn2.listener.EntityListener
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

object EntitySpawn2Setup : Command(
    "entityspawn2",
    "生成实体",
    "/entityspawn2",
    listOf("es2", "生成实体")
) {

    private fun team(entityName: String): String = if (cyanPlugin.mobConfig.contains("红.$entityName")) {
        "红"
    } else {
        if (cyanPlugin.mobConfig.contains("蓝.$entityName")) {
            "蓝"
        } else {
            throw Exception("实体不存在")
        }
    }


    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§c你必须")
            return true
        }
        if (args.isEmpty()) {
            sender.sendMessage("§c请输入参数")
            return true
        }
        when (args[0]) {
            "create" -> { // 创建实体 /es2 create <红/蓝> <Entity Name> <实体类型> <生命值>`
                if (args.size != 5) {
                    sender.sendMessage("§c参数不足")
                    return true
                }
                val team = args[1]
                val entityName = args[2]
                // 检查一下是否已经存在这个名字的实体
                if (cyanPlugin.mobConfig.contains("红.$entityName") || cyanPlugin.mobConfig.contains("蓝.$entityName")) {
                    sender.sendMessage("§c已经存在这个名字的实体注意不要与已经的红蓝方存在同一个名字的")
                    return true
                }
                val entityType = args[3]
                val health = try {
                    args[4].toDouble()
                } catch (e: Exception) {
                    sender.sendMessage("§c生命值必须是数字")
                    return true
                }
                // 创建实体
                cyanPlugin.mobConfig.set("${team}.$entityName.Type", entityType)
                cyanPlugin.mobConfig.set("${team}.$entityName.ModelData", 0)
                cyanPlugin.mobConfig.set("${team}.$entityName.Location", "world,0,0,0,0,0")
                cyanPlugin.mobConfig.set("${team}.$entityName.Health", health)
                cyanPlugin.mobConfig.set("${team}.$entityName.WalkTarget", mutableListOf<String>())
                cyanPlugin.mobConfig.set("${team}.$entityName.SpawnMode", "fixed")
                sender.sendMessage("§a创建成功记得保存 /es2 save")
            }

            "setModelData" -> { // 设置实体的模型数据
                if (args.size != 3) {
                    sender.sendMessage("§c参数不足")
                    return true
                }
                val entityName = args[1]
                val modelData = try {
                    args[2].toInt()
                } catch (e: Exception) {
                    sender.sendMessage("§c模型数据必须是数字")
                    return true
                }
                val team = try {
                    team(entityName)
                } catch (e: Exception) {
                    return true
                }
                cyanPlugin.mobConfig.set("$team.$entityName.ModelData", modelData)
                sender.sendMessage("§a设置成功记得保存 /es2 save")
            }

            "setlocation" -> { // 设置实体的位置
                if (args.size != 2) {
                    sender.sendMessage("§c参数不足")
                    return true
                }
                val entityName = args[1]
                val loc = sender.location
                val x = loc.x
                val y = loc.y
                val z = loc.z
                val yaw = loc.yaw
                val pitch = loc.pitch
                val team = try {
                    team(entityName)
                } catch (e: Exception) {
                    return true
                }
                cyanPlugin.mobConfig.set("$team.$entityName.Location", "${sender.world.name},$x,$y,$z,$yaw,$pitch")
                sender.sendMessage("§a设置成功记得保存 /es2 save")
            }

            "spawnmode" -> { // 设置实体的生成模式
                if (args.size != 3) {
                    sender.sendMessage("§c参数不足")
                    return true
                }
                val entityName = args[1]
                val mode = args[2]
                val team = try {
                    team(entityName)
                } catch (e: Exception) {
                    return true
                }
                cyanPlugin.mobConfig.set("$team.$entityName.SpawnMode", mode)
                sender.sendMessage("§a设置成功记得保存 /es2 save")
            }

            "walkadd" -> { // 设置实体的行走路径
                if (args.size != 2) {
                    sender.sendMessage("§c参数不足")
                    return true
                }
                val entityName = args[1]
                val team = try {
                    team(entityName)
                } catch (e: Exception) {
                    return true
                }
                val has = cyanPlugin.mobConfig.getStringList("$team.$entityName.WalkTarget")
                val l = sender.location
                has.add("${l.world!!.name},${l.x},${l.y},${l.z},${l.yaw},${l.pitch}")
                cyanPlugin.mobConfig.set("$team.$entityName.WalkTarget", has)
                sender.sendMessage("§a设置成功记得保存 /es2 save")
            }

            "givetools" -> {
                giveTools(sender)
            }

            "setRegion" -> {
                cyanPlugin.config.set("Room.Pos1", "${pos1.world.name},${pos1.x},${pos1.y},${pos1.z}")
                cyanPlugin.config.set("Room.Pos2", "${pos1.world.name},${pos1.x},${pos1.y},${pos1.z}")
                sender.sendMessage("§a设置成功 请保存")
            }

            "setSpawn" -> {
                cyanPlugin.config.set("Spawn", "${sender.world.name},${sender.location.x},${sender.location.y},${sender.location.z},${sender.location.yaw},${sender.location.pitch}")
                sender.sendMessage("§a设置成功 请保存")
            }

            "save" -> {
                cyanPlugin.saveConfig()
                sender.sendMessage("§a保存成功")
            }

            "reload" -> { // 重载配置文件
                cyanPlugin.reloadConfig()
            }

            else -> {
                sender.sendHelp()
            }

        }

        return true
    }

    lateinit var pos1 : Block
    lateinit var pos2 : Block

    private val interactListener = object : Listener {

    }

    private fun giveTools(sender: Player) {
        val item = ItemStack(Material.BLAZE_ROD)
        val meta = item.itemMeta
        meta?.setDisplayName("§6绑图工具")
        item.itemMeta = meta
        sender.inventory.addItem(item)
        sender.sendMessage("§a给予成功")
        // 注册临时监听
        cyanPlugin.server.pluginManager.registerEvents(EntityListener, cyanPlugin)
    }


    override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String> {
        // 根据参数长度 如果是第一个
        val list = mutableListOf<String>()
        when (args.size) {
            1 -> {
                list.add("reload")
                list.add("create")
                list.add("givetools")
                list.add("setRegion")
                list.add("setSpawn")
                list.add("setModelData")
                list.add("setlocation")
                list.add("spawnmode")
                list.add("walkadd")
                list.add("set")
                list.add("remove")
            }

            2 -> {
                when (args[0]) {
                    "setModelData" -> {
                        list.add("1")
                        list.add("2")
                        list.add("3")
                        list.add("4")
                        list.add("5")
                        list.add("6")
                        list.add("7")
                        list.add("8")
                        list.add("9")
                        list.add("10")
                    }


                    "create" -> {
                        list.add("红")
                        list.add("蓝")
                    }
                }
            }

            3 -> {
                when (args[0]) {
                    "create" -> {
                        list.add("输入名称")
                    }


                    "spawnmode" -> {
                        list.add("fixed")
                        list.add("region")
                        list.add("follow|x|y|z")
                    }
                }
            }

            4 -> {
                when (args[0]) {
                    "create" -> { // 把所有实体名称都加进去
                        EntityType.entries.forEach {
                            list.add(it.name)
                        }
                    }
                }
            }

            5 -> {
                when (args[0]) {
                    "create" -> {
                        list.add("输入生命值")
                    }
                }
            }
        }
        return list
    }


    fun Player.sendHelp() {
        sendMessage("§6/entityspawn2 reload §7- 重载配置文件")
        sendMessage("§6/entityspawn2 givetools §7- 绑图工具")
        sendMessage("§6/entityspawn2 create <红/蓝> <实体名字> <实体类型> <生命值> §7- 创建实体")
        sendMessage("§6/entityspawn2 setRegion §7- 设置区域")
        sendMessage("§6/entityspawn2 setSpawn §7- 设置出生点")
        sendMessage("§6/entityspawn2 setModelData <实体名字> §7- 设置实体的模型数据")
        sendMessage("§6/entityspawn2 setlocation <实体名字> §7- 设置实体的位置")
        sendMessage("§6/entityspawn2 spawnmode <实体名字> <模式> §7- 设置实体的生成模式")
        sendMessage("§6/entityspawn2 walkadd §7- 设置实体的行走路径")
        sendMessage("§6/entityspawn2 save §7- 设置实体的行走路径")
        sendMessage("§6/entityspawn2 reload §7- 设置实体的行走路径")
    }

}