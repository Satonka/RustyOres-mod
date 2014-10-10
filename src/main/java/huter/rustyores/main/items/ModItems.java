package huter.rustyores.main.items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ModItems {

	public static Item wetiron;
	public static Item rust;
	public static Item thermitedust;
	
	public static void init(){
		wetiron = new WetIronIngot();
		rust = new IronOxide();
		thermitedust = new ThermiteDust();
	}
}
