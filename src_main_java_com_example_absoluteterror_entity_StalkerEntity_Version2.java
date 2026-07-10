package com.example.absoluteterror.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StalkerEntity extends PathfinderMob {
    private int whisperCooldown = 0;

    public StalkerEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new StalkGoal(this));
    }

    static class StalkGoal extends Goal {
        private final StalkerEntity entity;

        public StalkGoal(StalkerEntity e) {
            this.entity = e;
        }

        @Override
        public boolean canUse() {
            return entity.level().getNearestPlayer(entity, 40) != null;
        }

        @Override
        public void tick() {
            Player p = entity.level().getNearestPlayer(entity, 40);
            if (p == null) return;

            // Move to a point ~15 blocks behind the player's look direction
            Vec3 behind = p.position().subtract(p.getLookAngle().scale(15.0));
            entity.getNavigation().moveTo(behind.x, behind.y, behind.z, 1.2D);

            // If the player is looking almost directly at the entity, play a smoke particle and vanish
            Vec3 toEntity = entity.position().subtract(p.position());
            if (!toEntity.equals(Vec3.ZERO)) {
                toEntity = toEntity.normalize();
                Vec3 look = p.getViewVector(1.0F).normalize();
                if (look.dot(toEntity) > 0.995) {
                    entity.level().addParticle(ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getY() + 0.5, entity.getZ(), 0, 0, 0);
                    entity.discard();
                    return;
                }
            }

            // Occasionally send an obfuscated whisper using '§k'
            if (entity.whisperCooldown++ > 400 && entity.getRandom().nextFloat() > 0.98F) {
                // Use §k for obfuscated text as requested
                p.sendSystemMessage(Component.literal("§kBehind you..."));
                entity.whisperCooldown = 0;
            }
        }
    }
}