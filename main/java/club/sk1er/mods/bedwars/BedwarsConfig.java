package club.sk1er.mods.bedwars;

import club.sk1er.mods.bedwars.displayitems.DisplayItemType;
import club.sk1er.mods.bedwars.displayitems.IDisplayItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public class BedwarsConfig {

    private boolean enabled = true;
    private File configFile;
    private List<DisplayElement> elements;

    public BedwarsConfig(File configFile) {
        this.configFile = configFile;
        elements = new ArrayList<>();
        reloadConfig(false);
    }

    public void reloadConfig(boolean save) {
        if (save) {
            saveConfig();
            elements.clear();
        }
        try {
            if (!configFile.exists())
                packConfig();
            FileReader fr = new FileReader(configFile);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            String l;
            while ((l = br.readLine()) != null) {
                builder.append(l);
            }
            JsonObject config = new JsonParser().parse(builder.toString()).getAsJsonObject();
            System.out.println("Config: " + config);
            this.enabled = config.has("enabled") && config.get("enabled").getAsBoolean();
            JsonArray displayElements = config.has("elements") ? config.getAsJsonArray("elements").getAsJsonArray() : new JsonArray();
            for (int i = 0; i < displayElements.size(); i++) {
                List<IDisplayItem> items = new ArrayList<>();
                JsonObject object = displayElements.get(i).getAsJsonObject();
                System.out.println("Object " + i + ": " + object + " ");
                try {
                    int ord = 0;
                    JsonArray itemss = object.has("items") ? object.getAsJsonArray("items").getAsJsonArray() : new JsonArray();
                    for (int i1 = 0; i1 < itemss.size(); i1++) {
                        JsonObject item = itemss.get(i1).getAsJsonObject();
                        System.out.println("Item: " + item);
                        DisplayItemType type = DisplayItemType.valueOf(item.get("type").getAsString());
                        items.add(IDisplayItem.parse(type, ord, item));
                        ord++;
                    }
                    DisplayElement element = new DisplayElement(object.get("x").getAsDouble(), object.get("y").getAsDouble(), object.get("scale").getAsDouble(), object.get("color").getAsInt(), items, object.has("shadow") && object.get("shadow").getAsBoolean(), object.has("highlighted") && object.get("highlighted").getAsBoolean());
                    elements.add(element);
                    System.out.println("Adding element:  " + element);
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.getLogger("Tayber 50k").severe("A fatal error occurred while loading the display element " + object);
                }
            }


        } catch (IOException e) {

        }
    }

    private void packConfig() {
        JsonObject master = new JsonObject();
        master.addProperty("enabled", true);
        JsonArray elements = new JsonArray();
        JsonObject theOnlyElement = new JsonObject();
        JsonArray itemArray = new JsonArray();
        JsonObject cordsThing = new JsonObject();
        cordsThing.addProperty("type", DisplayItemType.IRON.name());
        cordsThing.addProperty("item", DisplayItemType.IRON.getItemName());

        cordsThing.addProperty("state", 0);
        itemArray.add(cordsThing);
        theOnlyElement.add("items", itemArray);
        theOnlyElement.addProperty("color", 6);
        theOnlyElement.addProperty("x", .1);
        theOnlyElement.addProperty("y", .2);
        theOnlyElement.addProperty("scale", 1.1);
        elements.add(theOnlyElement);
        master.add("elements", elements);
        saveFile(master);
    }

    private void saveFile(JsonObject cont) {
        try {
            FileWriter fw = new FileWriter(configFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cont.toString());
            bw.close();
            fw.close();
        } catch (IOException e) {

        }
    }

    public void saveConfig() {
        JsonObject master = new JsonObject();
        master.addProperty("enabled", enabled);
        JsonArray elementArray = new JsonArray();
        master.add("elements", elementArray);
        for (DisplayElement element : elements) {
            JsonObject tmp = new JsonObject();
            tmp.addProperty("color", element.getColor());
            tmp.addProperty("x", element.getXloc());
            tmp.addProperty("y", element.getYloc());
            tmp.addProperty("scale", element.getScale());
            tmp.addProperty("shadow", element.isShadow());
            tmp.addProperty("highlighted", element.isHighlighted());
            JsonArray items = new JsonArray();
            for (IDisplayItem iDisplayItem : element.getDisplayItems()) {
                JsonObject raw = iDisplayItem.getRaw();
                raw.addProperty("type", iDisplayItem.getState().name());
                items.add(raw);
            }
            tmp.add("items", items);
            elementArray.add(tmp);
        }
        saveFile(master);
    }

    public List<DisplayElement> getElements() {
        return elements;
    }
}
