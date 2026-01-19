package net.nerdman.multiversalhome.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.nerdman.multiversalhome.capabilities.PlayerHomesProvider;

public class CreateHome {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("createhome").requires(commandSourceStack -> commandSourceStack.hasPermission(0))
                        .then(Commands.argument("Name", StringArgumentType.string()).executes(CreateHome::CreateHomeHere))
        );
    }

    private static int CreateHomeHere(CommandContext<CommandSourceStack> Argument) {
        String name = StringArgumentType.getString(Argument, "Name");
        ServerPlayer player = Argument.getSource().getPlayer();
        ResourceLocation currentDimension = player.level().dimension().location();
        BlockPos position = player.blockPosition();

        player.getCapability(PlayerHomesProvider.PLAYER_HOMES).ifPresent(homes -> {
            int response = homes.addHome(name, player.blockPosition(), currentDimension.toString());
            switch (response){
                case 1:
                    player.sendSystemMessage(Component.literal("Creating home \"" + name + "\" at position " + position.toShortString() + " in dimension " + currentDimension.toString()));
                    break;
                case 2:
                    player.sendSystemMessage(Component.literal("Moving home \"" + name + "\" to position " + position.toShortString() + " in dimension " + currentDimension.toString()));
                    break;
                case 3:
                    player.sendSystemMessage(Component.literal("Renaming home at position " + position.toShortString() + " in dimension " + currentDimension.toString() + " to \"" + name + "\""));
                    break;
                default:
                    player.sendSystemMessage(Component.literal("Something went wrong creating home"));
                    break;
            }
        });

        return 1;
    }
}
