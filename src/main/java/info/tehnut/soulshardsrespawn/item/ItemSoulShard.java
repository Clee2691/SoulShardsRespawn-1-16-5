package info.tehnut.soulshardsrespawn.item;

import info.tehnut.soulshardsrespawn.SoulShards;
import info.tehnut.soulshardsrespawn.api.IShardTier;
import info.tehnut.soulshardsrespawn.api.ISoulShard;
import info.tehnut.soulshardsrespawn.block.TileEntitySoulCage;
import info.tehnut.soulshardsrespawn.core.RegistrarSoulShards;
import info.tehnut.soulshardsrespawn.core.data.Binding;
import info.tehnut.soulshardsrespawn.core.data.Tier;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.List;

public class ItemSoulShard extends Item implements ISoulShard {

    private static final Method GET_ENTITY_ID_METHOD;

    static {
        GET_ENTITY_ID_METHOD = ObfuscationReflectionHelper.findMethod(AbstractSpawner.class, "func_190895_g");
    }

    public ItemSoulShard() {
        super(new Properties().tab(SoulShards.TAB_SS));
//        ItemModelsProperties.register(RegistrarSoulShards.SOUL_SHARD.getItem(), new ResourceLocation(SoulShards.MODID, "bound"), (stack, worldIn, entityIn) -> getBinding(stack) != null ? 1.0F : 0.0F);
//        ItemModelsProperties.register(RegistrarSoulShards.SOUL_SHARD.getItem(), new ResourceLocation(SoulShards.MODID, "tier"), (stack, world, entity) -> {
//            Binding binding = getBinding(stack);
//            if (binding == null)
//                return 0F;
//
//            return Float.parseFloat("0." + Tier.INDEXED.indexOf(binding.getTier()));
//        });
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getPlayer() == null)
            return ActionResultType.PASS;

        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        Binding binding = getBinding(stack);
        if (binding == null)
            return ActionResultType.PASS;

        if (state.getBlock() instanceof SpawnerBlock) {
            if (!SoulShards.CONFIG.getBalance().allowSpawnerAbsorption()) {
                context.getPlayer().displayClientMessage(new TranslationTextComponent("chat.soulshards.absorb_disabled"), true);
                return ActionResultType.PASS;
            }

            if (binding.getKills() >= Tier.maxKills)
                return ActionResultType.PASS;

            MobSpawnerTileEntity mobSpawner = (MobSpawnerTileEntity) context.getLevel().getBlockEntity(context.getClickedPos());
            if (mobSpawner == null)
                return ActionResultType.PASS;

            try {
                ResourceLocation entityId = (ResourceLocation) GET_ENTITY_ID_METHOD.invoke(mobSpawner.getSpawner());
                if (!SoulShards.CONFIG.getEntityList().isEnabled(entityId))
                    return ActionResultType.PASS;

                if (entityId == null || binding.getBoundEntity() == null || !binding.getBoundEntity().equals(entityId))
                    return ActionResultType.FAIL;

                updateBinding(stack, binding.addKills(SoulShards.CONFIG.getBalance().getAbsorptionBonus()));
                context.getLevel().destroyBlock(context.getClickedPos(), false);
                return ActionResultType.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (state.getBlock() == RegistrarSoulShards.SOUL_CAGE) {
            if (binding.getBoundEntity() == null)
                return ActionResultType.FAIL;

            TileEntitySoulCage cage = (TileEntitySoulCage) context.getLevel().getBlockEntity(context.getClickedPos());
            if (cage == null)
                return ActionResultType.PASS;

            IItemHandler itemHandler = cage.getInventory();
            if (itemHandler != null && itemHandler.getStackInSlot(0).isEmpty()) {
                ItemHandlerHelper.insertItem(itemHandler, stack.copy(), false);
                cage.setChanged();
                cage.setState(true);
                context.getPlayer().setItemInHand(context.getHand(), ItemStack.EMPTY);
                return ActionResultType.SUCCESS;
            }
        }

        return super.useOn(context);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (!allowdedIn(group))
            return;

        items.add(new ItemStack(this));
        for (IShardTier tier : Tier.INDEXED) {
            ItemStack stack = new ItemStack(this);
            Binding binding = new Binding(null, tier.getKillRequirement());
            updateBinding(stack, binding);
            items.add(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        Binding binding = getBinding(stack);
        if (binding == null)
            return;

        if (binding.getBoundEntity() != null) {
            EntityType<?> entityEntry = ForgeRegistries.ENTITIES.getValue(binding.getBoundEntity());
            if (entityEntry != null)
                tooltip.add(new TranslationTextComponent("tooltip.soulshards.bound", entityEntry.getRegistryName()));
        }

        tooltip.add(new TranslationTextComponent("tooltip.soulshards.tier", binding.getTier().getIndex()));
        tooltip.add(new TranslationTextComponent("tooltip.soulshards.kills", binding.getKills()));
        if (flag.isAdvanced() && binding.getOwner() != null)
            tooltip.add(new TranslationTextComponent("tooltip.soulshards.owner", binding.getOwner().toString()));
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        Binding binding = getBinding(stack);
        return super.getDescriptionId(stack) + (binding == null || binding.getBoundEntity() == null ? "_unbound" : "");
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        Binding binding = getBinding(stack);
        return binding != null && binding.getKills() >= Tier.maxKills;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        Binding binding = getBinding(stack);
        return SoulShards.CONFIG.getClient().displayDurabilityBar() && binding != null && binding.getKills() < Tier.maxKills;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        Binding binding = getBinding(stack);
        if (binding == null)
            return 1.0D;

        return 1.0D - ((double) binding.getKills() / (double) Tier.maxKills);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return super.getRGBDurabilityForDisplay(stack);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return getBinding(stack) == null ? 64 : 1;
    }

    @Nullable
    @Override
    public Binding getBinding(ItemStack stack) {
        return Binding.fromNBT(stack);
    }

    public void updateBinding(ItemStack stack, Binding binding) {
        if (!stack.hasTag())
            stack.setTag(new CompoundNBT());

        stack.getTag().put("binding", binding.serializeNBT());
    }
}
