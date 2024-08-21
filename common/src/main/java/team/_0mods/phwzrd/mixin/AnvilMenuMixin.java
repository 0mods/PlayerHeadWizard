package team._0mods.phwzrd.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;
import java.util.Map;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Inject(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
                    ordinal = 3
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void createResultInject(CallbackInfo ci, ItemStack itemStack, int _i, int _i1, int _i2, ItemStack itemStack1, ItemStack itemStack2, Map _map) {
        if (itemStack1.is(Items.PLAYER_HEAD)) {
            if (itemStack1.hasCustomHoverName()) {
                var hover = itemStack1.getHoverName().getString();
                if (hover.contains("profile=")) {
                    var arr = Arrays.asList(hover.split("="));

                    if (arr.size() > 1) {
                        var s = arr.get(1);

                        itemStack1.setTag(null);
                        itemStack1.getOrCreateTag().putString(PlayerHeadItem.TAG_SKULL_OWNER, s);
                    } else {
                        itemStack1.setHoverName(Component.literal("Enable to load the head for null profile!").withStyle(ChatFormatting.DARK_RED));
                    }
                }
            }
        }
    }
}
