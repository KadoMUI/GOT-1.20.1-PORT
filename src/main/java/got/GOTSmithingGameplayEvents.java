package got;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GOTMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTSmithingGameplayEvents {
    private static final UUID DAMAGE_UUID = UUID.fromString("56197f4b-620e-44a2-a4c2-0b4b9862f090");
    private static final UUID SPEED_UUID = UUID.fromString("d9258590-aa38-481c-a3f6-e2cc5fa55927");
    private static final UUID REACH_UUID = UUID.fromString("87d3f7f3-5ec6-49b9-b998-0436ef60d4ca");
    private static final UUID ARMOR_UUID = UUID.fromString("84dc8f93-e31d-4754-9662-abce48d68a6c");

    private GOTSmithingGameplayEvents() {}

    @SubscribeEvent
    public static void attributes(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();

        if (event.getSlotType() == net.minecraft.world.entity.EquipmentSlot.MAINHAND) {
            float damage = GOTSmithingModifierData.baseMeleeDamageBoost(stack);
            if (damage != 0.0F) {
                event.addModifier(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(DAMAGE_UUID, "GOT smithing damage",
                                damage, AttributeModifier.Operation.ADDITION));
            }

            float speed = GOTSmithingModifierData.meleeSpeedFactor(stack);
            if (speed != 1.0F) {
                event.addModifier(Attributes.ATTACK_SPEED,
                        new AttributeModifier(SPEED_UUID, "GOT smithing speed",
                                speed - 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }

            float reach = GOTSmithingModifierData.meleeReachFactor(stack);
            if (reach != 1.0F) {
                event.addModifier(ForgeMod.ENTITY_REACH.get(),
                        new AttributeModifier(REACH_UUID, "GOT smithing reach",
                                reach - 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }

        int protection = GOTSmithingModifierData.commonArmorProtection(stack);
        if (protection != 0 && event.getSlotType().getType()
                == net.minecraft.world.entity.EquipmentSlot.Type.ARMOR) {
            event.addModifier(Attributes.ARMOR,
                    new AttributeModifier(ARMOR_UUID, "GOT smithing armor",
                            protection, AttributeModifier.Operation.ADDITION));
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        float factor = GOTSmithingModifierData.toolSpeedFactor(event.getEntity().getMainHandItem());
        if (factor != 1.0F) event.setNewSpeed(event.getNewSpeed() * factor);
    }

    @SubscribeEvent
    public static void hurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();

        // Special armor protections are additive across equipped pieces in the
        // original helper. Each point is treated as one protection point here.
        int special = 0;
        for (ItemStack armor : victim.getArmorSlots()) {
            if (event.getSource().is(DamageTypes.IN_FIRE)
                    || event.getSource().is(DamageTypes.ON_FIRE)
                    || event.getSource().is(DamageTypes.LAVA)) {
                special += GOTSmithingModifierData.fireProtection(armor);
            }
            if (event.getSource().is(DamageTypes.FALL)) {
                special += GOTSmithingModifierData.fallProtection(armor);
            }
            if (event.getSource().getDirectEntity() instanceof net.minecraft.world.entity.projectile.Projectile) {
                special += GOTSmithingModifierData.rangedProtection(armor);
            }
        }
        if (special > 0) {
            float factor = Math.max(0.0F, 1.0F - 0.04F * special);
            event.setAmount(event.getAmount() * factor);
        }

        boolean specialFire = false;
        boolean specialChill = false;
        boolean specialHeadhunting = false;

        if (event.getSource().getDirectEntity() instanceof Projectile projectile) {
            specialFire = projectile.getPersistentData().getBoolean("GOTSpecialFire");
            specialChill = projectile.getPersistentData().getBoolean("GOTSpecialChill");
            specialHeadhunting = projectile.getPersistentData().getBoolean("GOTSpecialHeadhunting");
        } else if (event.getSource().getEntity() instanceof LivingEntity specialAttacker) {
            ItemStack specialWeapon = specialAttacker.getMainHandItem();
            specialFire = GOTSmithingModifierData.has(specialWeapon, GOTSmithingModifier.FIRE);
            specialChill = GOTSmithingModifierData.has(specialWeapon, GOTSmithingModifier.CHILL);
            specialHeadhunting = GOTSmithingModifierData.has(specialWeapon, GOTSmithingModifier.HEADHUNTING);
        }

        if (specialFire && !victim.level().isClientSide) {
            int difficultyId = victim.level().getDifficulty().getId();
            victim.setSecondsOnFire(1 + difficultyId * 10);
        }

        if (specialChill && !victim.level().isClientSide) {
            // Exact old weapon-special duration/amplifier: 5 seconds, Slowness II.
            victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 1));

            // 1.20.1 has native freezing state; use it as the closest modern
            // representation of GOTDamage.doFrostDamage for all living targets.
            victim.setTicksFrozen(Math.max(victim.getTicksFrozen(), victim.getTicksRequiredToFreeze()));
        }

        if (specialHeadhunting) {
            victim.getPersistentData().putBoolean("GOTHeadhuntingLastHit", true);
            if (event.getSource().getEntity() instanceof Player player) {
                victim.getPersistentData().putUUID("GOTHeadhuntingKiller", player.getUUID());
            }
        }

        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            int knockback = GOTSmithingModifierData.meleeKnockback(weapon);
            if (knockback > 0 && event.getAmount() > 0.0F) {
                double dx = attacker.getX() - victim.getX();
                double dz = attacker.getZ() - victim.getZ();
                victim.knockback(0.35D * knockback, dx, dz);
            }
        }
    }


    @SubscribeEvent
    public static void headhuntingDeath(net.minecraftforge.event.entity.living.LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player victim) || victim.level().isClientSide) return;

        boolean headhunting = false;
        if (event.getSource().getDirectEntity() instanceof Projectile projectile) {
            headhunting = projectile.getPersistentData().getBoolean("GOTSpecialHeadhunting");
        } else if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            headhunting = GOTSmithingModifierData.has(attacker.getMainHandItem(), GOTSmithingModifier.HEADHUNTING);
        }
        if (!headhunting) return;

        ItemStack head = new ItemStack(net.minecraft.world.item.Items.PLAYER_HEAD);
        net.minecraft.nbt.CompoundTag owner = new net.minecraft.nbt.CompoundTag();
        net.minecraft.nbt.NbtUtils.writeGameProfile(owner, victim.getGameProfile());
        head.getOrCreateTag().put("SkullOwner", owner);
        victim.spawnAtLocation(head);
    }

    @SubscribeEvent
    public static void looting(LootingLevelEvent event) {
        if (!(event.getDamageSource().getEntity() instanceof LivingEntity attacker)) return;
        int extra = GOTSmithingModifierData.lootingLevel(attacker.getMainHandItem());
        if (extra > 0) event.setLootingLevel(event.getLootingLevel() + extra);
    }

    @SubscribeEvent
    public static void projectileSpawn(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;
        if (!(arrow.getOwner() instanceof LivingEntity owner)) return;

        ItemStack launcher = owner.getUseItem();
        if (launcher.isEmpty()) launcher = owner.getMainHandItem();

        float factor = GOTSmithingModifierData.rangedDamageFactor(launcher);
        if (factor != 1.0F) arrow.setBaseDamage(arrow.getBaseDamage() * factor);

        int knockback = GOTSmithingModifierData.rangedKnockback(launcher);
        if (knockback > 0) arrow.setKnockback(knockback);

        if (GOTSmithingModifierData.has(launcher, GOTSmithingModifier.FIRE)) {
            arrow.getPersistentData().putBoolean("GOTSpecialFire", true);
        }
        if (GOTSmithingModifierData.has(launcher, GOTSmithingModifier.CHILL)) {
            arrow.getPersistentData().putBoolean("GOTSpecialChill", true);
        }
        if (GOTSmithingModifierData.has(launcher, GOTSmithingModifier.HEADHUNTING)) {
            arrow.getPersistentData().putBoolean("GOTSpecialHeadhunting", true);
        }
    }

    /**
     * Deterministic durability scaling. Damage added since the previous tick is
     * partially refunded according to the legacy durability multiplier.
     */
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;

        for (ItemStack stack : event.player.getInventory().items) refundDurability(stack);
        for (ItemStack stack : event.player.getInventory().armor) refundDurability(stack);
        refundDurability(event.player.getOffhandItem());
    }

    private static void refundDurability(ItemStack stack) {
        if (stack.isEmpty() || !stack.isDamageableItem()) return;
        float factor = GOTSmithingModifierData.durabilityFactor(stack);
        var tag = stack.getOrCreateTag();
        int damage = stack.getDamageValue();
        int previous = tag.getInt("GOTSmithLastDamage");
        float credit = tag.getFloat("GOTSmithDurabilityCredit");

        if (previous > 0 && damage > previous && factor > 1.0F) {
            int delta = damage - previous;
            credit += delta * (1.0F - 1.0F / factor);
            int refund = (int) credit;
            if (refund > 0) {
                stack.setDamageValue(Math.max(0, damage - refund));
                damage = stack.getDamageValue();
                credit -= refund;
            }
        }

        tag.putInt("GOTSmithLastDamage", damage);
        tag.putFloat("GOTSmithDurabilityCredit", credit);
    }

    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() == GOTItems.SMITH_SCROLL.get()) return;

        if (GOTAnvilNameColors.isNameColorGem(event.getItemStack())) {
            var color = GOTAnvilNameColors.colorFor(event.getItemStack());
            event.getToolTip().add(net.minecraft.network.chat.Component.translatable(
                    "item.got.name_color_gem").withStyle(color));
        }

        String owner = GOTItemOwnership.getCurrentOwner(event.getItemStack());
        if (owner != null) {
            event.getToolTip().add(net.minecraft.network.chat.Component.translatable(
                    "item.got.current_owner", owner).withStyle(net.minecraft.ChatFormatting.GRAY));
            for (String previous : GOTItemOwnership.getPreviousOwners(event.getItemStack())) {
                event.getToolTip().add(net.minecraft.network.chat.Component.translatable(
                        "item.got.previous_owner", previous).withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
            }
        }

        var modifiers = GOTSmithingModifierData.getModifiers(event.getItemStack());
        if (!modifiers.isEmpty()) {
            event.getToolTip().add(net.minecraft.network.chat.Component.translatable(
                    "item.got.smithing.modifiers_header").withStyle(net.minecraft.ChatFormatting.GOLD));
            for (GOTSmithingModifier modifier : modifiers) {
                event.getToolTip().add(net.minecraft.network.chat.Component.literal("  ")
                        .append(modifier.displayName())
                        .withStyle(net.minecraft.ChatFormatting.GRAY));
            }
        }
    }
}
