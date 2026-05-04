package net.pitan76.nexton.machinery.item;

import net.minecraft.item.Item;
import net.pitan76.nexton.machinery.block.Blocks;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;
import net.pitan76.mcpitanlib.api.util.ItemUtil;

import static net.pitan76.nexton.machinery.NextonMachinery._id;
import static net.pitan76.nexton.machinery.NextonMachinery.registry;

public class Items {

    public static final CompatibleItemSettings STANDARD_ITEM_SETTINGS = CompatibleItemSettings.of()
            .addGroup(ItemGroups.EI_GROUP);

    public static RegistryResult<Item> FUEL_GENERATOR;
    public static RegistryResult<Item> ELECTRIC_FURNACE;

    public static void init() {
        FUEL_GENERATOR = registry.registerItem(_id("generator"), () -> ItemUtil.ofBlock(Blocks.FUEL_GENERATOR.getOrNull(), STANDARD_ITEM_SETTINGS));
        ELECTRIC_FURNACE = registry.registerItem(_id("electric_furnace"), () -> ItemUtil.ofBlock(Blocks.ELECTRIC_FURNACE.getOrNull(), STANDARD_ITEM_SETTINGS));
    }
}
