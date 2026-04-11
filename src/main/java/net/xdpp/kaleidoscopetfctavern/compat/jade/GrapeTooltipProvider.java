package net.xdpp.kaleidoscopetfctavern.compat.jade;

import net.dries007.tfc.common.blocks.soil.HoeOverlayBlock;
import net.dries007.tfc.config.TFCConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.xdpp.kaleidoscopetfctavern.Kaleidoscopetfctavern;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

public enum GrapeTooltipProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = Kaleidoscopetfctavern.modLoc("grape_climate");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor access, IPluginConfig config) {
        if (!TFCConfig.CLIENT.showHoeOverlaysInInfoMods.get()) {
            return;
        }
        if (!(access.getBlockState().getBlock() instanceof HoeOverlayBlock overlay)) {
            return;
        }

        final List<Component> lines = new ArrayList<>();
        overlay.addHoeOverlayInfo(access.getLevel(), access.getPosition(), access.getBlockState(), lines, false);
        lines.forEach(tooltip::add);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
