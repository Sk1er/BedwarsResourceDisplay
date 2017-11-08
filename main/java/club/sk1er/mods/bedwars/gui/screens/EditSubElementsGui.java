package club.sk1er.mods.bedwars.gui.screens;

import club.sk1er.mods.bedwars.BedwarsResourceDisplay;
import club.sk1er.mods.bedwars.DisplayElement;
import club.sk1er.mods.bedwars.ElementRenderer;
import club.sk1er.mods.bedwars.ResolutionUtil;
import club.sk1er.mods.bedwars.displayitems.IDisplayItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Collections;

/**
 * Created by mitchellkatz on 5/31/17.
 */
public class EditSubElementsGui extends GuiScreen {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    IDisplayItem current = null;
    private DisplayElement element;
    private BedwarsResourceDisplay mod;
    private boolean addingElement = false;

    public EditSubElementsGui(DisplayElement element, BedwarsResourceDisplay mod) {
        this.element = element;
        this.mod = mod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EditSubElementsGui that = (EditSubElementsGui) o;

        if (addingElement != that.addingElement) return false;
        if (current != null ? !current.equals(that.current) : that.current != null) return false;
        if (element != null ? !element.equals(that.element) : that.element != null) return false;
        if (mod != null ? !mod.equals(that.mod) : that.mod != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = current != null ? current.hashCode() : 0;
        result = 31 * result + (element != null ? element.hashCode() : 0);
        result = 31 * result + (mod != null ? mod.hashCode() : 0);
        result = 31 * result + (addingElement ? 1 : 0);
        return result;
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
        buttonList.add(new GuiButton(1, resolution.getScaledWidth() - resolution.getScaledWidth() / 10, resolution.getScaledHeight() - 20, resolution.getScaledWidth() / 10, 20, "Back") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    if (element.getDisplayItems().isEmpty()) {
                        mod.getDisplayElements().remove(element);
                        Minecraft.getMinecraft().displayGuiScreen(mod.getConfigGuiInstance());
                    } else Minecraft.getMinecraft().displayGuiScreen(new EditElementScreen(mod, element));
                }
                return pressed;
            }
        });

        buttonList.add(new GuiButton(2, 40, resolution.getScaledHeight() - 60, resolution.getScaledWidth() / 10, 20, "Delete") {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY) {
                super.enabled = current != null;
                super.drawButton(mc, mouseX, mouseY);
            }

            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    if (current != null) {
                        element.removeDisplayItem(current.getOrdinal());
                        current = null;
                    }
                }
                return pressed;
            }
        });


        buttonList.add(new GuiButton(9, resolution.getScaledWidth() / 2 + 10, 50, resolution.getScaledWidth() / 10, 20, "Move Up") {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY) {
                super.enabled = current != null && current.getOrdinal() != 0;
                super.drawButton(mc, mouseX, mouseY);
            }

            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    if (current != null && current.getOrdinal() != 0) {
                        Collections.swap(element.getDisplayItems(), current.getOrdinal(), current.getOrdinal() - 1);
                        element.adjustOrdinal();
                    }
                }
                return pressed;
            }
        });

        buttonList.add(new GuiButton(9, 5, 5, Minecraft.getMinecraft().fontRendererObj.getStringWidth("Delete element") + 10, 20, "Delete element") {
            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    mod.getDisplayElements().remove(element);
                    Minecraft.getMinecraft().displayGuiScreen(mod.getConfigGuiInstance());

                }
                return pressed;
            }
        });

        buttonList.add(new GuiButton(10, resolution.getScaledWidth() / 2 + 10, 72, resolution.getScaledWidth() / 10, 20, "Move Down") {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY) {
                super.enabled = current != null && current.getOrdinal() != element.getDisplayItems().size() - 1;
                super.drawButton(mc, mouseX, mouseY);
            }

            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    if (current != null && current.getOrdinal() != element.getDisplayItems().size() - 1) {
                        Collections.swap(element.getDisplayItems(), current.getOrdinal(), current.getOrdinal() + 1);
                        element.adjustOrdinal();
                    }
                }
                return pressed;
            }
        });


        int width = Math.max(Minecraft.getMinecraft().fontRendererObj.getStringWidth("New Coords Display") + 10, resolution.getScaledWidth() / 9);
