package team._0mods.phwzrd.fabric;

import net.fabricmc.api.ModInitializer;
import team._0mods.phwzrd.PlayerHeadWizard;

public final class PlayerHeadWizardFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PlayerHeadWizard.init();
    }
}
