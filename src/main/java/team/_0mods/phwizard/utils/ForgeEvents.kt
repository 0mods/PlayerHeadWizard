//? if forge {
/*@file:Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
*///?} elif neoforge {
@file:EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
//?}
package team._0mods.phwizard.utils

//? if forge {
/*import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
*///?} elif neoforge {
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.AddReloadListenerEvent
//?}
//? if fabric {
/*import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
*///?}
import team._0mods.phwizard.PlayerHeadWizard.Companion.ModId
import team._0mods.phwizard.config.ConfigReloadListener
import team._0mods.phwizard.config.configJson

//? if forge || neoforge {
@SubscribeEvent(priority = EventPriority.HIGHEST)
fun onMCReload(e: AddReloadListenerEvent) {
    e.addListener(ConfigReloadListener(configJson))
}
//?} elif fabric {
/*fun onMCReloadFabric() {
    ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(object : IdentifiableResourceReloadListener {
        override fun reload(
            preparationBarrier: PreparableReloadListener.PreparationBarrier,
            resourceManager: ResourceManager,
            preparationsProfiler: ProfilerFiller,
            reloadProfiler: ProfilerFiller,
            backgroundExecutor: Executor,
            gameExecutor: Executor
        ): CompletableFuture<Void> = ConfigReloadListener(configJson).reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor)

        override fun getFabricId(): ResourceLocation = ResourceLocation/^? if >=1.21 {^/.parse/^?}^/("$ModId:config_reload")
    })
}
*///?}
