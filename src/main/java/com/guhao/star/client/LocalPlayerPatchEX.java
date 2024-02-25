package com.guhao.star.client;

import com.guhao.star.api.api;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPSetPlayerTarget;

import static com.guhao.star.units.MathUnit.getXRotOfVector;
import static com.guhao.star.units.MathUnit.getYRotOfVector;

public class LocalPlayerPatchEX extends LocalPlayerPatch implements api {
    private Minecraft minecraft;
    private LivingEntity rayTarget;
    private boolean targetLockedOn;

    private float lockOnXRot;
    private float lockOnXRotO;
    private float lockOnYRot;
    private float lockOnYRotO;

    public void toggleLockOn() {
        this.targetLockedOn = !this.targetLockedOn;
    }

    @Override
    public void clientTick(LivingEvent.LivingUpdateEvent event) {
        super.clientTick(event);

        HitResult cameraHitResult = this.minecraft.hitResult;

        if (cameraHitResult != null && cameraHitResult.getType() == HitResult.Type.ENTITY) {
            Entity hit = ((EntityHitResult) cameraHitResult).getEntity();

            if (hit != this.rayTarget) {
                if (hit instanceof LivingEntity livingentity) {
                    if (!(hit instanceof ArmorStand) && !this.targetLockedOn) {
                        this.rayTarget = livingentity;
                        this.rayTarget = livingentity;
                    }
                } else if (hit instanceof PartEntity<?> partEntity) {
                    Entity parent = partEntity.getParent();

                    if (parent instanceof LivingEntity parentLivingEntity && !this.targetLockedOn) {
                        this.rayTarget = parentLivingEntity;
                    }
                } else {
                    this.rayTarget = null;
                }

                if (this.rayTarget != null) {
                    EpicFightNetworkManager.sendToServer(new CPSetPlayerTarget(this.getTarget().getId()));
                }
            }
        }

        if (this.rayTarget != null) {
            if (this.targetLockedOn) {
                Vec3 playerPosition = this.original.getEyePosition();
                Vec3 targetPosition = this.rayTarget.getEyePosition();
                Vec3 toTarget = targetPosition.subtract(playerPosition);
                float yaw = (float) getYRotOfVector(toTarget);
                float pitch = (float) getXRotOfVector(toTarget);
                CameraType cameraType = this.minecraft.options.getCameraType();
                this.lockOnXRotO = this.lockOnXRot;
                this.lockOnYRotO = this.lockOnYRot;
                float lockOnXRotDst = pitch + (cameraType.isFirstPerson() ? 0.0F : 30.0F);
                lockOnXRotDst = Mth.clamp(lockOnXRotDst, 0.0F, 60.0F);

                if (cameraType.isMirrored()) {
                    lockOnXRotDst = -lockOnXRotDst;
                }

                float lockOnYRotDst = yaw + (cameraType.isMirrored() ? 180.0F : 0.0F);
                float xDiff = Mth.wrapDegrees(lockOnXRotDst - this.lockOnXRotO);
                float yDiff = Mth.wrapDegrees(lockOnYRotDst - this.lockOnYRotO);
                float xLerp = Mth.clamp(xDiff * 0.4F, -30.0F, 30.0F);
                float yLerp = Mth.clamp(yDiff * 0.4F, -30.0F, 30.0F);

                this.lockOnXRot = this.lockOnXRotO + xLerp;
                this.lockOnYRot = this.lockOnYRotO + yLerp;

                if (!getEntityState().turningLocked() || EntityStateEX.lockonRotate()) {
                    this.original.setXRot(lockOnXRotDst);
                    this.original.setYRot(lockOnYRotDst);
                }
            } else {
                this.lockOnXRot = this.original.getXRot();
                this.lockOnYRot = this.original.getYRot();
                this.lockOnXRotO = this.lockOnXRot;
                this.lockOnYRotO = this.lockOnYRot;
            }

            if (!this.rayTarget.isAlive() || this.getOriginal().distanceToSqr(this.rayTarget) > 400.0D || (this.getAngleTo(this.rayTarget) > 100.0D && !this.targetLockedOn)) {
                this.rayTarget = null;
                EpicFightNetworkManager.sendToServer(new CPSetPlayerTarget(-1));
            }
        } else {
            this.lockOnXRot = this.original.getXRot();
            this.lockOnYRot = this.original.getYRot();
            this.targetLockedOn = false;
        }
    }

    @Override
    public void onDeath(LivingDeathEvent event) {
        super.onDeath();
        this.original.setXRot(this.lockOnXRot);
        this.original.setYRot(this.lockOnYRot);
    }

    @Override
    public void correctRotation() {
        if (this.targetLockedOn) {
            if (this.rayTarget != null && !this.rayTarget.isRemoved()) {
                Vec3 playerPosition = this.original.position();
                Vec3 targetPosition = this.rayTarget.position();
                Vec3 toTarget = targetPosition.subtract(playerPosition);
                float yaw = (float) getYRotOfVector(toTarget);
                float pitch = (float) getXRotOfVector(toTarget);
                this.original.setYRot(yaw);
                this.original.setXRot(pitch);
            } else {
                this.original.setYRot(this.lockOnYRot);
                this.original.setXRot(this.lockOnXRot);
            }
        }
    }
}
