package net.xdpp.kaleidoscopetfctavern.recipe;

import com.google.gson.JsonObject;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.util.JsonHelpers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.dries007.tfc.util.calendar.Season;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GrapeClimateRequirementRecipe implements Recipe<net.minecraft.world.inventory.CraftingContainer> {

    private final ResourceLocation id;
    private final Ingredient grapeType;
    private final float minTemperature;
    private final float maxTemperature;
    private final float minRainfall;
    private final float maxRainfall;
    private final Season season;

    public GrapeClimateRequirementRecipe(ResourceLocation id, Ingredient grapeType, 
                                         float minTemperature, float maxTemperature, 
                                         float minRainfall, float maxRainfall, 
                                         Season season) {
        this.id = id;
        this.grapeType = grapeType;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.minRainfall = minRainfall;
        this.maxRainfall = maxRainfall;
        this.season = season;
    }

    public Ingredient getGrapeType() {
        return grapeType;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public float getMinRainfall() {
        return minRainfall;
    }

    public float getMaxRainfall() {
        return maxRainfall;
    }

    public Season getSeason() {
        return season;
    }

    public boolean matches(float temperature, float rainfall, Season currentSeason, ItemStack grapeStack) {
        return grapeType.test(grapeStack)
                && temperature >= minTemperature && temperature <= maxTemperature
                && rainfall >= minRainfall && rainfall <= maxRainfall
                && season == currentSeason;
    }

    public boolean matchesClimateOnly(float temperature, float rainfall, ItemStack grapeStack) {
        return grapeType.test(grapeStack)
                && temperature >= minTemperature && temperature <= maxTemperature
                && rainfall >= minRainfall && rainfall <= maxRainfall;
    }

    @Override
    public boolean matches(net.minecraft.world.inventory.CraftingContainer container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(net.minecraft.world.inventory.CraftingContainer container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.GRAPE_CLIMATE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.GRAPE_CLIMATE_TYPE.get();
    }

    public static class Serializer extends RecipeSerializerImpl<GrapeClimateRequirementRecipe> {

        @Override
        public GrapeClimateRequirementRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            final Ingredient grapeType = Ingredient.fromJson(JsonHelpers.get(json, "grape_type"));
            final float minTemperature = GsonHelper.getAsFloat(json, "min_temperature");
            final float maxTemperature = GsonHelper.getAsFloat(json, "max_temperature");
            final float minRainfall = GsonHelper.getAsFloat(json, "min_rainfall");
            final float maxRainfall = GsonHelper.getAsFloat(json, "max_rainfall");
            final String seasonName = GsonHelper.getAsString(json, "season");
            final Season season = Season.valueOf(seasonName.toUpperCase());

            return new GrapeClimateRequirementRecipe(recipeId, grapeType, 
                    minTemperature, maxTemperature, 
                    minRainfall, maxRainfall, season);
        }

        @Nullable
        @Override
        public GrapeClimateRequirementRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            final Ingredient grapeType = Ingredient.fromNetwork(buffer);
            final float minTemperature = buffer.readFloat();
            final float maxTemperature = buffer.readFloat();
            final float minRainfall = buffer.readFloat();
            final float maxRainfall = buffer.readFloat();
            final String seasonName = buffer.readUtf();
            final Season season = Season.valueOf(seasonName);

            return new GrapeClimateRequirementRecipe(recipeId, grapeType, 
                    minTemperature, maxTemperature, 
                    minRainfall, maxRainfall, season);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, GrapeClimateRequirementRecipe recipe) {
            recipe.grapeType.toNetwork(buffer);
            buffer.writeFloat(recipe.minTemperature);
            buffer.writeFloat(recipe.maxTemperature);
            buffer.writeFloat(recipe.minRainfall);
            buffer.writeFloat(recipe.maxRainfall);
            buffer.writeUtf(recipe.season.name());
        }
    }
}
