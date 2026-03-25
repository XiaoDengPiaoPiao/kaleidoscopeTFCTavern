package net.xdpp.kaleidoscopetfctavern.init;

import net.xdpp.kaleidoscopetfctavern.block.plant.WildGrapevineType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 野生葡萄藤类型注册表
 * <p>
 * 管理所有已注册的野生葡萄藤类型
 * 支持动态注册和随机选择，添加新类型时概率会自动平分
 */
public class WildGrapevineTypes {
    /**
     * 已注册的野生葡萄藤类型列表
     */
    private static final List<WildGrapevineType> TYPES = new ArrayList<>();
    
    /**
     * 随机数生成器
     */
    private static final Random RANDOM = new Random();

    /**
     * 注册野生葡萄藤类型
     * 
     * @param type 要注册的野生葡萄藤类型
     */
    public static void register(WildGrapevineType type) {
        TYPES.add(type);
    }

    /**
     * 获取随机的野生葡萄藤类型
     * 
     * @return 随机的野生葡萄藤类型，如果没有注册任何类型则返回null
     */
    public static WildGrapevineType getRandomType() {
        if (TYPES.isEmpty()) {
            return null;
        }
        return TYPES.get(RANDOM.nextInt(TYPES.size()));
    }

    /**
     * 获取所有已注册的野生葡萄藤类型
     * 
     * @return 所有已注册的野生葡萄藤类型的副本列表
     */
    public static List<WildGrapevineType> getAllTypes() {
        return new ArrayList<>(TYPES);
    }

    /**
     * 获取已注册的野生葡萄藤类型数量
     * 
     * @return 已注册的野生葡萄藤类型数量
     */
    public static int getTypeCount() {
        return TYPES.size();
    }
}
