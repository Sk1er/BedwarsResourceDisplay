package club.sk1er.mods.bedwars;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Created by mitchellkatz on 5/31/17.
 */
public class ResolutionUtil {

    public static ScaledResolution current() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }
}
