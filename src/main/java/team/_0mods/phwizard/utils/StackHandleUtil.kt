@file:JvmName("StackHandleUtil")

package team._0mods.phwizard.utils

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.component.ResolvableProfile
import net.minecraft.world.level.block.entity.SkullBlockEntity
//?}

fun ItemStack.overwriteItemStack(/*? if <1.20.5 {*//*tagId: String,*//*?}*/ prefix: String, /*? if >=1.20.5 {*/player: Player /*?}*/) {
    //? if <1.20.5 {
    /*if (this.`is`(Items.PLAYER_HEAD)) {
        if (this.hasCustomHoverName()) {
            val hover = this.hoverName.string
            if (hover.startsWith(prefix)) {
                val splitted = hover.removePrefix(prefix)

                if (splitted.isNotEmpty()) {
                    this.tag = null

                    this.orCreateTag.putString(tagId, splitted)
                } else
                    this.setHoverName(Component.literal("Enable to load the head for null profile!").withStyle(ChatFormatting.DARK_RED))
            }
        }
    }
    *///?} else {
    if (this.`is`(Items.PLAYER_HEAD)) {
        val name = this.hoverName
        val hover = name.string
        val level = player.level()

        if (hover.startsWith(prefix)) {
            val splitted = hover.removePrefix(prefix)

            if (splitted.isNotEmpty()) {
                if (!level.isClientSide) {
                    SkullBlockEntity.fetchGameProfile(splitted).thenAcceptAsync({
                        (level as ServerLevel).server.executeIfPossible {
                            if (it.isPresent) {
                                this.set(DataComponents.CUSTOM_NAME, null as? Component?)
                                this.set(DataComponents.PROFILE, ResolvableProfile(it.get()))
                            } else {
                                this.set(DataComponents.PROFILE, null as? ResolvableProfile?)
                                this.set(DataComponents.CUSTOM_NAME, Component.literal("That player is not present! Check name or try again.").withStyle(ChatFormatting.DARK_RED))
                            }
                        }
                    }, SkullBlockEntity.CHECKED_MAIN_THREAD_EXECUTOR)
                }
            } else {
                this.set(DataComponents.CUSTOM_NAME, Component.literal("Enable to load the head for null profile!").withStyle(ChatFormatting.DARK_RED))
            }
        }
    }
    //?}
}
