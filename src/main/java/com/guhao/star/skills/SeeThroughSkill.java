package com.guhao.star.skills;

import com.guhao.star.regirster.Keys;
import com.guhao.star.units.Guard_Array;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class SeeThroughSkill extends Skill {
    private final int active = 60;
    private static final UUID EVENT_UUID = UUID.fromString("31a396ea-0361-11ee-be56-0242ac114514");
    private boolean isCutDown;
    public SeeThroughSkill(Builder<? extends Skill> builder) {
        super(builder);
    }

    public static final SkillDataManager.SkillDataKey<Boolean> CUT;
    public static final SkillDataManager.SkillDataKey<Boolean> LEG;
    public static final SkillDataManager.SkillDataKey<Integer> ACTIVE_TIME;

    public static Builder createSeeThroughSkillBuilder() {
        return (new Builder())
                .setCategory(SkillCategories.IDENTITY)
                .setActivateType(ActivateType.DURATION)
                .setResource(Resource.NONE);
    }
    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(CUT);
        container.getDataManager().registerData(LEG);
        container.getDataManager().registerData(ACTIVE_TIME);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
            LivingEntityPatch<?> ep = EpicFightCapabilities.getEntityPatch(event.getDamageSource().getEntity(), LivingEntityPatch.class);
            if (ep != null && event.isParried() && ep.getAnimator().getPlayerFor(null).getAnimation() instanceof StaticAnimation animation && Guard_Array.isNoParry(animation)) {
                container.getExecuter().getOriginal().addEffect(new MobEffectInstance(MobEffects.GLOWING,60,60,false,false));
                container.getDataManager().setData(CUT,true);
                container.getDataManager().setData(ACTIVE_TIME,0);
                container.getDataManager().setData(LEG,false);
            }
            if (event.getResult() == AttackResult.ResultType.SUCCESS) {
                container.getDataManager().setData(ACTIVE_TIME,active);
            }
        });
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            LivingEntityPatch<?> ep = EpicFightCapabilities.getEntityPatch(event.getDamageSource().getEntity(), LivingEntityPatch.class);
                if (ep != null && ep.getAnimator().getPlayerFor(null).getAnimation() instanceof StaticAnimation animation && Guard_Array.canDodge(animation)) {
                    container.getExecuter().getOriginal().addEffect(new MobEffectInstance(MobEffects.GLOWING,60,60,false,false));
                    container.getDataManager().setData(LEG, true);
                    container.getDataManager().setData(ACTIVE_TIME, 0);
                    container.getDataManager().setData(CUT, false);
                }
        });

    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        isCutDown = Keys.CUT.isDown();
        if (container.getDataManager().getDataValue(ACTIVE_TIME) == null) container.getDataManager().setData(ACTIVE_TIME,active);
        if (container.getDataManager().getDataValue(CUT) == null) container.getDataManager().setData(CUT,false);
        if (container.getDataManager().getDataValue(LEG) == null) container.getDataManager().setData(LEG,false);
        if (container.getDataManager().getDataValue(ACTIVE_TIME) >= active) {
            container.getDataManager().setData(CUT,false);
            container.getDataManager().setData(LEG,false);
        }
        if (container.getDataManager().getDataValue(ACTIVE_TIME) < active)  container.getDataManager().setData(ACTIVE_TIME,container.getDataManager().getDataValue(ACTIVE_TIME) + 1);


        boolean isCut = container.getDataManager().getDataValue(CUT);
        boolean isLeg = container.getDataManager().getDataValue(LEG);
        if (isCut && isCutDown && (!(container.getExecuter().getAnimator().getPlayerFor(null).getAnimation() instanceof AttackAnimation))) {
            container.getExecuter().playAnimationSynchronized(Animations.RUSHING_TEMPO2,0.0F);
            container.getExecuter().getOriginal().removeEffect(MobEffects.GLOWING);
            container.getDataManager().setData(CUT,false);
            container.getDataManager().setData(ACTIVE_TIME,active);
        }
        if (isLeg && isCutDown && (!(container.getExecuter().getAnimator().getPlayerFor(null).getAnimation() instanceof AttackAnimation))) {
            container.getExecuter().playAnimationSynchronized(Animations.REVELATION_TWOHAND,0.0F);
            container.getExecuter().getOriginal().removeEffect(MobEffects.GLOWING);
            container.getDataManager().setData(LEG,false);
            container.getDataManager().setData(ACTIVE_TIME,active);
        }
    }

    static {
        CUT = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        LEG = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        ACTIVE_TIME = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
    }
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void executeOnClient(LocalPlayerPatch executer, FriendlyByteBuf args) {
//        isCutDown = Keys.CUT.isDown();
//    }

//    @Override
//    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
//        LocalPlayerPatch lpp = EpicFightCapabilities.getEntityPatch(mc.player, LocalPlayerPatch.class);
//        this.executeOnClient(lpp,args);
//        boolean isCut = executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(CUT);
//        boolean isLeg = executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(LEG);
//        if (isCut && isCutDown && (!(executer.getAnimator().getPlayerFor(null).getAnimation() instanceof AttackAnimation))) {
//            executer.playAnimationSynchronized(Animations.RUSHING_TEMPO2,0.0F);
//            executer.getSkill(StarSkill.SEE_THROUGH).getDataManager().setData(ACTIVE_TIME,60);
//        }
//        if (isLeg && isCutDown && (!(executer.getAnimator().getPlayerFor(null).getAnimation() instanceof AttackAnimation))) {
//            executer.playAnimationSynchronized(Animations.REVELATION_TWOHAND,0.0F);
//            executer.getSkill(StarSkill.SEE_THROUGH).getDataManager().setData(ACTIVE_TIME,60);
//        }
//        super.executeOnServer(executer, args);
//    }
}

