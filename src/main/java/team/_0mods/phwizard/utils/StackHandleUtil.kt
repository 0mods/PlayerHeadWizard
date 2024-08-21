@file:JvmName("StackHandleUtil")

package team._0mods.phwizard.utils

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

fun ItemStack.overwriteItemStack(tagId: String, prefix: String) {
    if (this.`is`(Items.PLAYER_HEAD)) {
        if (this.hasCustomHoverName()) {
            val hover = this.hoverName.string
            if (hover.startsWith(prefix)) {
                val splitted = hover.removePrefix(prefix)

                if (splitted.isNotEmpty()) {
                    this.tag = null

                    this.orCreateTag.putString(tagId, prefix)
                } else
                    this.setHoverName(
                        Component.literal("Enable to load the head for null profile!").withStyle(ChatFormatting.DARK_RED)
                    )
            }
        }
    }
}
