package net.xdpp.kaleidoscopetfctavern.mixin;

import com.github.ysbbbbbb.kaleidoscopetavern.crafting.recipe.BarrelRecipe;
import com.github.ysbbbbbb.kaleidoscopetavern.compat.jei.category.BarrelRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Arrays;
import java.util.List;

/**
 * BarrelRecipeCategory 混合类
 * <p>
 * 修改KT酒桶配方在JEI中的显示方式，将流体从桶图标改为直接显示流体图标和毫升数
 */
@Mixin(value = BarrelRecipeCategory.class, remap = false)
public abstract class BarrelRecipeCategoryMixin {

    /**
     * 覆盖setRecipe方法，使用流体显示而不是桶显示
     *
     * @reason 需要将JEI中显示的"4桶流体"改为"4000ml流体"，参考TFC的流体显示方式
     * @author xdpp
     * @param builder JEI配方布局构建器
     * @param recipe 酒桶配方
     * @param focuses 焦点组
     */
    @Overwrite
    public void setRecipe(IRecipeLayoutBuilder builder, BarrelRecipe recipe, IFocusGroup focuses) {
        int offsetX = 0;
        for (Ingredient input : recipe.getIngredients()) {
            List<ItemStack> list = Arrays.stream(input.getItems())
                    .map(s -> s.copyWithCount(16))
                    .toList();
            builder.addSlot(RecipeIngredientRole.INPUT, 30 + offsetX, 9)
                    .addIngredients(VanillaTypes.ITEM_STACK, list);
            offsetX += 18;
        }

        FluidStack fluidStack = new FluidStack(recipe.fluid(), 4000);
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 9)
                .addIngredient(ForgeTypes.FLUID_STACK, fluidStack)
                .setFluidRenderer(4000, false, 16, 16);

        builder.addSlot(RecipeIngredientRole.CATALYST, 84, 117)
                .addIngredients(recipe.carrier());

        ItemStack outputStack = recipe.result().copyWithCount(16);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 152, 86)
                .addItemStack(outputStack);
    }
}
