package net.sntyhzrd.staffmod;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.core.init.ItemsHL;
import org.zeith.hammerlib.proxy.HLConstants;

@Mod(StaffMod.MOD_ID)
public class StaffMod
{
	public static final String MOD_ID = "staffmod";
	
	@CreativeTab.RegisterTab
	public static final CreativeTab MOD_TAB = new CreativeTab(id("root"),
			builder -> builder
					.icon(() -> ItemsHL.COPPER_GEAR.getDefaultInstance())
					.withTabsBefore(HLConstants.HL_TAB.id())
	);
	
	public StaffMod(IEventBus bus)
	{
		LanguageAdapter.registerMod(MOD_ID);
		bus.addListener(StaffMod::clientSetup);
	}

	private static void clientSetup(final FMLClientSetupEvent event) {
		CustomRenderers.renderEntity();
	}
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}