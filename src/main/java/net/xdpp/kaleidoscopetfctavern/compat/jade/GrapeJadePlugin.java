package net.xdpp.kaleidoscopetfctavern.compat.jade;

import net.xdpp.kaleidoscopetfctavern.block.plant.BaseGrapeCropBlock;
import net.xdpp.kaleidoscopetfctavern.block.plant.TFCGrapevineTrellisBlock;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public final class GrapeJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registry) {
        registry.registerBlockComponent(GrapeTooltipProvider.INSTANCE, TFCGrapevineTrellisBlock.class);
        registry.registerBlockComponent(GrapeTooltipProvider.INSTANCE, BaseGrapeCropBlock.class);
    }
}
