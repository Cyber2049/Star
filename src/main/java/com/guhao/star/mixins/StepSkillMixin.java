package com.guhao.star.mixins;

import com.guhao.star.Config;
import com.guhao.star.regirster.Sounds;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.dodge.StepSkill;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mixin(value = StepSkill.class,remap = false)
public class StepSkillMixin extends Skill {
    public StepSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }
    @Unique
    final Boolean star_new$isSlow = Config.SLOW_TIME.get();
    private static final UUID EVENT_UUID = UUID.fromString("99e5c782-fdaf-11eb-9a03-0242ac130004");
    @Unique
    public void star_new$delayedTask(SkillContainer container) {
        if (container.getExecuter().getOriginal().getLevel() instanceof ServerLevel _level) {
            _level.getServer().getCommands().performCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ()), Vec2.ZERO, _level, 4, "", new TextComponent(""), _level.getServer(), null).withSuppressedOutput(), "tickrate change 20");
        }
    }
    @Inject(method = "onInitiate",at = @At("HEAD"))
    public void onInitiate(SkillContainer container, CallbackInfo ci) {
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            if (container.getExecuter().getOriginal().getLevel() instanceof ServerLevel level) level.addAlwaysVisibleParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ(), Double.longBitsToDouble(container.getExecuter().getOriginal().getId()), 0, 0);
            container.getExecuter().playSound(Sounds.FORESIGHT,0.8f,1.2f);
            if(star_new$isSlow) {
                if (container.getExecuter().getOriginal().getLevel() instanceof ServerLevel _level) {
                    _level.getServer().getCommands().performCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ()), Vec2.ZERO, _level, 4, "", new TextComponent(""), _level.getServer(), null).withSuppressedOutput(), "tickrate change 2");
                }
                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                scheduledExecutorService.schedule(() -> star_new$delayedTask(container), 100, TimeUnit.MILLISECONDS);
            }
        });
    }
    @Inject(method = "onRemoved",at = @At("HEAD"))
    public void onRemoved(SkillContainer container, CallbackInfo ci) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
    }
}
