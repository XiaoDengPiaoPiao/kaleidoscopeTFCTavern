package net.xdpp.kaleidoscopetfctavern.init;

import com.github.ysbbbbbb.kaleidoscopetavern.item.JuiceBucketItem;
import net.xdpp.kaleidoscopetfctavern.Kaleidoscopetfctavern;
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
    DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Kaleidoscopetfctavern.MODID);

    RegistryObject<Item> WILD_GRAPEVINE_PURPLE = ITEMS.register("wild_grapevine_purple",
            () -> new BlockItem(ModBlocks.WILD_GRAPEVINE_PURPLE.get(), new Item.Properties()));
    RegistryObject<Item> WILD_GRAPEVINE_RED = ITEMS.register("wild_grapevine_red",
            () -> new BlockItem(ModBlocks.WILD_GRAPEVINE_RED.get(), new Item.Properties()));
    RegistryObject<Item> WILD_GRAPEVINE_WHITE = ITEMS.register("wild_grapevine_white",
            () -> new BlockItem(ModBlocks.WILD_GRAPEVINE_WHITE.get(), new Item.Properties()));
    RegistryObject<Item> WILD_GRAPEVINE_GREEN = ITEMS.register("wild_grapevine_green",
            () -> new BlockItem(ModBlocks.WILD_GRAPEVINE_GREEN.get(), new Item.Properties()));

    RegistryObject<Item> GRAPE_PURPLE = ITEMS.register("grape_purple",
            () -> new Item(new Item.Properties()));
    RegistryObject<Item> GRAPE_RED = ITEMS.register("grape_red",
            () -> new Item(new Item.Properties()));
    RegistryObject<Item> GRAPE_WHITE = ITEMS.register("grape_white",
            () -> new Item(new Item.Properties()));
    RegistryObject<Item> GRAPE_GREEN = ITEMS.register("grape_green",
            () -> new Item(new Item.Properties()));

    RegistryObject<Item> GRAPEVINE_LOCATOR = ITEMS.register("grapevine_locator",
            () -> new GrapevineLocatorItem(new Item.Properties()));

    RegistryObject<Item> PURPLE_GRAPE_BUCKET = ITEMS.register("purple_grape_bucket",
            () -> new JuiceBucketItem(ModFluids.PURPLE_GRAPE_JUICE));
    RegistryObject<Item> RED_GRAPE_BUCKET = ITEMS.register("red_grape_bucket",
            () -> new JuiceBucketItem(ModFluids.RED_GRAPE_JUICE));
    RegistryObject<Item> WHITE_GRAPE_BUCKET = ITEMS.register("white_grape_bucket",
            () -> new JuiceBucketItem(ModFluids.WHITE_GRAPE_JUICE));
    RegistryObject<Item> GREEN_GRAPE_BUCKET = ITEMS.register("green_grape_bucket",
            () -> new JuiceBucketItem(ModFluids.GREEN_GRAPE_JUICE));
}
