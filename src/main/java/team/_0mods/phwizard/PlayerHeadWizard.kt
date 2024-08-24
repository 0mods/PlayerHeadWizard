package team._0mods.phwizard

//? if forge {
/*import net.minecraftforge.fml.common.Mod
*///?} elif neoforge {
/*import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
*///?} elif fabric {
import net.fabricmc.loader.api.FabricLoader
import team._0mods.phwizard.utils.onMCReloadFabric
//?}

// COMMON SIDE, DON'T TOUCH IT!
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import team._0mods.phwizard.config.PHWConfig
import team._0mods.phwizard.config.configJson
import team._0mods.phwizard.config.loadConfig

//? if forge || neoforge
/*@Mod(PlayerHeadWizard.ModId)*/
class PlayerHeadWizard/*? if neoforge {*//*(container: ModContainer)*//*?}*/ {
    companion object {
        @JvmField var PREFIX = "profile="
        const val ModId = "playerwizard"
        @JvmField val LOGGER: Logger = LoggerFactory.getLogger(ModId)
        @JvmStatic
        var config: PHWConfig = PHWConfig()
            internal set

        @JvmStatic
        fun onInit(/*? if neoforge {*//*container: ModContainer*//*?}*/) {
            LOGGER.info("Initializing Player Head Wizard!")

            config = config.loadConfig(configJson, ModId)

            //? if fabric {
            if (FabricLoader.getInstance().isModLoaded("fabric-resource-loader-v0")) {
                onMCReloadFabric()
            }
            //?}
        }
    }

    //? if forge || neoforge {
    /*init {
        onInit(/^? if neoforge {^//^container^//^?}^/)
    }
    *///?}
}
