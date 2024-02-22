package com.guhao.star.client;

import com.guhao.star.api.RenderCustomKatanaE;
import com.guhao.star.item_S.HumanoidArmature;
import com.guhao.star.regirster.Items;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@OnlyIn(Dist.CLIENT)
public class RenderCustomKatana extends RenderItemBase implements RenderCustomKatanaE {
	private final ItemStack sheathStack = new ItemStack(Items.CUSTOM_SHEATH.get());
	
	@Override
	public void renderItemInHand(ItemStack stack, LivingEntityPatch<?> entitypatch, InteractionHand hand, HumanoidArmature armature, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight) {
		OpenMatrix4f modelMatrix = new OpenMatrix4f(this.mainhandcorrectionMatrix);
		modelMatrix.mulFront(poses[armature.toolR.getId()]);
		
		poseStack.pushPose();
		this.mulPoseStack(poseStack, modelMatrix);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
        poseStack.popPose();
        
		modelMatrix = new OpenMatrix4f(this.mainhandcorrectionMatrix);
		modelMatrix.mulFront(poses[armature.toolL.getId()]);
		
		poseStack.pushPose();
		this.mulPoseStack(poseStack, modelMatrix);
        Minecraft.getInstance().getItemRenderer().renderStatic(this.sheathStack, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
        poseStack.popPose();
    }
}