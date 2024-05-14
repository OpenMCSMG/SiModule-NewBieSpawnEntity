# 指令模块

高端的指令设置功能！
按照步骤继续创建
1. [ ] `/es2 create <红/蓝> <Entity Name> <实体类型> <生命值>`
2. [ ] `/es2 setModelData <数字>`
3. [ ] `/es2 setlocation`
4. [ ] `/es2 spawnmode <mode>`
    - fixed 固定
    - region 区域随机
    - follow | x| y | z   跟随偏移
5. [ ] `/es2 walkadd `
6. [ ] `/es2 save`
7. [ ] `/es2 reload`

# 模块接口 

包`cn.cyanbukkit.entityspawn2` 下的 `GameHandle`

```kotlin
@Mode("1")
fun start(team: String, name: String, user: String, amount: Int = 1) {}
```
`team` 配置里的队伍名字
`name` 实体名字
`user` 观众名字
`amount` 数量




