package com.guhao.star.skills;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public class SeeThroughSkill extends Skill {
    protected int seethrough;
    private static final UUID EVENT_UUID = UUID.fromString("31a396ea-0361-11ee-be56-0242ac120002");
    protected static final SkillDataManager.SkillDataKey<Integer> STACKS;
    protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation>> motions;
    protected final Map<EntityType<?>, Integer> maxSeeThroughStacks = Maps.newHashMap();
    protected int blockStack;
    protected int parryStack;
    protected int dodgeStack;
    protected int defaultSeeThroughStacks;

    public static SeeThroughSkill.Builder createSeeThroughSkillBuilder() {
        return (new SeeThroughSkill.Builder()).setCategory(SkillCategories.IDENTITY).setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.NONE).addMotion(CapabilityItem.WeaponCategories.LONGSWORD, (item, player) -> {
            return Animations.REVELATION_TWOHAND;
        }).addMotion(CapabilityItem.WeaponCategories.GREATSWORD, (item, player) -> {
            return Animations.REVELATION_TWOHAND;
        }).addMotion(CapabilityItem.WeaponCategories.TACHI, (item, player) -> {
            return Animations.REVELATION_TWOHAND;
        });
    }

    public SeeThroughSkill(SeeThroughSkill.Builder builder) {
        super(builder);
        this.motions = builder.motions;
    }

    public void setParams(CompoundTag parameters) {
        super.setParams(parameters);
        this.maxSeeThroughStacks.clear();
        this.blockStack = parameters.getInt("block_stacks");
        this.parryStack = parameters.getInt("parry_stacks");
        this.dodgeStack = parameters.getInt("dodge_stacks");
        this.defaultSeeThroughStacks = parameters.getInt("default_SeeThrough_stacks");
        CompoundTag maxStacks = parameters.getCompound("max_SeeThroughs");
        Iterator var3 = maxStacks.getAllKeys().iterator();

        while(var3.hasNext()) {
            String registryName = (String)var3.next();
            EntityType<?> entityType = (EntityType)EntityType.byString(registryName).orElse((EntityType<?>) null);
            if (entityType != null) {
                this.maxSeeThroughStacks.put(entityType, maxStacks.getInt(registryName));
            } else {
                EpicFightMod.LOGGER.warn("SeeThrough registry error: no entity type named : " + registryName);
            }
        }

    }

    public void onInitiate(SkillContainer container) {
        container.getDataManager().registerData(STACKS);
        PlayerEventListener listener = container.getExecuter().getEventListener();
        listener.addEventListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID, (event) -> {
            if (container.getExecuter().isLogicalClient()) {
                Skill skill = event.getSkillContainer().getSkill();
                if (skill.getCategory() != SkillCategories.WEAPON_INNATE) {
                    return;
                }

                if (container.getExecuter().getTarget() != null) {
                    LivingEntityPatch<?> entitypatch = (LivingEntityPatch) EpicFightCapabilities.getEntityPatch(container.getExecuter().getTarget(), LivingEntityPatch.class);
                    if (entitypatch != null && container.isActivated() && container.sendExecuteRequest((LocalPlayerPatch)container.getExecuter(), ClientEngine.getInstance().controllEngine).isExecutable()) {
                        container.setDuration(0);
                        event.setCanceled(true);
                    }
                }
            }

        });
        listener.addEventListener(PlayerEventListener.EventType.SET_TARGET_EVENT, EVENT_UUID, (event) -> {
            container.getDataManager().setDataSync(STACKS, 100, (ServerPlayer)((ServerPlayerPatch)event.getPlayerPatch()).getOriginal());
        });
        listener.addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            LivingEntity target = container.getExecuter().getTarget();
            if (target != null && target.is(event.getDamageSource().getDirectEntity())) {
                this.checkStackAndActivate(container, (ServerPlayerPatch)event.getPlayerPatch(), target, (Integer)container.getDataManager().getDataValue(STACKS), this.dodgeStack);
            }

        }, -1);
        listener.addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
            if (event.getResult() == AttackResult.ResultType.BLOCKED) {
                LivingEntity target = container.getExecuter().getTarget();
                if (target != null && target.is(((DamageSource)event.getDamageSource()).getDirectEntity())) {
                    int stacks = event.isParried() ? this.parryStack : this.blockStack;
                    this.checkStackAndActivate(container, (ServerPlayerPatch)event.getPlayerPatch(), target, (Integer)container.getDataManager().getDataValue(STACKS), stacks);
                }
            }

        }, -1);
        listener.addEventListener(PlayerEventListener.EventType.TARGET_INDICATOR_ALERT_CHECK_EVENT, EVENT_UUID, (event) -> {
            if (container.isActivated()) {
                event.setCanceled(false);
            }

        });
    }

    public void onRemoved(SkillContainer container) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SET_TARGET_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.TARGET_INDICATOR_ALERT_CHECK_EVENT, EVENT_UUID);
    }

    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        super.executeOnServer(executer, args);
        CapabilityItem holdingItem = executer.getHoldingItemCapability(InteractionHand.MAIN_HAND);
        StaticAnimation animation = this.motions.containsKey(holdingItem.getWeaponCategory()) ? (StaticAnimation)((BiFunction)this.motions.get(holdingItem.getWeaponCategory())).apply(holdingItem, executer) : Animations.REVELATION_ONEHAND;
        executer.playAnimationSynchronized(animation, 0.0F);
    }

    public void checkStackAndActivate(SkillContainer container, ServerPlayerPatch playerpatch, LivingEntity target, int stacks, int addStacks) {
        int maxStackSize = (Integer)this.maxSeeThroughStacks.getOrDefault(target.getType(), this.defaultSeeThroughStacks);
        int plusStack = stacks + addStacks;
        if (plusStack < maxStackSize) {
            container.getDataManager().setDataSync(STACKS, plusStack, (ServerPlayer)playerpatch.getOriginal());
        } else {
            if (!container.isActivated()) {
                this.setDurationSynchronize(playerpatch, this.maxDuration);
            }

            container.getDataManager().setDataSync(STACKS, 0, (ServerPlayer)playerpatch.getOriginal());
        }

    }


    static {
        STACKS = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
    }

    public static class Builder extends Skill.Builder<SeeThroughSkill> {
        protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation>> motions = Maps.newHashMap();

        public Builder() {
        }

        public SeeThroughSkill.Builder setCategory(SkillCategory category) {
            this.category = category;
            return this;
        }

        public SeeThroughSkill.Builder setActivateType(Skill.ActivateType activateType) {
            this.activateType = activateType;
            return this;
        }

        public SeeThroughSkill.Builder setResource(Skill.Resource resource) {
            this.resource = resource;
            return this;
        }

        public SeeThroughSkill.Builder setCreativeTab(CreativeModeTab tab) {
            this.tab = tab;
            return this;
        }

        public SeeThroughSkill.Builder addMotion(WeaponCategory weaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation> function) {
            this.motions.put(weaponCategory, function);
            return this;
        }
    }
}
