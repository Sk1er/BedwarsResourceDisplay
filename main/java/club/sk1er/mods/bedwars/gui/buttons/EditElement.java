package club.sk1er.mods.bedwars.gui.buttons;

import club.sk1er.mods.bedwars.BedwarsResourceDisplay;
import club.sk1er.mods.bedwars.gui.ConfigGui;
import club.sk1er.mods.bedwars.gui.screens.EditElementScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

/**
 * Created by mitchellkatz on 5/30/17.
 */
public class EditElement extends GuiButton {
    private ConfigGui gui;
    private BedwarsResourceDisplay mod;

    public EditElement(int buttonId, int x, int y, String buttonText, ConfigGui gui, BedwarsResourceDisplay mod) {
        super(buttonId, x, y, 80, 20, buttonText);
        this.gui = gui;
        this.mod = mod;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        enabled = gui.getCurrentElement() != null;
        super.drawButton(mc, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean pressed = super.mousePressed(mc, mouseX, mouseY);
        if (pressed)
            Minecraft.getMinecraft().displayGuiScreen(new EditElementScreen(mod, gui.getCurrentElement()));
        return pressed;
    }
}