//
//
        buttonList.add(new GuiButton(3, 40 + width, resolution.getScaledHeight() - 60, width, 20, "Add item") {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY) {
                enabled = element.getDisplayItems().size() < 8;
                super.drawButton(mc, mouseX, mouseY);
            }

            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    Minecraft.getMinecraft().displayGuiScreen(new AddItemScreen(element, mod));
                }
                return pressed;
            }
        });
        buttonList.add(new GuiButton(4, resolution.getScaledWidth() / 2 + 10, 95, width, 20, "Hide with no items") {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY) {
                enabled = current != null;
                if (!enabled) {
                    super.displayString = "Hide with no items ";
                } else {
                    boolean b1 = current.getRaw().has("hidenone") && current.getRaw().get("hidenone").getAsBoolean();
                    if (b1)
                        displayString = "Hide with no items";
                    else displayString = "Show when no items";
                }
                super.drawButton(mc, mouseX, mouseY);
            }

            @Override
            public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                boolean pressed = super.mousePressed(mc, mouseX, mouseY);
                if (pressed) {
                    current.getRaw().addProperty("hidenone", !current.getRaw().has("hidenone") || !current.getRaw().get("hidenone").getAsBoolean());
                }
                return pressed;
            }
        });


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution resolution = ResolutionUtil.current();
        drawHorizontalLine(40, resolution.getScaledWidth() / 2, 40, ElementRenderer.getColor(6));
        drawHorizontalLine(40, resolution.getScaledWidth() / 2, resolution.getScaledHeight() - 70, ElementRenderer.getColor(6));
        drawVerticalLine(40, 40, resolution.getScaledHeight() - 70, ElementRenderer.getColor(6));
        drawVerticalLine(resolution.getScaledWidth() / 2, 40, resolution.getScaledHeight() - 70, ElementRenderer.getColor(6));

        int line = 0;
        int startX = 50;
        int startY = 50;

        for (IDisplayItem iDisplayItem : element.getDisplayItems()) {
            int twith = resolution.getScaledWidth() / 2 - 60;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean hovered = mouseX >= startX && mouseX < startX + twith && mouseY > startY + line * 30 && mouseY < startY + line * 30 + 15;
            if (iDisplayItem.equals(current)) {
                hovered = true;
            }
            int i = hovered ? 2 : 1;

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawTexturedModalRect(startX, startY + line * 30, 0, 46 + i * 20, twith / 2, 20);
            this.drawTexturedModalRect(startX + twith / 2, startY + line * 30, 200 - twith / 2, 46 + i * 20, twith / 2, 20);
            drawCenteredString(Minecraft.getMinecraft().fontRendererObj, iDisplayItem.getState().getName(), startX + twith / 2, startY + line * 30 + 5, ElementRenderer.getColor(1));
            line++;
        }

        ElementRenderer.currentScale = 1.0;
        ElementRenderer.highlighted = element.isHighlighted();
        ElementRenderer.color = element.getColor();
        element.renderEditView();

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int line = 0;
        int startX = 50;
        int startY = 50;
        boolean found = false;
        ScaledResolution resolution = ResolutionUtil.current();
        for (GuiButton button : buttonList)
            if (button.isMouseOver())
                return;


        for (IDisplayItem iDisplayItem : element.getDisplayItems()) {
            int twith = resolution.getScaledWidth() / 2 - 60;
            boolean hovered = mouseX >= startX && mouseX < startX + twith && mouseY > startY + line * 30 && mouseY < startY + line * 30 + 15;
            System.out.println("mouseX = " + mouseX);

            if (hovered) {
                current = iDisplayItem;
                System.out.println("Current: " + iDisplayItem);
                found = true;
            }
            line++;
        }
        if (!found)
            current = null;


    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

    }
}
