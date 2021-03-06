package main.redstonearmory.util;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/*
 * DISCLAIMER ABOUT THIS CLASS
 *
 * This was originally by the CoFH team for (AFAIK) 1.7. We have backported it for our needs in 1.6.4. 99% of the code is exactly the same.
 */

public class RFHelper {

    public static ItemStack setDefaultEnergyTag(ItemStack container, int energy) {
        container.setTagCompound(new NBTTagCompound());
        container.stackTagCompound.setInteger("Energy", energy);
        return container;
    }

    public static int getRFStored(ItemStack container) {
        if (container.stackTagCompound == null)
            setDefaultEnergyTag(container, 0);

        int energyStored = container.stackTagCompound.getInteger("Energy");
        return energyStored;
    }

    public static ItemStack setEnergy(ItemStack container, int energy) {
        container.stackTagCompound.setInteger("Energy", energy);
        return container;
    }

    public static int receiveEnergy(ItemStack container, int maxReceive, boolean simulate, int capacity, int transferLimit) {
        if (container.stackTagCompound == null)
            setDefaultEnergyTag(container, 0);

        int energy = getRFStored(container);
        int add = Math.min(maxReceive, Math.min(capacity - energy, transferLimit));

        if (!simulate) {
            energy += add;
            RFHelper.setEnergy(container, energy);
        }

        return add;
    }

    public static int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.stackTagCompound == null)
            setDefaultEnergyTag(container, 0);

        int energy = getRFStored(container);
        int remove = Math.min(maxExtract, energy);

        if (!simulate) {
            energy -= remove;
            RFHelper.setEnergy(container, energy);
        }
        return remove;
    }

    public static int getEnergyStored(ItemStack container) {
        return getRFStored(container);
    }


    public static int insertEnergyIntoHeldContainer(EntityPlayer player, int maxReceive, boolean simulate) {

        ItemStack container = player.getCurrentEquippedItem();

        return isEnergyContainerItem(container) ? ((IEnergyContainerItem) container.getItem()).receiveEnergy(container, maxReceive, simulate) : 0;
    }

    public static boolean isPlayerHoldingEnergyContainerItem(EntityPlayer player) {

        return isEnergyContainerItem(player.getCurrentEquippedItem());
    }

    public static boolean isEnergyContainerItem(ItemStack container) {

        return container != null && container.getItem() instanceof IEnergyContainerItem;
    }
}
