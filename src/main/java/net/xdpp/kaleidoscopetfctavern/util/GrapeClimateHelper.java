package net.xdpp.kaleidoscopetfctavern.util;

import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.Season;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.xdpp.kaleidoscopetfctavern.block.plant.BaseGrapeCropBlock;
import net.xdpp.kaleidoscopetfctavern.block.plant.TFCGrapevineTrellisBlock;
import net.xdpp.kaleidoscopetfctavern.block.properties.GrapevineType;
import net.xdpp.kaleidoscopetfctavern.compat.GreenhouseCompat;
import net.xdpp.kaleidoscopetfctavern.init.ModItems;
import net.xdpp.kaleidoscopetfctavern.recipe.GrapeClimateRequirementRecipe;
import net.xdpp.kaleidoscopetfctavern.recipe.ModRecipes;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public final class GrapeClimateHelper {
    public record ClimateContext(float temperature, int humidity, Season season, boolean greenhouseControlled) {
    }

    private GrapeClimateHelper() {
    }

    public static ClimateContext capture(Level level, BlockPos pos) {
        return new ClimateContext(
                getTemperature(level, pos),
                getHumidity(level, pos),
                Calendars.get(level).getCalendarMonthOfYear().getSeason(),
                GreenhouseCompat.isControlledGreenhouse(level, pos)
        );
    }

    public static ItemStack getGrapeStackForType(GrapevineType grapeType) {
        return switch (grapeType) {
            case PURPLE -> ModItems.GRAPE_PURPLE.get().getDefaultInstance();
            case RED -> ModItems.GRAPE_RED.get().getDefaultInstance();
            case WHITE -> ModItems.GRAPE_WHITE.get().getDefaultInstance();
            case GREEN -> ModItems.GRAPE_GREEN.get().getDefaultInstance();
        };
    }

    public static boolean matches(Level level, BlockPos pos, ItemStack grapeStack) {
        return matches(level, capture(level, pos), grapeStack);
    }

    public static boolean matches(Level level, ClimateContext context, ItemStack grapeStack) {
        for (var recipe : level.getRecipeManager().getAllRecipesFor(ModRecipes.GRAPE_CLIMATE_TYPE.get())) {
            if (recipe.matches(context.temperature(), context.humidity(), context.season(), context.greenhouseControlled(), grapeStack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean matchesClimateOnly(Level level, BlockPos pos, ItemStack grapeStack) {
        return matchesClimateOnly(level, capture(level, pos), grapeStack);
    }

    public static boolean matchesClimateOnly(Level level, ClimateContext context, ItemStack grapeStack) {
        for (var recipe : level.getRecipeManager().getAllRecipesFor(ModRecipes.GRAPE_CLIMATE_TYPE.get())) {
            if (recipe.matchesClimateOnly(context.temperature(), context.humidity(), grapeStack)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static GrapeClimateRequirementRecipe findRecipe(Level level, ItemStack grapeStack) {
        for (var recipe : level.getRecipeManager().getAllRecipesFor(ModRecipes.GRAPE_CLIMATE_TYPE.get())) {
            if (recipe.getGrapeType().test(grapeStack)) {
                return recipe;
            }
        }
        return null;
    }

    public static void addTooltip(Level level, BlockPos pos, ItemStack grapeStack, List<Component> text) {
        final GrapeClimateRequirementRecipe recipe = findRecipe(level, grapeStack);
        text.add(getTypeTooltip(grapeStack));
        if (recipe == null) {
            text.add(Component.translatable("kaleidoscopetfctavern.tooltip.grape.no_recipe"));
            return;
        }

        final ClimateContext context = capture(level, pos);
        text.add(getTemperatureTooltip(context.temperature(), recipe.getMinTemperature(), recipe.getMaxTemperature()));
        text.add(getHumidityTooltip(context.humidity(), recipe.getMinHumidity(), recipe.getMaxHumidity()));
        text.add(getSeasonTooltip(context.season(), recipe.getSeason(), context.greenhouseControlled()));
    }

    public static float getTemperature(Level level, BlockPos pos) {
        return GreenhouseCompat.getControlledTemperature(level, pos, Climate.getTemperature(level, pos));
    }

    public static int getHumidity(Level level, BlockPos pos) {
        final int climateHumidity = toHumidity(Climate.getRainfall(level, pos));
        final int localHumidity = FarmlandBlock.getHydration(level, findHydrationSourcePos(level, pos));
        return Math.max(climateHumidity, localHumidity);
    }

    private static BlockPos findHydrationSourcePos(Level level, BlockPos pos) {
        final BlockPos.MutableBlockPos cursor = pos.mutable();
        while (cursor.getY() > level.getMinBuildHeight()) {
            final BlockState state = level.getBlockState(cursor);
            if (!state.isAir()
                    && !(state.getBlock() instanceof TFCGrapevineTrellisBlock)
                    && !(state.getBlock() instanceof BaseGrapeCropBlock)) {
                return cursor.immutable();
            }
            cursor.move(Direction.DOWN);
        }
        return pos;
    }

    private static int toHumidity(float rainfall) {
        return Mth.clamp(Math.round(rainfall / 5f), 0, 100);
    }

    private static Component getTemperatureTooltip(float current, float min, float max) {
        final MutableComponent text = Component.translatable(
                "kaleidoscopetfctavern.tooltip.grape.temperature",
                String.format(Locale.ROOT, "%.1f", current)
        );
        if (current >= min && current <= max) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.good"));
        } else if (current < min) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.too_low", String.format(Locale.ROOT, "%.1f", min)));
        } else {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.too_high", String.format(Locale.ROOT, "%.1f", max)));
        }
        return text;
    }

    private static Component getTypeTooltip(ItemStack grapeStack) {
        return Component.translatable("kaleidoscopetfctavern.tooltip.grape.type", grapeStack.getHoverName());
    }

    private static Component getHumidityTooltip(int current, int min, int max) {
        final MutableComponent text = Component.translatable("kaleidoscopetfctavern.tooltip.grape.humidity", Integer.toString(current));
        if (current >= min && current <= max) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.good"));
        } else if (current < min) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.too_low", Integer.toString(min)));
        } else {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.too_high", Integer.toString(max)));
        }
        return text;
    }

    private static Component getSeasonTooltip(Season current, Season required, boolean greenhouseControlled) {
        final MutableComponent text = Component.translatable("kaleidoscopetfctavern.tooltip.grape.season", getSeasonName(current));
        if (greenhouseControlled) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.ignored_in_greenhouse"));
        } else if (current == required) {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.suitable", getSeasonName(required)));
        } else {
            text.append(Component.translatable("kaleidoscopetfctavern.tooltip.grape.not_suitable", getSeasonName(required)));
        }
        return text;
    }

    private static Component getSeasonName(Season season) {
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
