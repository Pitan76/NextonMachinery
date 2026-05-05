package net.pitan76.nexton.machinery.fabric;

import net.pitan76.nexton.machinery.NextonMachinery;
import net.fabricmc.api.ModInitializer;

public class NextonMachineryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        new NextonMachinery();
    }
}