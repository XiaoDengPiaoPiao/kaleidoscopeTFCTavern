package net.xdpp.kaleidoscopetfctavern.block.plant;

import com.github.ysbbbbbb.kaleidoscopetavern.block.properties.TrellisType;
import net.dries007.tfc.common.blocks.soil.HoeOverlayBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.Season;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolActions;
import net.xdpp.kaleidoscopetfctavern.config.KTConfig;
import net.xdpp.kaleidoscopetfctavern.recipe.GrapeClimateRequirementRecipe;
import net.xdpp.kaleidoscopetfctavern.recipe.ModRecipes;
import net.xdpp.kaleidoscopetfctavern.util.GrapeClimateHelper;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * 葡萄作物方块基类
 * <p>
 * 基于 Kaleidoscope Tavern 的 GrapeCropBlock 修改
 * 支持动态设置葡萄物品和TFC气候检查
 */
@SuppressWarnings("deprecation")
public class BaseGrapeCropBlock extends Block implements HoeOverlayBlock {
    /**
     * 生长阶段属性
     */
    public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
    
    /**
     * 最大生长阶段
     */
    public static final int MAX_AGE = BlockStateProperties.MAX_AGE_5;
    
    /**
     * 方块碰撞形状
     */
    public static final VoxelShape SHAPE = Block.box(2, 6, 2, 14, 16, 14);

    /**
     * 葡萄物品供应商
     * <p>
     * 用于延迟获取对应的葡萄物品
     */
    private final Supplier<Item> grapeItemSupplier;

