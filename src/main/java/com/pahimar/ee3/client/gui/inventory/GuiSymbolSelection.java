package com.pahimar.ee3.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;

import com.pahimar.ee3.inventory.ContainerSymbolSelection;

public class GuiSymbolSelection extends GuiContainer {

    public GuiSymbolSelection() {
        super(new ContainerSymbolSelection());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float opacity, int x, int y) {

    }

    @Override
    public void drawDefaultBackground() {
        // NOOP
    }
}
