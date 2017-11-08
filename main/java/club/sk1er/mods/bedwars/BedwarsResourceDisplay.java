package club.sk1er.mods.bedwars;

import club.sk1er.mods.bedwars.commands.CommandBedwarsResouces;
import club.sk1er.mods.bedwars.gui.ConfigGui;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.List;

@Mod(modid = BedwarsResourceDisplay.MODID, version = BedwarsResourceDisplay.VERSION)
public class BedwarsResourceDisplay {
    public static final String MODID = "sk1er-bedwars_resource_display";
    public static final String VERSION = "1.0";
    private static boolean enabled = true;
    private BedwarsConfig config;
    private File configFile;
    private Sk1erMod sk1erMod;

    public static boolean isEnabled() {
        return enabled;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public Sk1erMod getSk1erMod() {
        return sk1erMod;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.configFile = event.getSuggestedConfigurationFile();
        sk1erMod = new Sk1erMod(MODID, VERSION, "Bedwars Resource Display");
        sk1erMod.checkStatus();
        reloadConfig();
        ClientCommandHandler.instance.registerCommand(new CommandBedwarsResouces(this));
        MinecraftForge.EVENT_BUS.register(new ElementRenderer(this));
    }


    public List<DisplayElement> getDisplayElements() {
        return config.getElements();
    }

    public void reloadConfig() {
        config = new BedwarsConfig(configFile);
    }

    public ConfigGui getConfigGuiInstance() {
        return new ConfigGui(this);

    }

    public void saveConfig() {
        config.saveConfig();
    }
}
