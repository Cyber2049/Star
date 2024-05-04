package com.guhao.star.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.TechnicianSkill;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

@Mixin(value = TechnicianSkill.class,remap = false)
public class TechnicianSkillMixin extends PassiveSkill {
    @Shadow
    private static final UUID EVENT_UUID = UUID.fromString("99e5c782-fdaf-11eb-9a03-0242ac130003");

    public TechnicianSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "onInitiate",at = @At("TAIL"))
    public void onInitiate(SkillContainer container, CallbackInfo ci) {
        super.onInitiate(container);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            float consumption = container.getExecuter().getModifiedStaminaConsume(container.getExecuter().getSkill(SkillSlots.DODGE).getSkill().getConsumption());
            float add = container.getExecuter().getMaxStamina() - container.getExecuter().getStamina();
            container.getExecuter().setStamina(container.getExecuter().getStamina() + consumption + add * 0.1F);
        });
    }
}
