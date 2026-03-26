package net.xdpp.kaleidoscopetfctavern.block.plant;

import com.github.ysbbbbbb.kaleidoscopetavern.block.plant.GrapevineTrellisBlock;
import net.xdpp.kaleidoscopetfctavern.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ToolActions;

/**
 * 紫葡萄作物方块
 * <p>
 * 生长在TFC兼容葡萄藤藤架下方的紫葡萄作物
 * 支持剪刀收割，有5个生长阶段
 * 支持TFC气候检查，气候不适时会自动销毁
 */
@SuppressWarnings("deprecation")
public class GrapeCropBlockPurple extends BaseGrapeCropBlock {

    /**
     * 构造紫葡萄作物方块
     * <p>
     * 设置方块属性并注册默认状态
     */
    public GrapeCropBlockPurple() {
        super(() -> ModItems.GRAPE_PURPLE.get());
    }

    /**
     * 获取克隆物品栈
     * <p>
     * 当玩家用鼠标中键点击方块时返回对应的紫葡萄物品
     * 
     * @param state 方块状态
     * @param target 点击目标
     * @param level 世界
     * @param pos 方块位置
     * @param player 玩家
     * @return 紫葡萄物品栈
     */
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return ModItems.GRAPE_PURPLE.get().getDefaultInstance();
    }

    /**
     * 右键交互方块
     * <p>
     * 当使用剪刀且作物成熟时，收割紫葡萄并移除方块
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
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.canPerformAction(ToolActions.SHEARS_HARVEST) && isMaxAge(state)) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            Block.popResource(level, pos, new ItemStack(ModItems.GRAPE_PURPLE.get(), 3));
            heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            player.playSound(SoundEvents.BEEHIVE_SHEAR);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    /**
     * 检查方块是否能继续存活
     * <p>
     * 需要上方是成熟的葡萄藤藤架（KT或TFC兼容的）
     * 
     * @param state 方块状态
     * @param level 世界
     * @param pos 方块位置
     * @return 是否能继续存活
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var aboveState = level.getBlockState(pos.above());
        if (aboveState.getBlock() instanceof GrapevineTrellisBlock trellis) {
            return trellis.isMaxAge(aboveState);
        }
        if (aboveState.getBlock() instanceof TFCGrapevineTrellisBlock ourTrellis) {
            return ourTrellis.isMaxAge(aboveState);
        }
        return false;
    }
}
