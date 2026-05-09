package net.pitan76.nexton.machinery.screen;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.pitan76.mcpitanlib.api.util.inventory.CompatInventory;
import net.pitan76.mcpitanlib.api.util.inventory.CompatPlayerInventory;
import net.pitan76.mcpitanlib.api.util.inventory.ICompatInventory;
import net.pitan76.nexton.machinery.block.entity.FuelGeneratorBlockEntity;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.guilib.api.container.ExtendedBlockEntityContainerGui;

public class FuelGeneratorScreenHandler extends ExtendedBlockEntityContainerGui<FuelGeneratorBlockEntity> {

    protected final CompatPlayerInventory playerInventory;
    protected final ICompatInventory inventory;

    public FuelGeneratorScreenHandler(CreateMenuEvent e, PacketByteBuf buf) {
        super(ScreenHandlers.FUEL_GENERATOR_SCREEN_HANDLER.get(), e, buf);
        this.inventory = new CompatInventory(1);
        this.playerInventory = e.getCompatPlayerInventory();

        initSlots();
    }

    public FuelGeneratorScreenHandler(CreateMenuEvent e, ICompatInventory inventory, FuelGeneratorBlockEntity blockEntity) {
        this(ScreenHandlers.FUEL_GENERATOR_SCREEN_HANDLER.get(), e, inventory);
        this.blockEntity = blockEntity;
    }

    protected FuelGeneratorScreenHandler(ScreenHandlerType<?> type, CreateMenuEvent e, ICompatInventory inventory) {
        super(type, e);
        this.inventory = inventory;
        this.playerInventory = e.getCompatPlayerInventory();

        initSlots();
    }

    public void initSlots() {
        addPlayerMainInventorySlots(this.playerInventory.getRaw(), 8, 84);
        addPlayerHotbarSlots(this.playerInventory.getRaw(), 8, 142);
        addNormalSlot(this.inventory, 0, 80, 35);
    }

    @Override
    public ItemStack quickMoveOverride(Player player, int index) {
        return super.quickMoveOverride(player, index);
    }

    @Override
    public void receiveSync(PacketByteBuf buf) {
        long energy = PacketByteUtil.readLong(buf);
        int burnTime = PacketByteUtil.readInt(buf);
        int maxBurnTime = PacketByteUtil.readInt(buf);

        this.blockEntity.setEnergyStored(energy);
        this.blockEntity.burnTime = burnTime;
        this.blockEntity.maxBurnTime = maxBurnTime;
    }
}
