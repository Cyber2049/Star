package com.guhao.star.mixins;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import static yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch.STAMINA;

@Mixin(value = PlayerPatch.class,remap = false)
public class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {

    @Shadow
    protected int tickSinceLastAction;
    @Shadow
    public float getMaxStamina() {
        AttributeInstance maxStamina = this.original.getAttribute(EpicFightAttributes.MAX_STAMINA.get());
        return (float)(maxStamina == null ? 0.0 : maxStamina.getValue());
    }
    @Shadow
    public float getStamina() {
        return getMaxStamina() == 0.0F ? 0.0F : this.original.getEntityData().get(STAMINA);
    }
    @Shadow
    public void setStamina(float value) {
        float f1 = Math.max(Math.min(value, this.getMaxStamina()), 0.0F);
        this.original.getEntityData().set(STAMINA, f1);
    }
    @Shadow
    protected double xo;
    @Shadow
    protected double yo;
    @Shadow
    protected double zo;


    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        clientAnimator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
        clientAnimator.addLivingAnimation(LivingMotions.WALK, Animations.BIPED_WALK);
        clientAnimator.addLivingAnimation(LivingMotions.RUN, Animations.BIPED_RUN);
        clientAnimator.addLivingAnimation(LivingMotions.SNEAK, Animations.BIPED_SNEAK);
        clientAnimator.addLivingAnimation(LivingMotions.SWIM, Animations.BIPED_SWIM);
        clientAnimator.addLivingAnimation(LivingMotions.FLOAT, Animations.BIPED_FLOAT);
        clientAnimator.addLivingAnimation(LivingMotions.KNEEL, Animations.BIPED_KNEEL);
        clientAnimator.addLivingAnimation(LivingMotions.FALL, Animations.BIPED_FALL);
        clientAnimator.addLivingAnimation(LivingMotions.MOUNT, Animations.BIPED_MOUNT);
        clientAnimator.addLivingAnimation(LivingMotions.SIT, Animations.BIPED_SIT);
        clientAnimator.addLivingAnimation(LivingMotions.FLY, Animations.BIPED_FLYING);
        clientAnimator.addLivingAnimation(LivingMotions.DEATH, Animations.BIPED_DEATH);
        clientAnimator.addLivingAnimation(LivingMotions.JUMP, Animations.BIPED_JUMP);
        clientAnimator.addLivingAnimation(LivingMotions.CLIMB, Animations.BIPED_CLIMBING);
        clientAnimator.addLivingAnimation(LivingMotions.SLEEP, Animations.BIPED_SLEEPING);
        clientAnimator.addLivingAnimation(LivingMotions.CREATIVE_FLY, Animations.BIPED_CREATIVE_FLYING);
        clientAnimator.addLivingAnimation(LivingMotions.CREATIVE_IDLE, Animations.BIPED_CREATIVE_IDLE);
        clientAnimator.addLivingAnimation(LivingMotions.DIGGING, Animations.BIPED_DIG);
        clientAnimator.addLivingAnimation(LivingMotions.AIM, Animations.BIPED_BOW_AIM);
        clientAnimator.addLivingAnimation(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT);
        clientAnimator.addLivingAnimation(LivingMotions.DRINK, Animations.BIPED_DRINK);
        clientAnimator.addLivingAnimation(LivingMotions.EAT, Animations.BIPED_EAT);
        clientAnimator.addLivingAnimation(LivingMotions.SPECTATE, Animations.BIPED_SPYGLASS_USE);
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean b) {

    }

    protected void initAttributes() {
        super.initAttributes();
        this.original.getAttribute(EpicFightAttributes.MAX_STAMINA.get()).setBaseValue(12.0);
        this.original.getAttribute(EpicFightAttributes.STAMINA_REGEN.get()).setBaseValue(1.0);
        this.original.getAttribute(EpicFightAttributes.OFFHAND_IMPACT.get()).setBaseValue(0.5);
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        if (this.original.getVehicle() != null) {
            return Animations.BIPED_HIT_ON_MOUNT;
        } else {
            switch(stunType) {
                case LONG:
                    return Animations.BIPED_HIT_LONG;
                case SHORT:
                    return Animations.BIPED_HIT_SHORT;
                case HOLD:
                    return Animations.BIPED_HIT_SHORT;
                case KNOCKDOWN:
                    return Animations.BIPED_KNOCKDOWN;
                case NEUTRALIZE:
                    return Animations.BIPED_COMMON_NEUTRALIZED;
                case FALL:
                    return Animations.BIPED_LANDING;
                case NONE:
                    return null;
            }
        }

        return null;
    }

    public void serverTick(LivingEvent.LivingUpdateEvent event) {
        super.serverTick(event);
        if (this.state.canBasicAttack()) {
            ++tickSinceLastAction;
        }

        float stamina = getStamina();
        float maxStamina = getMaxStamina();
        float staminaRegen = (float) this.original.getAttributeValue(EpicFightAttributes.STAMINA_REGEN.get());
        int regenStandbyTime = 900 / (int)(150.0F * staminaRegen);
        if (stamina < maxStamina && tickSinceLastAction > regenStandbyTime) {
            float staminaFactor = 1.0F;
            setStamina(stamina + staminaFactor * (0.075F + maxStamina * 0.00625F) * staminaRegen);
        }

        if (maxStamina < stamina) {
            setStamina(maxStamina);
        }

        xo = this.original.getX();
        yo = this.original.getY();
        zo = this.original.getZ();
    }
}
