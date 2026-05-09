package net.pitan76.nexton.machinery.item;

import net.pitan76.mcpitanlib.api.item.v2.ItemSettingsBuilder;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.midohra.item.SupplierTypedBlockItemWrapper;
import net.pitan76.nexton.core.item.ItemGroups;
import net.pitan76.nexton.machinery.block.Blocks;
import net.pitan76.nexton.machinery.block.ElectricFurnaceBlock;
import net.pitan76.nexton.machinery.block.FuelGeneratorBlock;

import static net.pitan76.nexton.machinery.NextonMachinery._id;
import static net.pitan76.nexton.machinery.NextonMachinery.registry;

public class Items {

    public static final ItemSettingsBuilder STANDARD_ITEM_SETTINGS = ItemSettingsBuilder.of()
            .addGroup(ItemGroups.NI_GROUP);

    public static SupplierTypedBlockItemWrapper<FuelGeneratorBlock> FUEL_GENERATOR;
    public static SupplierTypedBlockItemWrapper<FuelGeneratorBlock> FUEL_GENERATOR_MK2;
    public static SupplierTypedBlockItemWrapper<FuelGeneratorBlock> FUEL_GENERATOR_MK3;
    public static SupplierTypedBlockItemWrapper<ElectricFurnaceBlock> ELECTRIC_FURNACE;

    public static void init() {
        FUEL_GENERATOR = registry.registerBlockItem(_id("generator"), Blocks.FUEL_GENERATOR, STANDARD_ITEM_SETTINGS.build(_id("generator")));
        FUEL_GENERATOR_MK2 = registry.registerBlockItem(_id("generator_mk2"), Blocks.FUEL_GENERATOR_MK2, STANDARD_ITEM_SETTINGS.build(_id("generator_mk2")));
        FUEL_GENERATOR_MK3 = registry.registerBlockItem(_id("generator_mk3"), Blocks.FUEL_GENERATOR_MK3, STANDARD_ITEM_SETTINGS.build(_id("generator_mk3")));
        ELECTRIC_FURNACE = registry.registerBlockItem(_id("electric_furnace"), Blocks.ELECTRIC_FURNACE, STANDARD_ITEM_SETTINGS.build(_id("electric_furnace")));

        ItemGroups.NI_GROUP.setIcon(() -> ItemStackUtil.create(FUEL_GENERATOR.get()));
    }
}
