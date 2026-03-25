package net.xdpp.kaleidoscopetfctavern.mixin;

import com.github.ysbbbbbb.kaleidoscopetavern.api.blockentity.IPressingTub;
import com.github.ysbbbbbb.kaleidoscopetavern.block.brew.PressingTubBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * PressingTubBlock 混合类
 * <p>
 * 修复交互顺序，优先尝试取结果（装桶），再尝试加材料
 * 同时阻止流体容器（桶）被放入物品槽
 */
@Mixin(value = PressingTubBlock.class, remap = false)
public abstract class PressingTubBlockMixin {

    /**
     * 在use方法执行前拦截
     * <p>
     * 调整交互顺序：先尝试取结果（装桶），再尝试加材料
     * 同时阻止流体容器被放入物品槽
     * 
     * @param state 方块状态
     * @param level 世界
     * @param pos 方块位置
     * @param player 玩家
     * @param hand 交互的手
     * @param hitResult 交互结果
     * @param cir 回调信息
     */
    @Inject(method = "use(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    private void beforeUse(BlockState state, Level level, BlockPos pos, Player player,
                           InteractionHand hand, BlockHitResult hitResult,
                           CallbackInfoReturnable<InteractionResult> cir) {
        if (!(level.getBlockEntity(pos) instanceof IPressingTub pressingTub)) {
            return;
        }

        ItemStack itemInHand = player.getItemInHand(hand);
        
        if (itemInHand.isEmpty()) {
            int removeCount = player.isSecondaryUseActive() ? 64 : 1;
            if (pressingTub.removeIngredient(player, removeCount)) {
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
            return;
        }

        if (pressingTub.getResult(player, itemInHand)) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        if (FluidUtil.getFluidHandler(itemInHand).isPresent()) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }

        if (pressingTub.addIngredient(itemInHand)) {
            cir.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        cir.setReturnValue(InteractionResult.PASS);
    }
}