    /**
     * 构造葡萄作物方块
     * <p>
     * 设置方块属性、注册默认状态
     * 初始化葡萄结果部分
     * 
     * @param grapeItemSupplier 葡萄结果部分
     */
    public BaseGrapeCropBlock(Supplier<Item> grapeItemSupplier) {
        super(Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP)
                .offsetType(BlockBehaviour.OffsetType.XYZ)
                .pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0));
        this.grapeItemSupplier = grapeItemSupplier;
    }
    
    /**
     * 获取对应的葡萄物品栈
     * <p>
     * 通过供应商获取对应葡萄类型的物品
     * 
     * @return 葡萄物品栈
     */
    protected ItemStack getGrapeStack() {
        return grapeItemSupplier.get().getDefaultInstance();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.canPerformAction(ToolActions.SHEARS_HARVEST) && isMaxAge(state)) {
            if (!level.isClientSide) {
                LootParams.Builder lootParamsBuilder = new LootParams.Builder((ServerLevel) level)
                        .withParameter(LootContextParams.ORIGIN, pos.getCenter())
                        .withParameter(LootContextParams.TOOL, heldItem)
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.BLOCK_STATE, state);
                
                List<ItemStack> drops = getDrops(state, lootParamsBuilder);
                for (ItemStack drop : drops) {
                    Block.popResource(level, pos, drop);
                }
            }
            
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            player.playSound(SoundEvents.BEEHIVE_SHEAR);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return super.isRandomlyTicking(state) && state.getValue(AGE) < MAX_AGE;
    }

    /**
     * 随机刻执行
     * <p>
     * 检查气候条件，如果不符合则销毁方块
     * 检查是否能继续存活，如果不能则销毁
     * 检查是否需要生长
     * 
     * @param state 方块状态
     * @param level 世界
     * @param pos 方块位置
     * @param random 随机源
     */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }

        int age = state.getValue(AGE);
        final GrapeClimateHelper.ClimateContext climate = GrapeClimateHelper.capture(level, pos);
        final boolean canGrowToMax = GrapeClimateHelper.matches(level, climate, getGrapeStack());
        
        if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextDouble() < KTConfig.GRAPE_CROP_GROWTH_CHANCE.get())) {
            if (age < MAX_AGE && (age < MAX_AGE - 1 || canGrowToMax)) {
                level.setBlockAndUpdate(pos, state.setValue(AGE, age + 1));
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }
    
    /**
     * 检查气候条件
     * <p>
     * 根据配方系统定义的气候条件检查温度、降雨量和季节是否合适
     * 
     * @param level 世界
     * @param pos 方块位置
     * @return 气候条件是否满足
     */
    private boolean checkClimateConditions(Level level, BlockPos pos) {
        return GrapeClimateHelper.matches(level, GrapeClimateHelper.capture(level, pos), getGrapeStack());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.canSurvive(level, pos)) {
            return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var aboveState = level.getBlockState(pos.above());
        if (aboveState.getBlock() instanceof com.github.ysbbbbbb.kaleidoscopetavern.block.plant.GrapevineTrellisBlock trellis) {
            return trellis.isMaxAge(aboveState);
        }
        if (aboveState.getBlock() instanceof TFCGrapevineTrellisBlock trellis) {
            return trellis.isMaxAge(aboveState);
        }
        return false;
    }

    public boolean isMaxAge(BlockState state) {
        return state.getValue(AGE) >= MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder lootParamsBuilder) {
        if (isMaxAge(state)) {
            return super.getDrops(state, lootParamsBuilder);
        }
        return Collections.emptyList();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return grapeItemSupplier.get().getDefaultInstance();
    }

    /**
     * 添加锄头查看信息
     * <p>
     * 显示葡萄作物的温度、降水量和季节要求
     *
     * @param level 世界
     * @param pos 位置
     * @param state 方块状态
     * @param text 信息列表
     * @param isDebug 是否调试模式
     */
    @Override
    public void addHoeOverlayInfo(Level level, BlockPos pos, BlockState state, List<Component> text, boolean isDebug) {
        GrapeClimateHelper.addTooltip(level, pos, getGrapeStack(), text);
    }

    /**
     * 获取温度提示文本
     *
     * @param current 当前温度
     * @param min 最小温度
     * @param max 最大温度
     * @return 提示文本
     */
    private Component getTemperatureTooltip(float current, float min, float max) {
        MutableComponent text = Component.translatable("kaleidoscopetfctavern.tooltip.grape.temperature", String.format("%.1f", current));
        if (current >= min && current <= max) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.good"));
        } else if (current < min) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.too_low", String.format("%.1f", min)));
        } else {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.too_high", String.format("%.1f", max)));
        }
        return text;
    }

    /**
     * 获取降水量提示文本
     *
     * @param current 当前降水量
     * @param min 最小降水量
     * @param max 最大降水量
     * @return 提示文本
     */
    private Component getRainfallTooltip(float current, float min, float max) {
        MutableComponent text = Component.translatable("kaleidoscopetfctavern.tooltip.grape.rainfall", String.format("%.0f", current));
        if (current >= min && current <= max) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.good"));
        } else if (current < min) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.too_low", String.format("%.0f", min)));
        } else {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.too_high", String.format("%.0f", max)));
        }
        return text;
    }

    /**
     * 获取季节提示文本
     *
     * @param current 当前季节
     * @param required 要求季节
     * @return 提示文本
     */
    private Component getSeasonTooltip(Season current, Season required) {
        MutableComponent text = Component.translatable("kaleidoscopetfctavern.tooltip.grape.season", getSeasonName(current));
        if (current == required) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.suitable", getSeasonName(required)));
        } else {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.not_suitable", getSeasonName(required)));
        }
        return text;
    }

    /**
     * 获取季节名称
     *
     * @param season 季节
     * @return 季节名称
     */
    private Component getSeasonName(Season season) {
        if (season == Season.SPRING) {
            return Component.translatable("kaleidoscopetfctavern.season.spring");
        } else if (season == Season.SUMMER) {
            return Component.translatable("kaleidoscopetfctavern.season.summer");
        } else if (season == Season.FALL) {
            return Component.translatable("kaleidoscopetfctavern.season.fall");
        } else if (season == Season.WINTER) {
            return Component.translatable("kaleidoscopetfctavern.season.winter");
        }
        return Component.empty();
    }
}
