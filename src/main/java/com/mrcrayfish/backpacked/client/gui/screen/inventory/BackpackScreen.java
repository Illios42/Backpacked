package com.mrcrayfish.backpacked.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.backpacked.client.gui.screen.CustomiseBackpackScreen;
import com.mrcrayfish.backpacked.client.gui.screen.widget.MiniButton;
import com.mrcrayfish.backpacked.inventory.container.BackpackContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class BackpackScreen extends ContainerScreen<BackpackContainer>
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ITextComponent CUSTOMISE_TOOLTIP = new TranslationTextComponent("backpacked.button.customise.tooltip");
    private static final ITextComponent CONFIG_TOOLTIP = new TranslationTextComponent("backpacked.button.config.tooltip");

    private final int rows;
    private boolean opened;

    public BackpackScreen(BackpackContainer backpackContainer, PlayerInventory playerInventory, ITextComponent titleIn)
    {
        super(backpackContainer, playerInventory, titleIn);
        this.rows = backpackContainer.getRows();
        this.imageHeight = 114 + this.rows * 18;
    }

    @Override
    public void init(Minecraft minecraft, int width, int height)
    {
        super.init(minecraft, width, height);
        if(!this.opened)
        {
            minecraft.getSoundManager().play(SimpleSound.forUI(SoundEvents.ARMOR_EQUIP_LEATHER, 0.75F, 1.0F));
            this.opened = true;
        }
        int titleWidth = minecraft.font.width(this.title);
        this.addButton(new MiniButton(this.leftPos + titleWidth + 8 + 3, this.topPos + 5, 200, 0, CustomiseBackpackScreen.GUI_TEXTURE, onPress -> {
            minecraft.setScreen(new CustomiseBackpackScreen());
        }, (button, matrixStack, mouseX, mouseY) -> {
            this.renderTooltip(matrixStack, CUSTOMISE_TOOLTIP, mouseX, mouseY);
        }));
        this.addButton(new MiniButton(this.leftPos + titleWidth + 8 + 3 + 13, this.topPos + 5, 210, 0, CustomiseBackpackScreen.GUI_TEXTURE, onPress -> {
            //TODO
        }, (button, matrixStack, mouseX, mouseY) -> {
            this.renderTooltip(matrixStack, CONFIG_TOOLTIP, mouseX, mouseY);
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack); //Draw background
        super.render(matrixStack, mouseX, mouseY, partialTicks); //Super
        this.renderTooltip(matrixStack, mouseX, mouseY); //Render hovered tooltips
        this.buttons.forEach(widget -> {
            if(widget.isHovered()) {
                widget.renderToolTip(matrixStack, mouseX, mouseY);
            }
        });
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI_TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.rows * 18 + 17);
        this.blit(matrixStack, this.leftPos, this.topPos + this.rows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        this.font.draw(matrixStack, this.title, 8.0F, 6.0F, 0x404040);
        this.font.draw(matrixStack, this.inventory.getDisplayName(), 8.0F, (float) (this.imageHeight - 96 + 2), 0x404040);
    }
}
