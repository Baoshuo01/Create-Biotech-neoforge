# Create: Biotech — NeoForge 1.21.1 移植版

[English](README.md) | [简体中文](README.zh-CN.md) | [Player Guide](docs/INTRODUCTION.md) | [玩家指南](docs/INTRODUCTION.zh-CN.md)

这是 [Create: Biotech](https://github.com/Nobodiiiii/Create-Biotech) 面向 **Minecraft 1.21.1**、**NeoForge** 和 **Create 6.0.10 及以上版本**的社区维护移植版。

Create: Biotech 将生物、生物材料、流体、物流、加工和动态结构玩法接入 [Create](https://www.curseforge.com/minecraft/mc-mods/create) 提供的机械系统。

> [!IMPORTANT]
> 本仓库是原项目的移植版本，不主张取得原模组或原项目资源的所有权。原项目由 **Nobodiiiii** 创作，NeoForge 1.21.1 移植版由 **Baoshuo01** 维护。

## 移植状态

| 项目 | 版本或说明 |
| --- | --- |
| Minecraft | `1.21.1` |
| 模组加载器 | NeoForge `21.1.219` 或更高版本 |
| Java | `21` |
| Create | `6.0.10` 或更高版本 |
| 模组 ID | `create_biotech` |
| 移植版版本 | `1.1.0` |
| 必需运行库 | Flywheel、Vanillin、Ponder |
| 可选兼容 | JEI、Jade |
| 映射 | Minecraft 1.21.1 的 Parchment `2024.11.17` |

依赖版本及允许范围定义在 [gradle.properties](gradle.properties) 中。Create 的最低支持版本明确设为 **6.0.10**。

## 移植范围

本仓库将保留的 Create: Biotech 系统迁移到 Minecraft 1.21.1 NeoForge API，包括注册、网络通信、能力系统、渲染、配方、标签、数据格式、JEI、Jade 和思索系统兼容。

本移植在尽量保持现有功能行为的同时，清理了过时兼容代码、旧加载器专用实现、未使用资源，以及不再属于本版本的功能。

## 有意删除的功能

本移植已完整删除以下旧功能：

### 黄油猫模块

黄油猫集成已被完整删除，包括：

- 黄油猫引擎及相关方块；
- 黄油猫相关物品、食物、流体、效果和旋转机制；
- 属于该模块的客户端渲染器与 Flywheel Visual；
- 黄油猫相关思索场景及注册；
- 相关配方、标签、语言文本、模型、贴图、数据和配置。

本仓库当前不应分发任何黄油猫或 CreateButterCat 衍生代码与资源。

### 恶魂驾驶模块

恶魂驾驶玩法也已完整删除，包括：

- 恶魂头盔及恶魂控制行为；
- 热气球装配站及相关装配内容；
- 属于该功能的驾驶交互、注册、配方、语言文本、模型、贴图和数据。

这里删除的是原有的**可骑乘、可控制恶魂玩法系统**。普通原版 Minecraft 内容，例如在配方中作为材料使用的恶魂之泪，不属于已删除的恶魂驾驶模块。

> [!WARNING]
> 使用旧版本创建的世界可能包含已删除模块的方块、物品、实体、流体或配置项。升级前请备份世界。本移植不会为已删除的注册项提供兼容占位或自动数据迁移。

当前保留在 `com.yision.phantom` 下的 Create Phantom 衍生物流功能与已删除的恶魂驾驶模块相互独立。

## 保留的主要系统

- 史莱姆传送带、岩浆传送带和动力传送带，以及与 Create 漏斗和隧道的交互。
- 实体捕获、工作盆生物加工和自定义机器加工。
- 缓冲垫及动态结构碰撞行为。
- 蜘蛛装配台。
- 鱿鱼打印机。
- 唤魔者附魔室。
- 苦力怕爆破室。
- 生物打包机。
- 经验流体、经验泵、经验芽、经验簇和储罐渲染。
- 潜影传送站。
- Create Phantom 衍生物流功能。
- 万向节、骨制棘轮、史莱姆离合器和固定胡萝卜钓竿。
- 防爆物品保险库和纸板箱。
- 保留内容对应的 JEI 配方显示、Jade 信息和思索场景。

## 安装方法

1. 安装 Minecraft 1.21.1 和兼容的 NeoForge 21.1.x 版本。
2. 安装 Create 6.0.10 或更高版本及其所需运行库。
3. 将 Create: Biotech 的 JAR 文件放入游戏实例的 `mods` 文件夹。
4. 可选安装兼容版本的 JEI 和 Jade。
5. 启动游戏前移除旧版 Create: Biotech 文件。

更换模组版本前，请始终备份已有世界。

## 从源码构建

构建要求：

- JDK 21；
- 可用的 Gradle 环境，或使用仓库附带的 Gradle Wrapper；
- 能够访问 [build.gradle](build.gradle) 中配置的依赖仓库。

Windows PowerShell 或命令提示符：

```powershell
.\gradlew.bat build
.\gradlew.bat runClient
.\gradlew.bat runServer
```

Linux 或 macOS：

```bash
./gradlew build
./gradlew runClient
./gradlew runServer
```

构建产物将生成在 `build/libs/` 中。

## 仓库结构

```text
src/main/java/com/nobodiiiii/createbiotech/
  client/          客户端注册、渲染、粒子和 GUI
  compat/          JEI 与 Jade 兼容
  content/         功能实现
  event/           事件处理
  foundation/      共用工具
  infrastructure/ 共用注册和思索系统接线
  mixin/           Create 与原版 Mixin
  network/         自定义网络载荷注册与处理
  ponder/          思索场景和生成支持
  registry/        方块、物品、流体、实体、菜单、配方和配置注册

src/main/java/com/yision/phantom/
  按上游许可证保留的 Create Phantom 衍生物流代码

src/main/resources/
  assets/create_biotech/  模型、贴图、语言文件、粒子和思索数据
  data/                   配方、标签、战利品表和进度
  META-INF/neoforge.mods.toml
```

## 兼容性与问题反馈

报告崩溃或渲染问题时，请提供：

- 完整崩溃报告；
- 可用时提供 `latest.log`；
- 准确的 NeoForge、Create 和 Create: Biotech 版本；
- 完整模组列表；
- 复现步骤，以及问题是否能在仅安装必需依赖的纯净实例中复现。

除非问题也能在原版上游版本中复现，否则请勿将本移植版的问题报告给原项目。

## 许可证与署名

本仓库不主张拥有上游 Create: Biotech 内容或任何其他第三方内容的所有权。

- 所有上游代码、文档、资源、名称及其他内容均归各自权利人所有，并继续适用其上游许可证和授权条件。
- Baoshuo01 仅对本人专门为 NeoForge 1.21.1 移植编写、具有独创性且有权主张的代码改动声明权利；这些改动仅在 Baoshuo01 有权授权的范围内采用 MIT 许可证。
- 对上游内容进行移动、重命名、格式转换、重新排版或机械性适配，不构成对该内容的新所有权主张。
- Create Phantom 衍生内容及其他第三方部分继续保留 [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) 中列出的上游声明。

重新分发源码、二进制文件或资源前，请阅读 [LICENSE.md](LICENSE.md)、[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) 和 [docs/PERMISSIONS.zh-CN.md](docs/PERMISSIONS.zh-CN.md)。
## 致谢

- **Nobodiiiii** — Create: Biotech 原作者及原项目资源权利人。
- **Baoshuo01** — Minecraft 1.21.1 NeoForge 移植与维护。
- **Yison** — 按 BSD-3-Clause 许可证保留的 Create Phantom 内容作者。
- **Tim Heidler** — 按 MIT 许可证保留的上游 Create Mobile Packages 部分作者。
- **Create 团队及贡献者** — Create 模组及其公开 API。
- 所有翻译者、测试者、依赖作者和社区贡献者。

除非相关方明确声明，本仓库不代表 Microsoft、Mojang Studios、Create 团队或上游作者的官方认可或合作关系。