package net.pitan76.nexton.machinery.client;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawBackgroundArgs;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawForegroundArgs;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawMouseoverTooltipArgs;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;
import net.pitan76.mcpitanlib.guilib.GuiTextures;
import net.pitan76.mcpitanlib.guilib.api.render.PartsRenderer;
import net.pitan76.mcpitanlib.guilib.api.screen.ContainerGuiScreen;
import net.pitan76.nexton.machinery.screen.ElectricFurnaceScreenHandler;

import static net.pitan76.nexton.machinery.NextonMachinery._id;

public class ElectricFurnaceScreen extends ContainerGuiScreen<ElectricFurnaceScreenHandler> {
    public ElectricFurnaceScreen(ElectricFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        setTitleX(getBackgroundWidth() / 2 - ScreenUtil.getWidth(title) / 2);
    }

    @Override
    public void initOverride() {
        super.initOverride();
    }

    @Override
    protected void drawForegroundOverride(DrawForegroundArgs args) {
        super.drawForegroundOverride(args);

        int cookTime = this.handler.blockEntity.cookTime;
        int cookTimeTotal = this.handler.blockEntity.cookTimeTotal;

        double percentage = cookTimeTotal > 0 ? (double) (cookTimeTotal - cookTime) / cookTimeTotal * 100 : 0;

        percentage = Math.floor(percentage * 10) / 10;
        Text percentageText = TextUtil.literal(percentage + "%");
        drawText(args.drawObjectDM, percentageText, 88 - ScreenUtil.getWidth(percentageText) / 2, 24);
    }

    @Override
    public void drawBackgroundOverride(DrawBackgroundArgs args) {
        super.drawBackgroundOverride(args);

        int cookTime = this.handler.blockEntity.cookTime;
        int cookTimeTotal = this.handler.blockEntity.cookTimeTotal;

        PartsRenderer.drawBurningBar(args.drawObjectDM, x + 50, y + 35 + 18, cookTimeTotal, cookTimeTotal);
        PartsRenderer.drawLeft2RightProgressBar(args.drawObjectDM, x + backgroundWidth / 2 - 12, y + 35, 16, 168, 16, 184, 24, 16, cookTimeTotal - cookTime, cookTimeTotal, GuiTextures.BASE_FURNACE_BACKGROUND);

        drawEnergyBar(args);
    }

    protected void drawEnergyBar(DrawBackgroundArgs args) {
        if (this.handler.blockEntity.getCapacityEnergy() == 0) return;

        PartsRenderer.drawBottom2TopProgressBar(args.drawObjectDM, x + 20, y + 16, 0, 0, 14, 0, 14, 46, this.handler.blockEntity.getEnergyStored(), this.handler.blockEntity.getCapacityEnergy(), _id("textures/container/ei_parts.png"));
    }

    @Override
    public void callDrawMouseoverTooltip(DrawMouseoverTooltipArgs args) {
        super.callDrawMouseoverTooltip(args);

        int x = args.mouseX;
        int y = args.mouseY;

        // Energy amount
        if (isPointInRegion(this.x + 20, this.y + 16, 14, 46, x, y)) {
            ScreenUtil.RendererUtil.drawTooltip(args.drawObjectDM, this.textRenderer, TextUtil.literal(this.handler.blockEntity.getEnergyStored() + " / " + this.handler.blockEntity.getCapacityEnergy() + " FE"), x, y);
        }

    }

    public static boolean isPointInRegion(int x, int y, int width, int height, int pointX, int pointY) {
        return pointX >= x && pointX < x + width && pointY >= y && pointY < y + height;
    }
}
