package net.xdpp.kaleidoscopetfctavern.mixin;

import com.github.ysbbbbbb.kaleidoscopetavern.api.blockentity.IPressingTub;
import com.github.ysbbbbbb.kaleidoscopetavern.blockentity.brew.PressingTubBlockEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 果盆方块实体混合类
 * <p>
 * 用于修改KT果盆的流体转移逻辑
 * 让果盆踩出的葡萄汁可以用TFC木桶（tfc:wooden_bucket）接取
 */
@Mixin(value = PressingTubBlockEntity.class, remap = false)
public abstract class PressingTubBlockEntityMixin {
    
    /**
     * 在getResult方法执行前拦截
     * <p>
     * 检测是否使用TFC木桶从果盆接取流体
     * 如果是，则尝试将果盆的流体转移到TFC木桶中
     * 
     * @param target 目标实体
     * @param carriedStack 手持物品
     * @param cir 回调信息
     */
    @Inject(method = "getResult", at = @At("HEAD"), cancellable = true, remap = false)
    private void beforeGetResult(LivingEntity target, ItemStack carriedStack, CallbackInfoReturnable<Boolean> cir) {
        PressingTubBlockEntity self = (PressingTubBlockEntity) (Object) this;
        
        if (self.getLevel() == null) {
            return;
        }

        if (self.getFluidAmount() < IPressingTub.MAX_FLUID_AMOUNT) {
            return;
        }

        ItemStack copy = carriedStack.copyWithCount(1);
        LazyOptional<IFluidHandlerItem> tfcFluidHandler = getTFCFluidHandler(copy);
        
        if (tfcFluidHandler.isPresent()) {
            final boolean[] success = {false};
            tfcFluidHandler.ifPresent(stackFluid -> {
                FluidStack transfer = FluidUtil.tryFluidTransfer(self.getFluid(), stackFluid, IPressingTub.MAX_FLUID_AMOUNT, false);
                if (!transfer.isEmpty()) {
                    FluidUtil.tryFluidTransfer(self.getFluid(), stackFluid, IPressingTub.MAX_FLUID_AMOUNT, true);
                    ItemStack result = stackFluid.getContainer();
                    
                    if (!(target instanceof Player player) || !player.isCreative()) {
                        carriedStack.shrink(1);
                    }
                    
                    giveItemToLivingEntity(target, result);
                    
                    SoundEvent sound = transfer.getFluid().getFluidType().getSound(transfer, SoundActions.BUCKET_FILL);
                    if (sound != null) {
                        target.playSound(sound);
                    }
                    success[0] = true;
                }
            });
            if (success[0]) {
                cir.setReturnValue(true);
            }
        }
    }
    
    /**
     * 获取TFC流体物品能力
     * <p>
     * 通过反射获取TFC的FLUID_ITEM能力
     * 
     * @param stack 物品栈
     * @return TFC流体物品能力的懒加载可选对象
     */
    @SuppressWarnings("unchecked")
    private LazyOptional<IFluidHandlerItem> getTFCFluidHandler(ItemStack stack) {
        try {
            Class<?> capabilitiesClass = Class.forName("net.dries007.tfc.common.capabilities.Capabilities");
            Object fluidItemCapField = capabilitiesClass.getField("FLUID_ITEM").get(null);
            if (fluidItemCapField instanceof Capability<?>) {
                Capability<IFluidHandlerItem> fluidItemCap = (Capability<IFluidHandlerItem>) fluidItemCapField;
                return stack.getCapability(fluidItemCap);
            }
        } catch (Exception e) {
        }
        return LazyOptional.empty();
    }
    
    /**
     * 给实体物品
     * <p>
     * 先尝试调用KT的ItemUtils.getItemToLivingEntity方法
     * 如果失败，则直接给玩家物品栏
     * 
     * @param user 用户实体
     * @param item 物品
     */
    private void giveItemToLivingEntity(LivingEntity user, ItemStack item) {
        try {
            Class<?> itemUtilsClass = Class.forName("com.github.ysbbbbbb.kaleidoscopetavern.util.ItemUtils");
            itemUtilsClass.getMethod("getItemToLivingEntity", LivingEntity.class, ItemStack.class)
                    .invoke(null, user, item);
        } catch (Exception e) {
            if (user instanceof Player player) {
                player.getInventory().placeItemBackInInventory(item);
            }
        }
    }
}
