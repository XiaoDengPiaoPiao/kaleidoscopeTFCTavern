package net.xdpp.kaleidoscopetfctavern.block.plant;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * 野生葡萄藤类型封装类
 * <p>
 * 用于封装一种野生葡萄藤的名称、头部方块和主体方块
 * 支持动态注册和随机选择
 */
public class WildGrapevineType {
    /**
     * 葡萄藤类型名称
     */
    private final String name;
    
    /**
     * 头部方块注册表对象
     */
    private final RegistryObject<Block> headBlock;
    
    /**
     * 主体方块注册表对象
     */
    private final RegistryObject<Block> bodyBlock;

    /**
     * 构造野生葡萄藤类型
     * 
     * @param name 葡萄藤类型名称
     * @param headBlock 头部方块注册表对象
     * @param bodyBlock 主体方块注册表对象
     */
    public WildGrapevineType(String name, RegistryObject<Block> headBlock, RegistryObject<Block> bodyBlock) {
        this.name = name;
        this.headBlock = headBlock;
        this.bodyBlock = bodyBlock;
    }

    /**
     * 获取葡萄藤类型名称
     * 
     * @return 葡萄藤类型名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取头部方块注册表对象
     * 
     * @return 头部方块注册表对象
     */
    public RegistryObject<Block> getHeadBlock() {
        return headBlock;
    }

    /**
     * 获取主体方块注册表对象
     * 
     * @return 主体方块注册表对象
     */
    public RegistryObject<Block> getBodyBlock() {
        return bodyBlock;
    }
}
