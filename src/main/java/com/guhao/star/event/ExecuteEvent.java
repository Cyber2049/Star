package com.guhao.star.event;

import com.guhao.star.efmex.StarAnimations;
import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Sounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.LongHitAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import javax.annotation.Nullable;
import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ExecuteEvent {
    private static final ArrayList<CapabilityItem.WeaponCategories> sekiro = new ArrayList<>();
    static {
        sekiro.add(CapabilityItem.WeaponCategories.SWORD);
        sekiro.add(CapabilityItem.WeaponCategories.DAGGER);
        sekiro.add(CapabilityItem.WeaponCategories.SHIELD);
        sekiro.add(CapabilityItem.WeaponCategories.TACHI);
        sekiro.add(CapabilityItem.WeaponCategories.UCHIGATANA);
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != event.getPlayer().getUsedItemHand())
            return;
        execute(event, event.getTarget(), event.getPlayer());
    }

    public static void execute(Entity entity, Entity sourceentity) {
        execute(null, entity, sourceentity);
    }

    private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        if (entity instanceof LivingEntity target) {
            LivingEntityPatch<?> ep = EpicFightCapabilities.getEntityPatch(target, LivingEntityPatch.class);
            if (ep != null && sourceentity instanceof Player player) {
                if ((ep.getAnimator().getPlayerFor(null).getAnimation() instanceof StaticAnimation staticAnimation && (staticAnimation == Animations.BIPED_KNEEL)) |
                        (ep.getAnimator().getPlayerFor(null).getAnimation() instanceof LongHitAnimation longHitAnimation && ((longHitAnimation == Animations.BIPED_KNEEL) | (longHitAnimation == Animations.WITHER_NEUTRALIZED) | (longHitAnimation == Animations.VEX_NEUTRALIZED) | (longHitAnimation == Animations.SPIDER_NEUTRALIZED) | (longHitAnimation == Animations.DRAGON_NEUTRALIZED) | (longHitAnimation == Animations.ENDERMAN_NEUTRALIZED) | (longHitAnimation == Animations.BIPED_COMMON_NEUTRALIZED) | (longHitAnimation == Animations.GREATSWORD_GUARD_BREAK)))) {
                    var viewVec = target.getViewVector(1.0F);
                    var viewVec_r = target.getViewVector(1.0F).reverse();
                    double sideOffsetX = -viewVec.z;
                    double sideOffsetZ = viewVec.x;
                    PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
                    if (sekiro.contains(pp.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory())) {
                        ep.cancelAnyAction();
                        ep.getAnimator().init();
                        ep.playSound(Sounds.SEKIRO,1F,1F);
                        player.teleportTo(target.getX() + viewVec.x() * 1.8, target.getY(), target.getZ() + viewVec.z() * 1.8);
                        player.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(target.getX(), target.getEyeY() - 0.1, target.getZ()));
                        target.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(player.getX(), player.getEyeY(), player.getZ()));
                        player.addEffect(new MobEffectInstance(Effect.EXECUTE.get(), 63, 0));
                        target.addEffect(new MobEffectInstance(Effect.EXECUTED.get(), 80, 0));
                        pp.playAnimationSynchronized(StarAnimations.EXECUTE_SEKIRO, 0.0F);
                        ep.getAnimator().playAnimation(StarAnimations.EXECUTED_SEKIRO, 0.0F);
                    } else {
                        ep.cancelAnyAction();
                        ep.getAnimator().init();
                        ep.playSound(Sounds.SEKIRO,1F,1F);
                        player.teleportTo(target.getX() + viewVec_r.x(), target.getY(), target.getZ() + viewVec_r.z());
                        player.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(target.getX(), target.getEyeY() - 0.1, target.getZ()));
                        target.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(player.getX(), player.getEyeY(), player.getZ()));
                        target.addEffect(new MobEffectInstance(Effect.EXECUTED.get(), 42, 0));
                        player.addEffect(new MobEffectInstance(Effect.EXECUTE.get(), 42, 0));
                        pp.playAnimationSynchronized(StarAnimations.EXECUTE, 0.0F);
                    }
                }
            }
        }
    }
}
