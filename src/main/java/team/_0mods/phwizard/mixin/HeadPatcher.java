package team._0mods.phwizard.mixin;

//? if <1.20.5 {
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import team._0mods.phwizard.utils.StackHandleUtil;

import java.util.Map;

@Mixin(AnvilMenu.class)
public class HeadPatcher {
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
        StackHandleUtil.overwriteItemStack(itemStack1, PlayerHeadItem.TAG_SKULL_OWNER, "prefix=");
    }
}
//?}