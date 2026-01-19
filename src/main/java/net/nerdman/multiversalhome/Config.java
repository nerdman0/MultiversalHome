package net.nerdman.multiversalhome;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = MultiversalHome.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ALLOW_PEASANT_TELEPORT = BUILDER
            .comment("Whether players with operator permission level below 2 are able to use the teleporthome command")
            .define("allowPeasantTeleport", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean allowPeasantTeleport;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        allowPeasantTeleport = ALLOW_PEASANT_TELEPORT.get();
    }
}
