# Soul Shards Respawn [![](http://cf.way2muchnoise.eu/full_soul-shards-respawn_downloads.svg)](https://minecraft.curseforge.com/projects/soul-shards-respawn)

Ever wanted to create your own mob spawners? Now you can!
# Soul Shard Recipe
To obtain a shard, create a structure and right-click the center with a diamond, like so:
![](./images/PoshGeneralCuckoo-max-14mb.gif)

The first kill you get with a Shard binds that shard to the killed entity. Shards must be on your hotbar or in your offhand. It will only receive souls from that entity from then on. To speed up the process, you can right click a Mob Spawner that contains the bound entity to consume the spawner and increase the shard's stored souls by 200 (configurable).

To efficiently gain souls, you should look into getting the Vile Sword, which adds another soul per kill, or the Soul Stealer enchantment, which adds an additional soul per kill per level. The maxiumum amount of souls you can obtain per kill is 7 (1 base + 1 Vile Sword + 5 max level Soul Stealer).

Once you begin progressing through the tiers, you should craft a Soul Cage. A Soul Cage will let you insert your Soul Shard and will begin spawning the bound entity indefinitely.
## Links

* [Maven](http://tehnut.info/maven/info/tehnut/soulshardsrespawn/SoulShardsRespawn/)

## Information

This is a fan continuation of the popular 1.4.7 mod, [Soul Shards](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1285901-1-6-4-forgeirc-v1-0-18-soul-shards-v2-0-15-and#soulshards).

This version of the mod is based on the sources of [Soul Shards: Reborn by Moze_Intel](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/wip-mods/1445947-1-7-10-soul-shards-reborn-original-soul-shards) and [Soul Shards: The Old Ways by Team Whammich](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2329877-soul-shards-the-old-ways-rc9-update).

This version is a near direct clone of the original mod.

For more information, you can see the [Wiki](https://github.com/TehNut/Soul-Shards-Respawn/wiki). Information about modifying the `tiers.json` config can be found there.

## Development Setup

1. Fork this project to your own Github repository and clone it to your desktop.
2. Follow [the steps listed](http://mcforge.readthedocs.io/en/latest/gettingstarted/#from-zero-to-modding) in the Forge docs for your chosen IDE.

## Developing Addons

Add to your `build.gradle`:

    repositories {
      maven {
        url "http://tehnut.info/maven/"
      }
    }
    
    dependencies {
      deobfCompile "info.tehnut.soulshardsrespawn:SoulShardsRespawn:${ssr_version}"
    }
`${ssr_version}` can be found on CurseForge (or via the Maven itself), check the file name of the version you want.

## License

Soul Shards Respawn is licensed under the [MIT](https://tldrlegal.com/license/mit-license) license.

All of the art is property of [BBoldt](https://github.com/BBoldt/). The art is released into the public domain.

## Custom Builds
   
**Custom builds are *unsupported*. If you have an issue while using an unofficial build, it is not guaranteed that you will get support.**
   
### How to make a custom build:
   
1. Clone directly from this repository to your desktop.
2. Navigate to the directory you cloned to. Open a command window there and run `gradlew build`
3. Once it completes, your new build will be found at `../build/libs/SoulShards-TOW-*.jar`. You can ignore the `api`, `sources`, and `javadoc` jars.