package com.guhao.star.mixins;

import com.guhao.star.Config;
import com.guhao.star.regirster.Sounds;
import com.guhao.star.units.Guard_Array;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.TechnicianSkill;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mixin(value = TechnicianSkill.class,remap = false)
public class TechnicianSkillMixin extends PassiveSkill {
    Guard_Array g = new Guard_Array();
    StaticAnimation[] GUARD = g.getGuard();
    @Unique
    final Boolean star_new$isSlow = Config.SLOW_TIME.get();
    @Shadow
    private static final UUID EVENT_UUID = UUID.fromString("99e5c782-fdaf-11eb-9a03-0242ac130003");

    @Unique
    public void star_new$delayedTask(SkillContainer container) {
        if (container.getExecuter().getOriginal().getLevel() instanceof ServerLevel _level) {
            _level.getServer().getCommands().performCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ()), Vec2.ZERO, _level, 4, "", new TextComponent(""), _level.getServer(), null).withSuppressedOutput(), "tickrate change 20");
        }
    }


    public TechnicianSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            float consumption = container.getExecuter().getModifiedStaminaConsume(container.getExecuter().getSkill(SkillSlots.DODGE).getSkill().getConsumption());
            float add = container.getExecuter().getMaxStamina() - container.getExecuter().getStamina();
            container.getExecuter().setStamina(container.getExecuter().getStamina() + consumption + (add * 0.1F));
            Entity entity = container.getExecuter().getOriginal();
            entity.level.addParticle(EpicFightParticles.ENTITY_AFTER_IMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
            container.getExecuter().playSound(Sounds.FORESIGHT,0.8f,1.2f);
            if(star_new$isSlow) {
                if (container.getExecuter().getOriginal().getLevel() instanceof ServerLevel _level) {
                    _level.getServer().getCommands().performCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY(), container.getExecuter().getOriginal().getZ()), Vec2.ZERO, _level, 4, "", new TextComponent(""), _level.getServer(), null).withSuppressedOutput(), "tickrate change 2");
                }
                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                scheduledExecutorService.schedule(() -> star_new$delayedTask(container), 750, TimeUnit.MILLISECONDS);
            }
        } ,1);
    }
}
