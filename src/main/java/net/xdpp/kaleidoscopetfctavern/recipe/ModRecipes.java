package net.xdpp.kaleidoscopetfctavern.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.xdpp.kaleidoscopetfctavern.Kaleidoscopetfctavern.MODID;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);

    public static final RegistryObject<RecipeSerializer<GrapeClimateRequirementRecipe>> GRAPE_CLIMATE_SERIALIZER = RECIPE_SERIALIZERS.register(
            "grape_climate_requirement",
            () -> new GrapeClimateRequirementRecipe.Serializer()
    );

    public static final RegistryObject<RecipeType<GrapeClimateRequirementRecipe>> GRAPE_CLIMATE_TYPE = RECIPE_TYPES.register(
            "grape_climate_requirement",
            () -> new RecipeType<GrapeClimateRequirementRecipe>() {
                @Override
                public String toString() {
                    return "grape_climate_requirement";
                }
            }
    );
}
