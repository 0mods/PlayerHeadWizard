package team._0mods.phwizard.config

import kotlinx.serialization.Serializable

@Serializable
data class PHWConfig(
    val customPrefix: Commented = Commented(
            "Overwrites the value of the prefix. The default is empty and the prefix is \"prefix=\"",
        ""
    )
) {
    @Serializable
    class Commented(
        private val comment: String,
        val value: String
    )
}