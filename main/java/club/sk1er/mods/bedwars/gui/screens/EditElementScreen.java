package club.sk1er.mods.bedwars.gui.screens;

import club.sk1er.mods.bedwars.BedwarsResourceDisplay;
import club.sk1er.mods.bedwars.DisplayElement;
import club.sk1er.mods.bedwars.ElementRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Created by mitchellkatz on 5/30/17.
 */
public class EditElementScreen extends GuiScreen {
    int oldX = 0;
    int oldY;
    boolean allow = false;
    private boolean mouseDown;
    private DisplayElement currentElement;
    private BedwarsResourceDisplay mod;

    public EditElementScreen(BedwarsResourceDisplay mod, DisplayElement element) {
        this.mod = mod;
        this.currentElement = element;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        DisplayElement element = currentElement;
        if (element.getScale() != 1.0)
        GlStateManager.scale(element.getScale(), element.getScale(), 0);
        ElementRenderer.highlighted = element.isHighlighted();
        ElementRenderer.currentScale = element.getScale();
        ElementRenderer.color = element.getColor();
        element.drawForConfig();
        if (element.getScale() != 1.0)
        GlStateManager.scale(1.0 / element.getScale(), 1.0 / element.getScale(), 0);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        mod.saveConfig();
    }

    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        buttonList.add(new GuiButton(1, resolution.getScaledWidth() - 50, resolution.getScaledHeight() - 40, 50, 20, "Edit") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    Minecraft.getMinecraft().displayGuiScreen(new EditSubElementsGui(currentElement, mod));
                }
                return pressed;
            }
        });


        buttonList.add(new GuiButton(2, resolution.getScaledWidth() - 50, resolution.getScaledHeight() - 20, 50, 20, "Back") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    Minecraft.getMinecraft().displayGuiScreen(mod.getConfigGuiInstance());
                }
                return pressed;
            }
        });
//        GuiSlider slider = new GuiSlider(3, 1, resolution.getScaledHeight() - 20, "Size: ", 10, 200, currentElement.getScale() * 210.0 / 2.0, (guiSlider) -> {
//        }) {
//            @Override
//            public void drawButton(Minecraft mc, int mouseX, int mouseY) {
//                enabled = false;
//                super.drawButton(mc, mouseX, mouseY);
//            }
//
//            @Override
//            public void updateSlider() {
//                super.updateSlider();
//                showDecimal = false;
//                currentElement.setScale(getValue() / 100.0);
//            }
//        };

        buttonList.add(new GuiButton(8, 1, resolution.getScaledHeight() - 40, 80, 20, "Rotate color") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    int color = currentElement.getColor();
                    if (color < 6)
                        currentElement.setColor(color + 1);
                    else currentElement.setColor(0);
                }
                return pressed;
            }
        });


//        buttonList.add(slider);
        buttonList.add(new GuiButton(4, resolution.getScaledWidth() - 50, resolution.getScaledHeight() - 60, 50, 20, "Reset") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    currentElement.setScale(1);
//                    slider.setValue(100);
                    currentElement.setXloc(.5);
                    currentElement.setYloc(.5);
                    currentElement.setShadow(true);
                }
                return pressed;
            }
        });


        buttonList.add(new GuiButton(10, 1, resolution.getScaledHeight() - 80, 80, 20, "Toggle Shadow") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    boolean shadow = currentElement.isShadow();
                    currentElement.setShadow(!shadow
                    );
                }
                return pressed;
            }
        });
        buttonList.add(new GuiButton(11, 1, resolution.getScaledHeight() - 60, 80, 20, "Toggle Highlight") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    boolean highlight = currentElement.isHighlighted();
                    currentElement.setHighlighted(!highlight);
                }
                return pressed;
            }
        });


    }

    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        if (mouseDown && Mouse.isButtonDown(0) && allow) {
            if (currentElement != null) {
                for (GuiButton button : buttonList) {
                    if (button.isMouseOver())
                        return;
                }
//                Dimension dimension = currentElement.getDimensions();
//                double d1 = resolution.getScaledWidth_double() * currentElement.getXloc();
//                double d2 = resolution.getScaledHeight_double() * currentElement.getYloc();
//                int x = Mouse.getX();
//                int y = Mouse.getY();
//                if (x > d1 && x < x + dimension.getWidth() && y < d2 && y > d2 + dimension.getHeight()) {
//                    System.out.println("Inside close box");
//                }
                double xloc = resolution.getScaledWidth_double() * currentElement.getXloc() + (double) (Mouse.getX() - oldX) / (double) resolution.getScaleFactor();
                double yloc = resolution.getScaledHeight_double() * currentElement.getYloc() - (double) (Mouse.getY() - oldY) / (double) resolution.getScaleFactor();
                double newX = xloc / resolution.getScaledWidth_double();
                double newY = yloc / resolution.getScaledHeight_double();
                currentElement.setXloc(newX);
                currentElement.setYloc(newY);
            }
        }
        if (!mouseDown && Mouse.isButtonDown(0)) {
            //Clicked
            allow = true;
            for (GuiButton button : buttonList) {
                if (button.isMouseOver())
                    allow = false;
            }
        }
        mouseDown = Mouse.isButtonDown(0);
        if (mouseDown) {
            oldX = Mouse.getX();
            oldY = Mouse.getY();
        }
    }
}
