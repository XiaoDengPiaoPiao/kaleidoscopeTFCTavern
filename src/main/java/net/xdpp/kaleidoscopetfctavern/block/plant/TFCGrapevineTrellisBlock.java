package net.xdpp.kaleidoscopetfctavern.block.plant;

import com.github.ysbbbbbb.kaleidoscopetavern.block.properties.TrellisType;
import com.github.ysbbbbbb.kaleidoscopetavern.block.plant.ITrellis;
import net.dries007.tfc.common.blocks.soil.HoeOverlayBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.Season;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.xdpp.kaleidoscopetfctavern.block.properties.GrapevineType;
import net.xdpp.kaleidoscopetfctavern.config.KTConfig;
import net.xdpp.kaleidoscopetfctavern.init.ModBlocks;
import net.xdpp.kaleidoscopetfctavern.init.ModItems;
import net.xdpp.kaleidoscopetfctavern.recipe.GrapeClimateRequirementRecipe;
import net.xdpp.kaleidoscopetfctavern.recipe.ModRecipes;
import net.xdpp.kaleidoscopetfctavern.util.GrapeClimateHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolActions;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopetavern.block.plant.ITrellis.axisHasTrellis;
import static com.github.ysbbbbbb.kaleidoscopetavern.block.plant.ITrellis.updateType;

/**
 * TFC兼容葡萄藤藤架方块
 * <p>
 * 基于 Kaleidoscope Tavern 的 GrapevineTrellisBlock 修改
 * 增加了TFC的气候和季节支持，以及TFC泥土/草方块兼容性
 */
