package net.zimo.bor.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.zimo.bor.borMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {


    @Shadow
    Minecraft minecraft;

    @Inject(method="shouldRenderBlockOutline()Z", at = @At("RETURN"), cancellable = true)
    public void removeOutline(CallbackInfoReturnable<Boolean> cir){
        Entity entity = this.minecraft.getCameraEntity();
        boolean flag = entity instanceof Player && !this.minecraft.options.hideGui;
        if (flag) {
            ItemStack itemstack = ((LivingEntity) entity).getMainHandItem();
            HitResult hitresult = this.minecraft.hitResult;
            if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
                BlockState blockstate = this.minecraft.level.getBlockState(blockpos);
                if(blockstate.getBlock().getName().toString().contains("nicoonepixel:grass")){
                    cir.setReturnValue(false);
                }
            }
        }
        cir.cancel();

    }
}
