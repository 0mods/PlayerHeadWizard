package team._0mods.phwizard.config

import kotlinx.serialization.json.Json
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import team._0mods.phwizard.PlayerHeadWizard
import team._0mods.phwizard.PlayerHeadWizard.Companion.ModId

class ConfigReloadListener(private val json: Json): SimplePreparableReloadListener<Unit>() {
    //DON'T TOUCH IT
    override fun prepare(resourceManager: ResourceManager, profiler: ProfilerFiller) {}

    override fun apply(`object`: Unit, resourceManager: ResourceManager, profiler: ProfilerFiller) {
        PlayerHeadWizard.config = PHWConfig().loadConfig(json, ModId)
    }
}