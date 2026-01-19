package net.nerdman.multiversalhome.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import net.nerdman.multiversalhome.capabilities.PlayerHomesProvider;
import net.nerdman.multiversalhome.Config;

public class TeleportHome {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("teleporthome").requires(commandSourceStack -> commandSourceStack.hasPermission(0))
                        .then(Commands.argument("Name", StringArgumentType.string()).executes(TeleportHome::TeleportToHome))
        );
    }

    private static int TeleportToHome(CommandContext<CommandSourceStack> Argument) {
        String name = StringArgumentType.getString(Argument, "Name");
        ServerPlayer player = Argument.getSource().getPlayer();

        if(player.server.getProfilePermissions(player.getGameProfile()) < 2 && !Config.allowPeasantTeleport){
            player.sendSystemMessage(Component.literal("You don't have permission to use this command"));
            return 0;
        }

        player.getCapability(PlayerHomesProvider.PLAYER_HOMES).ifPresent(homes -> {
            for(int i = 0; i < homes.getHomes().size(); i++){
                if(name.equals(homes.getHomes().get(i).getString("Name"))){
                    String[] noms = homes.getHomes().get(i).getString("Dimension").split(":");
                    ServerLevel serverLevel = player.server.getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(noms[0], noms[1])));
                    int[] intPosition = homes.getHomes().get(i).getIntArray("Pos");
                    player.teleportTo(
                            serverLevel,
                            intPosition[0]+0.5,
                            intPosition[1],
                            intPosition[2]+0.5,
                            player.getYRot(),
                            player.getXRot()
                    );
                    player.sendSystemMessage(Component.literal("Teleported to " + name + " at " + intPosition[0] + ", " + intPosition[1] + ", " + intPosition[2] + " in dimension " + homes.getHomes().get(i).getString("Dimension")));
                    return;
                }
            }
            player.sendSystemMessage(Component.literal("Unable to find find home " + name));
        });

        return 1;
    }
}
