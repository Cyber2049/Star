package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

@Mixin(value = HumanoidMobPatch.class,remap = false)
public abstract class HumanoidMobPatchMixin<T extends PathfinderMob> extends MobPatch<T> {
    public HumanoidMobPatchMixin(Faction faction) {
        super(faction);
    }

    @Inject(method = "setAIAsInfantry",at = @At("HEAD"), cancellable = true)
    public void setAIAsInfantry(boolean holdingRanedWeapon, CallbackInfo ci) {
        if (this.getOriginal().hasEffect(Effect.EXECUTED.get())) ci.cancel();
    }
}
