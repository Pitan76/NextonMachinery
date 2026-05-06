package net.pitan76.nexton.machinery.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandler;
import net.pitan76.mcpitanlib.api.util.IngredientUtil;
import net.pitan76.mcpitanlib.midohra.recipe.RecipeManager;
import net.pitan76.mcpitanlib.midohra.recipe.RecipeType;
import net.pitan76.mcpitanlib.midohra.recipe.ServerRecipeManager;
import net.pitan76.mcpitanlib.midohra.recipe.entry.RecipeEntry;
import net.pitan76.mcpitanlib.midohra.world.ServerWorld;
import net.pitan76.nexton.machinery.NextonMachinery;
import net.pitan76.nexton.machinery.api.energy.EnergyStorageProvider;
import net.pitan76.nexton.machinery.api.energy.IEnergyStorage;
import net.pitan76.nexton.machinery.api.energy.SimpleEnergyStorage;
import net.pitan76.nexton.machinery.api.energy.util.EnergyUtil;
import net.pitan76.nexton.machinery.block.entity.base.MachineBlockEntityWithExtendedContainer;
import net.pitan76.nexton.machinery.screen.ElectricFurnaceScreenHandler;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.mcpitanlib.api.event.nbt.ReadNbtArgs;
import net.pitan76.mcpitanlib.api.event.nbt.WriteNbtArgs;
import net.pitan76.mcpitanlib.api.event.tile.TileTickEvent;
import net.pitan76.mcpitanlib.api.gui.args.CreateMenuEvent;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.ItemStackUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.guilib.api.block.entity.ExtendedBlockEntityWithContainer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ElectricFurnaceBlockEntity extends MachineBlockEntityWithExtendedContainer implements EnergyStorageProvider {

    public static final int DEFAULT_COOK_TIME = 200;

    public int cookTime;
    public int cookTimeTotal;

    public static SimpleEnergyStorage.Builder energyStorageBuilder =
            new SimpleEnergyStorage.Builder().capacity(10_000).maxInput(500).maxOutput(0);

    private final IEnergyStorage energyStorage = energyStorageBuilder.build();

    public ElectricFurnaceBlockEntity(BlockEntityType<?> type, TileCreateEvent e) {
        super(type, e);
    }

    public ElectricFurnaceBlockEntity(TileCreateEvent e) {
        this(BlockEntities.ELECTRIC_FURNACE.getOrNull(), e);
    }

    @Override
    public void tick(TileTickEvent<ExtendedBlockEntityWithContainer> e) {
        super.tick(e);
        if (isClient()) return;

        ItemStack input = getInputStack();
        ItemStack output = getOutputStack();

        // if empty input slot, stop
        if (input.isEmpty()) {
            cookTime = 0;
            cookTimeTotal = 0;
            setActive(false);
            callMarkDirty();
            return;
        }

        // if it cannot output, stop
        if (!canOutput(getSmeltResult(input))) {
            setActive(false);
            callMarkDirty();
            return;
        }

        if (!isCooking()) {
            startCook();
            setActive(true);
        }

        if (hasEnergy()) {
            cookTime--;

            removeEnergyStored(getConsumingEnergyAmountOnTick());
            setActive(true);

            if (cookTime <= 0) {
                finishSmelt();
                startCook(); // next cook
            }
        } else {
            setActive(false);
        }

        if (!NextonMachinery.isUsingRebornEnergy)
            EnergyUtil.transferNearby(this, getEnergyStored());
    }

    private ItemStack cachedInput = ItemStackUtil.empty();
    private ItemStack cachedResult = ItemStackUtil.empty();

    protected ItemStack getSmeltResult(ItemStack input) {
        if (ItemStackUtil.areItemsEqual(cachedInput, input)) {
            return cachedResult;
        }

        cachedInput = input.copy();
        cachedResult = computeSmelt(input);

        return cachedResult;
    }

    protected ItemStack computeSmelt(ItemStack input) {
        Optional<ServerWorld> world = getMidohraWorld().toServerWorld();
        if (!world.isPresent()) return ItemStackUtil.empty();

        ServerRecipeManager manager = world.get().getRecipeManager();

        for (RecipeEntry entry : manager.getRecipeEntries()) {
            if (!entry.getRecipeType().equals(RecipeType.SMELTING)) continue;

            for (Ingredient ingredient : entry.getRecipe().getInputs()) {
                ItemStack[] matches = IngredientUtil.getMatchingStacks(ingredient);

                for (ItemStack stack : matches) {
                    if (ItemStackUtil.areItemsEqual(stack, input)) {
                        return entry.getRecipe().getOutput(world.get()).toMinecraft();
                    }
                }
            }
        }

        return ItemStackUtil.empty();
    }

    protected void finishSmelt() {
        ItemStack input = getInputStack();
        ItemStack output = getOutputStack();

        ItemStack result = getSmeltResult(input);

        if (result.isEmpty()) return;
        if (!canOutput(result)) return;

        input.decrement(1);

        if (output.isEmpty()) {
            setStack(1, result.copy());
        } else {
            output.increment(result.getCount());
        }
    }

    @Override
    public void writeNbt(WriteNbtArgs args) {
        super.writeNbt(args);
        NbtUtil.putInt(args.nbt, "cookTime", cookTime);
        NbtUtil.putInt(args.nbt, "cookTimeTotal", cookTimeTotal);
    }

    @Override
    public void readNbt(ReadNbtArgs args) {
        super.readNbt(args);
        cookTime = NbtUtil.getInt(args.nbt, "cookTime");
        cookTimeTotal = NbtUtil.getInt(args.nbt, "cookTimeTotal");
    }

    public ItemStack getInputStack() {
        return getStack(0);
    }

    public ItemStack getOutputStack() {
        return getStack(1);
    }

    public boolean canOutput(ItemStack result) {
        ItemStack output = getOutputStack();

        if (result.isEmpty()) return false;
        if (output.isEmpty()) return true;

        if (!ItemStackUtil.areItemsEqual(output, result)) return false;

        return output.getCount() + result.getCount() <= output.getMaxCount();
    }

    public long getConsumingEnergyAmountOnTick() {
        return 1;
    }

    public boolean isCooking() {
        return cookTime > 0;
    }

    public void startCook(int time) {
        cookTime = time;
        cookTimeTotal = time;
    }

    public void startCook() {
        startCook(DEFAULT_COOK_TIME);
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public void sync(Player player, PacketByteBuf buf) {
        PacketByteUtil.writeLong(buf, this.getEnergyStored());
        PacketByteUtil.writeInt(buf, this.cookTime);
        PacketByteUtil.writeInt(buf, this.cookTimeTotal);
    }

    @Override
    public int getDefaultInvSize() {
        return 2;
    }

    @Override
    public ScreenHandler createMenu(CreateMenuEvent e) {
        return new ElectricFurnaceScreenHandler(e, this, this);
    }
}
