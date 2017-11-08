package club.sk1er.mods.bedwars.displayitems;

/**
 * Created by Mitchell Katz on 5/25/2017.
 */
public enum DisplayItemType {
    //Raw resources
    IRON("Iron", "iron_ingot"),
    GOLD("Gold", "gold_ingot"),
    DIAMOND("Diamond", "diamond"),
    EMERALD("Emerald", "emerald"),
    //BLOCKS
    WOOL("Wool", "wool"),
    HARDENED_CLAY("Hardened Clay", "hardened_clay"),
    GLASS("Blast-Proof Glass", "glass"),
    END_STONE("End Stone", "end_stone"),
    LADDER("Ladder", "ladder"),
    WOOD("Wood", "planks"),
    OBSIDIAN("Obsidian", "obsidian"),
    //Ranged
    ARROW("Arrow", "arrow"),

    //Utility
    GOLDEN_APPLE("Golden Apple", "golden_apple"),
    BED_BUG("Bedbug", "snowball"),
    IRON_GOLEM("Iron Golem", "spawn_egg"),
    FIREBALL("Fireball", "fire_charge"),
    TNT("TNT", "tnt"),
    ENDER_PEARL("Ender Pearl", "ender_pearl"),
    WATER_BUCKET("Water Bucket", "water_bucket"),
    EGG("Bridge Egg", "egg");


    private String name;
    private String itemName;

    DisplayItemType(String name, String itemName) {
        this.name = name;
        this.itemName = itemName;
    }

    public static DisplayItemType getByItem(String source) {
        for (DisplayItemType displayItemType : values()) {
            if (displayItemType.getItemName().equalsIgnoreCase(source))
                return displayItemType;
        }
        throw new IllegalArgumentException(source + " is not a registered item!");
    }

    public String getName() {
        return name;
    }

    public String getItemName() {
        return itemName;
    }
}
