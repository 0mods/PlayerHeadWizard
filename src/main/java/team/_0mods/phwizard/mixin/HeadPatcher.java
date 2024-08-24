package team._0mods.phwizard.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import team._0mods.phwizard.PlayerHeadWizard;
import team._0mods.phwizard.utils.StackHandleUtil;
//? if <1.20.5 {
/*import net.minecraft.world.item.PlayerHeadItem;

import java.util.Map;
*///?}
@Mixin(AnvilMenu.class)
public abstract class HeadPatcher extends ItemCombinerMenu {
    public HeadPatcher(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    @Inject(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    //? if >=1.20.5 {
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;setEnchantments(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/enchantment/ItemEnchantments;)V"
                    //?} else {
                    /*shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
                    ordinal = 3
                    *///?}
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    //? if >=1.20.5 {
    public void createResultInject(CallbackInfo ci, ItemStack itemstack, int i, long j, int k, ItemStack itemstack2) {
        StackHandleUtil.overwriteItemStack(itemstack2, PlayerHeadWizard.PREFIX, player);
    }
    //?} else {
    /*public void createResultInject(CallbackInfo ci, ItemStack itemStack, int _i, int _i1, int _i2, ItemStack itemStack1, ItemStack itemStack2, Map _map) {
        StackHandleUtil.overwriteItemStack(itemStack1, PlayerHeadItem.TAG_SKULL_OWNER, PlayerHeadWizard.PREFIX);
    }
    *///?}
}
