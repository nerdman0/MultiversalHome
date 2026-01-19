package net.nerdman.multiversalhome.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import net.nerdman.multiversalhome.capabilities.PlayerHomesProvider;

public class RemoveHome {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("removehome").requires(commandSourceStack -> commandSourceStack.hasPermission(0))
                        .then(Commands.argument("Name", StringArgumentType.string()).executes(RemoveHome::RemoveHomeName))
        );
    }

    private static int RemoveHomeName(CommandContext<CommandSourceStack> Argument) {
        String name = StringArgumentType.getString(Argument, "Name");
        ServerPlayer player = Argument.getSource().getPlayer();

        player.getCapability(PlayerHomesProvider.PLAYER_HOMES).ifPresent(homes -> {
            if(!homes.removeHome(name)){
                player.sendSystemMessage(Component.literal("No homes named " + name));
            } else {
                player.sendSystemMessage(Component.literal("Removed home with name " + name));
            }
        });

        return 1;
    }
}
