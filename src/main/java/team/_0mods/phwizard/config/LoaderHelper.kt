package team._0mods.phwizard.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import team._0mods.phwizard.PlayerHeadWizard.Companion.LOGGER

//? if neoforge
import net.neoforged.fml.loading.FMLPaths
//? if forge
/*import net.minecraftforge.fml.loading.FMLPaths*/
//? if fabric
/*import net.fabricmc.loader.api.FabricLoader*/

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

@OptIn(ExperimentalSerializationApi::class)
val configJson = Json {
    encodeDefaults = true
    allowComments = true
    prettyPrintIndent = "  "
    prettyPrint = true
}

inline fun <reified T> T.loadConfig(json: Json, fileName: String): T {
    LOGGER.debug("Loading config '$fileName'")

    val file =
    //? if fabric {
         /*FabricLoader.getInstance().configDir.toFile().resolve("$fileName.json") 
    *///?} elif forge || neoforge {
        FMLPaths.CONFIGDIR.get().toFile().resolve("$fileName.json")
    //?}
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
inline fun <reified T> T.encodeCfg(json: Json, file: File) {
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
inline fun <reified T> decodeCfg(json: Json, file: File): T = try {
    json.decodeFromStream(file.inputStream())
} catch (e: FileSystemException) {
    LOGGER.error("Failed to read config from file '$file'", e)
    throw e
}

fun backupFile(original: File) {
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