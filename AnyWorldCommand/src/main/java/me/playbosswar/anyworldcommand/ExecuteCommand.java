package me.playbosswar.anyworldcommand;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class ExecuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String world = args[0];
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);
        StringBuilder command = new StringBuilder();
        for (String commandArg : commandArgs) {
            command.append(commandArg).append(" ");
        }

        // TODO: probably work with a queue

        Block block = Bukkit.getWorld(world).getBlockAt(0, -65, 0);
        block.setType(XMaterial.COMMAND_BLOCK.parseMaterial());
        CommandBlock commandBlock = (CommandBlock) block.getState();
        commandBlock.setCommand(command.toString());
        commandBlock.update();
        Block redstone = Bukkit.getWorld(world).getBlockAt(0, -66, 0);
        redstone.setType(XMaterial.REDSTONE_BLOCK.parseMaterial());
//        block.setType(XMaterial.AIR.parseMaterial());
//        redstone.setType(XMaterial.AIR.parseMaterial());

        return true;
    }
}
