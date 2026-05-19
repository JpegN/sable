package dev.ryanhcode.sable.mixin.entity.entity_sublevel_collision;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.entity.EntitySubLevelUtil;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin extends Entity {

    public AbstractMinecartMixin(final EntityType<?> entityType, final Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void sable$postTick(final CallbackInfo ci) {
        final SubLevel containingSubLevel = Sable.HELPER.getContaining(this);
        if (containingSubLevel == null) return;

        final BlockPos pos = this.blockPosition();
        final BlockState here = this.level().getBlockState(pos);
        final BlockState below = this.level().getBlockState(pos.below());

        if (BaseRailBlock.isRail(here) || BaseRailBlock.isRail(below)) return;

        EntitySubLevelUtil.kickEntity(containingSubLevel, this);
    }
}
