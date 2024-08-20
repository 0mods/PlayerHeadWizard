package team._0mods.phwzrd.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Arrays;

public final class SetUtil {
    public static boolean playerHeadItemUpdate(ItemStack stack, Level level, String skullOwner) {
        if (stack.is(Items.PLAYER_HEAD)) {
            if (stack.hasCustomHoverName()) {
                var name = stack.getHoverName().getString();

                if (name.startsWith("profile=")) {
                    var separated = Arrays.asList(name.split("=")).get(1);
                    stack.setTag(null); // clear tags

                    stack.getOrCreateTag().putString(skullOwner, separated);

                    return true;
                }
            }
        }

        return false;
    }
}
