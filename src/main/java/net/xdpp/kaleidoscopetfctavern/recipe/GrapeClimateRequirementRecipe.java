package net.xdpp.kaleidoscopetfctavern.recipe;

import com.google.gson.JsonObject;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.util.JsonHelpers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.dries007.tfc.util.calendar.Season;
import org.jetbrains.annotations.Nullable;

public class GrapeClimateRequirementRecipe implements Recipe<net.minecraft.world.inventory.CraftingContainer> {

    private final ResourceLocation id;
    private final Ingredient grapeType;
    private final float minTemperature;
    private final float maxTemperature;
    private final int minHumidity;
    private final int maxHumidity;
    private final Season season;

    public GrapeClimateRequirementRecipe(ResourceLocation id, Ingredient grapeType, 
                                         float minTemperature, float maxTemperature, 
                                         int minHumidity, int maxHumidity, 
                                         Season season) {
        this.id = id;
        this.grapeType = grapeType;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
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

    public int getMinHumidity() {
        return minHumidity;
    }

    public int getMaxHumidity() {
        return maxHumidity;
    }

    public Season getSeason() {
        return season;
    }

    public boolean matches(float temperature, int humidity, Season currentSeason, boolean ignoreSeason, ItemStack grapeStack) {
        return grapeType.test(grapeStack)
                && temperature >= minTemperature && temperature <= maxTemperature
                && humidity >= getMinHumidity() && humidity <= getMaxHumidity()
                && (ignoreSeason || season == currentSeason);
    }

    public boolean matchesClimateOnly(float temperature, int humidity, ItemStack grapeStack) {
        return grapeType.test(grapeStack)
                && temperature >= minTemperature && temperature <= maxTemperature
                && humidity >= getMinHumidity() && humidity <= getMaxHumidity();
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
            final int minHumidity = GsonHelper.getAsInt(json, "min_humidity");
            final int maxHumidity = GsonHelper.getAsInt(json, "max_humidity");
            final String seasonName = GsonHelper.getAsString(json, "season");
            final Season season = Season.valueOf(seasonName.toUpperCase());

            return new GrapeClimateRequirementRecipe(recipeId, grapeType, 
                    minTemperature, maxTemperature, 
                    minHumidity, maxHumidity, season);
        }

        @Nullable
        @Override
        public GrapeClimateRequirementRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            final Ingredient grapeType = Ingredient.fromNetwork(buffer);
            final float minTemperature = buffer.readFloat();
            final float maxTemperature = buffer.readFloat();
            final int minHumidity = buffer.readInt();
            final int maxHumidity = buffer.readInt();
            final String seasonName = buffer.readUtf();
            final Season season = Season.valueOf(seasonName);

            return new GrapeClimateRequirementRecipe(recipeId, grapeType, 
                    minTemperature, maxTemperature, 
                    minHumidity, maxHumidity, season);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, GrapeClimateRequirementRecipe recipe) {
            recipe.grapeType.toNetwork(buffer);
            buffer.writeFloat(recipe.minTemperature);
            buffer.writeFloat(recipe.maxTemperature);
            buffer.writeInt(recipe.minHumidity);
            buffer.writeInt(recipe.maxHumidity);
            buffer.writeUtf(recipe.season.name());
        }
    }
}
