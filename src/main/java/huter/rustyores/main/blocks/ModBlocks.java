package huter.rustyores.main.blocks;

import net.minecraft.block.Block;

public class ModBlocks {

	public static Block thermitewire;
	public static Block thermiteBlock;
	
	public static void init(){
		thermitewire = new ThermiteWire();
		thermiteBlock = new ThermiteBlock();
	}
	
}
