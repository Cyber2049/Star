package com.guhao.star.units;


import L_Ender.cataclysm.entity.projectile.Ignis_Abyss_Fireball_Entity;
import L_Ender.cataclysm.entity.projectile.Ignis_Fireball_Entity;
import L_Ender.cataclysm.init.ModEntities;
import cc.xypp.damage_number.network.DamagePackage;
import cc.xypp.damage_number.network.Network;
import com.guhao.star.efmex.StarAnimations;
import com.guhao.star.regirster.Sounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import yesman.epicfight.api.animation.types.LongHitAnimation;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.guhao.ranksystem.ServerEventExtra.*;

public class BattleUnit {
    public BattleUnit() {
    }

    public static void fire(LivingEntityPatch<?> livingEntityPatch) {
        OpenMatrix4f transformMatrix = livingEntityPatch.getArmature().getBindedTransformFor(livingEntityPatch.getArmature().getPose(1.0F), Armatures.BIPED.toolR);
        OpenMatrix4f transformMatrix2 = livingEntityPatch.getArmature().getBindedTransformFor(livingEntityPatch.getArmature().getPose(1.0F), Armatures.BIPED.toolR);
        transformMatrix.translate(new Vec3f(0.0F, -0.6F, -0.3F));
        transformMatrix2.translate(new Vec3f(0.0F, -0.8F, -0.3F));
        OpenMatrix4f CORRECTION = (new OpenMatrix4f()).rotate(-((float) Math.toRadians(livingEntityPatch.getOriginal().yRotO + 180.0F)), new Vec3f(0.0F, 1.0F, 0.0F));
        OpenMatrix4f.mul(CORRECTION, transformMatrix, transformMatrix);
        OpenMatrix4f.mul(CORRECTION, transformMatrix2, transformMatrix2);
        int n = 40;
        double r = 0.2;
        double t = 0.01;

        for (int i = 0; i < n; ++i) {
            double theta = 6.283185307179586 * (new Random()).nextDouble();
            double phi = ((new Random()).nextDouble() - 0.5) * Math.PI * t / r;
            double x = r * Math.cos(phi) * Math.cos(theta);
            double y = r * Math.cos(phi) * Math.sin(theta);
            double z = r * Math.sin(phi);
            Vec3f direction = new Vec3f((float) x, (float) y, (float) z);
            OpenMatrix4f rotation = (new OpenMatrix4f()).rotate(-((float) Math.toRadians(livingEntityPatch.getOriginal().yBodyRotO)), new Vec3f(0.0F, 1.0F, 0.0F));
            rotation.rotate((transformMatrix.m11 + 0.07F) * 1.5F, new Vec3f(1.0F, 0.0F, 0.0F));
            OpenMatrix4f.transform3v(rotation, direction, direction);
            livingEntityPatch.getOriginal().level.addParticle(ParticleTypes.FLAME,
                    livingEntityPatch.getOriginal().getX(),
                    livingEntityPatch.getOriginal().getEyeY() + 1.0,
                    livingEntityPatch.getOriginal().getZ(),
                    transformMatrix2.m30 - transformMatrix.m30 + direction.x,
                    transformMatrix2.m31 - transformMatrix.m31 + direction.y,
                    transformMatrix2.m32 - transformMatrix.m32 + direction.z);
        }
    }

