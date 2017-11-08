package club.sk1er.mods.bedwars.displayitems;

import club.sk1er.mods.bedwars.ElementRenderer;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/25/17.
 */
public class ItemDisplayElement implements IDisplayItem {
    private JsonObject data;
    private int ordinal;

    public ItemDisplayElement(JsonObject data, int ordinal) {
        this.data = data;
        this.ordinal = ordinal;
    }

    @Override
    public DisplayItemType getState() {
        return DisplayItemType.getByItem(getItem());
    }

    public String getItem() {
        return data.get("item").getAsString();
    }

    @Override
    public Dimension draw(int starX, int startY, boolean isConfig, boolean shadow) {
        List<ItemStack> list = new ArrayList<>();
        ItemStack seed = new ItemStack(Item.getByNameOrId(data.get("item").getAsString()), 64);
        list.add(seed);
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer != null) {
            int c = 0;
            for (ItemStack is : thePlayer.inventory.mainInventory) {
                if (is != null) {
                    if (is.getUnlocalizedName().equalsIgnoreCase(seed.getUnlocalizedName()))
                        c += is.stackSize;
                }
            }
            if (c == 0 && data.has("hidenone") && data.get("hidenone").getAsBoolean() && !isConfig) {
                return new Dimension(0, 0);
            }
            ElementRenderer.render(list, starX, startY);
            ElementRenderer.draw((int) (starX + 18.0 * ElementRenderer.currentScale), (int) (startY + 7.0 * ElementRenderer.currentScale), "x" + c, shadow);
            return new Dimension(16, 17);
        }
        return new Dimension(0, 0);
    }

    @Override
    public JsonObject getRaw() {
        return data;
    }

    @Override
    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}
