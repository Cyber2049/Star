package com.guhao.star.mixins;

import com.guhao.star.api.HitAnimationMixin;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = PlayerPatch.class , remap = false)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> implements HitAnimationMixin {


    /*
    @Unique
    public StaticAnimation star_new$getHitAnimation(StunType stunType) {
        if (((Player)this.original).getVehicle() != null) {
            return Animations.BIPED_HIT_ON_MOUNT;
        } else {
            switch (stunType) {
                case LONG -> {
                    if (!(this.getOriginal().hasEffect(Effect.REALLY_STUN_IMMUNITY.get()))) {
                        return Animations.BIPED_HIT_LONG;
                    } else {
                        return null;
                    }
                }
                case SHORT, HOLD -> {
                    if (!(this.getOriginal().hasEffect(Effect.REALLY_STUN_IMMUNITY.get()))) {
                        return Animations.BIPED_HIT_SHORT;
                    } else {
                        return null;
                    }
                }
                case KNOCKDOWN -> {
                    if (!(this.getOriginal().hasEffect(Effect.REALLY_STUN_IMMUNITY.get()))) {
                        return Animations.BIPED_KNOCKDOWN;
                    } else {
                        return null;
                    }
                }
                case NEUTRALIZE -> {
                    if (!(this.getOriginal().hasEffect(Effect.REALLY_STUN_IMMUNITY.get()))) {
                        return Animations.BIPED_COMMON_NEUTRALIZED;
                    } else {
                        return null;
                    }
                }
                case FALL -> {
                    if (!(this.getOriginal().hasEffect(Effect.REALLY_STUN_IMMUNITY.get()))) {
                        return Animations.BIPED_LANDING;
                    } else {
                        return null;
                    }
                }
                case NONE -> {
                    return null;
                }
                default -> {
                    return null;
                }
            }
        }
    }

     */
}