@SuppressWarnings("deprecation")
public class TFCGrapevineTrellisBlock extends Block implements SimpleWaterloggedBlock, ITrellis, HoeOverlayBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final int MAX_AGE = BlockStateProperties.MAX_AGE_3;
    public static final EnumProperty<GrapevineType> GRAPE_TYPE = EnumProperty.create("grape_type", GrapevineType.class);
    public static final BooleanProperty WITHERED = BooleanProperty.create("withered");
    public static final Direction[] CHECK_DIRECTION = new Direction[]{Direction.UP, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH};

    public TFCGrapevineTrellisBlock() {
        super(Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.GUITAR)
                .strength(0.8F)
                .sound(SoundType.WOOD)
                .randomTicks()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .ignitedByLava());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TYPE, TrellisType.SINGLE)
                .setValue(AGE, 0)
                .setValue(GRAPE_TYPE, GrapevineType.PURPLE)
                .setValue(WATERLOGGED, false)
                .setValue(WITHERED, false));
    }
    
    /**
     * 右键交互方块时执行
     * <p>
     * 支持用剪刀从藤架上取下葡萄藤
     * 
     * @param state 方块状态
     * @param level 世界
     * @param pos 方块位置
     * @param player 玩家
     * @param hand 交互的手
     * @param hitResult 交互结果
     * @return 交互结果
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (!itemInHand.canPerformAction(ToolActions.SHEARS_HARVEST)) {
            return super.use(state, level, pos, player, hand, hitResult);
        }
        BlockState newState = com.github.ysbbbbbb.kaleidoscopetavern.init.ModBlocks.TRELLIS.get()
                .defaultBlockState()
                .setValue(TYPE, state.getValue(TYPE))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
        level.setBlockAndUpdate(pos, newState);
        var grapeType = state.getValue(GRAPE_TYPE);
        ItemStack dropItem;
        switch (grapeType) {
            case PURPLE:
                dropItem = ModItems.WILD_GRAPEVINE_PURPLE.get().getDefaultInstance();
                break;
            case RED:
                dropItem = ModItems.WILD_GRAPEVINE_RED.get().getDefaultInstance();
                break;
            case WHITE:
                dropItem = ModItems.WILD_GRAPEVINE_WHITE.get().getDefaultInstance();
                break;
            case GREEN:
                dropItem = ModItems.WILD_GRAPEVINE_GREEN.get().getDefaultInstance();
                break;
            default:
                dropItem = ModItems.WILD_GRAPEVINE_PURPLE.get().getDefaultInstance();
                break;
        }
        Block.popResource(level, pos, dropItem);
        itemInHand.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        player.playSound(SoundEvents.BEEHIVE_SHEAR);
        return InteractionResult.SUCCESS;
    }
    
    /**
     * 更新方块形状
     * <p>
     * 处理水logged和藤架类型的更新
     * 
     * @param state 方块状态
     * @param direction 方向
     * @param neighborState 邻居方块状态
     * @param level 世界
     * @param pos 方块位置
     * @param neighborPos 邻居方块位置
     * @return 更新后的方块状态
     */
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        boolean xHas = axisHasTrellis(level, pos, Direction.Axis.X);
        boolean yHas = axisHasTrellis(level, pos, Direction.Axis.Y);
        boolean zHas = axisHasTrellis(level, pos, Direction.Axis.Z);
        var trellisType = updateType(state.getValue(TYPE), xHas, yHas, zHas);

        state = state.setValue(TYPE, trellisType);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
    
    /**
     * 检查方块类型是否相同
     * <p>
     * 判断是藤架还是葡萄藤藤架
     * 
     * @param state 方块状态
     * @return 是否是相同类型
     */
    @Override
    public boolean sameType(BlockState state) {
        return state.is(com.github.ysbbbbbb.kaleidoscopetavern.init.ModBlocks.TRELLIS.get()) 
                || state.is(com.github.ysbbbbbb.kaleidoscopetavern.init.ModBlocks.GRAPEVINE_TRELLIS.get())
                || state.is(this);
    }
    
    /**
     * 随机刻执行
     * <p>
     * 处理葡萄藤的生长
     * 
     * @param state 方块状态
     * @param level 世界
     * @param pos 方块位置
     * @param random 随机源
     */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        final ItemStack grapeStack = GrapeClimateHelper.getGrapeStackForType(state.getValue(GRAPE_TYPE));
        final GrapeClimateHelper.ClimateContext climate = GrapeClimateHelper.capture(level, pos);
        final boolean climateSuitable = GrapeClimateHelper.matchesClimateOnly(level, climate, grapeStack);
        final boolean shouldWither = (!climate.greenhouseControlled() && climate.season() == Season.WINTER) || !climateSuitable;
        if (state.getValue(WITHERED) != shouldWither) {
            level.setBlockAndUpdate(pos, state.setValue(WITHERED, shouldWither));
            if (shouldWither) {
                BlockPos cropPos = pos.below();
                BlockState cropState = level.getBlockState(cropPos);
                if (cropState.getBlock() instanceof BaseGrapeCropBlock) {
                    level.destroyBlock(cropPos, false);
                }
            }
        }
        
        if (!shouldWither && ForgeHooks.onCropsGrowPre(level, pos, state, random.nextDouble() < KTConfig.GRAPEVINE_GROWTH_CHANCE.get())) {
            this.doGrow(level, pos, state);
        }
    }
    
    /**
     * 检查是否达到最大年龄
     * 
     * @param state 方块状态
     * @return 是否达到最大年龄
     */
    public boolean isMaxAge(BlockState state) {
        return state.getValue(AGE) >= MAX_AGE;
    }
    
    /**
     * 检查下方方块是否支持生长
     * <p>
     * 支持原版泥土、TFC泥土和TFC草方块
     * 
     * @param belowState 下方方块状态
     * @return 是否支持生长
     */
    public boolean belowSupportGrow(BlockState belowState) {
        if (belowState.is(this)) {
            return isMaxAge(belowState);
        } else {
            return belowState.is(BlockTags.DIRT) ||
                   isInTag(belowState, "tfc", "dirt") ||
                   isInTag(belowState, "tfc", "grass");
        }
    }
    
    /**
     * 检查方块是否在指定标签中
     * 
     * @param state 方块状态
     * @param namespace 命名空间
     * @param tagName 标签名
     * @return 是否在标签中
     */
    private boolean isInTag(BlockState state, String namespace, String tagName) {
        var tag = net.minecraft.tags.TagKey.create(
            net.minecraft.core.registries.Registries.BLOCK,
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(namespace, tagName)
        );
        return state.is(tag);
    }
    
    /**
     * 检查是否可以生长到某个位置
     * 
     * @param checkState 要检查的方块状态
     * @return 是否可以生长
     */
    public boolean canGrowInto(BlockState checkState) {
        return checkState.is(com.github.ysbbbbbb.kaleidoscopetavern.init.ModBlocks.TRELLIS.get());
    }
    
    /**
     * 获取生长后的方块状态
     * 
     * @param direction 生长方向
     * @param checkState 目标位置的方块状态
     * @param grapeType 葡萄藤类型
     * @return 生长后的方块状态
     */
    public BlockState getGrowIntoState(Direction direction, BlockState checkState, GrapevineType grapeType) {
        var type = checkState.getOptionalValue(TYPE).orElse(TrellisType.SINGLE);
        boolean waterlogged = checkState.getOptionalValue(WATERLOGGED).orElse(false);
        int age = direction == Direction.UP ? 0 : MAX_AGE;
        return this.defaultBlockState()
                .setValue(TYPE, type)
                .setValue(AGE, age)
                .setValue(GRAPE_TYPE, grapeType)
                .setValue(WATERLOGGED, waterlogged);
    }
    
    /**
     * 检查是否可以生长葡萄作物
     * 
     * @param level 世界
     * @param pos 方块位置
     * @return 是否可以生长葡萄
     */
    public boolean canGrowGrape(LevelReader level, BlockPos pos) {
        if (pos.getY() < level.getMinBuildHeight() + 1) {
            return false;
        }
        return level.getBlockState(pos.below()).isAir();
    }
    
    /**
     * 检查是否可以生长
     * 
     * @param level 世界
     * @param pos 方块位置
     * @param state 方块状态
     * @return 是否可以生长
     */
    public boolean canGrow(LevelReader level, BlockPos pos, BlockState state) {
        if (state.getValue(TYPE) == TrellisType.SINGLE) {
            BlockState belowState = level.getBlockState(pos.below());
            if (!belowSupportGrow(belowState)) {
                return false;
            }
            if (!isMaxAge(state)) {
                return true;
            }
        }

        if (isMaxAge(state)) {
            for (Direction direction : CHECK_DIRECTION) {
                BlockPos checkPos = pos.relative(direction);
                BlockState checkState = level.getBlockState(checkPos);
                if (this.canGrowInto(checkState)) {
                    return true;
                }
            }

            return canGrowGrape(level, pos);
        } else {
            return true;
        }
    }
    
    /**
     * 执行生长逻辑
     * <p>
     * 处理葡萄藤的生长和葡萄作物的生成
     * 
     * @param level 世界
     * @param pos 方块位置
     * @param state 方块状态
     */
    public void doGrow(Level level, BlockPos pos, BlockState state) {
        var grapeType = state.getValue(GRAPE_TYPE);
        final ItemStack grapeStack = GrapeClimateHelper.getGrapeStackForType(grapeType);
        
        if (state.getValue(TYPE) == TrellisType.SINGLE) {
            BlockState belowState = level.getBlockState(pos.below());
            if (!belowSupportGrow(belowState)) {
                return;
            }
            if (!isMaxAge(state)) {
                level.setBlockAndUpdate(pos, state.cycle(AGE));
                ForgeHooks.onCropsGrowPost(level, pos, state);
                return;
            }
        }

        if (isMaxAge(state)) {
            for (Direction direction : CHECK_DIRECTION) {
                BlockPos checkPos = pos.relative(direction);
                BlockState checkState = level.getBlockState(checkPos);
                if (this.canGrowInto(checkState)) {
                    BlockState growIntoState = this.getGrowIntoState(direction, checkState, grapeType);
                    level.setBlockAndUpdate(checkPos, growIntoState);
                    ForgeHooks.onCropsGrowPost(level, checkPos, checkState);
                    return;
                }
            }

            if (canGrowGrape(level, pos) && GrapeClimateHelper.matches(level, pos, grapeStack)) {
                Block cropBlock;
                switch (grapeType) {
                    case PURPLE:
                        cropBlock = ModBlocks.GRAPE_CROP_PURPLE.get();
                        break;
                    case RED:
                        cropBlock = ModBlocks.GRAPE_CROP_RED.get();
                        break;
                    case WHITE:
                        cropBlock = ModBlocks.GRAPE_CROP_WHITE.get();
                        break;
                    case GREEN:
                        cropBlock = ModBlocks.GRAPE_CROP_GREEN.get();
                        break;
                    default:
                        cropBlock = ModBlocks.GRAPE_CROP_PURPLE.get();
                        break;
                }
                level.setBlockAndUpdate(pos.below(), cropBlock.defaultBlockState());
                ForgeHooks.onCropsGrowPost(level, pos.below(), state);
            }
        } else {
            level.setBlockAndUpdate(pos, state.setValue(AGE, MAX_AGE));
            ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }
    
    /**
     * 获取葡萄藤类型对应的物品栈
     * 
     * @param grapeType 葡萄藤类型
     * @return 对应的物品栈
     */
    private ItemStack getGrapeStackForType(GrapevineType grapeType) {
        return switch (grapeType) {
            case PURPLE -> ModItems.GRAPE_PURPLE.get().getDefaultInstance();
            case RED -> ModItems.GRAPE_RED.get().getDefaultInstance();
            case WHITE -> ModItems.GRAPE_WHITE.get().getDefaultInstance();
            case GREEN -> ModItems.GRAPE_GREEN.get().getDefaultInstance();
        };
    }
    
    /**
     * 检查气候条件（仅温度和降水，不检查季节）
     * <p>
     * 根据配方系统定义的气候条件检查温度、降雨量是否合适
     * 
     * @param level 世界
     * @param pos 方块位置
     * @param grapeType 葡萄藤类型
     * @return 温度和降水条件是否满足
     */
    private boolean checkClimateConditionsOnly(Level level, BlockPos pos, GrapevineType grapeType) {
        return GrapeClimateHelper.matchesClimateOnly(level, GrapeClimateHelper.capture(level, pos), GrapeClimateHelper.getGrapeStackForType(grapeType));
    }

    /**
     * 检查完整气候条件（温度、降水和季节）
     * <p>
     * 根据配方系统定义的气候条件检查温度、降雨量和季节是否合适
     * 
     * @param level 世界
     * @param pos 方块位置
     * @param grapeType 葡萄藤类型
     * @return 完整气候条件是否满足
     */
    private boolean checkClimateConditions(Level level, BlockPos pos, GrapevineType grapeType) {
        return GrapeClimateHelper.matches(level, GrapeClimateHelper.capture(level, pos), GrapeClimateHelper.getGrapeStackForType(grapeType));
    }
    
    /**
     * 创建方块状态定义
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, AGE, GRAPE_TYPE, WATERLOGGED, WITHERED);
    }
    
    /**
     * 获取碰撞形状
     */
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return collisionShape(state.getValue(TYPE));
    }
    
    /**
     * 获取选择形状
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return selectShape(state.getValue(TYPE));
    }
    
    /**
     * 获取克隆物品栈
     */
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        var grapeType = state.getValue(GRAPE_TYPE);
        ItemStack cloneItem;
        switch (grapeType) {
            case PURPLE:
                cloneItem = ModItems.WILD_GRAPEVINE_PURPLE.get().getDefaultInstance();
                break;
            case RED:
                cloneItem = ModItems.WILD_GRAPEVINE_RED.get().getDefaultInstance();
                break;
            case WHITE:
                cloneItem = ModItems.WILD_GRAPEVINE_WHITE.get().getDefaultInstance();
                break;
            case GREEN:
                cloneItem = ModItems.WILD_GRAPEVINE_GREEN.get().getDefaultInstance();
                break;
            default:
                cloneItem = ModItems.WILD_GRAPEVINE_PURPLE.get().getDefaultInstance();
                break;
        }
        return cloneItem;
    }
    
    /**
     * 获取流体状态
     */
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    /**
     * 添加锄头查看信息
     * <p>
     * 显示葡萄藤架的温度、降水量和季节要求
     *
     * @param level 世界
     * @param pos 位置
     * @param state 方块状态
     * @param text 信息列表
     * @param isDebug 是否调试模式
     */
    @Override
    public void addHoeOverlayInfo(Level level, BlockPos pos, BlockState state, List<Component> text, boolean isDebug) {
        GrapeClimateHelper.addTooltip(level, pos, GrapeClimateHelper.getGrapeStackForType(state.getValue(GRAPE_TYPE)), text);
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
