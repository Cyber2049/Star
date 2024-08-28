package com.guhao.star.mixins;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.*;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.weaponinnate.BladeRushSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(value = BladeRushSkill.class,remap = false)
public class BladeRushSkillMixin extends WeaponInnateSkill {
    public BladeRushSkillMixin(Builder builder, Map<EntityType<?>, StaticAnimation> tryAnimations) {
        super(builder);
        this.tryAnimations = tryAnimations;

        this.comboAnimations = new StaticAnimation[3];
        this.comboAnimations[0] = Animations.BLADE_RUSH_COMBO1;
        this.comboAnimations[1] = Animations.BLADE_RUSH_COMBO2;
        this.comboAnimations[2] = Animations.BLADE_RUSH_COMBO3;
    }

    @Shadow
    private static final SkillDataManager.SkillDataKey<Integer> COMBO_COUNT = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
    @Shadow
    private static final UUID EVENT_UUID = UUID.fromString("444a1a6a-c2f1-11eb-8529-0242ac130003");
    @Mutable
    @Final
    @Shadow
    private final StaticAnimation[] comboAnimations;
    @Mutable
    @Final
    @Shadow
    private final Map<EntityType<?>, StaticAnimation> tryAnimations;
    /**
     * @author
     * @reason
     */
    @Overwrite
    @Override
    public WeaponInnateSkill registerPropertiesToAnimation() {
        ((AttackAnimation)Animations.BLADE_RUSH_COMBO1).phases[0].addProperties(this.properties.get(0).entrySet());
        ((AttackAnimation)Animations.BLADE_RUSH_COMBO2).phases[0].addProperties(this.properties.get(0).entrySet());
        ((AttackAnimation)Animations.BLADE_RUSH_COMBO3).phases[0].addProperties(this.properties.get(0).entrySet());
        ((AttackAnimation)Animations.BLADE_RUSH_EXECUTE_BIPED).phases[0].addProperties(this.properties.get(0).entrySet());
        return this;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    @Override
    public FriendlyByteBuf gatherArguments(LocalPlayerPatch executer, ControllEngine controllEngine) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(true);

        return buf;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);

        container.getDataManager().registerData(COMBO_COUNT);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
            if (event.getDamageSource().getAnimation().between(Animations.BLADE_RUSH_COMBO1, Animations.BLADE_RUSH_COMBO3) && event.getTarget().isAlive() && this.tryAnimations.containsKey(event.getTarget().getType())) {
                MobEffectInstance effectInstance = event.getTarget().getEffect(EpicFightMobEffects.INSTABILITY.get());
                int amp = effectInstance == null ? 0 : effectInstance.getAmplifier() + 1;
                event.getTarget().addEffect(new MobEffectInstance(EpicFightMobEffects.INSTABILITY.get(), 100, amp));
            }
        });
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    @Override
    public void onRemoved(SkillContainer container) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static BladeRushSkill.Builder createBladeRushBuilder() {
        return (new BladeRushSkill.Builder()).setCategory(SkillCategories.WEAPON_INNATE)
                .setResource(Resource.WEAPON_INNATE_ENERGY)
                .putTryAnimation(EntityType.ZOMBIE, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.HUSK, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.DROWNED, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.SKELETON, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.STRAY, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.CREEPER, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.IRON_GOLEM, Animations.BLADE_RUSH_TRY);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        LivingEntity target = executer.getTarget();
        boolean execute = false;

        if (target != null) {
            if (target.hasEffect(EpicFightMobEffects.INSTABILITY.get()) && target.getEffect(EpicFightMobEffects.INSTABILITY.get()).getAmplifier() >= 2) {
                execute = true;
            }

            LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(target, LivingEntityPatch.class);

            if (entitypatch != null && entitypatch.getEntityState().hurtLevel() > 1 && this.tryAnimations.containsKey(target.getType())) {
                execute = true;
            }
        }

        if (execute) {
            executer.getSkill(this).getDataManager().setData(COMBO_COUNT, 0);
            executer.playAnimationSynchronized(Animations.BLADE_RUSH_TRY, 0);
        } else {
            int animationId = executer.getSkill(this).getDataManager().getDataValue(COMBO_COUNT);
            executer.getSkill(this).getDataManager().setDataF(COMBO_COUNT, (v) -> (v + 1) % this.comboAnimations.length);
            executer.playAnimationSynchronized(this.comboAnimations[animationId], 0);
        }

        super.executeOnServer(executer, args);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    @Override
    public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
        List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
        this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(0), "Each Strike:");
        this.generateTooltipforPhase(list, itemStack, cap, playerCap, this.properties.get(1), "Execution:");
        return list;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Override
    public boolean checkExecuteCondition(PlayerPatch<?> executer) {
        return executer.getTarget() != null && executer.getTarget().isAlive();
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    @OnlyIn(Dist.CLIENT)
    @Override
    public void onScreen(LocalPlayerPatch playerpatch, float resolutionX, float resolutionY) {

    }

}
