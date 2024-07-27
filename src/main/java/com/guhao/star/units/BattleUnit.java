package com.guhao.star.units;


import com.guhao.star.regirster.Sounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.StunType;

public class BattleUnit {
    public BattleUnit() {
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