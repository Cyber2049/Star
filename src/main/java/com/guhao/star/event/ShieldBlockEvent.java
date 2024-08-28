package com.guhao.star.event;

import com.guhao.star.units.Guard_Array;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.Arrays;

import static com.guhao.star.Star.MODID;

@Mod.EventBusSubscriber(
        modid = MODID
)
public class ShieldBlockEvent {
    Guard_Array star_new$GuardArray = new Guard_Array();
    StaticAnimation[] star_new$GUARD = star_new$GuardArray.getGuard();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void shieldEvent(net.minecraftforge.event.entity.living.ShieldBlockEvent event) {
        EpicFightDamageSource epicFightDamageSource = Guard_Array.getEpicFightDamageSources(event.getDamageSource());
        if (epicFightDamageSource != null && (!(Arrays.asList(star_new$GUARD).contains(epicFightDamageSource.getAnimation())))) {
            ItemStack itemStack1 = event.getEntityLiving().getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack itemStack2 = event.getEntityLiving().getItemInHand(InteractionHand.OFF_HAND);
            if (itemStack1.getItem() instanceof ShieldItem shieldItem1 && event.getEntityLiving() instanceof Player player) player.getCooldowns().addCooldown(shieldItem1,200);
            if (itemStack2.getItem() instanceof ShieldItem shieldItem2 && event.getEntityLiving() instanceof Player player) player.getCooldowns().addCooldown(shieldItem2,200);
        }
    }
}
