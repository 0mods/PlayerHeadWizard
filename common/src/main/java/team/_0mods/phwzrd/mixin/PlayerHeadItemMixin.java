package team._0mods.phwzrd.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import team._0mods.phwzrd.util.SetUtil;

@Mixin(PlayerHeadItem.class)
public class PlayerHeadItemMixin extends Item {
    @Shadow @Final public static String TAG_SKULL_OWNER;
    @Unique private boolean phw$updated = false;

    public PlayerHeadItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!phw$updated) {
            phw$updated = SetUtil.playerHeadItemUpdate(stack, level, TAG_SKULL_OWNER);
        } else if (stack.hasCustomHoverName() && stack.getHoverName().getString().contains("profile=")) {
            phw$updated = SetUtil.playerHeadItemUpdate(stack, level, TAG_SKULL_OWNER);
        }

        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
