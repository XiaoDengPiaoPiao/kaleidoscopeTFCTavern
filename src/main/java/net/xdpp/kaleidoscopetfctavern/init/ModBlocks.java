package net.xdpp.kaleidoscopetfctavern.init;

import com.github.ysbbbbbb.kaleidoscopetavern.block.brew.DrinkBlock;
import net.xdpp.kaleidoscopetfctavern.Kaleidoscopetfctavern;
import net.xdpp.kaleidoscopetfctavern.block.plant.BaseWildGrapevineBlock;
import net.xdpp.kaleidoscopetfctavern.block.plant.BaseWildGrapevinePlantBlock;
import net.xdpp.kaleidoscopetfctavern.block.plant.GrapeCropBlockPurple;
import net.xdpp.kaleidoscopetfctavern.block.plant.GrapeCropBlockRed;
import net.xdpp.kaleidoscopetfctavern.block.plant.GrapeCropBlockWhite;
import net.xdpp.kaleidoscopetfctavern.block.plant.GrapeCropBlockGreen;
import net.xdpp.kaleidoscopetfctavern.block.plant.TFCGrapevineTrellisBlock;
import net.xdpp.kaleidoscopetfctavern.block.plant.WildGrapevineType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 方块注册类
 * <p>
 * 注册所有野生葡萄藤相关的方块
 * 使用 Holder 模式解决头部方块和主体方块之间的循环依赖问题
 */
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Kaleidoscopetfctavern.MODID);

    /**
     * 方块持有者
     * <p>
     * 用于解决头部方块和主体方块之间的循环依赖
     * 通过 Supplier 延迟获取方块实例
     */
    private static class BlockHolder {
        RegistryObject<Block> head;
        RegistryObject<Block> body;

        Block getHead() {
            return head.get();
        }

        Block getBody() {
            return body.get();
        }
    }

    public static final RegistryObject<Block> WILD_GRAPEVINE_PURPLE;
    public static final RegistryObject<Block> WILD_GRAPEVINE_PLANT_PURPLE;

    public static final RegistryObject<Block> WILD_GRAPEVINE_RED;
    public static final RegistryObject<Block> WILD_GRAPEVINE_PLANT_RED;

    public static final RegistryObject<Block> WILD_GRAPEVINE_WHITE;
    public static final RegistryObject<Block> WILD_GRAPEVINE_PLANT_WHITE;

    public static final RegistryObject<Block> WILD_GRAPEVINE_GREEN;
    public static final RegistryObject<Block> WILD_GRAPEVINE_PLANT_GREEN;

    public static final RegistryObject<Block> GRAPE_CROP_PURPLE;
    public static final RegistryObject<Block> GRAPE_CROP_RED;
    public static final RegistryObject<Block> GRAPE_CROP_WHITE;
    public static final RegistryObject<Block> GRAPE_CROP_GREEN;
    public static final RegistryObject<Block> TFC_GRAPEVINE_TRELLIS;

    public static final RegistryObject<LiquidBlock> PURPLE_GRAPE_JUICE_BLOCK;
    public static final RegistryObject<LiquidBlock> RED_GRAPE_JUICE_BLOCK;
    public static final RegistryObject<LiquidBlock> WHITE_GRAPE_JUICE_BLOCK;
    public static final RegistryObject<LiquidBlock> GREEN_GRAPE_JUICE_BLOCK;

    public static final RegistryObject<Block> PURPLE_WINE;
    public static final RegistryObject<Block> RED_WINE;
    public static final RegistryObject<Block> WHITE_WINE;
    public static final RegistryObject<Block> ICE_PURPLE_WINE;
    public static final RegistryObject<Block> ICE_RED_WINE;
    public static final RegistryObject<Block> ICE_WHITE_WINE;
    public static final RegistryObject<Block> FLOWER_PURPLE_WINE;
    public static final RegistryObject<Block> FLOWER_RED_WINE;
    public static final RegistryObject<Block> FLOWER_WHITE_WINE;
    public static final RegistryObject<Block> WHISKEY;
    public static final RegistryObject<Block> CORN_WHISKEY;
    public static final RegistryObject<Block> RYE_WHISKEY;
    public static final RegistryObject<Block> BEER;
    public static final RegistryObject<Block> CIDER;
    public static final RegistryObject<Block> RUM;
    public static final RegistryObject<Block> SAKE;
    public static final RegistryObject<Block> VODKA;

    static {
        BlockHolder holderPurple = new BlockHolder();
        WILD_GRAPEVINE_PURPLE = BLOCKS.register("wild_grapevine_purple",
                () -> new BaseWildGrapevineBlock(holderPurple::getBody));
        WILD_GRAPEVINE_PLANT_PURPLE = BLOCKS.register("wild_grapevine_plant_purple",
                () -> new BaseWildGrapevinePlantBlock(holderPurple::getHead));
        holderPurple.head = WILD_GRAPEVINE_PURPLE;
        holderPurple.body = WILD_GRAPEVINE_PLANT_PURPLE;

        BlockHolder holderRed = new BlockHolder();
        WILD_GRAPEVINE_RED = BLOCKS.register("wild_grapevine_red",
                () -> new BaseWildGrapevineBlock(holderRed::getBody));
        WILD_GRAPEVINE_PLANT_RED = BLOCKS.register("wild_grapevine_plant_red",
                () -> new BaseWildGrapevinePlantBlock(holderRed::getHead));
        holderRed.head = WILD_GRAPEVINE_RED;
        holderRed.body = WILD_GRAPEVINE_PLANT_RED;

        BlockHolder holderWhite = new BlockHolder();
        WILD_GRAPEVINE_WHITE = BLOCKS.register("wild_grapevine_white",
                () -> new BaseWildGrapevineBlock(holderWhite::getBody));
        WILD_GRAPEVINE_PLANT_WHITE = BLOCKS.register("wild_grapevine_plant_white",
                () -> new BaseWildGrapevinePlantBlock(holderWhite::getHead));
        holderWhite.head = WILD_GRAPEVINE_WHITE;
        holderWhite.body = WILD_GRAPEVINE_PLANT_WHITE;

        BlockHolder holderGreen = new BlockHolder();
        WILD_GRAPEVINE_GREEN = BLOCKS.register("wild_grapevine_green",
                () -> new BaseWildGrapevineBlock(holderGreen::getBody));
        WILD_GRAPEVINE_PLANT_GREEN = BLOCKS.register("wild_grapevine_plant_green",
                () -> new BaseWildGrapevinePlantBlock(holderGreen::getHead));
        holderGreen.head = WILD_GRAPEVINE_GREEN;
        holderGreen.body = WILD_GRAPEVINE_PLANT_GREEN;

        GRAPE_CROP_PURPLE = BLOCKS.register("grape_crop_purple", GrapeCropBlockPurple::new);
        GRAPE_CROP_RED = BLOCKS.register("grape_crop_red", GrapeCropBlockRed::new);
        GRAPE_CROP_WHITE = BLOCKS.register("grape_crop_white", GrapeCropBlockWhite::new);
        GRAPE_CROP_GREEN = BLOCKS.register("grape_crop_green", GrapeCropBlockGreen::new);
        TFC_GRAPEVINE_TRELLIS = BLOCKS.register("tfc_grapevine_trellis", TFCGrapevineTrellisBlock::new);

        PURPLE_GRAPE_JUICE_BLOCK = BLOCKS.register("purple_grape_juice", () -> new LiquidBlock(() -> (net.minecraft.world.level.material.FlowingFluid) ModFluids.PURPLE_GRAPE_JUICE.get(), BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
        RED_GRAPE_JUICE_BLOCK = BLOCKS.register("red_grape_juice", () -> new LiquidBlock(() -> (net.minecraft.world.level.material.FlowingFluid) ModFluids.RED_GRAPE_JUICE.get(), BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
        WHITE_GRAPE_JUICE_BLOCK = BLOCKS.register("white_grape_juice", () -> new LiquidBlock(() -> (net.minecraft.world.level.material.FlowingFluid) ModFluids.WHITE_GRAPE_JUICE.get(), BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));
        GREEN_GRAPE_JUICE_BLOCK = BLOCKS.register("green_grape_juice", () -> new LiquidBlock(() -> (net.minecraft.world.level.material.FlowingFluid) ModFluids.GREEN_GRAPE_JUICE.get(), BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

        PURPLE_WINE = BLOCKS.register("purple_wine", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        RED_WINE = BLOCKS.register("red_wine", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        WHITE_WINE = BLOCKS.register("white_wine", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        ICE_PURPLE_WINE = BLOCKS.register("ice_purple_wine", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        ICE_RED_WINE = BLOCKS.register("ice_red_wine", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        ICE_WHITE_WINE = BLOCKS.register("ice_white_wine", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        FLOWER_PURPLE_WINE = BLOCKS.register("flower_purple_wine", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        FLOWER_RED_WINE = BLOCKS.register("flower_red_wine", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        FLOWER_WHITE_WINE = BLOCKS.register("flower_white_wine", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        WHISKEY = BLOCKS.register("whiskey", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        CORN_WHISKEY = BLOCKS.register("corn_whiskey", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        RYE_WHISKEY = BLOCKS.register("rye_whiskey", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        BEER = BLOCKS.register("beer", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        CIDER = BLOCKS.register("cider", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        RUM = BLOCKS.register("rum", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        SAKE = BLOCKS.register("sake", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());

        VODKA = BLOCKS.register("vodka", DrinkBlock.create().maxCount(4).shapes(
                Block.box(6, 0, 6, 10, 16, 10),
                Block.box(2, 0, 6, 14, 16, 10),
                Shapes.or(
                        Block.box(2, 0, 10, 14, 16, 14),
                        Block.box(6, 0, 2, 10, 16, 14)
                ),
                Block.box(2, 0, 2, 14, 16, 14)
        ).build());
    }

    /**
     * 注册葡萄藤类型
     * <p>
     * 将所有注册的葡萄藤方块注册到 WildGrapevineTypes 注册表中
     * 以便在世界生成时能够随机选择
     */
    public static void registerTypes() {
        WildGrapevineTypes.register(new WildGrapevineType("purple", WILD_GRAPEVINE_PURPLE, WILD_GRAPEVINE_PLANT_PURPLE));
        WildGrapevineTypes.register(new WildGrapevineType("red", WILD_GRAPEVINE_RED, WILD_GRAPEVINE_PLANT_RED));
        WildGrapevineTypes.register(new WildGrapevineType("white", WILD_GRAPEVINE_WHITE, WILD_GRAPEVINE_PLANT_WHITE));
        WildGrapevineTypes.register(new WildGrapevineType("green", WILD_GRAPEVINE_GREEN, WILD_GRAPEVINE_PLANT_GREEN));
    }
}
