package club.sk1er.mods.bedwars;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class ElementRenderer {

    public static double currentScale = 1.0;
    public static int color;
    public static boolean highlighted = false;
    private static int[] COLORS = new int[]{16777215, 16711680, 65280, 255, 16776960, 11141290};
    private static boolean display = false;
    private static List<Long> clicks = new ArrayList<>();
    private static boolean renderedAirThisTick = false;
    boolean last = false;
    private BedwarsResourceDisplay mod;
    private Minecraft minecraft;

    public ElementRenderer(BedwarsResourceDisplay mod) {
        this.mod = mod;
        minecraft = Minecraft.getMinecraft();
    }

    public static void render(List<ItemStack> itemStacks, int x, int y) {
        GL11.glPushMatrix();

        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.zLevel = 200.0F;

        RenderHelper.enableGUIStandardItemLighting();
        if (!renderedAirThisTick) {
            renderItem.renderItemAndEffectIntoGUI(new ItemStack(Blocks.stone, 1), 1, 1);
            renderedAirThisTick = true;
        }

        for (int i = 0; i < itemStacks.size(); i++) {
            ItemStack stack = itemStacks.get(i);

            renderItem.renderItemAndEffectIntoGUI(stack, (int) (x / ElementRenderer.getCurrentScale()), (int) ((y + (16 * i * ElementRenderer.getCurrentScale())) / ElementRenderer.getCurrentScale()));
        }

        GL11.glPopMatrix();

    }

    public static double getCurrentScale() {
        return currentScale;
    }

    public static int getColor(int index) {
        if (index == 6) {
            return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.8f, 0.8f);
        }
        return COLORS[index];
    }

    public static void display() {
        display = true;
    }

    public static void draw(int x, int y, String string, boolean shadow) {
        List<String> tmp = new ArrayList<>();
        tmp.add(string);
        draw(x, y, tmp, shadow);
    }

    public static void draw(int x, int y, List<String> list, boolean shadow) {
// draw
        int tx = x;
        int ty = y;
        for (String string : list) {
//            Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();

            if (highlighted) {
                int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(string);
                Gui.drawRect((int) (tx / getCurrentScale()), (int) (ty / getCurrentScale()), (int) (tx / getCurrentScale()) + stringWidth, (int) (ty / getCurrentScale()) + 8, -1442840576);
            }
            if (color == 6) {
                drawChromaString(string, (int) (tx / getCurrentScale()), (int) (ty / getCurrentScale()), shadow);
            } else
                Minecraft.getMinecraft().fontRendererObj.drawString(string, (int) (tx / getCurrentScale()), (int) (ty / getCurrentScale()), getColor(color), shadow);
            ty += 10 * currentScale;

        }
    }

    public static void drawChromaString(String text, int xIn, int y, boolean shadow) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        int x = xIn;
        for (char c : text.toCharArray()) {
            int i = Color.HSBtoRGB((float) ((System.currentTimeMillis() - (x * 10) - (y * 10)) % 2000) / 2000.0F, 0.8F, 0.8F);
            String tmp = String.valueOf(c);
            renderer.drawString(tmp, x, y, i, shadow);
            x += renderer.getCharWidth(c);
        }
    }

    public static int maxWidth(List<String> list) {
        int max = 0;
        for (String s : list) {
            max = Math.max(max, Minecraft.getMinecraft().fontRendererObj.getStringWidth(s));
        }
        return max;
    }

    public static int getColor() {
        return color;
    }

    public static int getCPS() {
        Iterator<Long> iterator = clicks.iterator();
        while (iterator.hasNext())
            if (System.currentTimeMillis() - iterator.next() > 1000L)
                iterator.remove();
        return clicks.size();
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (display) {
            Minecraft.getMinecraft().displayGuiScreen(mod.getConfigGuiInstance());
            display = false;
            int t = 24;
        }
    }

    @SubscribeEvent
    public void onRenderTick(final TickEvent.RenderTickEvent event) {
        if (!this.minecraft.inGameHasFocus || this.minecraft.gameSettings.showDebugInfo) {
            return;
        }

        if (BedwarsResourceDisplay.isEnabled())
            renderElements();
    }

    private void renderElements() {

// Old render IF BEDWARS
        if (!mod.getSk1erMod().isHypixel() || !mod.getSk1erMod().isEnabled()) {
            return;
        }
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld != null) {
            Scoreboard scoreboard = theWorld.getScoreboard();
            if (scoreboard != null) {
                ScoreObjective objectiveInDisplaySlot = scoreboard.getObjectiveInDisplaySlot(1);
                if (objectiveInDisplaySlot != null) {
                    String name = EnumChatFormatting.getTextWithoutFormattingCodes(objectiveInDisplaySlot.getDisplayName());
                    if (name.equalsIgnoreCase("bed wars")) {
                        //In bedwars of some sort. Go check if it is a lobby via head in slot 2.
                        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
                        if (thePlayer != null) {
                            ItemStack stackInSlot = thePlayer.inventory.getStackInSlot(8);
                            if (stackInSlot != null) {
                                if (stackInSlot.getUnlocalizedName().equalsIgnoreCase("item.netherStar")) {
                                    return;
                                }
                            }
                            renderedAirThisTick = false;
                            try {
                                List<DisplayElement> elementList = mod.getDisplayElements();
                                for (DisplayElement element : elementList) {
                                    if (element.getScale() != 1)
                                        GlStateManager.scale(element.getScale(), element.getScale(), 0);
                                    highlighted = element.isHighlighted();
                                    currentScale = element.getScale();
                                    color = element.getColor();
                                    element.draw();
                                    if (element.getScale() != 1)
                                        GlStateManager.scale(1.0 / element.getScale(), 1.0 / element.getScale(), 0);
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
        }

    }
}
