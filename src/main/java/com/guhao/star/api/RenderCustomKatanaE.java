package com.guhao.star.api;

import com.guhao.star.item_S.HumanoidArmature;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public interface RenderCustomKatanaE {
    void renderItemInHand(ItemStack stack, LivingEntityPatch<?> entitypatch, InteractionHand hand, HumanoidArmature armature, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight);
}
