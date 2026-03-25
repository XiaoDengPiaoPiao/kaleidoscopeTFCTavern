package net.xdpp.kaleidoscopetfctavern.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber
public class KTConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue GRAPE_CROP_GROWTH_CHANCE;
    public static final ForgeConfigSpec.DoubleValue GRAPEVINE_GROWTH_CHANCE;

    static {
        BUILDER.push("growth");

        GRAPE_CROP_GROWTH_CHANCE = BUILDER
                .comment("葡萄作物每随机刻生长的概率 (0.0 - 1.0)")
                .defineInRange("grapeCropGrowthChance", 0.25, 0.0, 1.0);

        GRAPEVINE_GROWTH_CHANCE = BUILDER
                .comment("葡萄藤每随机刻生长的概率 (0.0 - 1.0)")
                .defineInRange("grapevineGrowthChance", 0.25, 0.0, 1.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
    }
}
