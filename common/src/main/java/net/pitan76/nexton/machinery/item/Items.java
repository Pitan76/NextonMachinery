package net.pitan76.nexton.machinery.item;

import net.pitan76.mcpitanlib.api.item.v2.ItemSettingsBuilder;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.midohra.item.ItemWrapper;
import net.pitan76.mcpitanlib.midohra.registry.MidohraRegistry;
import net.pitan76.nexton.core.item.ItemGroups;
import net.pitan76.nexton.machinery.NextonMachinery;
import net.pitan76.nexton.machinery.block.Blocks;

import static net.pitan76.nexton.machinery.NextonMachinery._id;

public class Items {

    public static final ItemSettingsBuilder STANDARD_ITEM_SETTINGS = ItemSettingsBuilder.of()
            .addGroup(ItemGroups.NI_GROUP);

    public static ItemWrapper FUEL_GENERATOR;
    public static ItemWrapper FUEL_GENERATOR_MK2;
    public static ItemWrapper FUEL_GENERATOR_MK3;
    public static ItemWrapper ELECTRIC_FURNACE;

    public static void init() {
        MidohraRegistry registry = NextonMachinery.registry.getMidohraRegistryV1();

        FUEL_GENERATOR = registry.registerBlockItem(_id("generator"), Blocks.FUEL_GENERATOR, STANDARD_ITEM_SETTINGS.build(_id("generator")));
        FUEL_GENERATOR_MK2 = registry.registerBlockItem(_id("generator_mk2"), Blocks.FUEL_GENERATOR_MK2, STANDARD_ITEM_SETTINGS.build(_id("generator_mk2")));
        FUEL_GENERATOR_MK3 = registry.registerBlockItem(_id("generator_mk3"), Blocks.FUEL_GENERATOR_MK3, STANDARD_ITEM_SETTINGS.build(_id("generator_mk3")));
        ELECTRIC_FURNACE = registry.registerBlockItem(_id("electric_furnace"), Blocks.ELECTRIC_FURNACE, STANDARD_ITEM_SETTINGS.build(_id("electric_furnace")));

        ItemGroups.NI_GROUP.setIcon(() -> ItemStackUtil.create(FUEL_GENERATOR.get()));
    }
}
