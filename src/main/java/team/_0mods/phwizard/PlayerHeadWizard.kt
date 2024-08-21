package team._0mods.phwizard

//? if fabric {
/*import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.fabricmc.loader.api.FabricLoader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
*///?}

//? if forge {
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
//?}

// COMMON SIDE, DON'T TOUCH IT!
import org.slf4j.Logger
import org.slf4j.LoggerFactory

//? if forge
@Mod(PlayerHeadWizard.ModId)
class PlayerHeadWizard {
    companion object {
        @JvmField var PREFIX = "profile="
        const val ModId = "playerwizard"
        @JvmField val LOGGER: Logger = LoggerFactory.getLogger(ModId)

        //? if fabric
        /*@OptIn(ExperimentalSerializationApi::class)*/
        @JvmStatic
        fun onInit() {
            LOGGER.info("Initializing Player Head Wizard!")
            //? if forge {
            val b = ForgeConfigSpec.Builder()
            val str = b.define("rename_prefix", "profile=")
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, b.build())
            val prefix = str.get()
            //?}

            //? if fabric {
            /*val json = Json {
                encodeDefaults = true
                allowComments = true
                prettyPrintIndent = "  "
                prettyPrint = true
            }

            val config = Config().loadConfig(json, ModId)
            val prefix = config.prefix
            *///?}
            PREFIX = prefix
        }
    }

    //? if forge {
    init {
        onInit()
    }
    //?}

    //? if fabric {
    /*private inline fun <reified T> T.loadConfig(json: Json, fileName: String): T {
        LOGGER.debug("Loading config '$fileName'")

        val file = FabricLoader.getInstance().configDir.resolve("config/").toFile().resolve("$fileName.json")

        return if (file.exists()) {
            try {
                decodeCfg(json, file)
            } catch (e: Exception) {
                LOGGER.error("Failed to load config with name ${file.canonicalPath}.")
                LOGGER.warn("Regenerating config... Using defaults.")
                backupFile(file)
                file.delete()
                encodeCfg(json, file)
                this
            }
        } else {
            encodeCfg(json, file)
            this
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private inline fun <reified T> T.encodeCfg(json: Json, file: File) {
        try {
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }

            json.encodeToStream(this, file.outputStream())
        } catch (e: FileSystemException) {
            LOGGER.error("Failed to write config to file '$file'", e)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private inline fun <reified T> decodeCfg(json: Json, file: File): T = try {
        json.decodeFromStream(file.inputStream())
    } catch (e: FileSystemException) {
        LOGGER.error("Failed to read config from file '$file'", e)
        throw e
    }

    private fun backupFile(original: File) {
        val originalBakName = original.canonicalPath + ".bak"
        var i = 0
        var p: String
        var bakFile = File(originalBakName)

        while (true) {
            if (bakFile.exists()) {
                i++
                p = "${original.canonicalPath}.bak$i"
                bakFile = File(p)

                continue
            } else break
        }

        val l = original.readLines()

        BufferedWriter(FileWriter(bakFile, true)).use { w ->
            l.forEach {
                w.write(it)
                w.newLine()
            }
        }

        bakFile.createNewFile()
    }

    @Serializable private data class Config(val prefix: String = "prefix=")
    *///?}
}
