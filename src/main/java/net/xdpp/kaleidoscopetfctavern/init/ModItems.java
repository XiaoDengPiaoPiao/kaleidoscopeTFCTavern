package net.xdpp.kaleidoscopetfctavern.init;

import com.github.ysbbbbbb.kaleidoscopetavern.item.JuiceBucketItem;
import net.xdpp.kaleidoscopetfctavern.Kaleidoscopetfctavern;
import net.xdpp.kaleidoscopetfctavern.item.CustomDrinkBlockItem;
import net.xdpp.kaleidoscopetfctavern.item.GrapevineLocatorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 物品注册类
 * <p>
 * 注册所有葡萄藤相关的物品
 * 尽可能复用 Kaleidoscope Tavern 已实现的物品
 */
public interface ModItems {
    /**
     * 物品延迟注册表
     */
    DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Kaleidoscopetfctavern.MODID);

    /**
     * 野生紫葡萄藤物品
     */
    RegistryObject<Item> WILD_GRAPEVINE_PURPLE = ITEMS.register("wild_grapevine_purple",
            () -> new BlockItem(ModBlocks.WILD_GRAPEVINE_PURPLE.get(), new Item.Properties()));
    
    /**
     * 野生红葡萄藤物品
     */
    RegistryObject<Item> WILD_GRAPEVINE_RED = ITEMS.register("wild_grapevine_red",
            () -> new BlockItem(ModBlocks.WILD_GRAPEVINE_RED.get(), new Item.Properties()));
    
    /**
     * 野生白葡萄藤物品
     */
    RegistryObject<Item> WILD_GRAPEVINE_WHITE = ITEMS.register("wild_grapevine_white",
            () -> new BlockItem(ModBlocks.WILD_GRAPEVINE_WHITE.get(), new Item.Properties()));
    
    /**
     * 野生绿葡萄藤物品
     */
    RegistryObject<Item> WILD_GRAPEVINE_GREEN = ITEMS.register("wild_grapevine_green",
            () -> new BlockItem(ModBlocks.WILD_GRAPEVINE_GREEN.get(), new Item.Properties()));

    /**
     * 紫葡萄物品
     */
    RegistryObject<Item> GRAPE_PURPLE = ITEMS.register("grape_purple",
            () -> new Item(new Item.Properties()));
    
    /**
     * 红葡萄物品
     */
    RegistryObject<Item> GRAPE_RED = ITEMS.register("grape_red",
            () -> new Item(new Item.Properties()));
    
    /**
     * 白葡萄物品
     */
    RegistryObject<Item> GRAPE_WHITE = ITEMS.register("grape_white",
            () -> new Item(new Item.Properties()));
    
    /**
     * 绿葡萄物品
     */
    RegistryObject<Item> GRAPE_GREEN = ITEMS.register("grape_green",
            () -> new Item(new Item.Properties()));

    /**
     * 葡萄藤定位器物品
     */
    RegistryObject<Item> GRAPEVINE_LOCATOR = ITEMS.register("grapevine_locator",
            () -> new GrapevineLocatorItem(new Item.Properties()));

    /**
     * 紫葡萄汁桶物品
     */
    RegistryObject<Item> PURPLE_GRAPE_BUCKET = ITEMS.register("purple_grape_bucket",
            () -> new JuiceBucketItem(ModFluids.PURPLE_GRAPE_JUICE));
    
    /**
     * 红葡萄汁桶物品
     */
    RegistryObject<Item> RED_GRAPE_BUCKET = ITEMS.register("red_grape_bucket",
            () -> new JuiceBucketItem(ModFluids.RED_GRAPE_JUICE));
    
    /**
     * 白葡萄汁桶物品
     */
    RegistryObject<Item> WHITE_GRAPE_BUCKET = ITEMS.register("white_grape_bucket",
            () -> new JuiceBucketItem(ModFluids.WHITE_GRAPE_JUICE));
    
    /**
     * 绿葡萄汁桶物品
     */
    RegistryObject<Item> GREEN_GRAPE_BUCKET = ITEMS.register("green_grape_bucket",
            () -> new JuiceBucketItem(ModFluids.GREEN_GRAPE_JUICE));

    /**
     * 紫葡萄酒物品
     */
    RegistryObject<Item> PURPLE_WINE = ITEMS.register("purple_wine",
            () -> new CustomDrinkBlockItem(ModBlocks.PURPLE_WINE.get()));

    /**
     * 红葡萄酒物品
     */
    RegistryObject<Item> RED_WINE = ITEMS.register("red_wine",
            () -> new CustomDrinkBlockItem(ModBlocks.RED_WINE.get()));

    /**
     * 白葡萄酒物品
     */
    RegistryObject<Item> WHITE_WINE = ITEMS.register("white_wine",
            () -> new CustomDrinkBlockItem(ModBlocks.WHITE_WINE.get()));

    /**
     * 冰紫葡萄酒物品
     */
    RegistryObject<Item> ICE_PURPLE_WINE = ITEMS.register("ice_purple_wine",
            () -> new CustomDrinkBlockItem(ModBlocks.ICE_PURPLE_WINE.get()));

    /**
     * 冰红葡萄酒物品
     */
    RegistryObject<Item> ICE_RED_WINE = ITEMS.register("ice_red_wine",
            () -> new CustomDrinkBlockItem(ModBlocks.ICE_RED_WINE.get()));

    /**
     * 冰白葡萄酒物品
     */
    RegistryObject<Item> ICE_WHITE_WINE = ITEMS.register("ice_white_wine",
            () -> new CustomDrinkBlockItem(ModBlocks.ICE_WHITE_WINE.get()));

    /**
     * 花酿紫葡萄酒物品
     */
    RegistryObject<Item> FLOWER_PURPLE_WINE = ITEMS.register("flower_purple_wine",
            () -> new CustomDrinkBlockItem(ModBlocks.FLOWER_PURPLE_WINE.get()));

    /**
     * 花酿红葡萄酒物品
     */
    RegistryObject<Item> FLOWER_RED_WINE = ITEMS.register("flower_red_wine",
            () -> new CustomDrinkBlockItem(ModBlocks.FLOWER_RED_WINE.get()));

    /**
     * 花酿白葡萄酒物品
     */
    RegistryObject<Item> FLOWER_WHITE_WINE = ITEMS.register("flower_white_wine",
            () -> new CustomDrinkBlockItem(ModBlocks.FLOWER_WHITE_WINE.get()));

    /**
     * 威士忌物品
     */
    RegistryObject<Item> WHISKEY = ITEMS.register("whiskey",
            () -> new CustomDrinkBlockItem(ModBlocks.WHISKEY.get()));

    /**
     * 玉米威士忌物品
     */
    RegistryObject<Item> CORN_WHISKEY = ITEMS.register("corn_whiskey",
            () -> new CustomDrinkBlockItem(ModBlocks.CORN_WHISKEY.get()));

    /**
     * 黑麦威士忌物品
     */
    RegistryObject<Item> RYE_WHISKEY = ITEMS.register("rye_whiskey",
            () -> new CustomDrinkBlockItem(ModBlocks.RYE_WHISKEY.get()));

    /**
     * 啤酒物品
     */
    RegistryObject<Item> BEER = ITEMS.register("beer",
            () -> new CustomDrinkBlockItem(ModBlocks.BEER.get()));

    /**
     * 苹果酒物品
     */
    RegistryObject<Item> CIDER = ITEMS.register("cider",
            () -> new CustomDrinkBlockItem(ModBlocks.CIDER.get()));

    /**
     * 朗姆酒物品
     */
    RegistryObject<Item> RUM = ITEMS.register("rum",
            () -> new CustomDrinkBlockItem(ModBlocks.RUM.get()));

    /**
     * 清酒物品
     */
    RegistryObject<Item> SAKE = ITEMS.register("sake",
            () -> new CustomDrinkBlockItem(ModBlocks.SAKE.get()));

    /**
     * 伏特加物品
     */
    RegistryObject<Item> VODKA = ITEMS.register("vodka",
            () -> new CustomDrinkBlockItem(ModBlocks.VODKA.get()));
}
