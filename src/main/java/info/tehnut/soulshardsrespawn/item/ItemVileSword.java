package info.tehnut.soulshardsrespawn.item;

import info.tehnut.soulshardsrespawn.SoulShards;
import info.tehnut.soulshardsrespawn.api.ISoulWeapon;
import info.tehnut.soulshardsrespawn.core.RegistrarSoulShards;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

public class ItemVileSword extends SwordItem implements ISoulWeapon {

    public static final IItemTier MATERIAL_VILE = new MaterialVile();

    public ItemVileSword() {
        super(MATERIAL_VILE, 3, -2.4F, new Properties().tab(SoulShards.TAB_SS));
    }

    @Override
    public int getSoulBonus(ItemStack stack, PlayerEntity player, LivingEntity killedEntity) {
        return 1;
    }

    public static class MaterialVile implements IItemTier {

        private final LazyValue<Ingredient> ingredient;

        public MaterialVile() {
            this.ingredient = new LazyValue<>(() -> Ingredient.of(RegistrarSoulShards.CORRUPTED_INGOT));
        }

        @Override
        public int getUses() {
            return ItemTier.IRON.getUses();
        }

        @Override
        public float getSpeed() {
            return ItemTier.IRON.getSpeed();
        }

        @Override
        public float getAttackDamageBonus() {
            return ItemTier.IRON.getAttackDamageBonus();
        }

        @Override
        public int getLevel() {
            return ItemTier.IRON.getLevel();
        }

        @Override
        public int getEnchantmentValue() {
            return ItemTier.IRON.getEnchantmentValue();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return ingredient.get();
        }

    }
}
