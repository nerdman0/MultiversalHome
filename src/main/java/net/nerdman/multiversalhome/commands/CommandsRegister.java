package net.nerdman.multiversalhome.commands;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandsRegister {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        CreateHome.register(event.getDispatcher());
        RemoveHome.register(event.getDispatcher());
        ListHomes.register(event.getDispatcher());
        TeleportHome.register(event.getDispatcher());
    }
}
