package club.sk1er.mods.bedwars.gui;

import club.sk1er.mods.bedwars.BedwarsResourceDisplay;
import club.sk1er.mods.bedwars.DisplayElement;
import club.sk1er.mods.bedwars.ElementRenderer;
import club.sk1er.mods.bedwars.displayitems.IDisplayItem;
import club.sk1er.mods.bedwars.gui.buttons.EditElement;
import club.sk1er.mods.bedwars.gui.screens.EditSubElementsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 5/30/17.
 */
public class ConfigGui extends GuiScreen {
    private BedwarsResourceDisplay mod;
    private boolean mouseDown;
    private DisplayElement currentElement;

    public ConfigGui(BedwarsResourceDisplay mod) {
        this.mod = mod;
    }


    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        EditElement editElement = new EditElement(1, (int) (resolution.getScaledWidth_double() - 80), (int) (resolution.getScaledHeight_double() - 20),
                "Edit element", this, mod);
        buttonList.add(editElement);
        buttonList.add(new GuiButton(2, 1, (int) (resolution.getScaledHeight_double() - 20), 65, 20, "Save") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean b = super.mousePressed(mc, mouseX, mouseY);
                ;
                if (b) {
                    mod.saveConfig();
                }
                return b;
            }
        });
        buttonList.add(new GuiButton(3, 1, (int) (resolution.getScaledHeight_double() - 40), 65, 20, "New element") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean b = super.mousePressed(mc, mouseX, mouseY);
                if (b) {
                    List<IDisplayItem> iDisplayItems = new ArrayList<>();
                    DisplayElement element = new DisplayElement(.5, .5, 1.0, 6, iDisplayItems, true, true);
                    mod.getDisplayElements().add(element);
                    mc.displayGuiScreen(new EditSubElementsGui(element, mod));
                }
                return b;
            }
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        List<DisplayElement> elementList = mod.getDisplayElements();
        for (DisplayElement element : elementList) {
            try {
                if (element.getScale() != 1.0)
                    GlStateManager.scale(element.getScale(), element.getScale(), 0);
                ElementRenderer.currentScale = element.getScale();
                ElementRenderer.color = element.getColor();
                element.drawForConfig();
                if (element.getScale() != 1.0)
                    GlStateManager.scale(1.0 / element.getScale(), 1.0 / element.getScale(), 0);
            } catch (Exception e) {

            }
        }
        if (currentElement != null) {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            double x1 = currentElement.getXloc() * resolution.getScaledWidth_double();
            double x2 = currentElement.getXloc() * resolution.getScaledWidth_double() + currentElement.getDimensions().getWidth();
            double y1 = currentElement.getYloc() * resolution.getScaledHeight_double();
            double y2 = currentElement.getYloc() * resolution.getScaledHeight_double() + currentElement.getDimensions().getHeight();
            //TODO outline the object. Cords of the cords are above. Ideally, it is expanded by 5 in all directions
            //Left top right bottom

            Gui.drawRect((int) x1, (int) y1, (int) x2, (int) y2, Color.WHITE.getRGB());
            DisplayElement element = currentElement;
            if (element.getScale() != 1.0)
                GlStateManager.scale(element.getScale(), element.getScale(), 0);
            ElementRenderer.currentScale = element.getScale();
            ElementRenderer.color = element.getColor();
            element.drawForConfig();
            if (element.getScale() != 1.0)
                GlStateManager.scale(1.0 / element.getScale(), 1.0 / element.getScale(), 0);
//
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        boolean isOver = false;
        for (GuiButton button : buttonList) {
            if (button.isMouseOver())
                isOver = true;
        }
        if (!mouseDown && Mouse.isButtonDown(0) && !isOver) {
            //Mouse pushed down. Calculate current element
            int clickX = Mouse.getX() / resolution.getScaleFactor();
            int clickY = Mouse.getY() / resolution.getScaleFactor();
            boolean found = false;
            for (DisplayElement element : mod.getDisplayElements()) {
                Dimension dimension = element.getDimensions();
                double displayXLoc = resolution.getScaledWidth_double() * element.getXloc();
                double displayYLoc = resolution.getScaledHeight_double() - resolution.getScaledHeight_double() * element.getYloc();
                if (clickX > displayXLoc
                        && clickX < displayXLoc + dimension.getWidth()
                        && clickY < displayYLoc
                        && clickY > displayYLoc - dimension.getHeight()) {
                    this.currentElement = element;
                    found = true;
                }
            }
            if (!found)
                currentElement = null;
        }
        mouseDown = Mouse.isButtonDown(0);
    }

    public BedwarsResourceDisplay getMod() {
        return mod;
    }

    public boolean isMouseDown() {
        return mouseDown;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        mod.saveConfig();
    }

    public DisplayElement getCurrentElement() {
        return currentElement;
    }
}
