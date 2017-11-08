package club.sk1er.mods.bedwars.displayitems;

import com.google.gson.JsonObject;

import java.awt.*;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public interface IDisplayItem {


    static IDisplayItem parse(DisplayItemType type, int ord, JsonObject item) {
        return new ItemDisplayElement(item, ord);
    }

    DisplayItemType getState();

    Dimension draw(int starX, int startY, boolean isConfig, boolean shadow);

    JsonObject getRaw();

    int getOrdinal();

    void setOrdinal(int ordinal);
}


