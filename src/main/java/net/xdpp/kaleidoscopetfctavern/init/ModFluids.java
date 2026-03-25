package net.xdpp.kaleidoscopetfctavern.init;

import com.github.ysbbbbbb.kaleidoscopetavern.fluid.JuiceFluidType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import static net.xdpp.kaleidoscopetfctavern.Kaleidoscopetfctavern.MODID;
import static net.xdpp.kaleidoscopetfctavern.Kaleidoscopetfctavern.modLoc;

/**
 * 流体注册类
 * <p>
 * 注册所有葡萄汁流体（源流体和流动态流体）
 * 尽可能复用 Kaleidoscope Tavern 已实现的流体类型
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFluids {
    /**
     * 紫葡萄汁源流体ID
     */
    public static final ResourceLocation PURPLE_GRAPE_JUICE_ID = modLoc("purple_grape_juice");
    
    /**
     * 流动态紫葡萄汁流体ID
     */
    public static final ResourceLocation FLOWING_PURPLE_GRAPE_JUICE_ID = modLoc("flowing_purple_grape_juice");

    /**
     * 红葡萄汁源流体ID
     */
    public static final ResourceLocation RED_GRAPE_JUICE_ID = modLoc("red_grape_juice");
    
    /**
     * 流动态红葡萄汁流体ID
     */
    public static final ResourceLocation FLOWING_RED_GRAPE_JUICE_ID = modLoc("flowing_red_grape_juice");

    /**
     * 白葡萄汁源流体ID
     */
    public static final ResourceLocation WHITE_GRAPE_JUICE_ID = modLoc("white_grape_juice");
    
    /**
     * 流动态白葡萄汁流体ID
     */
    public static final ResourceLocation FLOWING_WHITE_GRAPE_JUICE_ID = modLoc("flowing_white_grape_juice");

    /**
     * 绿葡萄汁源流体ID
     */
    public static final ResourceLocation GREEN_GRAPE_JUICE_ID = modLoc("green_grape_juice");
    
    /**
     * 流动态绿葡萄汁流体ID
     */
    public static final ResourceLocation FLOWING_GREEN_GRAPE_JUICE_ID = modLoc("flowing_green_grape_juice");

    /**
     * 紫葡萄汁流体类型注册表对象
     */
    public static final RegistryObject<FluidType> PURPLE_GRAPE_JUICE_TYPE = RegistryObject.create(PURPLE_GRAPE_JUICE_ID, ForgeRegistries.Keys.FLUID_TYPES.location(), MODID);
    
    /**
     * 红葡萄汁流体类型注册表对象
     */
    public static final RegistryObject<FluidType> RED_GRAPE_JUICE_TYPE = RegistryObject.create(RED_GRAPE_JUICE_ID, ForgeRegistries.Keys.FLUID_TYPES.location(), MODID);
    
    /**
     * 白葡萄汁流体类型注册表对象
     */
    public static final RegistryObject<FluidType> WHITE_GRAPE_JUICE_TYPE = RegistryObject.create(WHITE_GRAPE_JUICE_ID, ForgeRegistries.Keys.FLUID_TYPES.location(), MODID);
    
    /**
     * 绿葡萄汁流体类型注册表对象
     */
    public static final RegistryObject<FluidType> GREEN_GRAPE_JUICE_TYPE = RegistryObject.create(GREEN_GRAPE_JUICE_ID, ForgeRegistries.Keys.FLUID_TYPES.location(), MODID);

    /**
     * 紫葡萄汁源流体注册表对象
     */
    public static final RegistryObject<Fluid> PURPLE_GRAPE_JUICE = RegistryObject.create(PURPLE_GRAPE_JUICE_ID, ForgeRegistries.FLUIDS);
    
    /**
     * 流动态紫葡萄汁流体注册表对象
     */
    public static final RegistryObject<Fluid> FLOWING_PURPLE_GRAPE_JUICE = RegistryObject.create(FLOWING_PURPLE_GRAPE_JUICE_ID, ForgeRegistries.FLUIDS);

    /**
     * 红葡萄汁源流体注册表对象
     */
    public static final RegistryObject<Fluid> RED_GRAPE_JUICE = RegistryObject.create(RED_GRAPE_JUICE_ID, ForgeRegistries.FLUIDS);
    
    /**
     * 流动态红葡萄汁流体注册表对象
     */
    public static final RegistryObject<Fluid> FLOWING_RED_GRAPE_JUICE = RegistryObject.create(FLOWING_RED_GRAPE_JUICE_ID, ForgeRegistries.FLUIDS);

    /**
     * 白葡萄汁源流体注册表对象
     */
    public static final RegistryObject<Fluid> WHITE_GRAPE_JUICE = RegistryObject.create(WHITE_GRAPE_JUICE_ID, ForgeRegistries.FLUIDS);
    
    /**
     * 流动态白葡萄汁流体注册表对象
     */
    public static final RegistryObject<Fluid> FLOWING_WHITE_GRAPE_JUICE = RegistryObject.create(FLOWING_WHITE_GRAPE_JUICE_ID, ForgeRegistries.FLUIDS);

    /**
     * 绿葡萄汁源流体注册表对象
     */
    public static final RegistryObject<Fluid> GREEN_GRAPE_JUICE = RegistryObject.create(GREEN_GRAPE_JUICE_ID, ForgeRegistries.FLUIDS);
    
    /**
     * 流动态绿葡萄汁流体注册表对象
     */
    public static final RegistryObject<Fluid> FLOWING_GREEN_GRAPE_JUICE = RegistryObject.create(FLOWING_GREEN_GRAPE_JUICE_ID, ForgeRegistries.FLUIDS);

    /**
     * 注册所有流体
     * <p>
     * 在模组加载时执行，注册流体类型和流体
     * 
     * @param event 注册事件
     */
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.FLUID_TYPES, helper -> {
            helper.register(PURPLE_GRAPE_JUICE_ID, new JuiceFluidType(PURPLE_GRAPE_JUICE_ID, 0));
            helper.register(RED_GRAPE_JUICE_ID, new JuiceFluidType(RED_GRAPE_JUICE_ID, 0));
            helper.register(WHITE_GRAPE_JUICE_ID, new JuiceFluidType(WHITE_GRAPE_JUICE_ID, 0));
            helper.register(GREEN_GRAPE_JUICE_ID, new JuiceFluidType(GREEN_GRAPE_JUICE_ID, 0));
        });

        event.register(ForgeRegistries.Keys.FLUIDS, helper -> {
            ForgeFlowingFluid.Properties purpleGrapeJuice = new ForgeFlowingFluid.Properties(PURPLE_GRAPE_JUICE_TYPE, PURPLE_GRAPE_JUICE, FLOWING_PURPLE_GRAPE_JUICE).bucket(ModItems.PURPLE_GRAPE_BUCKET);
            ForgeFlowingFluid.Properties redGrapeJuice = new ForgeFlowingFluid.Properties(RED_GRAPE_JUICE_TYPE, RED_GRAPE_JUICE, FLOWING_RED_GRAPE_JUICE).bucket(ModItems.RED_GRAPE_BUCKET);
            ForgeFlowingFluid.Properties whiteGrapeJuice = new ForgeFlowingFluid.Properties(WHITE_GRAPE_JUICE_TYPE, WHITE_GRAPE_JUICE, FLOWING_WHITE_GRAPE_JUICE).bucket(ModItems.WHITE_GRAPE_BUCKET);
            ForgeFlowingFluid.Properties greenGrapeJuice = new ForgeFlowingFluid.Properties(GREEN_GRAPE_JUICE_TYPE, GREEN_GRAPE_JUICE, FLOWING_GREEN_GRAPE_JUICE).bucket(ModItems.GREEN_GRAPE_BUCKET);

            helper.register(PURPLE_GRAPE_JUICE_ID, new ForgeFlowingFluid.Source(purpleGrapeJuice));
            helper.register(FLOWING_PURPLE_GRAPE_JUICE_ID, new ForgeFlowingFluid.Flowing(purpleGrapeJuice));

            helper.register(RED_GRAPE_JUICE_ID, new ForgeFlowingFluid.Source(redGrapeJuice));
            helper.register(FLOWING_RED_GRAPE_JUICE_ID, new ForgeFlowingFluid.Flowing(redGrapeJuice));

            helper.register(WHITE_GRAPE_JUICE_ID, new ForgeFlowingFluid.Source(whiteGrapeJuice));
            helper.register(FLOWING_WHITE_GRAPE_JUICE_ID, new ForgeFlowingFluid.Flowing(whiteGrapeJuice));

            helper.register(GREEN_GRAPE_JUICE_ID, new ForgeFlowingFluid.Source(greenGrapeJuice));
            helper.register(FLOWING_GREEN_GRAPE_JUICE_ID, new ForgeFlowingFluid.Flowing(greenGrapeJuice));
        });
    }
}
