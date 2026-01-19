package net.nerdman.multiversalhome.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerPlayer;

import net.nerdman.multiversalhome.Config;
import net.nerdman.multiversalhome.capabilities.PlayerHomesProvider;

public class ListHomes {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("listhomes").requires(commandSourceStack -> commandSourceStack.hasPermission(0))
                        .executes(ListHomes::ListHome)//.then(Commands.argument("Name", StringArgumentType.string()).executes(ListHomes::ListHome))
        );
    }

    private static int ListHome(CommandContext<CommandSourceStack> Argument) {
        ServerPlayer player = Argument.getSource().getPlayer();

        player.getCapability(PlayerHomesProvider.PLAYER_HOMES).ifPresent(homes -> {
            if(homes.getHomes().size() == 0) {
                player.sendSystemMessage(Component.literal("No homes to list"));
                return;
            }

            player.sendSystemMessage(Component.literal("Homes:").withStyle(ChatFormatting.RED));
            for(int i = 0; i < homes.getHomes().size(); i++){
                if(i != 0){
                    player.sendSystemMessage(Component.literal(""));
                }
                CompoundTag home = homes.getHomes().get(i);
                String name = home.getString("Name");
                String dimension = home.getString("Dimension");
                int[] position = home.getIntArray("Pos");
                String positionString = position[0] + ", " + position[1] + ", " + position[2];
                player.sendSystemMessage(
                        Component.literal("   Name: §d§l" + name)
                );
                player.sendSystemMessage(
                        Component.literal("   Position: " + positionString)
                                .withStyle(ChatFormatting.GRAY)
                );
                player.sendSystemMessage(
                        Component.literal("   Dimension: " + dimension)
                                .withStyle(ChatFormatting.GRAY)
                );
                if(player.server.getProfilePermissions(player.getGameProfile()) >= 2 || Config.allowPeasantTeleport){
                    player.sendSystemMessage(
                            Component.literal("   [§bTeleport§r]")
                                    .withStyle(s -> s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teleporthome " + name)))
                                    .withStyle(s -> s.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Teleport to this home"))))
                    );
                }
                player.sendSystemMessage(
                        Component.literal("   [§eRemove§r]")
                                .withStyle(s -> s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/removehome " + name)))
                                .withStyle(s -> s.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Removes this home"))))
                );
            }
        });

        return 1;
    }
}
