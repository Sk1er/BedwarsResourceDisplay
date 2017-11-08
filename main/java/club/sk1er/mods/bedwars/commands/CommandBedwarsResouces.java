package club.sk1er.mods.bedwars.commands;

import club.sk1er.mods.bedwars.BedwarsResourceDisplay;
import club.sk1er.mods.bedwars.ElementRenderer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

/**
 * Created by Mitchell Katz on 5/29/2017.
 */
public class CommandBedwarsResouces extends CommandBase {
    private BedwarsResourceDisplay mod;

    public CommandBedwarsResouces(BedwarsResourceDisplay mod) {
        this.mod = mod;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "bedwarsresources";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bedwarsresources";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            mod.reloadConfig();
        } else ElementRenderer.display();


    }
}
