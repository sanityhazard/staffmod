package net.sntyhzrd.staffmod.init;

import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@ProvideRecipes
public class ModRecipes implements IRecipeProvider {
    @Override
    public void provideRecipes(RegisterRecipesEvent event)
    {
        event.shaped()
                .result(ItemsMI.ELECTRIC_STAFF_ITEM)
                .shape(" b ", " a ", " s ")
                .map('b', Items.BLUE_DYE)
                .map('a', Items.AMETHYST_SHARD)
                .map('s', Items.STICK)
                .register();
    }
}
