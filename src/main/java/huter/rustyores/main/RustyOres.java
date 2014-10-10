package huter.rustyores.main;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import huter.rustyores.main.blocks.ModBlocks;
import huter.rustyores.main.handlers.CraftingHandler;
import huter.rustyores.main.items.ModItems;
import huter.rustyores.main.lib.Constants;
import huter.rustyores.main.proxies.CommonProxy;


@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION)
public class RustyOres {
	
	public static int thermiteWireColor = 0x7F6A00;
	public static int thermiteWireColorR = 127;
	public static int thermiteWireColorG = 106;
	public static int thermiteWireColorB = 0;

	@SidedProxy(clientSide = "huter.rustyores.main.proxies.ClientProxy", serverSide = "huter.rustyores.main.proxies.CommonProxy")
    public static CommonProxy proxy;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		proxy.registerRenderInformation();
        
		ModItems.init();
		ModBlocks.init();
    }

	@Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	CraftingHandler.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

}