    public static void fireball(LivingEntityPatch<?> livingEntityPatch) {
        float speed = 2.5F;
        Entity _shootFrom = livingEntityPatch.getOriginal();
        Level projectileLevel = _shootFrom.level;
        Projectile _entityToSpawn = new Object() {
            public Projectile getProjectile() {
                Level level = livingEntityPatch.getOriginal().level;
                Entity shooter = livingEntityPatch.getOriginal();
                Ignis_Fireball_Entity entityToSpawn = new Ignis_Fireball_Entity(ModEntities.IGNIS_FIREBALL.get(), level);
                entityToSpawn.setOwner(shooter);
                return entityToSpawn;
            }
        }.getProjectile();
        _entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY(), _shootFrom.getZ());
        _entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, speed, 0);
        projectileLevel.addFreshEntity(_entityToSpawn);
    }

    public static void ab_fireball(LivingEntityPatch<?> livingEntityPatch) {
        float speed = 2.5F;
        Entity _shootFrom = livingEntityPatch.getOriginal();
        Level projectileLevel = _shootFrom.level;
        Projectile _entityToSpawn = new Object() {
            public Projectile getProjectile() {
                Level level = livingEntityPatch.getOriginal().level;
                Entity shooter = livingEntityPatch.getOriginal();
                Ignis_Abyss_Fireball_Entity entityToSpawn = new Ignis_Abyss_Fireball_Entity(ModEntities.IGNIS_ABYSS_FIREBALL.get(), level);
                entityToSpawn.setOwner(shooter);
                return entityToSpawn;
            }
        }.getProjectile();
        _entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY(), _shootFrom.getZ());
        _entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, speed, 0);
        projectileLevel.addFreshEntity(_entityToSpawn);
    }

    public static void execute_socres(LivingEntityPatch<?> livingEntityPatch) {
        if (livingEntityPatch.getOriginal().getType() == net.minecraft.world.entity.EntityType.PLAYER) {
            String uid = livingEntityPatch.getOriginal().getUUID().toString();
            damageCount.put(uid, damageCount.getOrDefault(uid, 0) + 1);
            userDamage.put(uid, 100f + userDamage.getOrDefault(uid, 0.0f));
            keepUntil.put(uid, new Date().getTime() + 9000);
            Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) livingEntityPatch.getOriginal()),
                    new DamagePackage("emit",
                            userDamage.get(uid),
                            damageCount.get(uid),
                            100f));
        }
    }

    public static void turn(LivingEntityPatch<?> livingEntityPatch) {
        {
            final Vec3 _center = new Vec3(livingEntityPatch.getOriginal().getX(), livingEntityPatch.getOriginal().getEyeY(), livingEntityPatch.getOriginal().getZ());
            List<LivingEntity> _entfound = livingEntityPatch.getOriginal().getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entityiterator : _entfound) {
                LivingEntityPatch<?> ep = EpicFightCapabilities.getEntityPatch(entityiterator, LivingEntityPatch.class);
                if (ep != null && (entityiterator != livingEntityPatch.getOriginal())) {
                    PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(livingEntityPatch.getOriginal(), PlayerPatch.class);
                    if ((ep.getAnimator().getPlayerFor(null).getAnimation() instanceof LongHitAnimation longHitAnimation && (longHitAnimation == StarAnimations.EXECUTED_SEKIRO))) {
//                        if (pp instanceof LocalPlayerPatch lpp) {
//                            lpp.toggleLockOn();
//                            lpp.setLockOn(true);
//                        }

                        Vec3 playerP = pp.getOriginal().position();
                        Vec3 targetP = ep.getOriginal().position();
                        Vec3 toTarget = targetP.subtract(playerP);
                        float yaw = (float) MathUtils.getYRotOfVector(toTarget);
                        float pitch = (float) MathUtils.getXRotOfVector(toTarget);
                        pp.getOriginal().setYRot(yaw);
                        pp.getOriginal().setXRot(pitch);
                        livingEntityPatch.getOriginal().lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(ep.getOriginal().getX(), ep.getOriginal().getEyeY() - 0.1, ep.getOriginal().getZ()));
                        ep.getOriginal().lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(livingEntityPatch.getOriginal().getX(), livingEntityPatch.getOriginal().getEyeY() - 0.1, livingEntityPatch.getOriginal().getZ()));
                        break;
                    }
                }
            }
        }
    }

    public static class Star_Battle_utils {

        public static void ex(LivingEntityPatch<?> ep) {
            if(ep.isLogicalClient()){;}
            else {
                if(!ep.getCurrenltyAttackedEntities().isEmpty()){
                    ep.getCurrenltyAttackedEntities().forEach((entity)->{
                        if(entity instanceof LivingEntity) {
                            LivingEntity le = entity;
                            if(le.equals(ep.getOriginal())) return;
                            LivingEntityPatch<?> lep = EpicFightCapabilities.getEntityPatch(le, LivingEntityPatch.class);
                            if (lep != null) {
                                lep.applyStun(StunType.KNOCKDOWN, 5.0F);
                                EpicFightParticles.EVISCERATE.get().spawnParticleWithArgument((ServerLevel) lep.getOriginal().getLevel(), HitParticleType.MIDDLE_OF_ENTITIES, HitParticleType.ZERO, lep.getOriginal(), le);
                                lep.playSound(Sounds.DUANG2,1f,1f);
                            }
                        }
                    });
                }
            }
        }

        public static void duang(LivingEntityPatch<?> ep) {
            if(ep.isLogicalClient()){;}
            else {
                if(!ep.getCurrenltyAttackedEntities().isEmpty()){
                    ep.getCurrenltyAttackedEntities().forEach((entity)->{
                        if(entity instanceof LivingEntity) {
                            LivingEntity le = entity;
                            if(le.equals(ep.getOriginal())) return;
                            LivingEntityPatch<?> lep = EpicFightCapabilities.getEntityPatch(le, LivingEntityPatch.class);
                            if (lep != null) {
                                lep.playSound(Sounds.DUANG1,1f,1f);
                            }
                        }
                    });
                }
            }
        }

        public static void duang2(LivingEntityPatch<?> ep) {
            ep.playSound(Sounds.DUANG1,1f,1f);
        }

        public static void duang3(LivingEntityPatch<?> ep) {
            ep.playSound(Sounds.DUANG2,1f,1f);
        }

    }
}