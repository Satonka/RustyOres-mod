package huter.rustyores.main.handlers;

import huter.rustyores.main.blocks.ModBlocks;
import huter.rustyores.main.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
public class CraftingHandler {

	public static void init(){
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.wetiron, 1), new ItemStack(Items.water_bucket),
				new ItemStack(Items.iron_ingot));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.thermitedust, 1), new ItemStack(Items.redstone),
				new ItemStack(ModItems.rust));
		GameRegistry.addRecipe(new ItemStack(ModBlocks.thermiteBlock), "xxx", "xxx", "xxx",
		        'x', new ItemStack(ModItems.thermitedust, 1));
	}
	
}
