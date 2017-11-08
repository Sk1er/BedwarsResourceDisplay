package club.sk1er.mods.bedwars;

import club.sk1er.mods.bedwars.displayitems.IDisplayItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class DisplayElement {
    private double xloc, yloc;
    private List<IDisplayItem> displayItems = new ArrayList<>();
    private double scale = 1;
    private int color;
    private double prevX, prevY;
    private boolean shadow;
    private boolean highlighted;

    public DisplayElement(double xloc, double yloc, double scale, int color, List<IDisplayItem> items, boolean shadow, boolean highlighted) {
        this.xloc = xloc;
        this.yloc = yloc;
        this.scale = scale;
        this.color = color;
        this.displayItems = items;
        this.shadow = shadow;
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public String toString() {
        return "DisplayElement{" +
                "xloc=" + xloc +
                ", yloc=" + yloc +
                ", displayItems=" + displayItems +
                ", scale=" + scale +
                ", color=" + color +
                '}';
    }

    public void draw() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) (xloc * resolution.getScaledWidth_double());
        int y = (int) (yloc * resolution.getScaledHeight_double());
        for (IDisplayItem iDisplayItem : displayItems) {
            Dimension d = iDisplayItem.draw(x, y, false, shadow);
            y += d.getHeight() * scale;
        }
//        System.out.println("Prev X:" + prevX + " Prev Y: " + prevY);


    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    private void render(double x, double y, String string) {
        GL11.glScaled(scale, scale, 0);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(string, (float) ((float) x / scale), (float) y, ElementRenderer.getColor(color));
        GL11.glScaled(1.0 / scale, 1.0 / scale, 0);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getXloc() {
        return xloc;
    }

    public void setXloc(double xloc) {
        this.xloc = xloc;
    }

    public void removeDisplayItem(int ordinal) {
        displayItems.remove(ordinal);
        adjustOrdinal();
    }

    public double getYloc() {
        return yloc;
    }

    public void setYloc(double yloc) {
        this.yloc = yloc;
    }

    public List<IDisplayItem> getDisplayItems() {
        return displayItems;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public Dimension getDimensions() {
        return new Dimension((int) prevX, (int) prevY);
    }

    public void drawForConfig() {

        this.prevX = 0;
        this.prevY = 0;
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int addy = 0;
        int x = (int) (xloc * resolution.getScaledWidth_double());
        int y = (int) (yloc * resolution.getScaledHeight_double());
        for (IDisplayItem iDisplayItem : displayItems) {
            Dimension d = iDisplayItem.draw(x, y, true, shadow);
            y += d.getHeight() * scale;
            addy += d.getHeight() * scale;
            prevX = (int) Math.max(((double) d.getWidth()) * scale, prevX);
        }
        this.prevY = addy;
//        System.out.println("Prev X:" + prevX + " Prev Y: " + prevY);

    }

    public void renderEditView() {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int x = (int) (.8 * resolution.getScaledWidth_double());
        int y = (int) (.2 * resolution.getScaledHeight_double());
        for (IDisplayItem iDisplayItem : displayItems) {
            Dimension d = iDisplayItem.draw(x, y, false, shadow);
            y += d.getHeight();
        }

    }

    public void adjustOrdinal() {
        for (int ord = 0; ord < displayItems.size(); ord++) {
            displayItems.get(ord).setOrdinal(ord);
        }
    }
}
