package com.guhao.star.event;

import com.guhao.star.efmex.StarAnimations;
import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Sounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.LongHitAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


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
    public static void onRightClickEntity(PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() != event.getPlayer().getUsedItemHand())
            return;
        execute(event, event.getPlayer(), event.getPlayer().getLevel());
    }


    public static void execute(Player player, Level level) {
        execute(null, player, level);
    }

    private static void execute(@Nullable Event event, Player player, Level level) {
        if (player == null | level.isClientSide)
            return;
        {
                final Vec3 _center = new Vec3(player.getX(), player.getEyeY(), player.getZ());
                List<LivingEntity> _entfound = level.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (LivingEntity entityiterator : _entfound) {
                    LivingEntityPatch<?> ep = EpicFightCapabilities.getEntityPatch(entityiterator, LivingEntityPatch.class);
                    if (ep != null && (entityiterator != player)) {
                        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
                        if ((ep.getAnimator().getPlayerFor(null).getAnimation() instanceof StaticAnimation staticAnimation && (staticAnimation == Animations.BIPED_KNEEL)) |
                                (ep.getAnimator().getPlayerFor(null).getAnimation() instanceof LongHitAnimation longHitAnimation && ((longHitAnimation == Animations.WITHER_NEUTRALIZED) | (longHitAnimation == Animations.VEX_NEUTRALIZED) | (longHitAnimation == Animations.SPIDER_NEUTRALIZED) | (longHitAnimation == Animations.DRAGON_NEUTRALIZED) | (longHitAnimation == Animations.ENDERMAN_NEUTRALIZED) | (longHitAnimation == Animations.BIPED_COMMON_NEUTRALIZED) | (longHitAnimation == Animations.GREATSWORD_GUARD_BREAK)))) {
                            Vec3 viewVec = ep.getOriginal().getViewVector(1.0F);
                            Vec3 viewVec_r = ep.getOriginal().getViewVector(1.0F).reverse();
//                            if (sekiro.contains(pp.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory())) {
                                ep.playSound(Sounds.SEKIRO, 1.0F, 1.0F);
                                player.teleportTo(ep.getOriginal().getX() + viewVec.x() * 1.95, ep.getOriginal().getY(), ep.getOriginal().getZ() + viewVec.z() * 1.95);
                                player.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(ep.getOriginal().getX(), ep.getOriginal().getEyeY() - 0.1, ep.getOriginal().getZ()));
                                ep.getOriginal().lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(player.getX(), player.getEyeY(), player.getZ()));
                                player.addEffect(new MobEffectInstance(Effect.EXECUTE.get(), 100, 0));
                                ep.getOriginal().addEffect(new MobEffectInstance(Effect.EXECUTED.get(), 100, 0));
                                pp.playAnimationSynchronized(StarAnimations.EXECUTE_SEKIRO, 0.0F);
                                ep.playAnimationSynchronized(StarAnimations.EXECUTED_SEKIRO, 0.0F, SPPlayAnimation::new);
                            break;
                            }
//                          else {
//                                ep.playSound(Sounds.SEKIRO, 1.0F, 1.0F);
//                                if(!level.isClientSide) ep.playAnimationSynchronized(Animations.BIPED_HOLD_GREATSWORD,0.0f);
//                                player.teleportTo(ep.getOriginal().getX() - viewVec_r.x(), ep.getOriginal().getY(), ep.getOriginal().getZ() - viewVec_r.z());
//                                player.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(ep.getOriginal().getX(), ep.getOriginal().getEyeY() - 0.1, ep.getOriginal().getZ()));
//                                ep.getOriginal().lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(player.getX(), player.getEyeY(), player.getZ()));
//                                ep.getOriginal().addEffect(new MobEffectInstance(Effect.EXECUTED.get(), 42, 0));
//                                player.addEffect(new MobEffectInstance(Effect.EXECUTE.get(), 42, 0));
//                                pp.playAnimationSynchronized(StarAnimations.EXECUTE, 0.0f);
//                                break;
//                            }
                        }
                    }
                }
        }
    }
//}
