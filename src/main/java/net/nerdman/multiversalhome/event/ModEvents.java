package net.nerdman.multiversalhome.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nerdman.multiversalhome.MultiversalHome;
import net.nerdman.multiversalhome.capabilities.PlayerHomes;
import net.nerdman.multiversalhome.capabilities.PlayerHomesProvider;

@Mod.EventBusSubscriber(modid = MultiversalHome.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerHomesProvider.PLAYER_HOMES).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(MultiversalHome.MODID, "properties"), new PlayerHomesProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(PlayerHomesProvider.PLAYER_HOMES).ifPresent(oldStore -> {
            event.getEntity().getCapability(PlayerHomesProvider.PLAYER_HOMES).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerHomes.class);
    }
}