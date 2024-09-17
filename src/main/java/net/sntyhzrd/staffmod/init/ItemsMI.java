package net.sntyhzrd.staffmod.init;

import net.minecraft.world.item.Item;
import net.sntyhzrd.staffmod.item.ElectricStaffItem;
import org.zeith.hammerlib.annotations.*;
import net.sntyhzrd.staffmod.StaffMod;

@SimplyRegister
public interface ItemsMI
{
	@RegistryName("electric_staff")
	Item ELECTRIC_STAFF_ITEM = StaffMod.MOD_TAB.add(new ElectricStaffItem(
			new Item.Properties()
				.durability(10)
				.setNoRepair()));
}